package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Optional;

public class StoveBlockEntity extends SyncedBlockEntity
{
	private static final VoxelShape GRILLING_AREA = Block.box(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);
	private static final int INVENTORY_SLOT_COUNT = 6;

	private final ItemStackHandler inventory;
	private final int[] cookingTimes;
	private final int[] cookingTimesTotal;
	private ResourceLocation[] lastRecipeIDs;

	public StoveBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.STOVE.get(), pos, state);
		inventory = createHandler();
		cookingTimes = new int[INVENTORY_SLOT_COUNT];
		cookingTimesTotal = new int[INVENTORY_SLOT_COUNT];
		lastRecipeIDs = new ResourceLocation[INVENTORY_SLOT_COUNT];
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
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
	public void saveAdditional(CompoundTag compound) {
		writeItems(compound);
		compound.putIntArray("CookingTimes", cookingTimes);
		compound.putIntArray("CookingTotalTimes", cookingTimesTotal);
	}

	private CompoundTag writeItems(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put("Inventory", inventory.serializeNBT());
		return compound;
	}

	public static void cookingTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
		boolean isStoveLit = state.getValue(StoveBlock.LIT);

		if (stove.isStoveBlockedAbove()) {
			if (!ItemUtils.isInventoryEmpty(stove.inventory)) {
				ItemUtils.dropItems(level, pos, stove.inventory);
				stove.inventoryChanged();
			}
		} else if (isStoveLit) {
			stove.cookAndOutputItems();
		} else {
			for (int i = 0; i < stove.inventory.getSlots(); ++i) {
				if (stove.cookingTimes[i] > 0) {
					stove.cookingTimes[i] = Mth.clamp(stove.cookingTimes[i] - 2, 0, stove.cookingTimesTotal[i]);
				}
			}
		}
	}

	public static void animationTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stove) {
		for (int i = 0; i < stove.inventory.getSlots(); ++i) {
			if (!stove.inventory.getStackInSlot(i).isEmpty() && level.random.nextFloat() < 0.2F) {
				Vec2 stoveItemVector = stove.getStoveItemOffset(i);
				Direction direction = state.getValue(StoveBlock.FACING);
				int directionIndex = direction.get2DDataValue();
				Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);

				double x = ((double) pos.getX() + 0.5D) - (direction.getStepX() * offset.x) + (direction.getClockWise().getStepX() * offset.x);
				double y = (double) pos.getY() + 1.0D;
				double z = ((double) pos.getZ() + 0.5D) - (direction.getStepZ() * offset.y) + (direction.getClockWise().getStepZ() * offset.y);

				for (int k = 0; k < 3; ++k) {
					level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
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
					Container inventoryWrapper = new SimpleContainer(stoveStack);
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

	public Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper, int slot) {
		if (level == null) return Optional.empty();

		if (lastRecipeIDs[slot] != null) {
			Recipe<Container> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
					.getRecipeMap(RecipeType.CAMPFIRE_COOKING)
					.get(lastRecipeIDs[slot]);
			if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, level)) {
				return Optional.of((CampfireCookingRecipe) recipe);
			}
		}

		return level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeWrapper, level);
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public boolean isStoveBlockedAbove() {
		if (level != null) {
			BlockState above = level.getBlockState(worldPosition.above());
			return Shapes.joinIsNotEmpty(GRILLING_AREA, above.getShape(level, worldPosition.above()), BooleanOp.AND);
		}
		return false;
	}

	public Vec2 getStoveItemOffset(int index) {
		final float X_OFFSET = 0.3F;
		final float Y_OFFSET = 0.2F;
		final Vec2[] OFFSETS = {
				new Vec2(X_OFFSET, Y_OFFSET),
				new Vec2(0.0F, Y_OFFSET),
				new Vec2(-X_OFFSET, Y_OFFSET),
				new Vec2(X_OFFSET, -Y_OFFSET),
				new Vec2(0.0F, -Y_OFFSET),
				new Vec2(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}

	private void addParticles() {
		if (level == null) return;

		for (int i = 0; i < inventory.getSlots(); ++i) {
			if (!inventory.getStackInSlot(i).isEmpty() && level.random.nextFloat() < 0.2F) {
				Vec2 stoveItemVector = getStoveItemOffset(i);
				Direction direction = getBlockState().getValue(StoveBlock.FACING);
				int directionIndex = direction.get2DDataValue();
				Vec2 offset = directionIndex % 2 == 0 ? stoveItemVector : new Vec2(stoveItemVector.y, stoveItemVector.x);

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
	public CompoundTag getUpdateTag() {
		return writeItems(new CompoundTag());
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
