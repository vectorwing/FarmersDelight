package vectorwing.farmersdelight.data.recipe;

import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.condition.ValidateFluidTagCondition;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.data.builder.FluidEmptyingRecipeBuilder;
import vectorwing.farmersdelight.data.builder.FluidFillingRecipeBuilder;
import vectorwing.farmersdelight.data.builder.SoakingRecipeBuilder;

public class FluidHandlingRecipes
{
	public static void register(RecipeOutput output) {
		fillingRecipes(output);
		emptyingRecipes(output);
		waterBottleRecipes(output);
		soakInWater(output);
	}

	private static void fillingRecipes(RecipeOutput output) {
		FluidFillingRecipeBuilder.filling(SizedFluidIngredient.of(Tags.Fluids.MILK, 250), Ingredient.of(Items.GLASS_BOTTLE), ModItems.MILK_BOTTLE.get())
			.saveToFD(output);
		FluidFillingRecipeBuilder.filling(SizedFluidIngredient.of(Tags.Fluids.HONEY, 250), Ingredient.of(Items.GLASS_BOTTLE), Items.HONEY_BOTTLE)
			.saveToFD(output.withConditions(new ValidateFluidTagCondition(Tags.Fluids.HONEY)));
	}

	private static void emptyingRecipes(RecipeOutput output) {
		FluidEmptyingRecipeBuilder.emptying(new FluidStack(NeoForgeMod.MILK.get(), 250), Ingredient.of(ModItems.MILK_BOTTLE.get()), Items.GLASS_BOTTLE)
			.saveToFD(output, ModItems.MILK_BOTTLE.get());
	}

	private static void waterBottleRecipes(RecipeOutput output) {
		FluidFillingRecipeBuilder.filling(SizedFluidIngredient.of(Tags.Fluids.WATER, 250), Ingredient.of(Items.GLASS_BOTTLE), ModItems.MILK_BOTTLE.get())
			.setCustomResult(waterBottle(Items.POTION))
			.setNamespace(FarmersDelight.MODID)
			.save(output, "fluid_filling/water_bottle");
		FluidEmptyingRecipeBuilder.emptying(new FluidStack(Fluids.WATER, 250), DataComponentIngredient.of(true, DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER), Items.POTION), Items.GLASS_BOTTLE)
			.setNamespace(FarmersDelight.MODID)
			.save(output, "fluid_emptying/water_bottle");
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

	/**
	 * Creates a water bottle for the given potion item. If the item isn't a potion, returns an empty stack.
	 */
	public static ItemStack waterBottle(ItemLike potion) {
		if (potion instanceof PotionItem) {
			ItemStack waterBottle = new ItemStack(potion);
			waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
			return waterBottle;
		}
		return ItemStack.EMPTY;
	}
}
