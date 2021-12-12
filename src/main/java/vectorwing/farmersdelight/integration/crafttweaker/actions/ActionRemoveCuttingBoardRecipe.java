//package vectorwing.farmersdelight.integration.crafttweaker.actions;
//
//import com.blamejared.crafttweaker.api.exceptions.ScriptException;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.blamejared.crafttweaker.api.logger.ILogger;
//import com.blamejared.crafttweaker.api.managers.IRecipeManager;
//import com.blamejared.crafttweaker.impl.actions.recipes.ActionRecipeBase;
//import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.core.Registry;
//import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
//
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.Map;
//
//public class ActionRemoveCuttingBoardRecipe extends ActionRecipeBase
//{
//    private final IItemStack[] outputs;
//
//    public ActionRemoveCuttingBoardRecipe(IRecipeManager manager, IItemStack[] outputs) {
//        super(manager);
//        this.outputs = outputs;
//    }
//
//    @Override
//    public void apply() {
//        Iterator<Map.Entry<ResourceLocation, Recipe<?>>> it = getManager().getRecipes().entrySet().iterator();
//        while (it.hasNext()) {
//            CuttingBoardRecipe recipe = (CuttingBoardRecipe) it.next().getValue();
//            if (recipe.getResults().size() != outputs.length) {
//                continue;
//            }
//
//            check:
//            {
//                int i = 0;
//                for (ItemStack result : recipe.getResults()) {
//                    if (!outputs[i++].matches(new MCItemStackMutable(result))) {
//                        break check;
//                    }
//                }
//                it.remove();
//            }
//        }
//    }
//
//    @Override
//    public String describe() {
//        return "Removing \"" + Registry.RECIPE_TYPE
//                .getKey(getManager().getRecipeType()) + "\" recipes with output: " + Arrays.toString(outputs) + "\"";
//    }
//
//    @Override
//    public boolean validate(ILogger logger) {
//        if (outputs == null) {
//            logger.throwingWarn("output cannot be null!", new ScriptException("output IItemStacks cannot be null!"));
//            return false;
//        }
//        return true;
//    }
//}
