package vectorwing.farmersdelight.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

public class FDRecipeCategories {
    private static final ResourceLocation SIMPLIFIED_TEXTURES = RecipeUtils.FDLocation("textures/gui/emi/simplified.png");

    public static final EmiRecipeCategory COOKING = new EmiRecipeCategory(RecipeUtils.FDLocation("cooking"), FDRecipeWorkstations.COOKING_POT, simplifiedRenderer(0, 0));
    public static final EmiRecipeCategory CUTTING = new EmiRecipeCategory(RecipeUtils.FDLocation("cutting"), FDRecipeWorkstations.CUTTING_BOARD, simplifiedRenderer(16, 0));
    public static final EmiRecipeCategory DECOMPOSITION = new EmiRecipeCategory(RecipeUtils.FDLocation("decomposition"), FDRecipeWorkstations.ORGANIC_COMPOST, simplifiedRenderer(32, 0));

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) -> draw.blit(SIMPLIFIED_TEXTURES, x, y, u, v, 16, 16, 48, 16);
    }
}
