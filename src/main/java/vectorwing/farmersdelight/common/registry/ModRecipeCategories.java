package vectorwing.farmersdelight.common.registry;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.client.event.RegisterRecipeBookSearchCategoriesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ModRecipeCategories
{
	public static DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, FarmersDelight.MODID);

	public static DeferredHolder<RecipeBookCategory, RecipeBookCategory> COOKING_SEARCH = register("FARMERSDELIGHT_COOKING_SEARCH");
	public static DeferredHolder<RecipeBookCategory, RecipeBookCategory> COOKING_MEALS = register("FARMERSDELIGHT_COOKING_MEALS");
	public static DeferredHolder<RecipeBookCategory, RecipeBookCategory> COOKING_DRINKS = register("FARMERSDELIGHT_COOKING_DRINKS");
	public static DeferredHolder<RecipeBookCategory, RecipeBookCategory> COOKING_MISC = register("FARMERSDELIGHT_COOKING_MISC");

	private static DeferredHolder<RecipeBookCategory, RecipeBookCategory> register(String id) {
		return RECIPE_BOOK_CATEGORIES.register(id.toLowerCase(Locale.ROOT), RecipeBookCategory::new);
	}

	// We can't do this as a static field, or it'll try to access the holders before they're bound.
	// Makes more sense to put it here rather than in CookingPotRecipeBookComponent
	public static List<RecipeBookComponent.TabInfo> createCookingPotTabInfo() {
		return List.of(
			new RecipeBookComponent.TabInfo(Items.COMPASS.getDefaultInstance(), Optional.empty(), COOKING_SEARCH.get()),
			new RecipeBookComponent.TabInfo(ModItems.VEGETABLE_NOODLES.get().getDefaultInstance(), Optional.empty(), COOKING_MEALS.get()),
			new RecipeBookComponent.TabInfo(ModItems.APPLE_CIDER.get().getDefaultInstance(), Optional.empty(), COOKING_DRINKS.get()),
			new RecipeBookComponent.TabInfo(ModItems.DUMPLINGS.get().getDefaultInstance(), Optional.of(ModItems.TOMATO_SAUCE.get().getDefaultInstance()), COOKING_MISC.get())
		);
	}


	public static void init(RegisterRecipeBookSearchCategoriesEvent event) {
		event.register(COOKING_SEARCH.get(), COOKING_MEALS.get(), COOKING_DRINKS.get(), COOKING_MISC.get());

		//TODO lacking equivalents of this. different event?
		//event.registerBookCategories(RecipeBookType.valueOf("FARMERSDELIGHT_COOKING"), ImmutableList.of(COOKING_SEARCH, COOKING_MEALS, COOKING_DRINKS, COOKING_MISC));
//		event.registerRecipeCategoryFinder(ModRecipeTypes.COOKING.get(), recipe ->
//		{
//			if (recipe.value() instanceof CookingPotRecipe cookingRecipe) {
//				CookingPotRecipeBookTab tab = cookingRecipe.getRecipeBookTab();
//				if (tab != null) {
//					return switch (tab) {
//						case MEALS -> COOKING_MEALS;
//						case DRINKS -> COOKING_DRINKS;
//						case MISC -> COOKING_MISC;
//					};
//				}
//			}
//			return COOKING_MISC;
//		});
	}
}
