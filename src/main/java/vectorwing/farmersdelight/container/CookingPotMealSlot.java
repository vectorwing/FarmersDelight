package vectorwing.farmersdelight.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CookingPotMealSlot extends Slot
{
	public CookingPotMealSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	public boolean canTakeStack(PlayerEntity playerIn) {
		return false;
	}
}
