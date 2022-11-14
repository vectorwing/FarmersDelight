package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.IRecipeComponent;
import com.blamejared.crafttweaker.api.recipe.component.RecipeComponentEqualityCheckers;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;


public class RecipeHandlerUtils {
    public static final IRecipeComponent<String> SOUND_COMPONENT = IRecipeComponent.simple(
            new ResourceLocation(FarmersDelight.MODID, "recipe_component/sound"), new TypeToken<>(){}, String::equals);

    public static final IRecipeComponent<String> COOKING_TAB_COMPONENT = IRecipeComponent.simple(
            new ResourceLocation(FarmersDelight.MODID, "recipe_component/cooking_tab"), new TypeToken<>(){}, String::equals);


    public static final IRecipeComponent<IItemStack> CONTAINER_COMPONENT = IRecipeComponent.simple(
            new ResourceLocation(FarmersDelight.MODID, "recipe_component/cooking_pot_container"), new TypeToken<>(){},  RecipeComponentEqualityCheckers::areStacksEqual);

}
