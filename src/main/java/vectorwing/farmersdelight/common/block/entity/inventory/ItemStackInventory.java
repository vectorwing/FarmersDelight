package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * Item storage backed by NeoForge's transactional transfer API.
 */
public class ItemStackInventory extends ItemStacksResourceHandler
{
	public ItemStackInventory(int size) {
		super(size);
	}

	public ItemStack getStack(int index) {
		return stacks.get(index);
	}

	public void setStack(int index, ItemStack stack) {
		set(index, ItemResource.of(stack), stack.getCount());
	}

	public ItemStack insert(int index, ItemStack stack, boolean simulate) {
		return ItemUtil.insertItemReturnRemaining(this, index, stack, simulate, null);
	}

	public ItemStack extract(int index, int amount, boolean simulate) {
		ItemStack stack = getStack(index);
		if (stack.isEmpty() || amount <= 0) {
			return ItemStack.EMPTY;
		}

		try (var transaction = Transaction.openRoot()) {
			int extracted = extract(index, ItemResource.of(stack), amount, transaction);
			if (!simulate) {
				transaction.commit();
			}
			return stack.copyWithCount(extracted);
		}
	}

	public int getCapacity(int index) {
		return getCapacityAsInt(index, ItemResource.EMPTY);
	}

	@Override
	public void serialize(ValueOutput output) {
		ValueOutput.TypedOutputList<ItemStackWithSlot> itemList = output.list("Items", ItemStackWithSlot.CODEC);
		for (int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			if (!stack.isEmpty()) {
				itemList.add(new ItemStackWithSlot(i, stack));
			}
		}
		output.putInt("Size", stacks.size());
	}

	@Override
	public void deserialize(ValueInput input) {
		int size = input.getIntOr("Size", stacks.size());
		NonNullList<ItemStack> restoredStacks = NonNullList.withSize(size, ItemStack.EMPTY);
		input.listOrEmpty("Items", ItemStackWithSlot.CODEC).forEach(slot -> {
			if (slot.isValidInContainer(restoredStacks.size())) {
				restoredStacks.set(slot.slot(), slot.stack());
			}
		});
		setStacks(restoredStacks);
	}
}
