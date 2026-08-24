package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.SoakingRecipeBuilder;

public class SoakingRecipes
{
	public static void register(RecipeOutput output) {
		soakInWater(output);
	}

	private static void soakInWater(RecipeOutput output) {
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Tags.Items.CROPS_WHEAT), ModItems.WHEAT_DOUGH.get())
			.saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.DIRT), Items.MUD)
			.saveToFD(output);
	}
}
