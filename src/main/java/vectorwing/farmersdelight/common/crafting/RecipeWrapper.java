package vectorwing.farmersdelight.common.crafting;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.IItemHandler;

/** Runtime recipe input for the cooking-pot inventory. */
public final class RecipeWrapper implements RecipeInput {
    private final IItemHandler inventory;
    private final StackedItemContents stackedContents = new StackedItemContents();
    private int ingredientAmount;

    public RecipeWrapper(IItemHandler inventory) {
        this.inventory = inventory;
        int inputs = Math.min(CookingPotRecipe.INPUT_SLOTS, inventory.getSlots());
        for (int slot = 0; slot < inputs; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                ingredientAmount++;
                stackedContents.accountStack(stack, 1);
            }
        }
    }

    public StackedItemContents stackedContents() {
        return stackedContents;
    }

    public int ingredientAmount() {
        return ingredientAmount;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public int size() {
        return inventory.getSlots();
    }
}
