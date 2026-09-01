package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.FluidFillingRecipeBuilder;
import vectorwing.farmersdelight.data.builder.SoakingRecipeBuilder;

public class FluidHandlingRecipes
{
	public static void register(RecipeOutput output) {
		fillingRecipes(output);
		soakInWater(output);
	}

	private static void fillingRecipes(RecipeOutput output) {
		FluidFillingRecipeBuilder.filling(SizedFluidIngredient.of(Tags.Fluids.MILK, 250), Ingredient.of(Items.GLASS_BOTTLE), ModItems.MILK_BOTTLE.get())
			.saveToFD(output);
	}

	private static void soakInWater(RecipeOutput output) {
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Tags.Items.CROPS_WHEAT), ModItems.WHEAT_DOUGH.get())
			.saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.DIRT), Items.MUD)
			.saveToFD(output);
	}
}
