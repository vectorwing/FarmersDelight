package vectorwing.farmersdelight.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.action.recipe.ActionRecipeBase;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class ActionRemoveCuttingBoardRecipe extends ActionRecipeBase
{
    private final IItemStack[] outputs;

    public ActionRemoveCuttingBoardRecipe(IRecipeManager manager, IItemStack[] outputs) {
        super(manager);
        this.outputs = outputs;
    }

    @Override
    public void apply() {
        Iterator<Map.Entry<ResourceLocation, Recipe<?>>> it = getManager().getRecipes().entrySet().iterator();
        while (it.hasNext()) {
            CuttingBoardRecipe recipe = (CuttingBoardRecipe) it.next().getValue();
            if (recipe.getResults().size() != outputs.length) {
                continue;
            }

            check:
            {
                int i = 0;
                for (ItemStack result : recipe.getResults()) {
                    if (!outputs[i++].matches(new MCItemStackMutable(result))) {
                        break check;
                    }
                }
                it.remove();
            }
        }
    }

    @Override
    public String describe() {
        return "Removing \"" + ForgeRegistries.RECIPE_TYPES
                .getKey(getManager().getRecipeType()) + "\" recipes with output: " + Arrays.toString(outputs) + "\"";
    }

    @Override
    public boolean validate(Logger logger) {
        if (outputs == null) {
            logger.throwing(Level.WARN, new ScriptException("Output IItemStacks cannot be null!"));
            return false;
        }
        return true;
    }
}
