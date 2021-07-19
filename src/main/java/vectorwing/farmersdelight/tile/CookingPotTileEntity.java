package vectorwing.farmersdelight.tile;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.blocks.CookingPotBlock;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.mixin.accessors.RecipeManagerAccessor;
import vectorwing.farmersdelight.registry.ModParticleTypes;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.container.CookingPotContainer;
import vectorwing.farmersdelight.tile.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.utils.ItemUtils;
import vectorwing.farmersdelight.utils.TextUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CookingPotTileEntity extends FDSyncedTileEntity implements INamedContainerProvider, ITickableTileEntity, INameable
{
	public static final int MEAL_DISPLAY_SLOT = 6;
	public static final int CONTAINER_SLOT = 7;
	public static final int OUTPUT_SLOT = 8;
	public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

	private final ItemStackHandler inventory;
	private final LazyOptional<IItemHandler> inputHandler;
	private final LazyOptional<IItemHandler> outputHandler;

	private int cookTime;
	private int cookTimeTotal;
	private ItemStack mealContainerStack;
	private ITextComponent customName;

	protected final IIntArray cookingPotData;
	private final Object2IntOpenHashMap<ResourceLocation> experienceTracker;

	private ResourceLocation lastRecipeID;
	private boolean checkNewRecipe;

	public CookingPotTileEntity() {
		super(ModTileEntityTypes.COOKING_POT_TILE.get());
		this.inventory = createHandler();
		this.inputHandler = LazyOptional.of(() -> new CookingPotItemHandler(inventory, Direction.UP));
		this.outputHandler = LazyOptional.of(() -> new CookingPotItemHandler(inventory, Direction.DOWN));
		this.mealContainerStack = ItemStack.EMPTY;
		this.cookingPotData = createIntArray();
		this.experienceTracker = new Object2IntOpenHashMap<>();
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		cookTime = compound.getInt("CookTime");
		cookTimeTotal = compound.getInt("CookTimeTotal");
		mealContainerStack = ItemStack.read(compound.getCompound("Container"));
		if (compound.contains("CustomName", 8)) {
			customName = ITextComponent.Serializer.getComponentFromJson(compound.getString("CustomName"));
		}
		CompoundNBT compoundRecipes = compound.getCompound("RecipesUsed");
		for (String key : compoundRecipes.keySet()) {
			experienceTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("CookTime", cookTime);
		compound.putInt("CookTimeTotal", cookTimeTotal);
		compound.put("Container", mealContainerStack.serializeNBT());
		if (customName != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(customName));
		}
		compound.put("Inventory", inventory.serializeNBT());
		CompoundNBT compoundRecipes = new CompoundNBT();
		experienceTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
		compound.put("RecipesUsed", compoundRecipes);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		compound.put("Container", mealContainerStack.serializeNBT());
		compound.put("Inventory", inventory.serializeNBT());
		return compound;
	}

	public CompoundNBT writeMeal(CompoundNBT compound) {
		if (getMeal().isEmpty()) return compound;

		ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.setStackInSlot(i, i == MEAL_DISPLAY_SLOT ? inventory.getStackInSlot(i) : ItemStack.EMPTY);
		}
		if (customName != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(customName));
		}
		compound.put("Container", mealContainerStack.serializeNBT());
		compound.put("Inventory", drops.serializeNBT());
		return compound;
	}

	// ======== BASIC FUNCTIONALITY ========

	@Override
	public void tick() {
		if (world == null) return;

		boolean isHeated = isHeated();
		boolean didInventoryChange = false;

		if (!world.isRemote) {
			if (isHeated && hasInput()) {
				Optional<CookingPotRecipe> recipe = getMatchingRecipe(new RecipeWrapper(inventory));
				if (recipe.isPresent() && canCook(recipe.get())) {
					didInventoryChange = processCooking(recipe.get());
				} else {
					cookTime = 0;
				}
			} else if (cookTime > 0) {
				cookTime = MathHelper.clamp(cookTime - 2, 0, cookTimeTotal);
			}

			ItemStack mealStack = getMeal();
			if (!mealStack.isEmpty()) {
				if (!doesMealHaveContainer(mealStack)) {
					moveMealToOutput();
					didInventoryChange = true;
				} else if (!inventory.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
					useStoredContainersOnMeal();
					didInventoryChange = true;
				}
			}

		} else {
			if (isHeated) {
				animate();
			}
		}

		if (didInventoryChange) {
			inventoryChanged();
		}
	}

	private Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper) {
		if (world == null) return Optional.empty();

		if (lastRecipeID != null) {
			IRecipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) world.getRecipeManager())
					.getRecipeMap(CookingPotRecipe.TYPE)
					.get(lastRecipeID);
			if (recipe instanceof CookingPotRecipe) {
				if (recipe.matches(inventoryWrapper, world)) {
					return Optional.of((CookingPotRecipe) recipe);
				}
				if (recipe.getRecipeOutput().isItemEqual(getMeal())) {
					return Optional.empty();
				}
			}
		}

		if (checkNewRecipe) {
			Optional<CookingPotRecipe> recipe = world.getRecipeManager().getRecipe(CookingPotRecipe.TYPE, inventoryWrapper, world);
			if (recipe.isPresent()) {
				lastRecipeID = recipe.get().getId();
				return recipe;
			}
		}

		checkNewRecipe = false;
		return Optional.empty();
	}

	public ItemStack getContainer() {
		if (!mealContainerStack.isEmpty()) {
			return mealContainerStack;
		} else {
			return getMeal().getContainerItem();
		}
	}

	private boolean hasInput() {
		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			if (!inventory.getStackInSlot(i).isEmpty()) return true;
		}
		return false;
	}

	protected boolean canCook(CookingPotRecipe recipe) {
		if (hasInput()) {
			ItemStack resultStack = recipe.getRecipeOutput();
			if (resultStack.isEmpty()) {
				return false;
			} else {
				ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
				if (storedMealStack.isEmpty()) {
					return true;
				} else if (!storedMealStack.isItemEqual(resultStack)) {
					return false;
				} else if (storedMealStack.getCount() + resultStack.getCount() <= inventory.getSlotLimit(MEAL_DISPLAY_SLOT)) {
					return true;
				} else {
					return storedMealStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	private boolean processCooking(CookingPotRecipe recipe) {
		if (world == null) return false;

		++cookTime;
		cookTimeTotal = recipe.getCookTime();
		if (cookTime < cookTimeTotal) {
			return false;
		}

		cookTime = 0;
		mealContainerStack = recipe.getOutputContainer();
		ItemStack resultStack = recipe.getRecipeOutput();
		ItemStack storedMealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		if (storedMealStack.isEmpty()) {
			inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
		} else if (storedMealStack.isItemEqual(resultStack)) {
			storedMealStack.grow(resultStack.getCount());
		}
		trackRecipeExperience(recipe);

		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack.hasContainerItem()) {
				Direction direction = getBlockState().get(CookingPotBlock.HORIZONTAL_FACING).rotateYCCW();
				double x = pos.getX() + 0.5 + (direction.getXOffset() * 0.25);
				double y = pos.getY() + 0.7;
				double z = pos.getZ() + 0.5 + (direction.getZOffset() * 0.25);
				ItemUtils.spawnItemEntity(world, inventory.getStackInSlot(i).getContainerItem(), x, y, z,
						direction.getXOffset() * 0.08F, 0.25F, direction.getZOffset() * 0.08F);
			}
			if (!slotStack.isEmpty())
				slotStack.shrink(1);
		}
		return true;
	}

	private void animate() {
		if (world == null) return;

		Random random = world.rand;
		if (random.nextFloat() < 0.2F) {
			double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
			double y = (double) pos.getY() + 0.7D;
			double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
			world.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, 0.0D, 0.0D);
		}
		if (random.nextFloat() < 0.05F) {
			double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
			double y = (double) pos.getY() + 0.5D;
			double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
			world.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}

	public void trackRecipeExperience(@Nullable IRecipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation recipeID = recipe.getId();
			experienceTracker.addTo(recipeID, 1);
		}
	}

	public void clearUsedRecipes(PlayerEntity player) {
		grantStoredRecipeExperience(player.world, player.getPositionVec());
		experienceTracker.clear();
	}

	public void grantStoredRecipeExperience(World world, Vector3d pos) {
		for (Object2IntMap.Entry<ResourceLocation> entry : experienceTracker.object2IntEntrySet()) {
			world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> splitAndSpawnExperience(world, pos, entry.getIntValue(), ((CookingPotRecipe) recipe).getExperience()));
		}
	}

	private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
		int expTotal = MathHelper.floor((float) craftedAmount * experience);
		float expFraction = MathHelper.frac((float) craftedAmount * experience);
		if (expFraction != 0.0F && Math.random() < (double) expFraction) {
			++expTotal;
		}

		while (expTotal > 0) {
			int expValue = ExperienceOrbEntity.getXPSplit(expTotal);
			expTotal -= expValue;
			world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, expValue));
		}
	}

	public boolean isHeated() {
		if (world == null) return false;

		BlockState stateBelow = world.getBlockState(pos.down());

		if (ModTags.HEAT_SOURCES.contains(stateBelow.getBlock())) {
			if (stateBelow.hasProperty(BlockStateProperties.LIT))
				return stateBelow.get(BlockStateProperties.LIT);
			return true;
		}

		if (ModTags.HEAT_CONDUCTORS.contains(stateBelow.getBlock())) {
			BlockState stateFurtherBelow = world.getBlockState(pos.down(2));
			if (ModTags.HEAT_SOURCES.contains(stateFurtherBelow.getBlock())) {
				if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT))
					return stateFurtherBelow.get(BlockStateProperties.LIT);
				return true;
			}
		}

		return false;
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	public ItemStack getMeal() {
		return inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
	}

	public NonNullList<ItemStack> getDroppableInventory() {
		NonNullList<ItemStack> drops = NonNullList.create();
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			if (i != MEAL_DISPLAY_SLOT) {
				drops.add(inventory.getStackInSlot(i));
			}
		}
		return drops;
	}

	private void moveMealToOutput() {
		ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
		int mealCount = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
		if (outputStack.isEmpty()) {
			inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
		} else if (outputStack.getItem() == mealStack.getItem()) {
			mealStack.shrink(mealCount);
			outputStack.grow(mealCount);
		}
	}

	private void useStoredContainersOnMeal() {
		ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack containerInputStack = inventory.getStackInSlot(CONTAINER_SLOT);
		ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

		if (isContainerValid(containerInputStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
			int smallerStackCount = Math.min(mealStack.getCount(), containerInputStack.getCount());
			int mealCount = Math.min(smallerStackCount, mealStack.getMaxStackSize() - outputStack.getCount());
			if (outputStack.isEmpty()) {
				containerInputStack.shrink(mealCount);
				inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(mealCount));
			} else if (outputStack.getItem() == mealStack.getItem()) {
				mealStack.shrink(mealCount);
				containerInputStack.shrink(mealCount);
				outputStack.grow(mealCount);
			}
		}
	}

	public ItemStack useHeldItemOnMeal(ItemStack container) {
		if (isContainerValid(container) && !getMeal().isEmpty()) {
			container.shrink(1);
			return getMeal().split(1);
		}
		return ItemStack.EMPTY;
	}

	private boolean doesMealHaveContainer(ItemStack meal) {
		return !mealContainerStack.isEmpty() || meal.hasContainerItem();
	}

	public boolean isContainerValid(ItemStack containerItem) {
		if (containerItem.isEmpty()) return false;
		if (!mealContainerStack.isEmpty()) {
			return mealContainerStack.isItemEqual(containerItem);
		} else {
			return getMeal().getContainerItem().isItemEqual(containerItem);
		}
	}

	@Override
	public ITextComponent getName() {
		return customName != null ? customName : TextUtils.getTranslation("container.cooking_pot");
	}

	@Override
	public ITextComponent getDisplayName() {
		return getName();
	}

	@Override
	@Nullable
	public ITextComponent getCustomName() {
		return customName;
	}

	public void setCustomName(ITextComponent name) {
		customName = name;
	}

	@Override
	public Container createMenu(int id, PlayerInventory player, PlayerEntity entity) {
		return new CookingPotContainer(id, player, this, cookingPotData);
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
			if (side == null || side.equals(Direction.UP)) {
				return inputHandler.cast();
			} else {
				return outputHandler.cast();
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void remove() {
		super.remove();
		inputHandler.invalidate();
		outputHandler.invalidate();
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return writeItems(new CompoundNBT());
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SIZE)
		{
			@Override
			protected void onContentsChanged(int slot) {
				if (slot >= 0 && slot < MEAL_DISPLAY_SLOT) {
					checkNewRecipe = true;
					inventoryChanged();
				}
			}
		};
	}

	private IIntArray createIntArray() {
		return new IIntArray()
		{
			@Override
			public int get(int index) {
				switch (index) {
					case 0:
						return CookingPotTileEntity.this.cookTime;
					case 1:
						return CookingPotTileEntity.this.cookTimeTotal;
					default:
						return 0;
				}
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0:
						CookingPotTileEntity.this.cookTime = value;
						break;
					case 1:
						CookingPotTileEntity.this.cookTimeTotal = value;
						break;
				}
			}

			@Override
			public int size() {
				return 2;
			}
		};
	}
}
