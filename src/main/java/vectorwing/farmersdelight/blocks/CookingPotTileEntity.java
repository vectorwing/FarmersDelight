package vectorwing.farmersdelight.blocks;

import com.google.common.collect.Maps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SoupItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import vectorwing.farmersdelight.container.CookingPotContainer;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.init.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.Tags;
import vectorwing.farmersdelight.utils.Text;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CookingPotTileEntity extends LockableTileEntity implements IInventory, IRecipeHolder, INamedContainerProvider, IRecipeHelperPopulator, ITickableTileEntity
{
	public final int INPUT_SIZE = 6;
	public final int CONTAINER_INPUT = INPUT_SIZE + 1;
	public final int INVENTORY_SIZE = INPUT_SIZE + 3;
	protected NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
	private int cookTime;
	private int cookTimeTotal;
	protected final IIntArray cookingPotData = new IIntArray() {
		public int get(int index) {
			switch(index) {
				case 0:
					return CookingPotTileEntity.this.cookTime;
				case 1:
					return CookingPotTileEntity.this.cookTimeTotal;
				default:
					return 0;
			}
		}

		public void set(int index, int value) {
			switch(index) {
				case 0:
					CookingPotTileEntity.this.cookTime = value;
					break;
				case 1:
					CookingPotTileEntity.this.cookTimeTotal = value;
					break;
			}
		}
		public int size() {
			return 2;
		}
	};
	private final Map<ResourceLocation, Integer> recipes = Maps.newHashMap();
	protected final IRecipeType<? extends CookingPotRecipe> recipeType;

	public CookingPotTileEntity(TileEntityType<?> tileEntityTypeIn, IRecipeType<? extends CookingPotRecipe> recipeTypeIn) {
		super(tileEntityTypeIn);
		this.recipeType = recipeTypeIn;
	}

	public CookingPotTileEntity() {	this(ModTileEntityTypes.COOKING_POT_TILE.get(), CookingPotRecipe.TYPE); }

	// ======== NBT HANDLING ========

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
		this.cookTime = compound.getInt("CookTime");
		this.cookTimeTotal = compound.getInt("CookTimeTotal");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("CookTime", this.cookTime);
		compound.putInt("CookTimeTotal", this.cookTimeTotal);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	public CompoundNBT writeMealNbt(CompoundNBT compound) {
		if (this.isEmpty()) return compound;

		NonNullList<ItemStack> drops = NonNullList.create();
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.add(i == INPUT_SIZE ? this.items.get(i) : ItemStack.EMPTY);
		}
		ItemStackHelper.saveAllItems(compound, drops);
		return compound;
	}

	// ======== BASIC FUNCTIONALITY ========

	@Override
	public void tick() {
		boolean isHeated = this.isAboveLitHeatSource();
		boolean dirty = false;

		if (!this.world.isRemote) {
			if (isHeated && this.hasInput()) {
				IRecipe<?> irecipe = this.world.getRecipeManager()
						.getRecipe(this.recipeType, this, this.world).orElse(null);
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

			if (!this.items.get(INPUT_SIZE).isEmpty() && !this.items.get(INPUT_SIZE + 1).isEmpty()) {
				this.useStoredContainersOnMeal();
			}

		} else {
			if (isHeated) {
				this.addParticles();
			}
		}

		if (dirty) {
			this.markDirty();
		}
	}

	protected int getCookTime() {
		return this.world.getRecipeManager().getRecipe(this.recipeType, this, this.world).map(CookingPotRecipe::getCookTime).orElse(200);
	}

	private boolean hasInput() {
		for (int i = 0; i < INPUT_SIZE; ++i) {
			if (!this.items.get(i).isEmpty()) return true;
		}
		return false;
	}

	protected boolean canCook(@Nullable IRecipe<?> recipeIn) {
		if (this.hasInput() && recipeIn != null) {
			ItemStack recipeOutput = recipeIn.getRecipeOutput();
			if (recipeOutput.isEmpty()) {
				return false;
			} else {
				ItemStack currentOutput = this.items.get(INPUT_SIZE);
				if (currentOutput.isEmpty()) {
					return true;
				} else if (!currentOutput.isItemEqual(recipeOutput)) {
					return false;
				} else if (currentOutput.getCount() + recipeOutput.getCount() <= this.getInventoryStackLimit()) {
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
			ItemStack recipeOutput = recipe.getRecipeOutput();
			ItemStack currentOutput = this.items.get(INPUT_SIZE);
			if (currentOutput.isEmpty()) {
				this.items.set(INPUT_SIZE, recipeOutput.copy());
			} else if (currentOutput.getItem() == recipeOutput.getItem()) {
				currentOutput.grow(recipeOutput.getCount());
			}
		}
		for (int i = 0; i < INPUT_SIZE; ++i) {
			if (this.items.get(i).hasContainerItem()) {
				Direction direction = this.getBlockState().get(CookingPotBlock.FACING).rotateYCCW();
				ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, this.items.get(i).getContainerItem());
				entity.setMotion(direction.getXOffset() * 0.1F, 0.2F, direction.getZOffset() * 0.1F);
				world.addEntity(entity);
			}
			if (!this.items.get(i).isEmpty())
				this.items.get(i).shrink(1);
		}
	}

	private void addParticles() {
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
		return this.items.get(INPUT_SIZE);
	}

	// ======== CUSTOM THINGS ========

	/**
	 * Checks if the pot is on top of a heat source using the tag farmersdelight:heat_sources.
	 * If the given block has a LIT state, it will check if that state is true.
	 */
	public boolean isAboveLitHeatSource() {
		if (world == null)
			return false;
		BlockState checkState = world.getBlockState(pos.down());
		if (Tags.HEAT_SOURCES.contains(checkState.getBlock())) {
			if (checkState.has(BlockStateProperties.LIT))
				return checkState.get(BlockStateProperties.LIT);
			return true;
		}
		return false;
	}

	/**
	 * Returns every stored ItemStack in the pot, except for prepared meals.
	 */
	public NonNullList<ItemStack> getDroppableInventory() {
		NonNullList<ItemStack> drops = NonNullList.create();
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			drops.add(i == INPUT_SIZE ? ItemStack.EMPTY : this.items.get(i));
		}
		return drops;
	}

	/**
	 * Attempts to generate an ItemStack output using the meal and the inputted container together.
	 * If input and meal containers don't match, nothing happens.
	 */
	private void useStoredContainersOnMeal() {
		ItemStack mealDisplay = this.items.get(INPUT_SIZE);
		ItemStack containerInput = this.items.get(CONTAINER_INPUT);
		ItemStack finalOutput = this.items.get(CONTAINER_INPUT + 1);
		boolean hasBowlAndSoupItem = containerInput.getItem() == Items.BOWL && mealDisplay.getItem() instanceof SoupItem;
		boolean containerMatchesMeal = containerInput.isItemEqual(mealDisplay.getContainerItem());
		if ((hasBowlAndSoupItem || containerMatchesMeal) && finalOutput.getCount() < finalOutput.getMaxStackSize()) {
			int smallerStack = Math.min(mealDisplay.getCount(), containerInput.getCount());
			int mealCount = Math.min(smallerStack, mealDisplay.getMaxStackSize() - finalOutput.getCount());
			if (finalOutput.isEmpty()) {
				containerInput.shrink(mealCount);
				this.items.set(CONTAINER_INPUT + 1, mealDisplay.split(mealCount));
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
		if (container.isItemEqual(this.getMeal().getContainerItem())) {
			container.shrink(1);
			return this.getMeal().split(1);
		}
		return ItemStack.EMPTY;
	}

	// ======== IINVENTORY ========

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index >= 0 && index < INPUT_SIZE && !flag) {
			this.cookTimeTotal = this.getCookTime();
			this.markDirty();
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	// ======== ICLEARABLE ========

	public void clear() {
		this.items.clear();
	}

	// ======== LOCKABLE TILE ENTITY ========

	@Override
	protected ITextComponent getDefaultName()
	{
		return Text.getTranslation("container.cooking_pot");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return new CookingPotContainer(id, player, this, this.cookingPotData);
	}

	@Override
	public void fillStackedContents(RecipeItemHelper helper) {
		for(ItemStack itemstack : this.items) {
			helper.accountStack(itemstack);
		}
	}

	// ======== CAPABILITIES ========

	/*
	 * TODO: Give the Cooking Pot proper inventory handling for automation. Study Capabilities better.
	 * As it stands now, the pot is delegating capabilities to LockableTileEntity.
	 * This is making it allow items to be inserted across its entire inventory, outputs included.
	 * Obviously this is bad behavior, but there is a game-affecting bug to be fixed for now.
	 * Release this fix, then come back to properly implement this later.
	 */
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		return super.getCapability(cap, side);
	}

	@Override
	public void setRecipeUsed(@Nullable IRecipe<?> recipe) { }

	@Nullable
	@Override
	public IRecipe<?> getRecipeUsed() {	return null; }
}
