package vectorwing.farmersdelight.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import vectorwing.farmersdelight.common.block.entity.inventory.ItemStackInventory;

public record CookingPotRecipeInput(ItemStackInventory inventory) implements RecipeInput
{
	@Override
	public ItemStack getItem(int index) {
		if (index < 0 || index >= CookingPotRecipe.INPUT_SLOTS) {
			throw new IllegalArgumentException("Recipe does not contain slot " + index);
		}
		return inventory.getStack(index);
	}

	@Override
	public int size() {
		return CookingPotRecipe.INPUT_SLOTS;
	}
}
