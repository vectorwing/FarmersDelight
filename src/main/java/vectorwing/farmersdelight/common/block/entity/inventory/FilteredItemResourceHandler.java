package vectorwing.farmersdelight.common.block.entity.inventory;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.IntPredicate;

/**
 * A sided view over an item handler that limits insertion and extraction by slot.
 */
public record FilteredItemResourceHandler(ResourceHandler<ItemResource> delegate, IntPredicate insertSlots,
										 IntPredicate extractSlots) implements ResourceHandler<ItemResource>
{
	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public ItemResource getResource(int index) {
		return delegate.getResource(index);
	}

	@Override
	public long getAmountAsLong(int index) {
		return delegate.getAmountAsLong(index);
	}

	@Override
	public long getCapacityAsLong(int index, ItemResource resource) {
		return delegate.getCapacityAsLong(index, resource);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return resource.isEmpty() || insertSlots.test(index) && delegate.isValid(index, resource);
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		return insertSlots.test(index) ? delegate.insert(index, resource, amount, transaction) : 0;
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		return extractSlots.test(index) ? delegate.extract(index, resource, amount, transaction) : 0;
	}
}
