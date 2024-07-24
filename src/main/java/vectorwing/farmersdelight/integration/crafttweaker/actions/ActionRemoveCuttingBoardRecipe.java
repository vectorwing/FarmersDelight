package vectorwing.farmersdelight.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.action.recipe.ActionRecipeBase;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Iterator;

public class ActionRemoveCuttingBoardRecipe<T extends Recipe<?>> extends ActionRecipeBase<T>
{
	private final IItemStack[] outputs;

	public ActionRemoveCuttingBoardRecipe(IRecipeManager<T> manager, IItemStack[] outputs) {
		super(manager);
		this.outputs = outputs;
	}

	@Override
	public void apply() {
		Iterator<RecipeHolder<T>> it = getManager().getRecipes().iterator();
		while (it.hasNext()) {
			CuttingBoardRecipe recipe = (CuttingBoardRecipe) it.next().value();
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
		return "Removing \"" + BuiltInRegistries.RECIPE_TYPE
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
