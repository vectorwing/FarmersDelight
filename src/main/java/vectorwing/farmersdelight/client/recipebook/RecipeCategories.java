package vectorwing.farmersdelight.client.recipebook;

import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

public class RecipeCategories
{
	public static final ExtendedRecipeBookCategory COOKING_SEARCH = new ExtendedRecipeBookCategory() {
		@Override
		public String toString() {
			return "farmersdelight:cooking_search";
		}
	};

	public static void init(RegisterRecipeBookSearchCategoriesEvent event) {
		event.register(COOKING_SEARCH,
				ModRecipeBookCategories.COOKING_MEALS.get(),
				ModRecipeBookCategories.COOKING_DRINKS.get(),
				ModRecipeBookCategories.COOKING_MISC.get());
	}
}
