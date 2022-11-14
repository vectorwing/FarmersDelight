package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.recipe.component.IRecipeComponent;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;


public class RecipeHandlerUtils {
    public static final IRecipeComponent<String> SOUND_COMPONENT = IRecipeComponent.simple(
            new ResourceLocation(FarmersDelight.MODID, "recipe_component/sound"), new TypeToken<>(){}, String::equals);
}
