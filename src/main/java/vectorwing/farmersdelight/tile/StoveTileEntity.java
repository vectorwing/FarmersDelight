package vectorwing.farmersdelight.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.ItemUtils;
import vectorwing.farmersdelight.utils.MathUtils;

import java.util.Optional;

public class StoveTileEntity extends FDSyncedTileEntity implements ITickableTileEntity
{
	private static final VoxelShape GRILLING_AREA = Block.makeCuboidShape(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);
	private static final int INVENTORY_SLOT_COUNT = 6;

	private final ItemStackHandler inventory;
	private final int[] cookingTimes;
	private final int[] cookingTimesTotal;
//	TODO: private CampfireCookingRecipe lastRecipe;

	public StoveTileEntity() {
		super(ModTileEntityTypes.STOVE_TILE.get());
		inventory = createHandler();
		cookingTimes = new int[INVENTORY_SLOT_COUNT];
		cookingTimesTotal = new int[INVENTORY_SLOT_COUNT];
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
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
	public CompoundNBT write(CompoundNBT compound) {
		writeItems(compound);
		compound.putIntArray("CookingTimes", cookingTimes);
		compound.putIntArray("CookingTotalTimes", cookingTimesTotal);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", inventory.serializeNBT());
		return compound;
	}

	@Override
	public void tick() {
		if (world == null) return;

		boolean isStoveLit = getBlockState().get(StoveBlock.LIT);
		if (world.isRemote) {
			if (isStoveLit) {
				addParticles();
			}
		} else {
			if (isStoveBlockedAbove()) {
				if (!ItemUtils.isInventoryEmpty(inventory)) {
					ItemUtils.dropItems(world, pos, inventory);
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
		if (world == null) return;

		boolean didInventoryChange = false;
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack stoveStack = inventory.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				++cookingTimes[i];
				if (cookingTimes[i] >= cookingTimesTotal[i]) {
					IInventory inventoryWrapper = new Inventory(stoveStack);
					ItemStack resultStack = world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, inventoryWrapper, world).map((recipe) -> recipe.getCraftingResult(inventoryWrapper)).orElse(stoveStack);
					if (!resultStack.isEmpty()) {
						ItemUtils.spawnItemEntity(world, resultStack.copy(),
								pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
								MathUtils.RAND.nextGaussian() * (double) 0.01F, 0.1F, MathUtils.RAND.nextGaussian() * (double) 0.01F);
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

	public boolean addItem(ItemStack itemStackIn, int cookTime) {
		// TODO: Pass the recipe, then cache its ID
		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack.isEmpty()) {
				cookingTimesTotal[i] = cookTime;
				cookingTimes[i] = 0;
				inventory.setStackInSlot(i, itemStackIn.split(1));
				inventoryChanged();
				return true;
			}
		}
		return false;
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
		return world == null || isStoveFull() ? Optional.empty() : world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn), world);
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public boolean isStoveBlockedAbove() {
		if (world != null) {
			BlockState above = world.getBlockState(pos.up());
			return VoxelShapes.compare(GRILLING_AREA, above.getShape(world, pos.up()), IBooleanFunction.AND);
		}
		return false;
	}

	public boolean isStoveFull() {
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
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
		if (world == null) return;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			if (!inventory.getStackInSlot(i).isEmpty() && world.rand.nextFloat() < 0.2F) {
				Vector2f stoveItemVector = getStoveItemOffset(i);
				Direction direction = getBlockState().get(StoveBlock.HORIZONTAL_FACING);
				int directionIndex = direction.getHorizontalIndex();
				Vector2f offset = directionIndex % 2 == 0 ? stoveItemVector : new Vector2f(stoveItemVector.y, stoveItemVector.x);

				double x = ((double) pos.getX() + 0.5D) - (direction.getXOffset() * offset.x) + (direction.rotateY().getXOffset() * offset.x);
				double y = (double) pos.getY() + 1.0D;
				double z = ((double) pos.getZ() + 0.5D) - (direction.getZOffset() * offset.y) + (direction.rotateY().getZOffset() * offset.y);

				for (int k = 0; k < 3; ++k) {
					world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
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
