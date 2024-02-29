package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CookingPotItemHandler implements IItemHandler
{
	private static final int SLOTS_INPUT = 6;
	private static final int SLOT_CONTAINER_INPUT = 7;
	private static final int SLOT_MEAL_OUTPUT = 8;
	private final IItemHandler itemHandler;
	private final Direction side;

	public CookingPotItemHandler(IItemHandler itemHandler, @Nullable Direction side) {
		this.itemHandler = itemHandler;
		this.side = side;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return itemHandler.isItemValid(slot, stack);
	}

	@Override
	public int getSlots() {
		return itemHandler.getSlots();
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return itemHandler.getStackInSlot(slot);
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (side == null || side.equals(Direction.UP)) {
			return slot < SLOTS_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
		} else {
			return slot == SLOT_CONTAINER_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
		}
	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (side == null || side.equals(Direction.UP)) {
			return slot < SLOTS_INPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
		} else {
			return slot == SLOT_MEAL_OUTPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return itemHandler.getSlotLimit(slot);
	}
}
