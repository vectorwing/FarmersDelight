//package vectorwing.farmersdelight.integration.crafttweaker;
//
//import com.blamejared.crafttweaker.api.item.IIngredient;
//import com.blamejared.crafttweaker.api.managers.IRecipeManager;
//import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
//import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
//import com.blamejared.crafttweaker.api.recipes.ReplacementHandlerHelper;
//import com.blamejared.crafttweaker.api.util.StringUtils;
//import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.resources.ResourceLocation;
//import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@IRecipeHandler.For(CookingPotRecipe.class)
//public final class CookingPotRecipeHandler implements IRecipeHandler<CookingPotRecipe>
//{
//    @Override
//    public String dumpToCommandString(IRecipeManager manager, CookingPotRecipe recipe) {
//        return String.format(
//                "%s.addRecipe(%s, %s, %s, %s, %s, %s);",
//                manager.getCommandString(),
//                StringUtils.quoteAndEscape(recipe.getId()),
//                new MCItemStackMutable(recipe.getResultItem()).getCommandString(),
//                recipe.getIngredients().stream()
//                        .map(IIngredient::fromIngredient)
//                        .map(IIngredient::getCommandString)
//                        .collect(Collectors.joining(", ", "[", "]")),
//                new MCItemStackMutable(recipe.getOutputContainer()).getCommandString(),
//                recipe.getExperience(),
//                recipe.getCookTime()
//        );
//    }
//
//    @Override
//    public Optional<Function<ResourceLocation, CookingPotRecipe>> replaceIngredients(IRecipeManager manager, CookingPotRecipe recipe, List<IReplacementRule> rules) {
//        return ReplacementHandlerHelper.replaceNonNullIngredientList(
//                recipe.getIngredients(),
//                Ingredient.class,
//                recipe,
//                rules,
//                newIngredients -> id ->
//                        new CookingPotRecipe(id,
//                                recipe.getGroup(),
//                                newIngredients,
//                                recipe.getResultItem(),
//                                recipe.getOutputContainer(),
//                                recipe.getExperience(),
//                                recipe.getCookTime())
//        );
//    }
//}
