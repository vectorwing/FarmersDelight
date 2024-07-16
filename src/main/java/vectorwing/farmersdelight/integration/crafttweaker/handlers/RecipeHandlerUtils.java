package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.ingredient.type.IIngredientEmpty;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.component.IRecipeComponent;
import com.blamejared.crafttweaker.api.recipe.component.RecipeComponentEqualityCheckers;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.Arrays;


public class RecipeHandlerUtils {
    public static final IRecipeComponent<SoundEvent> SOUND_COMPONENT = IRecipeComponent.simple(
            ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_component/sound"), new TypeToken<>(){}, SoundEvent::equals);

    public static final IRecipeComponent<String> COOKING_TAB_COMPONENT = IRecipeComponent.simple(
            ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_component/cooking_tab"), new TypeToken<>(){}, String::equals);

    public static final IRecipeComponent<IIngredient> TOOL_COMPONENT = IRecipeComponent.composite(
            ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_component/cutting_board_tool"),
            new TypeToken<>() {},
            RecipeComponentEqualityCheckers::areIngredientsEqual,
            ingredient -> Arrays.asList(ingredient.getItems()),
            items -> items.size() < 1 ? IIngredientEmpty.getInstance() : items.stream()
                    .reduce(IIngredient::or)
                    .orElseThrow()
    );

    public static final IRecipeComponent<IItemStack> CONTAINER_COMPONENT = IRecipeComponent.simple(
            ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_component/cooking_pot_container"), new TypeToken<>(){},  RecipeComponentEqualityCheckers::areStacksEqual);

}