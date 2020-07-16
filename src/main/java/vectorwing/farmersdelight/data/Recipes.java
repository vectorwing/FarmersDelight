package vectorwing.farmersdelight.data;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.init.BlockInit;
import vectorwing.farmersdelight.init.ItemInit;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
	public Recipes(DataGenerator generator)
	{
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		recipesSmelting(consumer);
		recipesBlocks(consumer);
		recipesTools(consumer);
		recipesIngredients(consumer);
		recipesCraftedMeals(consumer);
		//recipesCookedMeals(consumer);
	}

	private void foodSmeltingRecipes(String name, IItemProvider ingredient, IItemProvider result, float experience, int cookingTime, Consumer<IFinishedRecipe> consumer) {
		String namePrefix = new ResourceLocation(FarmersDelight.MODID, name).toString();
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ingredient),
				result, experience, cookingTime)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer);
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient),
				result, experience, cookingTime, IRecipeSerializer.CAMPFIRE_COOKING)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer, namePrefix + "_from_campfire_cooking");
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient),
				result, experience, cookingTime, IRecipeSerializer.SMOKING)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer, namePrefix + "_from_smoking");
	}

	private void recipesSmelting(Consumer<IFinishedRecipe> consumer) {
		foodSmeltingRecipes("fried_egg", Items.EGG, ItemInit.FRIED_EGG.get(), 0.35F, 200, consumer);
	}
	private void recipesBlocks(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(BlockInit.STOVE.get())
				.patternLine("iii")
				.patternLine("B B")
				.patternLine("BCB")
				.key('i', Items.IRON_INGOT)
				.key('B', Blocks.BRICKS)
				.key('C', Blocks.CAMPFIRE)
				.addCriterion("campfire", InventoryChangeTrigger.Instance.forItems(Blocks.CAMPFIRE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(BlockInit.COOKING_POT.get())
				.patternLine("bSb")
				.patternLine("i i")
				.patternLine("iii")
				.key('b', Items.BRICK)
				.key('i', Items.IRON_INGOT)
				.key('S', Items.WOODEN_SHOVEL)
				.addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
				.build(consumer);
	}
	private void recipesTools(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ItemInit.FLINT_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.FLINT)
				.key('s', Items.STICK)
				.addCriterion("stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ItemInit.IRON_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.IRON_INGOT)
				.key('s', Items.STICK)
				.addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ItemInit.DIAMOND_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.DIAMOND)
				.key('s', Items.STICK)
				.addCriterion("diamond", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ItemInit.GOLDEN_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.GOLD_INGOT)
				.key('s', Items.STICK)
				.addCriterion("gold_ingot", InventoryChangeTrigger.Instance.forItems(Items.GOLD_INGOT))
				.build(consumer);
	}
	private void recipesIngredients(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.CABBAGE_SEEDS.get())
				.addIngredient(ItemInit.CABBAGE.get())
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ItemInit.CABBAGE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.TOMATO_SEEDS.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addCriterion("tomato", InventoryChangeTrigger.Instance.forItems(ItemInit.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.TOMATO_SAUCE.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(Items.BOWL)
				.addCriterion("tomato", InventoryChangeTrigger.Instance.forItems(ItemInit.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.MILK_BOTTLE.get(), 3)
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addCriterion("milk_bucket", InventoryChangeTrigger.Instance.forItems(Items.MILK_BUCKET))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.RAW_PASTA.get())
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.EGG)
				.addIngredient(new ItemTags.Wrapper(new ResourceLocation("forge", "knives")))
				.addCriterion("egg", InventoryChangeTrigger.Instance.forItems(Items.EGG))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.CAKE_SLICE.get(), 7)
				.addIngredient(Blocks.CAKE)
				.addIngredient(new ItemTags.Wrapper(new ResourceLocation("forge", "knives")))
				.addCriterion("cake", InventoryChangeTrigger.Instance.forItems(Blocks.CAKE))
				.build(consumer);
	}
	private void recipesCraftedMeals(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.FRESH_SALAD.get())
				.addIngredient(ItemInit.CABBAGE.get())
				.addIngredient(ItemInit.ONION.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BOWL)
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ItemInit.CABBAGE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.BARBECUE_STICK.get(), 2)
				.addIngredient(ItemInit.ONION.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(Items.STICK)
				.addIngredient(Items.STICK)
				.addCriterion("barbecue", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.CHICKEN_SANDWICH.get())
				.addIngredient(Items.BREAD)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(ItemInit.CABBAGE.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addCriterion("cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.HAMBURGER.get())
				.addIngredient(Items.BREAD)
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(ItemInit.CABBAGE.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(ItemInit.ONION.get())
				.addCriterion("hamburger", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(Items.CAKE)
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addIngredient(ItemInit.CAKE_SLICE.get())
				.addCriterion("cake_slice", InventoryChangeTrigger.Instance.forItems(ItemInit.CAKE_SLICE.get()))
				.build(consumer);
	}
	private void recipesCookedMeals(Consumer<IFinishedRecipe> consumer) {}
}
