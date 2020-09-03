package vectorwing.farmersdelight.tile.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CookingPotMealSlot extends SlotItemHandler {
    public CookingPotMealSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    public boolean canTakeStack(PlayerEntity playerIn) {
        return false;
    }
}
