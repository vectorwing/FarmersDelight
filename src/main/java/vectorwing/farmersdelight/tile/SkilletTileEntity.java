package vectorwing.farmersdelight.tile;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;

public class SkilletTileEntity extends TileEntity implements ITickableTileEntity, IHeatable
{
	private int cookingTime;
	private int cookingTimeTotal;

	private ItemStackHandler itemHandler = createHandler();
	private LazyOptional<IItemHandler> handlerPan = LazyOptional.of(() -> itemHandler);

	public SkilletTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	public SkilletTileEntity() {
		this(ModTileEntityTypes.SKILLET_TILE.get());
	}

	@Override
	public void tick() {
		// TODO: Cook items!
	}

	// NBT Handling

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.itemHandler.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", itemHandler.serializeNBT());
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

	public IItemHandler getInventory() {
		return this.itemHandler;
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
			return handlerPan.cast();
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
		handlerPan.invalidate();
	}
}
