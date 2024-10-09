package vectorwing.farmersdelight.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class FDRecipeCategories {
    private static final ResourceLocation SIMPLIFIED_TEXTURES = new ResourceLocation(FarmersDelight.MODID, "textures/gui/emi/simplified.png");

    public static final EmiRecipeCategory COOKING = new EmiRecipeCategory(new ResourceLocation(FarmersDelight.MODID, "cooking"), FDRecipeWorkstations.COOKING_POT, simplifiedRenderer(0, 0));
    public static final EmiRecipeCategory CUTTING = new EmiRecipeCategory(new ResourceLocation(FarmersDelight.MODID, "cutting"), FDRecipeWorkstations.CUTTING_BOARD, simplifiedRenderer(16, 0));
    public static final EmiRecipeCategory DECOMPOSITION = new EmiRecipeCategory(new ResourceLocation(FarmersDelight.MODID, "decomposition"), FDRecipeWorkstations.ORGANIC_COMPOST, simplifiedRenderer(32, 0));

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) -> {
            draw.blit(SIMPLIFIED_TEXTURES, x, y, u, v, 16, 16, 48, 16);
        };
    }
}
