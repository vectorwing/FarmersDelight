package vectorwing.farmersdelight.common.block.entity.inventory;

import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import vectorwing.farmersdelight.common.block.entity.Basket;

public class BasketInvWrapper extends DelegatingResourceHandler<ItemResource>
{
    protected final Basket basket;

    public BasketInvWrapper(Basket basket) {
        super(VanillaContainerWrapper.of(basket));
        this.basket = basket;
    }

	@Override
	public int insert(int index, ItemResource resource, int originalCount, TransactionContext transaction) {
		boolean wasEmpty = basket.isEmpty();
		int count = super.insert(index, resource, originalCount, transaction);
		if (wasEmpty && originalCount > count) {
			if (!basket.isOnCustomCooldown()) {
				basket.setCooldown(8);
			}
		}
		return count;
	}
}
