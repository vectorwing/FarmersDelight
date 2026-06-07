package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.List;
import java.util.Optional;

public class FDRecipes
{
	// TODO this absolutely sucks but might be necessary for JEI recipe registration
	public static MinecraftServer SERVER;
	private final RecipeManager recipeManager;

	public FDRecipes() {
		if (SERVER != null) {
			this.recipeManager = SERVER.getRecipeManager();
		} else {
			throw new NullPointerException("Minecraft level must not be null.");
		}
	}

	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return recipeManager.getRecipes().stream()
			.filter(r -> r.value().getType() == ModRecipeTypes.COOKING.get())
			.map(r -> (RecipeHolder<CookingPotRecipe>) (r) )
			.toList();
	}

	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return recipeManager.getRecipes().stream()
			.filter(r -> r.value().getType() == ModRecipeTypes.CUTTING.get())
			.map(r -> (RecipeHolder<CuttingBoardRecipe>) (r) )
			.toList();
	}

	public List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
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

	public void addValidatedSpecialRecipe(List<RecipeHolder<CraftingRecipe>> recipeList, String recipeId, String group, NonNullList<Ingredient> inputs, ItemLike output) {
		Optional<RecipeHolder<?>> specialRecipe = recipeManager.byKey(ResourceKey.create(Registries.RECIPE, RecipeUtils.FDLocation(recipeId)));

		specialRecipe.ifPresent((recipe) -> {
			ShapelessRecipe shapeless = new ShapelessRecipe(
				new Recipe.CommonInfo(false),
				new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group),
				ItemStackTemplate.fromNonEmptyStack(new ItemStack(output)),
				inputs
			);
			recipeList.add(new RecipeHolder<>(specialRecipe.get().id(), shapeless));
		});
	}
}
