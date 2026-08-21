package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

import java.util.Objects;

public class JugMenu extends AbstractContainerMenu
{
	public static final int INDEX_INPUT = 0;
	public static final int INDEX_OUTPUT = 1;

	public final JugBlockEntity jug;
	public final ItemStackHandler inventory;
	public final FluidTank fluidTank;

	public JugMenu(int containerId, Inventory playerInventory, final FriendlyByteBuf data) {
		this(containerId, playerInventory, getBlockEntity(playerInventory, data));
	}

	public JugMenu(int containerId, Inventory playerInventory, JugBlockEntity jug) {
		super(ModMenuTypes.JUG.get(), containerId);
		this.jug = jug;
		this.inventory = jug.getInventory();
		this.fluidTank = jug.getFluidTank();

		int startX = 8;
		int startY = 18;
		int borderSlotSize = 18;

		// Jug Input
		this.addSlot(new SlotItemHandler(inventory, 0, 51, 17));

		// Jug Output
		this.addSlot(new ResultSlotItemHandler(inventory, 1, 51, 65));

		// Player Inventory
		int startPlayerInvY = 96;
		int startPlayerHotbarY = 154;
		for (int row = 0; row < 3; ++row) {
			for (int column = 0; column < 9; ++column) {
				this.addSlot(new Slot(playerInventory, 9 + (row * 9) + column,
					startX + (column * borderSlotSize),
					startPlayerInvY + (row * borderSlotSize)));
			}
		}

		// Hotbar
		for (int column = 0; column < 9; ++column) {
			this.addSlot(new Slot(playerInventory, column, startX + (column * borderSlotSize), startPlayerHotbarY));
		}
	}

	private static JugBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
		Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
		Objects.requireNonNull(data, "data cannot be null");
		final BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(data.readBlockPos());
		if (blockEntity instanceof JugBlockEntity jug) {
			return jug;
		}
		throw new IllegalStateException("Block entity is not correct! " + blockEntity);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack slotStack = slot.getItem();
		ItemStack slotStackCopy = slotStack.copy();
		int indexInventoryStart = INDEX_OUTPUT + 1;
		int indexInventoryEnd = indexInventoryStart + 36;

		if (index == INDEX_OUTPUT) {
			if (!this.moveItemStackTo(slotStack, indexInventoryStart, indexInventoryEnd, true)) {
				return ItemStack.EMPTY;
			}
		} else if (index > INDEX_OUTPUT) {
			if (!this.moveItemStackTo(slotStack, INDEX_INPUT, INDEX_OUTPUT, false)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(slotStack, indexInventoryStart, indexInventoryEnd, false)) {
			return ItemStack.EMPTY;
		}

		if (slotStack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		if (slotStack.getCount() == slotStackCopy.getCount()) {
			return ItemStack.EMPTY;
		}

		slot.onTake(player, slotStack);

		return slotStackCopy;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
