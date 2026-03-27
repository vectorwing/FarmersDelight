package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
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
	private final RecipeManager recipeManager;

	public FDRecipes() {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;

		if (level != null) {
			this.recipeManager = level.getRecipeManager();
		} else {
			throw new NullPointerException("Minecraft level must not be null.");
		}
	}

	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get());
	}

	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return recipeManager.getAllRecipesFor(ModRecipeTypes.CUTTING.get());
	}

	public List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();

		addValidatedSpecialRecipe(recipes, "wheat_dough_from_water", "fd_dough",
				NonNullList.of(
						Ingredient.EMPTY,
						Ingredient.of(Items.WHEAT),
						Ingredient.of(Items.WATER_BUCKET)
				),
				ModItems.WHEAT_DOUGH.get()
		);

		return recipes;
	}

	public void addValidatedSpecialRecipe(List<RecipeHolder<CraftingRecipe>> recipeList, String recipeId, String group, NonNullList<Ingredient> inputs, ItemLike output) {
		Optional<RecipeHolder<?>> specialRecipe = recipeManager.byKey(RecipeUtils.FDLocation(recipeId));

		specialRecipe.ifPresent((recipe) -> {
			recipeList.add(new RecipeHolder<>(specialRecipe.get().id(), new ShapelessRecipe(group, CraftingBookCategory.MISC, new ItemStack(output.asItem()), inputs)));
		});
	}
}
