package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.handler.helper.ReplacementHandlerHelper;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import org.codehaus.plexus.util.StringUtils;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;
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
}
