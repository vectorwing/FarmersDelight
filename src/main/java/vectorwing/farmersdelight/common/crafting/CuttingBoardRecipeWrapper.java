package vectorwing.farmersdelight.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record CuttingBoardRecipeWrapper(ItemStack item, ItemStack tool) implements RecipeInput {
	@Override
	public ItemStack getItem(int p_345528_) {
		if (p_345528_ != 0) {
			throw new IllegalArgumentException("No item for index " + p_345528_);
		} else {
			return this.item;
		}
	}

	@Override
	public int size() {
		return 2;
	}
}