package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@IRecipeHandler.For(CookingPotRecipe.class)
public final class CookingPotRecipeHandler implements IRecipeHandler<CookingPotRecipe>
{
    @Override
    public String dumpToCommandString(IRecipeManager manager, CookingPotRecipe recipe) {
        return String.format(
                "%s.addRecipe(%s, %s, %s, %s, %s, %s);",
                manager.getCommandString(),
                StringUtil.quoteAndEscape(recipe.getId()),
                new MCItemStackMutable(recipe.getResultItem()).getCommandString(),
                recipe.getIngredients().stream()
                        .map(IIngredient::fromIngredient)
                        .map(IIngredient::getCommandString)
                        .collect(Collectors.joining(", ", "[", "]")),
                new MCItemStackMutable(recipe.getOutputContainer()).getCommandString(),
                recipe.getExperience(),
                recipe.getCookTime()
        );
    }

    /*
    @Override
    public Optional<Function<ResourceLocation, CookingPotRecipe>> replaceIngredients(IRecipeManager manager, CookingPotRecipe recipe, List<IReplacementRule> rules) {
        return ReplacementHandlerHelper.replaceNonNullIngredientList(
                recipe.getIngredients(),
                Ingredient.class,
                recipe,
                rules,
                newIngredients -> id ->
                        new CookingPotRecipe(id,
                                recipe.getGroup(),
                                recipe.getRecipeBookTab(),
                                newIngredients,
                                recipe.getResultItem(),
                                recipe.getOutputContainer(),
                                recipe.getExperience(),
                                recipe.getCookTime())
        );
    }

     */
    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CookingPotRecipe> manager, CookingPotRecipe firstRecipe, U secondRecipe) {
        return firstRecipe.equals(secondRecipe);
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CookingPotRecipe> manager, CookingPotRecipe recipe) {
        final IDecomposedRecipe decomposedRecipe = IDecomposedRecipe.builder()
                .with(BuiltinRecipeComponents.Metadata.GROUP, recipe.getGroup())
                .build();
        return Optional.of(decomposedRecipe);
    }

    @Override
    public Optional<CookingPotRecipe> recompose(IRecipeManager<? super CookingPotRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        return Optional.empty();
    }
}
