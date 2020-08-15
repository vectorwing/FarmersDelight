package vectorwing.farmersdelight.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

public class CuttingBoardTileEntity extends TileEntity
{
	private ItemStackHandler itemHandler = createHandler();

	public CuttingBoardTileEntity(TileEntityType<?> tileEntityTypeIn) {	super(tileEntityTypeIn); }

	public CuttingBoardTileEntity() { this(ModTileEntityTypes.CUTTING_BOARD_TILE.get()); }

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.itemHandler.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", itemHandler.serializeNBT());
		return compound;
	}

	// ======== ITEM HANDLING ========

	public IItemHandler getInventory() {
		return this.itemHandler;
	}

	public boolean isEmpty() {
		return this.itemHandler.getStackInSlot(0).isEmpty();
	}

	public ItemStack getItem() {
		return this.itemHandler.getStackInSlot(0);
	}

	public boolean addItem(ItemStack itemStack) {
		if (this.isEmpty() && !itemStack.isEmpty()) {
			this.itemHandler.setStackInSlot(0, itemStack.split(1));
			return true;
		}
		return false;
	}

	public ItemStack removeItem() {
		if (!this.isEmpty()) {
			return this.getItem().split(1);
		}
		return ItemStack.EMPTY;
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler() {
			@Override
			public int getSlotLimit(int slot)
			{
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				// TODO: Do we do anything here?
			}
		};
	}


}
