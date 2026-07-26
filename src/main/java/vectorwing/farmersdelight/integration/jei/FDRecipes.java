package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.*;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import java.util.List;

public class FDRecipes
{
	public FDRecipes() {
	}

	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		// TODO 26.2: Populate JEI recipes through the client recipe display/JEI registration APIs.
		return List.of();
	}

	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		// TODO 26.2: Populate JEI recipes through the client recipe display/JEI registration APIs.
		return List.of();
	}

	public List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();

		return recipes;
	}
}
