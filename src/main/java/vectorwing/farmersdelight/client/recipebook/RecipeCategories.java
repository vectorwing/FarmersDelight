package vectorwing.farmersdelight.client.recipebook;

import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;

/**
 * Cooking Pot recipe-book categories.
 *
 * <p>NOTE (26.1 port): In 1.21.1 Farmer's Delight added four custom {@code RecipeBookCategories} enum
 * constants (a search aggregate plus MEALS/DRINKS/MISC sub-tabs) and wired them up through the old
 * {@code RegisterRecipeBookSearchCategoriesEvent} methods {@code registerBookCategories(RecipeBookType, ...)},
 * {@code registerAggregateCategory(...)} and {@code registerRecipeCategoryFinder(RecipeType, finder)}.
 *
 * <p>None of that survives in 26.1:
 * <ul>
 *   <li>{@code net.minecraft.client.RecipeBookCategories} was moved to
 *       {@code net.minecraft.world.item.crafting.RecipeBookCategories} and is no longer an enum; it is a
 *       holder of {@link net.minecraft.world.item.crafting.RecipeBookCategory} instances registered into
 *       {@code BuiltInRegistries.RECIPE_BOOK_CATEGORY}. {@code RecipeBookCategories.valueOf(...)} no longer
 *       exists, so the four custom constants cannot be created the old way (and their enum-extension proxies
 *       in {@code EnumParameters} are gone too).</li>
 *   <li>The NeoForge event now only exposes
 *       {@link RegisterRecipeBookSearchCategoriesEvent#register(net.minecraft.world.item.crafting.ExtendedRecipeBookCategory, net.minecraft.world.item.crafting.RecipeBookCategory...)}.
 *       The per-{@code RecipeType} category finder and the {@code RecipeBookType}-to-categories binding are
 *       gone, so there is no longer any hook to route Cooking Pot recipes into bespoke sub-tabs.</li>
 * </ul>
 *
 * <p>Consequently the Cooking Pot recipe book has been simplified to the vanilla
 * {@link net.minecraft.world.item.crafting.RecipeBookCategories#CRAFTING_MISC} category:
 * {@code CookingPotRecipe#recipeBookCategory()} returns {@code CRAFTING_MISC} and
 * {@code CookingPotRecipeBookComponent} declares a single {@code CRAFTING_MISC} tab. Registering bespoke
 * {@code RecipeBookCategory} instances here would be dead code (nothing routes recipes into them), so this
 * registration is intentionally a no-op. The MEALS/DRINKS/MISC distinction still exists as
 * {@code CookingPotRecipeBookTab} on the recipe data, it just no longer drives separate recipe-book tabs.
 */
public class RecipeCategories
{
	public static void init(RegisterRecipeBookSearchCategoriesEvent event) {
		// Intentionally empty: see class javadoc. The Cooking Pot recipe book uses the vanilla
		// CRAFTING_MISC category, which needs no custom search-category registration.
	}
}
