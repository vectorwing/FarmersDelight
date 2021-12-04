package vectorwing.farmersdelight.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.mixin.accessors.RecipeManagerAccessor;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.ItemUtils;

import java.util.Optional;

public class StoveTileEntity extends FDSyncedTileEntity implements ITickableTileEntity
{
	private static final VoxelShape GRILLING_AREA = Block.box(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);
	private static final int INVENTORY_SLOT_COUNT = 6;

	private final ItemStackHandler inventory;
	private final int[] cookingTimes;
	private final int[] cookingTimesTotal;
	private ResourceLocation[] lastRecipeIDs;

	public StoveTileEntity() {
		super(ModTileEntityTypes.STOVE_TILE.get());
		inventory = createHandler();
		cookingTimes = new int[INVENTORY_SLOT_COUNT];
		cookingTimesTotal = new int[INVENTORY_SLOT_COUNT];
		lastRecipeIDs = new ResourceLocation[INVENTORY_SLOT_COUNT];
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("Inventory")) {
			inventory.deserializeNBT(compound.getCompound("Inventory"));
		} else {
			inventory.deserializeNBT(compound);
		}
		if (compound.contains("CookingTimes", 11)) {
			int[] arrayCookingTimes = compound.getIntArray("CookingTimes");
			System.arraycopy(arrayCookingTimes, 0, cookingTimes, 0, Math.min(cookingTimesTotal.length, arrayCookingTimes.length));
		}

		if (compound.contains("CookingTotalTimes", 11)) {
			int[] arrayCookingTimesTotal = compound.getIntArray("CookingTotalTimes");
			System.arraycopy(arrayCookingTimesTotal, 0, cookingTimesTotal, 0, Math.min(cookingTimesTotal.length, arrayCookingTimesTotal.length));
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		writeItems(compound);
		compound.putIntArray("CookingTimes", cookingTimes);
		compound.putIntArray("CookingTotalTimes", cookingTimesTotal);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.save(compound);
		compound.put("Inventory", inventory.serializeNBT());
		return compound;
	}

	@Override
	public void tick() {
		if (level == null) return;

		boolean isStoveLit = getBlockState().getValue(StoveBlock.LIT);
		if (level.isClientSide) {
			if (isStoveLit) {
				addParticles();
			}
		} else {
			if (isStoveBlockedAbove()) {
				if (!ItemUtils.isInventoryEmpty(inventory)) {
					ItemUtils.dropItems(level, worldPosition, inventory);
					inventoryChanged();
				}
			} else if (isStoveLit) {
				cookAndOutputItems();
			} else {
				for (int i = 0; i < inventory.getSlots(); ++i) {
					if (cookingTimes[i] > 0) {
						cookingTimes[i] = MathHelper.clamp(cookingTimes[i] - 2, 0, cookingTimesTotal[i]);
					}
				}
			}
		}
	}

	private void cookAndOutputItems() {
		if (level == null) return;

		boolean didInventoryChange = false;
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack stoveStack = inventory.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				++cookingTimes[i];
				if (cookingTimes[i] >= cookingTimesTotal[i]) {
					IInventory inventoryWrapper = new Inventory(stoveStack);
					Optional<CampfireCookingRecipe> recipe = getMatchingRecipe(inventoryWrapper, i);
					if (recipe.isPresent()) {
						ItemStack resultStack = recipe.get().getResultItem();
						if (!resultStack.isEmpty()) {
							ItemUtils.spawnItemEntity(level, resultStack.copy(),
									worldPosition.getX() + 0.5, worldPosition.getY() + 1.0, worldPosition.getZ() + 0.5,
									level.random.nextGaussian() * (double) 0.01F, 0.1F, level.random.nextGaussian() * (double) 0.01F);
						}
					}
					inventory.setStackInSlot(i, ItemStack.EMPTY);
					didInventoryChange = true;
				}
			}
		}

		if (didInventoryChange) {
			inventoryChanged();
		}
	}

	public int getNextEmptySlot() {
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack.isEmpty()) {
				return i;
			}
		}
		return -1;
	}

	public boolean addItem(ItemStack itemStackIn, CampfireCookingRecipe recipe, int slot) {
		if (0 <= slot && slot < inventory.getSlots()) {
			ItemStack slotStack = inventory.getStackInSlot(slot);
			if (slotStack.isEmpty()) {
				cookingTimesTotal[slot] = recipe.getCookingTime();
				cookingTimes[slot] = 0;
				inventory.setStackInSlot(slot, itemStackIn.split(1));
				lastRecipeIDs[slot] = recipe.getId();
				inventoryChanged();
				return true;
			}
		}
		return false;
	}

	public Optional<CampfireCookingRecipe> getMatchingRecipe(IInventory recipeWrapper, int slot) {
		if (level == null) return Optional.empty();

		if (lastRecipeIDs[slot] != null) {
			IRecipe<IInventory> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
					.getRecipeMap(IRecipeType.CAMPFIRE_COOKING)
					.get(lastRecipeIDs[slot]);
			if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, level)) {
				return Optional.of((CampfireCookingRecipe) recipe);
			}
		}

		return level.getRecipeManager().getRecipeFor(IRecipeType.CAMPFIRE_COOKING, recipeWrapper, level);
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public boolean isStoveBlockedAbove() {
		if (level != null) {
			BlockState above = level.getBlockState(worldPosition.above());
			return VoxelShapes.joinIsNotEmpty(GRILLING_AREA, above.getShape(level, worldPosition.above()), IBooleanFunction.AND);
		}
		return false;
	}

	public Vector2f getStoveItemOffset(int index) {
		final float X_OFFSET = 0.3F;
		final float Y_OFFSET = 0.2F;
		final Vector2f[] OFFSETS = {
				new Vector2f(X_OFFSET, Y_OFFSET),
				new Vector2f(0.0F, Y_OFFSET),
				new Vector2f(-X_OFFSET, Y_OFFSET),
				new Vector2f(X_OFFSET, -Y_OFFSET),
				new Vector2f(0.0F, -Y_OFFSET),
				new Vector2f(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}

	private void addParticles() {
		if (level == null) return;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			if (!inventory.getStackInSlot(i).isEmpty() && level.random.nextFloat() < 0.2F) {
				Vector2f stoveItemVector = getStoveItemOffset(i);
				Direction direction = getBlockState().getValue(StoveBlock.FACING);
				int directionIndex = direction.get2DDataValue();
				Vector2f offset = directionIndex % 2 == 0 ? stoveItemVector : new Vector2f(stoveItemVector.y, stoveItemVector.x);

				double x = ((double) worldPosition.getX() + 0.5D) - (direction.getStepX() * offset.x) + (direction.getClockWise().getStepX() * offset.x);
				double y = (double) worldPosition.getY() + 1.0D;
				double z = ((double) worldPosition.getZ() + 0.5D) - (direction.getStepZ() * offset.y) + (direction.getClockWise().getStepZ() * offset.y);

				for (int k = 0; k < 3; ++k) {
					level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
				}
			}
		}
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return writeItems(new CompoundNBT());
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SLOT_COUNT)
		{
			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}
		};
	}
}
