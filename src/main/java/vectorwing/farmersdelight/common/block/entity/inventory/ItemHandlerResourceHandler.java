package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

@SuppressWarnings({"deprecation", "removal"})
public class ItemHandlerResourceHandler extends SnapshotJournal<NonNullList<ItemStack>> implements ResourceHandler<ItemResource>
{
	private final IItemHandlerModifiable handler;
	private final IntPredicate insertSlots;
	private final IntPredicate extractSlots;
	private final Consumer<NonNullList<ItemStack>> rootCommitAction;

	public ItemHandlerResourceHandler(IItemHandlerModifiable handler, IntPredicate insertSlots, IntPredicate extractSlots, Consumer<NonNullList<ItemStack>> rootCommitAction) {
		this.handler = handler;
		this.insertSlots = insertSlots;
		this.extractSlots = extractSlots;
		this.rootCommitAction = rootCommitAction;
	}

	@Override
	public int size() {
		return handler.getSlots();
	}

	@Override
	public ItemResource getResource(int index) {
		Objects.checkIndex(index, size());
		return ItemResource.of(handler.getStackInSlot(index));
	}

	@Override
	public long getAmountAsLong(int index) {
		Objects.checkIndex(index, size());
		return handler.getStackInSlot(index).getCount();
	}

	@Override
	public long getCapacityAsLong(int index, ItemResource resource) {
		Objects.checkIndex(index, size());
		if (!resource.isEmpty() && !isValid(index, resource)) {
			return 0;
		}
		return resource.isEmpty() ? handler.getSlotLimit(index) : Math.min(resource.getMaxStackSize(), handler.getSlotLimit(index));
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		Objects.checkIndex(index, size());
		return resource.isEmpty() || insertSlots.test(index) && handler.isItemValid(index, resource.toStack());
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
		if (!insertSlots.test(index) || !handler.isItemValid(index, resource.toStack())) {
			return 0;
		}

		ItemStack currentStack = handler.getStackInSlot(index);
		if (!currentStack.isEmpty() && !resource.matches(currentStack)) {
			return 0;
		}

		int inserted = Math.min(amount, getCapacityAsInt(index, resource) - currentStack.getCount());
		if (inserted <= 0) {
			return 0;
		}

		updateSnapshots(transaction);
		currentStack = handler.getStackInSlot(index);
		if (currentStack.isEmpty()) {
			handler.setStackInSlot(index, resource.toStack(inserted));
		} else {
			ItemStack updatedStack = currentStack.copy();
			updatedStack.grow(inserted);
			handler.setStackInSlot(index, updatedStack);
		}
		return inserted;
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		Objects.checkIndex(index, size());
		TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
		if (!extractSlots.test(index)) {
			return 0;
		}

		ItemStack currentStack = handler.getStackInSlot(index);
		if (!resource.matches(currentStack)) {
			return 0;
		}

		int extracted = Math.min(amount, currentStack.getCount());
		if (extracted <= 0) {
			return 0;
		}

		updateSnapshots(transaction);
		ItemStack updatedStack = handler.getStackInSlot(index).copy();
		updatedStack.shrink(extracted);
		handler.setStackInSlot(index, updatedStack);
		return extracted;
	}

	@Override
	protected NonNullList<ItemStack> createSnapshot() {
		NonNullList<ItemStack> snapshot = NonNullList.withSize(size(), ItemStack.EMPTY);
		for (int i = 0; i < size(); i++) {
			snapshot.set(i, handler.getStackInSlot(i).copy());
		}
		return snapshot;
	}

	@Override
	protected void revertToSnapshot(NonNullList<ItemStack> snapshot) {
		for (int i = 0; i < snapshot.size(); i++) {
			handler.setStackInSlot(i, snapshot.get(i));
		}
	}

	@Override
	protected void onRootCommit(NonNullList<ItemStack> originalState) {
		rootCommitAction.accept(originalState);
	}
}
