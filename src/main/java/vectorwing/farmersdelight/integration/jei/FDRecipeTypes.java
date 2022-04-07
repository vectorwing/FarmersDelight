package vectorwing.farmersdelight.integration.jei;

import mezz.jei.api.recipe.RecipeType;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

public final class FDRecipeTypes
{
	public static final RecipeType<CookingPotRecipe> COOKING = RecipeType.create(FarmersDelight.MODID, "cooking", CookingPotRecipe.class);
	public static final RecipeType<CuttingBoardRecipe> CUTTING = RecipeType.create(FarmersDelight.MODID, "cutting", CuttingBoardRecipe.class);
	public static final RecipeType<DecompositionDummy> DECOMPOSITION = RecipeType.create(FarmersDelight.MODID, "decomposition", DecompositionDummy.class);
}
