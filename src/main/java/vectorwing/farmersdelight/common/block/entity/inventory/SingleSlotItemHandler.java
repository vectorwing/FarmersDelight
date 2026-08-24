package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Exposes a single slot from the given Item Handler.
 * Useful for sided automation.
 */
public class SingleSlotItemHandler extends ItemStackHandler
{
	private final IItemHandler itemHandler;
	private final int exposedSlot;

	public SingleSlotItemHandler(IItemHandler itemHandler, int exposedSlot) {
		this.itemHandler = itemHandler;
		this.exposedSlot = exposedSlot;
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		return slot == exposedSlot ? itemHandler.insertItem(slot, stack, simulate) : stack;
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return slot == exposedSlot ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return slot == exposedSlot && itemHandler.isItemValid(slot, stack);
	}

	@Override
	public int getSlots() {
		return itemHandler.getSlots();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return slot == exposedSlot ? itemHandler.getStackInSlot(slot) : ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return itemHandler.getSlotLimit(slot);
	}
}
