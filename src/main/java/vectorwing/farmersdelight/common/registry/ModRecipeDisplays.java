package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipe.CookingPotRecipeDisplay;

import java.util.function.Supplier;

public class ModRecipeDisplays
{
    public static final DeferredRegister<RecipeDisplay.Type<?>> RECIPE_DISPLAYS =
            DeferredRegister.create(Registries.RECIPE_DISPLAY, FarmersDelight.MODID);

    public static final Supplier<RecipeDisplay.Type<CookingPotRecipeDisplay>> COOKING =
            RECIPE_DISPLAYS.register(
                    "cooking",
                    () -> new RecipeDisplay.Type<>(
                            CookingPotRecipeDisplay.MAP_CODEC,
                            CookingPotRecipeDisplay.STREAM_CODEC
                    )
            );
}
