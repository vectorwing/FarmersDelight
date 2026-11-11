package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

public class CookingPotItemHandler implements ResourceHandler<ItemResource>
{
	private static final int SLOTS_INPUT = 6;
	private static final int SLOT_CONTAINER_INPUT = 7;
	private static final int SLOT_MEAL_OUTPUT = 8;
	private final ResourceHandler<ItemResource> itemHandler;
	private final Direction side;

	public CookingPotItemHandler(ResourceHandler<ItemResource> itemHandler, @Nullable Direction side) {
		this.itemHandler = itemHandler;
		this.side = side;
	}

	@Override
	public int size() {
		return itemHandler.size();
	}

	@Override
	public ItemResource getResource(int index) {
		return itemHandler.getResource(index);
	}

	@Override
	public long getAmountAsLong(int index) {
		return itemHandler.getAmountAsLong(index);
	}

	@Override
	public long getCapacityAsLong(int index, ItemResource resource) {
		return itemHandler.getCapacityAsLong(index, resource);
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return false;
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		if (side == null || side.equals(Direction.UP)) {
			return index < SLOTS_INPUT ? itemHandler.insert(index, resource, amount, transaction) : 0;
		} else {
			return index == SLOT_CONTAINER_INPUT ? itemHandler.insert(index, resource, amount, transaction) : 0;
		}
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		if (side == null || side.equals(Direction.UP)) {
			return index < SLOTS_INPUT ? itemHandler.extract(index, resource, amount, transaction) : 0;
		} else {
			return index == SLOT_MEAL_OUTPUT ? itemHandler.extract(index, resource, amount, transaction) : 0;
		}
	}
}
