package vectorwing.farmersdelight.common.block.entity.inventory;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.RootCommitJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import vectorwing.farmersdelight.common.block.entity.Basket;

/**
 * Transactional basket storage that retains the custom hopper cooldown on first insertion.
 */
public class BasketResourceHandler implements ResourceHandler<ItemResource>
{
	private final Basket basket;
	private final ResourceHandler<ItemResource> delegate;
	private final RootCommitJournal insertionJournal;

	public BasketResourceHandler(Basket basket) {
		this.basket = basket;
		this.delegate = VanillaContainerWrapper.of(basket);
		this.insertionJournal = new RootCommitJournal(() -> {
			if (!basket.isEmpty() && !basket.isOnCustomCooldown()) {
				basket.setCooldown(8);
			}
			basket.setChanged();
		});
	}

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
		return delegate.isValid(index, resource);
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		boolean wasEmpty = basket.isEmpty();
		int inserted = delegate.insert(index, resource, amount, transaction);
		if (wasEmpty && inserted > 0) {
			insertionJournal.updateSnapshots(transaction);
		}
		return inserted;
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		return delegate.extract(index, resource, amount, transaction);
	}
}
