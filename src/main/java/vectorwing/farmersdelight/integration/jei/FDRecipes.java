package vectorwing.farmersdelight.integration.jei;

import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class FDRecipes
{
    private static final List<RecipeHolder<CookingPotRecipe>> COOKING_RECIPES = new ArrayList<>();
    private static final List<RecipeHolder<CuttingBoardRecipe>> CUTTING_RECIPES = new ArrayList<>();

    private static Runnable recipeUpdateListener = () -> {};

    public FDRecipes() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        RecipeMap recipes = event.getRecipeMap();

        COOKING_RECIPES.clear();
        CUTTING_RECIPES.clear();

        COOKING_RECIPES.addAll(recipes.byType(ModRecipeTypes.COOKING.get()));
        CUTTING_RECIPES.addAll(recipes.byType(ModRecipeTypes.CUTTING.get()));

        recipeUpdateListener.run();
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        COOKING_RECIPES.clear();
        CUTTING_RECIPES.clear();
    }

    public static void setRecipeUpdateListener(Runnable listener) {
        recipeUpdateListener = listener != null ? listener : () -> {};
    }

    public static void clearRecipeUpdateListener() {
        recipeUpdateListener = () -> {};
    }

    public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
        return List.copyOf(COOKING_RECIPES);
    }

    public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
        return List.copyOf(CUTTING_RECIPES);
    }

    public List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
        return List.of();
    }
}
