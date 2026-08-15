package vectorwing.farmersdelight.client.recipebook;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

import java.util.List;
import java.util.Optional;

public enum RecipeCategories implements ExtendedRecipeBookCategory
{
	COOKING_SEARCH;

	public static List<RecipeBookComponent.TabInfo> cookingPotTabs() {
		return List.of(
			new RecipeBookComponent.TabInfo(new ItemStack(ModItems.COOKING_POT.get()), Optional.empty(), COOKING_SEARCH),
			new RecipeBookComponent.TabInfo(ModItems.COOKING_POT.get(), ModItems.BEEF_STEW.get(), ModRecipeBookCategories.COOKING_MEALS.get()),
			new RecipeBookComponent.TabInfo(ModItems.COOKING_POT.get(), ModItems.HOT_COCOA.get(), ModRecipeBookCategories.COOKING_DRINKS.get()),
			new RecipeBookComponent.TabInfo(ModItems.COOKING_POT.get(), ModItems.WHEAT_DOUGH.get(), ModRecipeBookCategories.COOKING_MISC.get())
		);
	}
}
