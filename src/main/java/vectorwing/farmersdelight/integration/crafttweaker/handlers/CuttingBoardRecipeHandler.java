//package vectorwing.farmersdelight.integration.crafttweaker.handlers;
//
//import com.blamejared.crafttweaker.api.ingredient.IIngredient;
//import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
//import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
//import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
//import com.blamejared.crafttweaker.api.recipe.handler.helper.ReplacementHandlerHelper;
//import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
//import com.blamejared.crafttweaker.api.util.StringUtil;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.resources.ResourceLocation;
//import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@IRecipeHandler.For(CuttingBoardRecipe.class)
//public class CuttingBoardRecipeHandler implements IRecipeHandler<CuttingBoardRecipe>
//{
//    @Override
//    public String dumpToCommandString(IRecipeManager manager, CuttingBoardRecipe recipe) {
//        return String.format(
//                "%s.addRecipe(%s, %s, %s, %s, %s);",
//                manager.getCommandString(),
//                StringUtil.quoteAndEscape(recipe.getId()),
//                IIngredient.fromIngredient(recipe.getIngredients().get(0)).getCommandString(),
//                recipe.getResults().stream()
//                        .map(MCItemStackMutable::new)
//                        .map(MCItemStackMutable::getCommandString)
//                        .collect(Collectors.joining(", ", "[", "]")),
//                IIngredient.fromIngredient(recipe.getTool()).getCommandString(),
//                recipe.getSoundEventID()
//        );
//    }
//
//    @Override
//    public Optional<Function<ResourceLocation, CuttingBoardRecipe>> replaceIngredients(IRecipeManager manager, CuttingBoardRecipe recipe, List<IReplacementRule> rules) {
//        return ReplacementHandlerHelper.replaceIngredientList(
//                recipe.getIngredientsAndTool(),
//                Ingredient.class,
//                recipe,
//                rules,
//                newIngredients -> id ->
//                        new CuttingBoardRecipe(id,
//                                recipe.getGroup(),
//                                newIngredients.get(0),
//                                newIngredients.get(1),
//                                recipe.getRollableResults(),
//                                recipe.getSoundEventID())
//        );
//    }
//}
