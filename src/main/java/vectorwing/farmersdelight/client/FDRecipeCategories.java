package vectorwing.farmersdelight.client;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.RecipeBookRegistry;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public class FDRecipeCategories
{
	public static final RecipeBookType RECIPE_TYPE_COOKING = RecipeBookType.create("COOKING");

	public static final Supplier<RecipeBookCategories> COOKING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_SEARCH", new ItemStack(Items.COMPASS)));
	public static final Supplier<RecipeBookCategories> COOKING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MEALS", new ItemStack(ModItems.PASTA_WITH_MEATBALLS.get())));
	public static final Supplier<RecipeBookCategories> COOKING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_DRINKS", new ItemStack(ModItems.HOT_COCOA.get())));

	public static void init()
	{
		RecipeBookRegistry.addCategoriesToType(RECIPE_TYPE_COOKING, ImmutableList.of(COOKING_SEARCH.get(), COOKING_MEALS.get(), COOKING_DRINKS.get()));
		RecipeBookRegistry.addAggregateCategories(COOKING_SEARCH.get(), ImmutableList.of(COOKING_MEALS.get(), COOKING_DRINKS.get()));
		RecipeBookRegistry.addCategoriesFinder(CookingPotRecipe.TYPE, r ->
		{
			if (r.getResultItem().getItem() instanceof DrinkableItem) {
				return COOKING_DRINKS.get();
			}
			return COOKING_MEALS.get();
		});
	}
}
