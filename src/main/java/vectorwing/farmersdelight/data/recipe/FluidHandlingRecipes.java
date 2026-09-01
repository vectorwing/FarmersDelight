package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.common.crafting.condition.FluidTagEmptyCondition;
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
		FluidFillingRecipeBuilder.filling(SizedFluidIngredient.of(Tags.Fluids.HONEY, 250), Ingredient.of(Items.GLASS_BOTTLE), Items.HONEY_BOTTLE)
			.saveToFD(output.withConditions(new NotCondition(
				new FluidTagEmptyCondition(Tags.Fluids.HONEY)
			)));
	}

	private static void soakInWater(RecipeOutput output) {
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Tags.Items.CROPS_WHEAT), ModItems.WHEAT_DOUGH.get())
			.saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.DIRT), Items.MUD)
			.saveToFD(output);
		SoakingRecipeBuilder.soaking(Ingredient.of(Items.SPONGE), SizedFluidIngredient.of(Tags.Fluids.WATER, 1000), Items.WET_SPONGE)
			.saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.WHITE_CONCRETE_POWDER), Items.WHITE_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.LIGHT_GRAY_CONCRETE_POWDER), Items.LIGHT_GRAY_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.GRAY_CONCRETE_POWDER), Items.GRAY_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.BLACK_CONCRETE_POWDER), Items.BLACK_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.BROWN_CONCRETE_POWDER), Items.BROWN_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.RED_CONCRETE_POWDER), Items.RED_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.ORANGE_CONCRETE_POWDER), Items.ORANGE_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.YELLOW_CONCRETE_POWDER), Items.YELLOW_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.LIME_CONCRETE_POWDER), Items.LIME_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.GREEN_CONCRETE_POWDER), Items.GREEN_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.CYAN_CONCRETE_POWDER), Items.CYAN_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.LIGHT_BLUE_CONCRETE_POWDER), Items.LIGHT_BLUE_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.BLUE_CONCRETE_POWDER), Items.BLUE_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.PURPLE_CONCRETE_POWDER), Items.PURPLE_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.MAGENTA_CONCRETE_POWDER), Items.MAGENTA_CONCRETE).saveToFD(output);
		SoakingRecipeBuilder.waterSoaking(Ingredient.of(Items.PINK_CONCRETE_POWDER), Items.PINK_CONCRETE).saveToFD(output);
	}
}
