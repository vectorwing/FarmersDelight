package vectorwing.farmersdelight.integration.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

public final class FDRecipeTypes
{
	public static final RecipeType<RecipeHolder<CookingPotRecipe>> COOKING = RecipeType.createFromVanilla(ModRecipeTypes.COOKING.get());
	public static final RecipeType<RecipeHolder<CuttingBoardRecipe>> CUTTING = RecipeType.createFromVanilla(ModRecipeTypes.CUTTING.get());
	public static final RecipeType<DecompositionDummy> DECOMPOSITION = RecipeType.create(FarmersDelight.MODID, "decomposition", DecompositionDummy.class);
}
