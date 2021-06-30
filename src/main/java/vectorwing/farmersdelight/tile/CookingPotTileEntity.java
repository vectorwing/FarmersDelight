package vectorwing.farmersdelight.tile;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.blocks.CookingPotBlock;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.container.CookingPotContainer;
import vectorwing.farmersdelight.tile.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.utils.TextUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CookingPotTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity, INameable
{
	public static final int MEAL_DISPLAY_SLOT = 6;
	public static final int CONTAINER_SLOT = 7;
	public static final int OUTPUT_SLOT = 8;
	public static final int INVENTORY_SIZE = OUTPUT_SLOT + 1;

	private ItemStackHandler itemHandler = createHandler();
	private LazyOptional<IItemHandler> handlerInput = LazyOptional.of(() -> new CookingPotItemHandler(itemHandler, Direction.UP));
	private LazyOptional<IItemHandler> handlerOutput = LazyOptional.of(() -> new CookingPotItemHandler(itemHandler, Direction.DOWN));

	private ITextComponent customName;

	private int cookTime;
	private int cookTimeTotal;
	private ItemStack container;
	protected final IIntArray cookingPotData = new IIntArray()
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
	private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
	protected final IRecipeType<? extends CookingPotRecipe> recipeType;

	public CookingPotTileEntity(TileEntityType<?> tileEntityTypeIn, IRecipeType<? extends CookingPotRecipe> recipeTypeIn) {
		super(tileEntityTypeIn);
		this.recipeType = recipeTypeIn;
		this.container = ItemStack.EMPTY;
	}

	public CookingPotTileEntity() {
		this(ModTileEntityTypes.COOKING_POT_TILE.get(), CookingPotRecipe.TYPE);
	}

	// ======== NBT & NETWORKING ========

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.writeItems(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(this.getBlockState(), pkt.getNbtCompound());
	}

	private void inventoryChanged() {
		super.markDirty();
		this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.itemHandler.deserializeNBT(compound.getCompound("Inventory"));
		this.cookTime = compound.getInt("CookTime");
		this.cookTimeTotal = compound.getInt("CookTimeTotal");
		this.container = ItemStack.read(compound.getCompound("Container"));
		if (compound.contains("CustomName", 8)) {
			this.customName = ITextComponent.Serializer.getComponentFromJson(compound.getString("CustomName"));
		}

		CompoundNBT compoundRecipes = compound.getCompound("RecipesUsed");
		for (String s : compoundRecipes.keySet()) {
			this.recipes.put(new ResourceLocation(s), compoundRecipes.getInt(s));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("CookTime", this.cookTime);
		compound.putInt("CookTimeTotal", this.cookTimeTotal);
		compound.put("Container", this.container.serializeNBT());
		if (this.customName != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
		}
		compound.put("Inventory", itemHandler.serializeNBT());
		CompoundNBT compoundRecipes = new CompoundNBT();
		this.recipes.forEach((recipeId, craftedAmount) -> {
			compoundRecipes.putInt(recipeId.toString(), craftedAmount);
		});
		compound.put("RecipesUsed", compoundRecipes);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		compound.put("Container", this.container.serializeNBT());
		compound.put("Inventory", itemHandler.serializeNBT());
		return compound;
	}

	public CompoundNBT writeMeal(CompoundNBT compound) {
		if (this.getMeal().isEmpty()) return compound;

		ItemStackHandler drops = new ItemStackHandler(INVENTORY_SIZE);
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.setStackInSlot(i, i == MEAL_DISPLAY_SLOT ? itemHandler.getStackInSlot(i) : ItemStack.EMPTY);
		}
		if (this.customName != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
		}
		compound.put("Container", this.container.serializeNBT());
		compound.put("Inventory", drops.serializeNBT());
		return compound;
	}

	// ======== BASIC FUNCTIONALITY ========

	@Override
	public void tick() {
		boolean isHeated = this.isHeated();
		boolean dirty = false;

		if (!this.world.isRemote) {
			if (isHeated && this.hasInput()) {
				CookingPotRecipe irecipe = this.world.getRecipeManager()
						.getRecipe(this.recipeType, new RecipeWrapper(itemHandler), this.world).orElse(null);
				if (this.canCook(irecipe)) {
					++this.cookTime;
					if (this.cookTime == this.cookTimeTotal) {
						this.cookTime = 0;
						this.cookTimeTotal = this.getCookTime();
						this.cook(irecipe);
						dirty = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
			}

			ItemStack meal = this.getMeal();
			if (!meal.isEmpty()) {
				if (!this.doesMealHaveContainer(meal)) {
					this.moveMealToOutput();
					dirty = true;
				} else if (!itemHandler.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
					this.useStoredContainersOnMeal();
					dirty = true;
				}
			}

		} else {
			if (isHeated) {
				this.animate();
			}
		}

		if (dirty) {
			this.inventoryChanged();
		}
	}

	protected int getCookTime() {
		return this.world.getRecipeManager().getRecipe(this.recipeType, new RecipeWrapper(itemHandler), this.world).map(CookingPotRecipe::getCookTime).orElse(200);
	}

	protected ItemStack getRecipeContainer() {
		return this.world.getRecipeManager().getRecipe(this.recipeType, new RecipeWrapper(itemHandler), this.world).map(CookingPotRecipe::getOutputContainer).orElse(ItemStack.EMPTY);
	}

	public ItemStack getContainer() {
		if (!this.container.isEmpty()) {
			return this.container;
		} else {
			return this.getMeal().getContainerItem();
		}
	}

	private boolean hasInput() {
		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			if (!itemHandler.getStackInSlot(i).isEmpty()) return true;
		}
		return false;
	}

	protected boolean canCook(@Nullable IRecipe<?> recipeIn) {
		if (this.hasInput() && recipeIn != null) {
			ItemStack recipeOutput = recipeIn.getRecipeOutput();
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				ItemStack currentOutput = itemHandler.getStackInSlot(MEAL_DISPLAY_SLOT);
				if (currentOutput.isEmpty()) {
					return true;
				} else if (!currentOutput.isItemEqual(recipeOutput)) {
					return false;
				} else if (currentOutput.getCount() + recipeOutput.getCount() <= itemHandler.getSlotLimit(MEAL_DISPLAY_SLOT)) {
					return true;
				} else {
					return currentOutput.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
				}
			}
		} else {
			return false;
		}
	}

	private void cook(@Nullable IRecipe<?> recipe) {
		if (recipe != null && this.canCook(recipe)) {
			this.container = this.getRecipeContainer();
			ItemStack recipeOutput = recipe.getRecipeOutput();
			ItemStack currentOutput = itemHandler.getStackInSlot(MEAL_DISPLAY_SLOT);
			if (currentOutput.isEmpty()) {
				itemHandler.setStackInSlot(MEAL_DISPLAY_SLOT, recipeOutput.copy());
			} else if (currentOutput.getItem() == recipeOutput.getItem()) {
				currentOutput.grow(recipeOutput.getCount());
			}
			if (this.world != null && !this.world.isRemote) {
				this.setRecipeUsed(recipe);
			}
		}
		for (int i = 0; i < MEAL_DISPLAY_SLOT; ++i) {
			if (itemHandler.getStackInSlot(i).hasContainerItem()) {
				Direction direction = this.getBlockState().get(CookingPotBlock.HORIZONTAL_FACING).rotateYCCW();
				double dropX = pos.getX() + 0.5 + (direction.getXOffset() * 0.25);
				double dropY = pos.getY() + 0.7;
				double dropZ = pos.getZ() + 0.5 + (direction.getZOffset() * 0.25);
				ItemEntity entity = new ItemEntity(world, dropX, dropY, dropZ, itemHandler.getStackInSlot(i).getContainerItem());
				entity.setMotion(direction.getXOffset() * 0.08F, 0.25F, direction.getZOffset() * 0.08F);
				world.addEntity(entity);
			}
			if (!itemHandler.getStackInSlot(i).isEmpty())
				itemHandler.getStackInSlot(i).shrink(1);
		}
	}

	private void animate() {
		World world = this.getWorld();
		if (world != null) {
			BlockPos blockpos = this.getPos();
			Random random = world.rand;
			if (random.nextFloat() < 0.2F) {
				double baseX = (double) blockpos.getX() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
				double baseY = (double) blockpos.getY() + 0.7D;
				double baseZ = (double) blockpos.getZ() + 0.5D + (random.nextDouble() * 0.6D - 0.3D);
				world.addParticle(ParticleTypes.BUBBLE_POP, baseX, baseY, baseZ, 0.0D, 0.0D, 0.0D);
			}
			if (random.nextFloat() < 0.05F) {
				double baseX = (double) blockpos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double baseY = (double) blockpos.getY() + 0.7D;
				double baseZ = (double) blockpos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				world.addParticle(ParticleTypes.EFFECT, baseX, baseY, baseZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	public ItemStack getMeal() {
		return itemHandler.getStackInSlot(MEAL_DISPLAY_SLOT);
	}

	public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
		if (recipe != null) {
			ResourceLocation resourcelocation = recipe.getId();
			this.recipes.addTo(resourcelocation, 1);
		}
	}

	public void clearUsedRecipes(PlayerEntity player) {
		this.grantStoredRecipeExperience(player.world, player.getPositionVec());
		this.recipes.clear();
	}

	public void grantStoredRecipeExperience(World world, Vector3d pos) {

		for (Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
			world.getRecipeManager().getRecipe(entry.getKey()).ifPresent((recipe) -> {
				splitAndSpawnExperience(world, pos, entry.getIntValue(), ((CookingPotRecipe) recipe).getExperience());
			});
		}
	}

	private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
		int i = MathHelper.floor((float) craftedAmount * experience);
		float f = MathHelper.frac((float) craftedAmount * experience);
		if (f != 0.0F && Math.random() < (double) f) {
			++i;
		}

		while (i > 0) {
			int j = ExperienceOrbEntity.getXPSplit(i);
			i -= j;
			world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
		}

	}

	// ======== CUSTOM THINGS ========

	/**
	 * Checks if the pot is receiving heat from below. This can happen in two ways:
	 * - The block directly below is in the "heat_sources" tag;
	 * - There is a block from "heat_conductors" directly between the Cooking Pot and a "heat_sources" block.
	 */
	public boolean isHeated() {
		if (world == null)
			return false;

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

	/**
	 * Returns every stored ItemStack in the pot, except for prepared meals.
	 */
	public NonNullList<ItemStack> getDroppableInventory() {
		NonNullList<ItemStack> drops = NonNullList.create();
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.add(i == MEAL_DISPLAY_SLOT ? ItemStack.EMPTY : itemHandler.getStackInSlot(i));
		}
		return drops;
	}

	/**
	 * Attempts to move all stored meals to the final output.
	 * Does NOT check if the meal has a container; this is done on tick.
	 */
	private void moveMealToOutput() {
		ItemStack mealDisplay = itemHandler.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack finalOutput = itemHandler.getStackInSlot(OUTPUT_SLOT);
		int mealCount = Math.min(mealDisplay.getCount(), mealDisplay.getMaxStackSize() - finalOutput.getCount());
		if (finalOutput.isEmpty()) {
			itemHandler.setStackInSlot(OUTPUT_SLOT, mealDisplay.split(mealCount));
		} else if (finalOutput.getItem() == mealDisplay.getItem()) {
			mealDisplay.shrink(mealCount);
			finalOutput.grow(mealCount);
		}
	}

	/**
	 * Attempts to generate an ItemStack output using the meal and the inputted container together.
	 * If input and meal containers don't match, nothing happens.
	 */
	private void useStoredContainersOnMeal() {
		ItemStack mealDisplay = itemHandler.getStackInSlot(MEAL_DISPLAY_SLOT);
		ItemStack containerInput = itemHandler.getStackInSlot(CONTAINER_SLOT);
		ItemStack finalOutput = itemHandler.getStackInSlot(OUTPUT_SLOT);

		if (isContainerValid(containerInput) && finalOutput.getCount() < finalOutput.getMaxStackSize()) {
			int smallerStack = Math.min(mealDisplay.getCount(), containerInput.getCount());
			int mealCount = Math.min(smallerStack, mealDisplay.getMaxStackSize() - finalOutput.getCount());
			if (finalOutput.isEmpty()) {
				containerInput.shrink(mealCount);
				itemHandler.setStackInSlot(OUTPUT_SLOT, mealDisplay.split(mealCount));
			} else if (finalOutput.getItem() == mealDisplay.getItem()) {
				mealDisplay.shrink(mealCount);
				containerInput.shrink(mealCount);
				finalOutput.grow(mealCount);
			}
		}
	}

	/**
	 * Checks if the given ItemStack is a container for the stored meal. If true, takes a serving and returns it.
	 */
	public ItemStack useHeldItemOnMeal(ItemStack container) {
		if (isContainerValid(container) && !this.getMeal().isEmpty()) {
			container.shrink(1);
			return this.getMeal().split(1);
		}
		return ItemStack.EMPTY;
	}

	private boolean doesMealHaveContainer(ItemStack meal) {
		return !this.container.isEmpty() || meal.hasContainerItem();
	}

	public boolean isContainerValid(ItemStack containerItem) {
		if (containerItem.isEmpty()) return false;
		if (!this.container.isEmpty()) {
			return this.container.isItemEqual(containerItem);
		} else {
			return this.getMeal().getContainerItem().isItemEqual(containerItem);
		}
	}

	public ItemStackHandler getInventory() {
		return this.itemHandler;
	}

	// ======== CONTAINER AND NAME PROVIDER ========

	public void setCustomName(ITextComponent name) {
		this.customName = name;
	}

	@Override
	public ITextComponent getName() {
		return this.customName != null
				? this.customName
				: TextUtils.getTranslation("container.cooking_pot");
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.getName();
	}

	@Override
	@Nullable
	public ITextComponent getCustomName() {
		return this.customName;
	}

	@Override
	public Container createMenu(int id, PlayerInventory player, PlayerEntity entity) {
		return new CookingPotContainer(id, player, this, this.cookingPotData);
	}

	// ======== CAPABILITIES ========

	private ItemStackHandler createHandler() {
		return new ItemStackHandler(INVENTORY_SIZE)
		{
			@Override
			protected void onContentsChanged(int slot) {
				if (slot >= 0 && slot < MEAL_DISPLAY_SLOT) {
					cookTimeTotal = getCookTime();
					inventoryChanged();
				}
			}
		};
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
			if (side == null || side.equals(Direction.UP)) {
				return handlerInput.cast();
			} else {
				return handlerOutput.cast();
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void remove() {
		super.remove();
		handlerInput.invalidate();
		handlerOutput.invalidate();
	}
}
