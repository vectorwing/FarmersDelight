package vectorwing.farmersdelight.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import vectorwing.farmersdelight.init.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BasketTileEntity extends LockableLootTileEntity implements IBasket, ITickableTileEntity
{
	private NonNullList<ItemStack> basketContents = NonNullList.withSize(27, ItemStack.EMPTY);
	private int transferCooldown = -1;
	private long tickedGameTime;

	protected BasketTileEntity(TileEntityType<?> typeIn) { super(typeIn); }

	public BasketTileEntity() {	this(ModTileEntityTypes.BASKET_TILE.get()); }

	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if (!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.basketContents);
		}

		compound.putInt("TransferCooldown", this.transferCooldown);
		return compound;
	}

	public void read(CompoundNBT compound) {
		super.read(compound);
		this.basketContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if (!this.checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, this.basketContents);
		}
		this.transferCooldown = compound.getInt("TransferCooldown");
	}

	// -- STANDARD INVENTORY STUFF --
	@Override
	protected NonNullList<ItemStack> getItems() { return this.basketContents; }
	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn)	{ this.basketContents = itemsIn; }
	@Override
	public int getSizeInventory() { return this.basketContents.size(); }
	@Override
	protected ITextComponent getDefaultName() {	return Text.getTranslation("container.basket"); }
	@Override
	protected Container createMenu(int id, PlayerInventory player)
	{
		return ChestContainer.createGeneric9X3(id, player, this);
	}

	public ItemStack decrStackSize(int index, int count) {
		this.fillWithLoot((PlayerEntity)null);
		return ItemStackHelper.getAndSplit(this.getItems(), index, count);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		this.fillWithLoot((PlayerEntity)null);
		this.getItems().set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
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

	private boolean updateHopper(Supplier<Boolean> supplier) {
		if (this.world != null && !this.world.isRemote) {
			if (!this.isOnTransferCooldown() && this.getBlockState().get(BlockStateProperties.ENABLED)) {
				boolean flag = false;
				if (!this.isFull()) {
					flag |= supplier.get();
				}

				if (flag) {
					this.setTransferCooldown(8);
					this.markDirty();
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	private boolean isFull() {
		for(ItemStack itemstack : this.basketContents) {
			if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
				return false;
			}
		}

		return true;
	}

	public static boolean pullItems(IBasket basket, int facingIndex) {
		//Boolean ret = net.minecraftforge.items.VanillaInventoryCodeHooks.extractHook(basket);
		//if (ret != null) return ret;

		for (ItemEntity itementity : getCaptureItems(basket, facingIndex)) {
			if (captureItem(basket, itementity)) {
				return true;
			}
		}

		return false;
	}

	public static ItemStack putStackInInventoryAllSlots(IInventory destination, ItemStack stack) {
		int i = destination.getSizeInventory();

		for(int j = 0; j < i && !stack.isEmpty(); ++j) {
			stack = insertStack(destination, stack, j);
		}

		return stack;
	}

	private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
		if (!inventoryIn.isItemValidForSlot(index, stack)) {
			return false;
		} else {
			return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
		}
	}

	private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		if (stack1.getItem() != stack2.getItem()) {
			return false;
		} else if (stack1.getDamage() != stack2.getDamage()) {
			return false;
		} else if (stack1.getCount() > stack1.getMaxStackSize()) {
			return false;
		} else {
			return ItemStack.areItemStackTagsEqual(stack1, stack2);
		}
	}

	private static ItemStack insertStack(IInventory destination, ItemStack stack, int index) {
		ItemStack itemstack = destination.getStackInSlot(index);
		if (canInsertItemInSlot(destination, stack, index, null)) {
			boolean flag = false;
			boolean isDestinationEmpty = destination.isEmpty();
			if (itemstack.isEmpty()) {
				destination.setInventorySlotContents(index, stack);
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
				if (isDestinationEmpty && destination instanceof BasketTileEntity) {
					BasketTileEntity firstBasket = (BasketTileEntity)destination;
					if (!firstBasket.mayTransfer()) {
						int k = 0;

						firstBasket.setTransferCooldown(8 - k);
					}
				}

				destination.markDirty();
			}
		}

		return stack;
	}

	public static boolean captureItem(IInventory inventory, ItemEntity itemEntity) {
		boolean flag = false;
		ItemStack itemstack = itemEntity.getItem().copy();
		ItemStack itemstack1 = putStackInInventoryAllSlots(inventory, itemstack);
		if (itemstack1.isEmpty()) {
			flag = true;
			itemEntity.remove();
		} else {
			itemEntity.setItem(itemstack1);
		}

		return flag;
	}

	public void onEntityCollision(Entity entity) {
		if (entity instanceof ItemEntity) {
			BlockPos blockpos = this.getPos();
			int facing = this.getBlockState().get(BasketBlock.FACING).getIndex();
			if (VoxelShapes.compare(VoxelShapes.create(entity.getBoundingBox().offset((double)(-blockpos.getX()), (double)(-blockpos.getY()), (double)(-blockpos.getZ()))), this.getFacingCollectionArea(facing), IBooleanFunction.AND)) {
				this.updateHopper(() -> {
					return captureItem(this, (ItemEntity)entity);
				});
			}
		}
	}

	public static List<ItemEntity> getCaptureItems(IBasket basket, int facingIndex) {

		return basket.getFacingCollectionArea(facingIndex).toBoundingBoxList().stream().flatMap((p_200110_1_) -> {
			return basket.getWorld().getEntitiesWithinAABB(ItemEntity.class, p_200110_1_.offset(basket.getXPos() - 0.5D, basket.getYPos() - 0.5D, basket.getZPos() - 0.5D), EntityPredicates.IS_ALIVE).stream();
		}).collect(Collectors.toList());
	}

	@Override
	public double getXPos() { return (double)this.pos.getX() + 0.5D; }

	@Override
	public double getYPos()	{ return (double)this.pos.getY() + 0.5D; }

	@Override
	public double getZPos()	{ return (double)this.pos.getZ() + 0.5D; }

	@Override
	public void tick()
	{
		if (this.world != null && !this.world.isRemote) {
			--this.transferCooldown;
			this.tickedGameTime = this.world.getGameTime();
			if (!this.isOnTransferCooldown()) {
				this.setTransferCooldown(0);
				int facing = this.getBlockState().get(BasketBlock.FACING).getIndex();
				this.updateHopper(() -> {
					return pullItems(this, facing);
				});
			}
		}
	}
}
