package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

public interface Basket extends Container
{
	VoxelShape[] COLLECTION_AREA_SHAPES = {
			Block.box(0.0D, -16.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // down
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 32.0D, 16.0D),        // up
			Block.box(0.0D, 0.0D, -16.0D, 16.0D, 16.0D, 16.0D),    // north
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 32.0D),        // south
			Block.box(-16.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),    // west
			Block.box(0.0D, 0.0D, 0.0D, 32.0D, 16.0D, 16.0D)        // east
	};

	/**
	 * Get the {@link VoxelShape area} for collecting in world {@link ItemEntity} for a specific facing direction.
	 * @param facingIndex the the {@link Direction#get3DDataValue() index} of the facing direction.
	 * @return the collection area for collecting in world {@link ItemEntity}.
	 */
	default VoxelShape getFacingCollectionArea(int facingIndex) {
		return COLLECTION_AREA_SHAPES[facingIndex];
	}

	/**
	 * Gets the world X position for this basket entity.
	 */
	double getLevelX();

	/**
	 * Gets the world Y position for this basket entity.
	 */
	double getLevelY();

	/**
	 * Gets the world Z position for this basket entity.
	 */
	double getLevelZ();

	/**
	 * Sets the transfer cooldown for the basket entity.
	 * @param ticks cooldown ticks
	 */
	void setCooldown(int ticks);

	/**
	 * @return if the basket is currently on cooldown,
	 * cooldown should only be set when the basket is not on cooldown.
	 */
	boolean isOnCooldown();

	/**
	 * @return if the basket's current cooldown exceeds default cooldown,
	 * and should be respected when updating cooldown with default.
	 */
	boolean isOnCustomCooldown();

	/**
	 * Updates the transfer cooldown upon a transfer attempt.
	 * @param transfer the transfer attempt, returning
	 */
	void tryTransfer(BooleanSupplier transfer);

	/**
	 * Collect items from the {@link #getFacingCollectionArea(int) collection area}.
	 * @param level the {@link Level} of the basket
	 * @param facingIndex the {@link Direction#get3DDataValue() index} of the facing direction.
	 * @return if any items are succesfully captured and placed in the basket.
	 */
	default boolean collectItems(Level level, int facingIndex) {
		for (ItemEntity itementity : getItemsToCollect(level, facingIndex)) {
			if (collectItem(itementity)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param level the {@link Level} of the basket
	 * @param facingIndex the {@link Direction#get3DDataValue() index} of the facing direction.
	 * @return all {@link ItemEntity item entities} within the {@link #getFacingCollectionArea(int) collection area}.
	 */
	default List<ItemEntity> getItemsToCollect(Level level, int facingIndex) {
		return this.getFacingCollectionArea(facingIndex).toAabbs()
				.stream()
				.flatMap((aabb) -> level.getEntitiesOfClass(
						ItemEntity.class,
						aabb.move(this.getLevelX() - 0.5D, this.getLevelY() - 0.5D, this.getLevelZ() - 0.5D),
						EntitySelector.ENTITY_STILL_ALIVE
				).stream())
				.collect(Collectors.toList());
	}

	/**
	 * Collect a single {@link ItemEntity item entity}.
	 * @param itemEntity the {@link ItemEntity item entity} to be collected.
	 * @return if the {@link ItemEntity item entity} is successfully collected.
	 */
	default boolean collectItem(ItemEntity itemEntity) {
		boolean flag = false;
		ItemStack entityItemStack = itemEntity.getItem().copy();
		ItemStack remainderStack = insert(entityItemStack);
		if (remainderStack.isEmpty()) {
			flag = true;
			itemEntity.discard();
		} else {
			itemEntity.setItem(remainderStack);
		}
		return flag;
	}

	/**
	 * Attempt to insert an {@link ItemStack} to all available slots of the basket.
	 * @param stack the {@link ItemStack} to insert.
	 * @return the remainder after the insertion.
	 */
	default ItemStack insert(ItemStack stack) {
		int size = this.getContainerSize();
		for (int slot = 0; slot < size && !stack.isEmpty(); ++slot) {
			stack = this.insert(slot, stack);
		}
		return stack;
	}

	/**
	 * Attempt to insert an {@link ItemStack} to one of the basket slots.
	 * @param slot the slot to insert into.
	 * @param stack the {@link ItemStack} to insert.
	 * @return the remainder after the insertion.
	 */
	default ItemStack insert(int slot, ItemStack stack) {
		ItemStack slotStack = this.getItem(slot);
		if (this.canPlaceItem(slot, stack)) {
			boolean inserted = false;
			if (slotStack.isEmpty()) {
				this.setItem(slot, stack);
				stack = ItemStack.EMPTY;
				inserted = true;
			} else if (canMergeItems(slotStack, stack)) {
				int insertCount = stack.getMaxStackSize() - slotStack.getCount();
				insertCount = Math.min(stack.getCount(), insertCount);
				stack.shrink(insertCount);
				slotStack.grow(insertCount);
				inserted = insertCount > 0;
			}
			if (inserted) {
				if (this.isEmpty() && !this.isOnCustomCooldown()) {
					this.setCooldown(8);
				}
				this.setChanged();
			}
		}
		return stack;
	}

	/**
	 * Helper method for checking if two {@link ItemStack} can be merged (with remainders).
	 * @param stack1 the first {@link ItemStack}.
	 * @param stack2 the second {@link ItemStack}.
	 * @return if the two {@link ItemStack} can be merged (with remainders).
	 */
	static boolean canMergeItems(ItemStack stack1, ItemStack stack2) {
		return stack1.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack1, stack2);
	}
}