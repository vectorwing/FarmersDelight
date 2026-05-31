package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;

/**
 * Sided view over the Cooking Pot inventory:
 * <ul>
 *     <li>From the top (or no side): items may be inserted into the ingredient slots only.</li>
 *     <li>From any other side (down): the meal container may be inserted, and the finished meal extracted.</li>
 * </ul>
 */
public class CookingPotItemHandler extends DelegatingResourceHandler<ItemResource>
{
	private static final int SLOTS_INPUT = 6;
	private static final int SLOT_CONTAINER_INPUT = 7;
	private static final int SLOT_MEAL_OUTPUT = 8;
	@Nullable
	private final Direction side;

	public CookingPotItemHandler(ResourceHandler<ItemResource> itemHandler, @Nullable Direction side) {
		super(itemHandler);
		this.side = side;
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		if (side == null || side.equals(Direction.UP)) {
			return index < SLOTS_INPUT ? super.insert(index, resource, amount, transaction) : 0;
		} else {
			return index == SLOT_CONTAINER_INPUT ? super.insert(index, resource, amount, transaction) : 0;
		}
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		if (side == null || side.equals(Direction.UP)) {
			return index < SLOTS_INPUT ? super.extract(index, resource, amount, transaction) : 0;
		} else {
			return index == SLOT_MEAL_OUTPUT ? super.extract(index, resource, amount, transaction) : 0;
		}
	}

	// Route the slotless overloads through the per-slot, side-filtered overrides above
	// (DelegatingResourceHandler would otherwise bypass the filtering by delegating directly).
	@Override
	public int insert(ItemResource resource, int amount, TransactionContext transaction) {
		int inserted = 0;
		int size = size();
		for (int index = 0; index < size; index++) {
			inserted += insert(index, resource, amount - inserted, transaction);
			if (inserted == amount) break;
		}
		return inserted;
	}

	@Override
	public int extract(ItemResource resource, int amount, TransactionContext transaction) {
		int extracted = 0;
		int size = size();
		for (int index = 0; index < size; index++) {
			extracted += extract(index, resource, amount - extracted, transaction);
			if (extracted == amount) break;
		}
		return extracted;
	}
}
