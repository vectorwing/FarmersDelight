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
//                StringUtils.quoteAndEscape(recipe.getId()),
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
