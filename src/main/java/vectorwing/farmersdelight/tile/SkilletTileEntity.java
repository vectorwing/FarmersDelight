package vectorwing.farmersdelight.tile;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.SkilletBlock;
import vectorwing.farmersdelight.client.sound.SkilletSizzleTickableSound;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.TextUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class SkilletTileEntity extends TileEntity implements ITickableTileEntity, IHeatableTileEntity
{
	private int cookingTime;
	private boolean isSizzling;

	private CampfireCookingRecipe currentRecipe;

	private ItemStackHandler inventory = createHandler();
	private LazyOptional<IItemHandler> handlerInput = LazyOptional.of(() -> inventory);

	public SkilletTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public SkilletTileEntity() {
		this(ModTileEntityTypes.SKILLET_TILE.get());
	}

	@Override
	public void tick() {
		if (this.world == null) {
			return;
		}
		boolean isHeated = this.isHeated(this.world, this.pos);
		if (!this.world.isRemote) {
			if (isHeated) {
				ItemStack cookingStack = this.getStoredStack();
				if (cookingStack.isEmpty()) {
					this.cookingTime = 0;
				} else if (this.canCook(cookingStack, this.world)) {
					++this.cookingTime;
					if (this.cookingTime >= this.getCookingTime()) {
						ItemStack resultStack = currentRecipe.getCraftingResult(new Inventory(cookingStack));

						ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, resultStack.copy());
						Direction direction = this.getBlockState().get(SkilletBlock.HORIZONTAL_FACING).getOpposite().rotateYCCW(); // TODO: Reorient Skilet, maybe?
						entity.setMotion(direction.getXOffset() * 0.08F, 0.25F, direction.getZOffset() * 0.08F);
						world.addEntity(entity);

						this.cookingTime = 0;
						cookingStack.shrink(1);
						this.inventoryChanged();
					}
				}
			} else if (this.cookingTime > 0) {
				this.cookingTime = MathHelper.clamp(this.cookingTime - 2, 0, this.getCookingTime());
			}
		} else {
			if (isHeated) {
				this.addParticles();
				if (!isSizzling && this.hasStoredStack()) {
					Minecraft.getInstance().getSoundHandler().play(new SkilletSizzleTickableSound(this));
					isSizzling = true;
				}
				if (!this.hasStoredStack() && isSizzling) {
					isSizzling = false;
				}
			} else {
				isSizzling = false;
			}
		}
	}

	public boolean isHeated() {
		if (world != null) {
			return this.isHeated(this.world, this.pos);
		}
		return false;
	}

	/**
	 * Checks if the given stack can be cooked.
	 * First, it looks into the cached recipe for a match. If not found, it searches for a recipe that matches the item.
	 * If a match is found, this new recipe is cached, and it returns true. If none are, the cache is cleared, and returns false.
	 */
	public boolean canCook(ItemStack stack, World world) {
		if (currentRecipe != null && currentRecipe.matches(new Inventory(stack), world)) {
			return true;
		} else {
			Optional<CampfireCookingRecipe> recipe = this.findMatchingRecipe(stack);
			if (recipe.isPresent()) {
				this.currentRecipe = recipe.get();
				return true;
			}
		}
		return false;
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
		return world == null
				? Optional.empty()
				: this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn), this.world);
	}

	// TODO: Make proper sizzling particles for the Skillet
	private void addParticles() {
		World world = this.getWorld();
		if (world != null) {
			BlockPos blockpos = this.getPos();
			Random random = world.rand;
			if (random.nextFloat() < 0.05F) {
				double baseX = (double) blockpos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double baseY = (double) blockpos.getY() + 0.3D;
				double baseZ = (double) blockpos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				world.addParticle(ParticleTypes.EFFECT, baseX, baseY, baseZ, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.inventory.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", inventory.serializeNBT());
		return compound;
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(this.getBlockState(), pkt.getNbtCompound());
	}

	// Logic

	public int getCookingTime() {
		return 120; // TODO: Check if Fire Aspect exists, and return shorter times accordingly.
	}

	// Inventory Handling

	/**
	 * Adds the given stack to the Skillet, returning the remainder.
	 * The item must have a valid recipe for Campfire cooking, or else it fails to be added.
	 * A PlayerEntity can be passed optionally, to notify them that the item can't be cooked with a status message.
	 */
	public ItemStack addItemToCook(ItemStack addedStack, @Nullable PlayerEntity player) {
		Optional<CampfireCookingRecipe> recipe = this.findMatchingRecipe(addedStack);
		if (recipe.isPresent()) {
			boolean wasEmpty = this.getStoredStack().isEmpty();
			ItemStack remainderStack = inventory.insertItem(0, addedStack, false);
			if (remainderStack != addedStack) {
				this.currentRecipe = recipe.get();
				this.cookingTime = 0;
				if (wasEmpty && this.world != null && isHeated(this.world, this.pos)) {
					this.world.playSound(null, this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
				}
				this.inventoryChanged();
				return remainderStack;
			}
		} else if (player != null) {
			player.sendStatusMessage(TextUtils.getTranslation("block.skillet.invalid_item"), true);
		}
		return addedStack;
	}

	public ItemStack removeItem() {
		ItemStack remainderStack = inventory.extractItem(0, this.getStoredStack().getMaxStackSize(), false);
		if (remainderStack != ItemStack.EMPTY) {
			this.inventoryChanged();
			return remainderStack;
		}
		return ItemStack.EMPTY;
	}

	public IItemHandler getInventory() {
		return this.inventory;
	}

	public ItemStack getStoredStack() {
		return this.inventory.getStackInSlot(0);
	}

	public boolean hasStoredStack() {
		return !this.getStoredStack().isEmpty();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	// TODO: Hopper insertions are still able to add non-cookable items! Make sure to check for that!
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
			return handlerInput.cast();
		}
		return super.getCapability(cap, side);
	}

	private void inventoryChanged() {
		super.markDirty();
		if (this.world != null) {
			this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
		}
	}

	@Override
	public void remove() {
		super.remove();
		handlerInput.invalidate();
	}
}
