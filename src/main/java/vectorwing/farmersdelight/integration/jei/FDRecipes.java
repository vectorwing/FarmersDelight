package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;


import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public final class FDRecipes
{

	private static RecipeMap recipeManager = RecipeMap.EMPTY;

	public static List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return getRecipesByType(ModRecipeTypes.COOKING.get());
	}

	public static List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return getRecipesByType(ModRecipeTypes.CUTTING.get());
	}

	public static List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();

		addValidatedSpecialRecipe(recipes, "wheat_dough_from_water", "fd_dough",
				NonNullList.of(
						Ingredient.of(),
						Ingredient.of(Items.WHEAT),
						Ingredient.of(Items.WATER_BUCKET)
				),
				ModItems.WHEAT_DOUGH.get()
		);

		return recipes;
	}

	public static void addValidatedSpecialRecipe(List<RecipeHolder<CraftingRecipe>> recipeList, String recipeId, String group, NonNullList<Ingredient> inputs, ItemLike output) {
		Optional<RecipeHolder<?>> specialRecipe = Optional.ofNullable(recipeManager.byKey(RecipeUtils.FDKey(recipeId)));

		specialRecipe.ifPresent((recipe) -> {
			recipeList.add(new RecipeHolder<>(
				specialRecipe.get().id(),
				new ShapelessRecipe(
					RecipeBuilder.createCraftingCommonInfo(true),
					new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group),
					ItemStackTemplate.fromNonEmptyStack(new ItemStack(output.asItem())),
					inputs)
				)
			);
		});
	}

	public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipesByType(RecipeType<T> type) {
		return recipeManager.byType(type).stream().toList();
	}

	@SubscribeEvent
	public static void onReceiveRecipes(RecipesReceivedEvent event) {
		recipeManager = event.getRecipeMap();
	}

	@SubscribeEvent
	public static void onRecipeSend(OnDatapackSyncEvent event) {
		ModRecipeTypes.RECIPE_TYPES.getEntries()
			.stream()
			.map(DeferredHolder::get)
			.forEach(event::sendRecipes);
	}

	private FDRecipes() {}
}
