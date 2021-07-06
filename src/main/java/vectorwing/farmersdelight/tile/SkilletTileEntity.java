package vectorwing.farmersdelight.tile;

import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class SkilletTileEntity extends TileEntity implements ITickableTileEntity, IHeatable
{
	private int cookingTime;
	private int cookingTimeTotal;

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
			ItemStack stack = inventory.getStackInSlot(0);
			if (isHeated && !stack.isEmpty()) {
				// TODO
			}
		} else {
			if (isHeated) {
				this.addParticles();
			}
		}
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
		return world == null || !this.inventory.getStackInSlot(0).isEmpty()
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

	// NBT Handling

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

	public ItemStack addItem(ItemStack addedStack) {
		ItemStack remainderStack = inventory.insertItem(0, addedStack, false);
		if (remainderStack != addedStack) {
			this.inventoryChanged();
			return remainderStack;
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

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

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
