package vectorwing.farmersdelight.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.integration.emi.handler.CookingPotEmiRecipeHandler;
import vectorwing.farmersdelight.integration.emi.recipe.CookingPotEmiRecipe;
import vectorwing.farmersdelight.integration.emi.recipe.CuttingEmiRecipe;
import vectorwing.farmersdelight.integration.emi.recipe.DecompositionEmiRecipe;

@EmiEntrypoint
public class EMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(FDRecipeCategories.COOKING);
        registry.addCategory(FDRecipeCategories.CUTTING);
        registry.addCategory(FDRecipeCategories.DECOMPOSITION);

        registry.addWorkstation(FDRecipeCategories.COOKING, FDRecipeWorkstations.COOKING_POT);
        registry.addWorkstation(FDRecipeCategories.CUTTING, FDRecipeWorkstations.CUTTING_BOARD);
        registry.addRecipeHandler(ModMenuTypes.COOKING_POT.get(), new CookingPotEmiRecipeHandler());

        for (RecipeHolder<CookingPotRecipe> recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
            registry.addRecipe(new CookingPotEmiRecipe(recipe.id(), recipe.value().getIngredients().stream().map(EmiIngredient::of).toList(),
                    EmiStack.of(recipe.value().getResultItem(Minecraft.getInstance().level.registryAccess())), EmiStack.of(recipe.value().getOutputContainer()), recipe.value().getCookTime(), recipe.value().getExperience()));
        }

        for (RecipeHolder<CuttingBoardRecipe> recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.CUTTING.get())) {
            registry.addRecipe(new CuttingEmiRecipe(recipe.id(), EmiIngredient.of(recipe.value().getTool()), EmiIngredient.of(recipe.value().getIngredients().get(0)),
                    recipe.value().getRollableResults().stream().map(chanceResult -> EmiStack.of(chanceResult.stack()).setChance(chanceResult.chance())).toList()));
        }
        registry.addRecipe(new DecompositionEmiRecipe());
    }
}
