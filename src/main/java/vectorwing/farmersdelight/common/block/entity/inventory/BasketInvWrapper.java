package vectorwing.farmersdelight.common.block.entity.inventory;

import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import vectorwing.farmersdelight.common.block.entity.Basket;

/**
 * Wraps a {@link Basket}'s inventory so that automation inserting items into an empty basket
 * triggers the same transfer cooldown the basket applies when it collects items itself.
 */
public class BasketInvWrapper extends DelegatingResourceHandler<ItemResource>
{
	protected final Basket basket;

	// Tracks whether this transaction filled a previously-empty basket. The cooldown is only applied once the
	// root transaction commits, and the flag is rolled back with the transaction if it is aborted (e.g. simulation).
	private boolean pendingCooldown;
	private final SnapshotJournal<Boolean> cooldownJournal = new SnapshotJournal<>()
	{
		@Override
		protected Boolean createSnapshot() {
			return pendingCooldown;
		}

		@Override
		protected void revertToSnapshot(Boolean snapshot) {
			pendingCooldown = snapshot;
		}

		@Override
		protected void onRootCommit(Boolean originalState) {
			if (pendingCooldown) {
				if (!basket.isOnCustomCooldown()) {
					basket.setCooldown(8);
				}
				pendingCooldown = false;
			}
		}
	};

	public BasketInvWrapper(Basket basket) {
		super(VanillaContainerWrapper.of(basket));
		this.basket = basket;
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		boolean wasEmpty = basket.isEmpty();
		int inserted = super.insert(index, resource, amount, transaction);
		if (inserted > 0 && wasEmpty) {
			cooldownJournal.updateSnapshots(transaction);
			pendingCooldown = true;
		}
		return inserted;
	}
}
