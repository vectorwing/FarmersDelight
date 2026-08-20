package vectorwing.farmersdelight.common.block.entity.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

public class JugMenu extends AbstractContainerMenu
{
	public JugMenu(int containerId, Inventory playerInventory, final FriendlyByteBuf data) {
		this(containerId, playerInventory);
	}

	public JugMenu(int containerId, Inventory playerInventory) {
		super(ModMenuTypes.JUG.get(), containerId);

		int startX = 8;
		int startY = 18;
		int borderSlotSize = 18;

		// Main Player Inventory
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

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack slotStackCopy = ItemStack.EMPTY;
		return slotStackCopy;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
