package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import vectorwing.farmersdelight.common.block.BasketBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BasketBlockEntity extends RandomizableContainerBlockEntity implements Basket
{
	private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
	private int transferCooldown = -1;

	public BasketBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.BASKET.get(), pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ContainerHelper.loadAllItems(compound, this.items);
		}
		this.transferCooldown = compound.getInt("TransferCooldown");
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		if (!this.trySaveLootTable(compound)) {
			ContainerHelper.saveAllItems(compound, this.items);
		}

		compound.putInt("TransferCooldown", this.transferCooldown);
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		this.unpackLootTable(null);
		return ContainerHelper.removeItem(this.getItems(), index, count);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		this.unpackLootTable(null);
		this.getItems().set(index, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	protected Component getDefaultName() {
		return TextUtils.getTranslation("container.basket");
	}

	public static boolean pullItems(Level level, Basket basket, int facingIndex) {
		for (ItemEntity itementity : getCaptureItems(level, basket, facingIndex)) {
			if (captureItem(basket, itementity)) {
				return true;
			}
		}
		return false;
	}

	public static ItemStack putStackInInventoryAllSlots(Container destination, ItemStack stack) {
		int i = destination.getContainerSize();

		for (int j = 0; j < i && !stack.isEmpty(); ++j) {
			stack = insertStack(destination, stack, j);
		}

		return stack;
	}

	private static boolean canInsertItemInSlot(Container inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
		if (!inventoryIn.canPlaceItem(index, stack)) {
			return false;
		} else {
			return !(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer) inventoryIn).canPlaceItemThroughFace(index, stack, side);
		}
	}

	private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		if (stack1.getItem() != stack2.getItem()) {
			return false;
		} else if (stack1.getDamageValue() != stack2.getDamageValue()) {
			return false;
		} else if (stack1.getCount() > stack1.getMaxStackSize()) {
			return false;
		} else {
			return ItemStack.tagMatches(stack1, stack2);
		}
	}

	private static ItemStack insertStack(Container destination, ItemStack stack, int index) {
		ItemStack itemstack = destination.getItem(index);
		if (canInsertItemInSlot(destination, stack, index, null)) {
			boolean flag = false;
			boolean isDestinationEmpty = destination.isEmpty();
			if (itemstack.isEmpty()) {
				destination.setItem(index, stack);
				stack = ItemStack.EMPTY;
				flag = true;
			} else if (canCombine(itemstack, stack)) {
				int i = stack.getMaxStackSize() - itemstack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.shrink(j);
				itemstack.grow(j);
				flag = j > 0;
			}

			if (flag) {
				if (isDestinationEmpty && destination instanceof BasketBlockEntity) {
					BasketBlockEntity firstBasket = (BasketBlockEntity) destination;
					if (!firstBasket.mayTransfer()) {
						int k = 0;

						firstBasket.setTransferCooldown(8 - k);
					}
				}

				destination.setChanged();
			}
		}

		return stack;
	}

	public static boolean captureItem(Container inventory, ItemEntity itemEntity) {
		boolean flag = false;
		ItemStack entityItemStack = itemEntity.getItem().copy();
		ItemStack remainderStack = putStackInInventoryAllSlots(inventory, entityItemStack);
		if (remainderStack.isEmpty()) {
			flag = true;
			itemEntity.discard();
		} else {
			itemEntity.setItem(remainderStack);
		}

		return flag;
	}

	public static List<ItemEntity> getCaptureItems(Level level, Basket basket, int facingIndex) {
		return basket.getFacingCollectionArea(facingIndex).toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move(basket.getLevelX() - 0.5D, basket.getLevelY() - 0.5D, basket.getLevelZ() - 0.5D), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
	}

	// -- STANDARD INVENTORY STUFF --
	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		this.items = itemsIn;
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return ChestMenu.threeRows(id, player, this);
	}

	public void setTransferCooldown(int ticks) {
		this.transferCooldown = ticks;
	}

	private boolean isOnTransferCooldown() {
		return this.transferCooldown > 0;
	}

	public boolean mayTransfer() {
		return this.transferCooldown > 8;
	}

	private void updateHopper(Supplier<Boolean> supplier) {
		if (this.level != null && !this.level.isClientSide) {
			if (!this.isOnTransferCooldown() && this.getBlockState().getValue(BlockStateProperties.ENABLED)) {
				boolean flag = false;
				if (!this.isFull()) {
					flag = supplier.get();
				}

				if (flag) {
					this.setTransferCooldown(8);
					this.setChanged();
				}
			}
		}
	}

	private boolean isFull() {
		for (ItemStack itemstack : this.items) {
			if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
				return false;
			}
		}

		return true;
	}

	public void onEntityCollision(Entity entity) {
		if (entity instanceof ItemEntity) {
			BlockPos blockpos = this.getBlockPos();
			int facing = this.getBlockState().getValue(BasketBlock.FACING).get3DDataValue();
			if (Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox().move(-blockpos.getX(), -blockpos.getY(), -blockpos.getZ())), this.getFacingCollectionArea(facing), BooleanOp.AND)) {
				this.updateHopper(() -> captureItem(this, (ItemEntity) entity));
			}
		}
	}

	@Override
	public double getLevelX() {
		return (double) this.worldPosition.getX() + 0.5D;
	}

	@Override
	public double getLevelY() {
		return (double) this.worldPosition.getY() + 0.5D;
	}

	@Override
	public double getLevelZ() {
		return (double) this.worldPosition.getZ() + 0.5D;
	}

	public static void pushItemsTick(Level level, BlockPos pos, BlockState state, BasketBlockEntity blockEntity) {
		--blockEntity.transferCooldown;
		if (!blockEntity.isOnTransferCooldown()) {
			blockEntity.setTransferCooldown(0);
			int facing = state.getValue(BasketBlock.FACING).get3DDataValue();
			blockEntity.updateHopper(() -> pullItems(level, blockEntity, facing));
		}
	}
}
