package vectorwing.farmersdelight.core.data;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import vectorwing.farmersdelight.core.data.recipe.CookingRecipes;
import vectorwing.farmersdelight.core.data.recipe.CraftingRecipes;
import vectorwing.farmersdelight.core.data.recipe.CuttingRecipes;
import vectorwing.farmersdelight.core.data.recipe.SmeltingRecipes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Recipes extends RecipeProvider
{
	public Recipes(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		CraftingRecipes.register(consumer);
		SmeltingRecipes.register(consumer);
		CookingRecipes.register(consumer);
		CuttingRecipes.register(consumer);
	}
}
