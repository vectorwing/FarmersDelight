package vectorwing.farmersdelight.data;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.init.ModItems;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.utils.Tags;

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
		recipesMaterials(consumer);
		recipesFoodstuffs(consumer);
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
		foodSmeltingRecipes("fried_egg", Items.EGG, ModItems.FRIED_EGG.get(), 0.35F, 200, consumer);
	}
	private void recipesBlocks(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.STOVE.get())
				.patternLine("iii")
				.patternLine("B B")
				.patternLine("BCB")
				.key('i', Items.IRON_INGOT)
				.key('B', Blocks.BRICKS)
				.key('C', Blocks.CAMPFIRE)
				.addCriterion("campfire", InventoryChangeTrigger.Instance.forItems(Blocks.CAMPFIRE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.COOKING_POT.get())
				.patternLine("bSb")
				.patternLine("i i")
				.patternLine("iii")
				.key('b', Items.BRICK)
				.key('i', Items.IRON_INGOT)
				.key('S', Items.WOODEN_SHOVEL)
				.addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASKET.get())
				.patternLine("b b")
				.patternLine("# #")
				.patternLine("b#b")
				.key('b', Items.BAMBOO)
				.key('#', ModItems.CANVAS.get())
				.addCriterion("canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.ROPE.get(), 3)
				.patternLine("s")
				.patternLine("s")
				.patternLine("s")
				.key('s', ModItems.STRAW.get())
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(Items.SCAFFOLDING, 6)
				.patternLine("b#b")
				.patternLine("b b")
				.patternLine("b b")
				.key('b', Items.BAMBOO)
				.key('#', ModItems.CANVAS.get())
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "scaffolding"));
	}
	private void recipesTools(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.FLINT_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.FLINT)
				.key('s', Items.STICK)
				.addCriterion("stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.IRON_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.IRON_INGOT)
				.key('s', Items.STICK)
				.addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.DIAMOND_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.DIAMOND)
				.key('s', Items.STICK)
				.addCriterion("diamond", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.GOLDEN_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.GOLD_INGOT)
				.key('s', Items.STICK)
				.addCriterion("gold_ingot", InventoryChangeTrigger.Instance.forItems(Items.GOLD_INGOT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(Items.LEAD, 1)
				.patternLine("rr ")
				.patternLine("rr ")
				.patternLine("  r")
				.key('r', ModItems.ROPE.get())
				.addCriterion("rope", InventoryChangeTrigger.Instance.forItems(ModItems.ROPE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "lead"));
	}

	private void recipesMaterials(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.CANVAS.get())
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.STRAW.get())
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer);
	}

	private void recipesFoodstuffs(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CABBAGE_SEEDS.get())
				.addIngredient(ModItems.CABBAGE.get())
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.TOMATO_SEEDS.get())
				.addIngredient(ModItems.TOMATO.get())
				.addCriterion("tomato", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.TOMATO_SAUCE.get())
				.addIngredient(ModItems.TOMATO.get())
				.addIngredient(ModItems.TOMATO.get())
				.addIngredient(Items.BOWL)
				.addCriterion("tomato", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.MILK_BOTTLE.get(), 3)
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addCriterion("milk_bucket", InventoryChangeTrigger.Instance.forItems(Items.MILK_BUCKET))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RAW_PASTA.get())
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.EGG)
				.addIngredient(new ItemTags.Wrapper(new ResourceLocation("forge", "knives")))
				.addCriterion("egg", InventoryChangeTrigger.Instance.forItems(Items.EGG))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CAKE_SLICE.get(), 7)
				.addIngredient(Blocks.CAKE)
				.addIngredient(Tags.FORGE_KNIVES)
				.addCriterion("cake", InventoryChangeTrigger.Instance.forItems(Blocks.CAKE))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.SWEET_BERRY_COOKIE.get(), 8)
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(Items.WHEAT)
				.addCriterion("sweet_berries", InventoryChangeTrigger.Instance.forItems(Items.SWEET_BERRIES))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HONEY_COOKIE.get(), 8)
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.WHEAT)
				.addCriterion("honey_bottle", InventoryChangeTrigger.Instance.forItems(Items.HONEY_BOTTLE))
				.build(consumer);
	}
	private void recipesCraftedMeals(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.MIXED_SALAD.get())
				.addIngredient(ModItems.CABBAGE.get())
				.addIngredient(ModItems.ONION.get())
				.addIngredient(ModItems.TOMATO.get())
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BOWL)
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.BARBECUE_STICK.get(), 2)
				.addIngredient(ModItems.ONION.get())
				.addIngredient(ModItems.TOMATO.get())
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(Items.STICK)
				.addIngredient(Items.STICK)
				.addCriterion("barbecue", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CHICKEN_SANDWICH.get())
				.addIngredient(Items.BREAD)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(ModItems.CABBAGE.get())
				.addIngredient(ModItems.TOMATO.get())
				.addCriterion("cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HAMBURGER.get())
				.addIngredient(Items.BREAD)
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(ModItems.CABBAGE.get())
				.addIngredient(ModItems.TOMATO.get())
				.addIngredient(ModItems.ONION.get())
				.addCriterion("hamburger", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(Items.CAKE)
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addCriterion("cake_slice", InventoryChangeTrigger.Instance.forItems(ModItems.CAKE_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "cake"));
	}
	private void recipesCookedMeals(Consumer<IFinishedRecipe> consumer) {}
}
