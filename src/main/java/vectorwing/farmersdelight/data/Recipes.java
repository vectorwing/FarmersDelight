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
import vectorwing.farmersdelight.utils.ForgeTags;
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
		recipesVanillaAlternatives(consumer);
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
				result, experience, 200)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer);
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient),
				result, experience, 600, IRecipeSerializer.CAMPFIRE_COOKING)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer, namePrefix + "_from_campfire_cooking");
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient),
				result, experience, 100, IRecipeSerializer.SMOKING)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer, namePrefix + "_from_smoking");
	}

	/**
	 * The following recipes should ALWAYS define a custom save location.
	 * If not, they fall on the minecraft namespace, overriding any other recipe.
	 */
	private void recipesVanillaAlternatives(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(Items.SCAFFOLDING, 6)
				.patternLine("b#b")
				.patternLine("b b")
				.patternLine("b b")
				.key('b', Items.BAMBOO)
				.key('#', ModItems.CANVAS.get())
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "scaffolding_from_canvas"));
		ShapedRecipeBuilder.shapedRecipe(Items.LEAD)
				.patternLine("rr ")
				.patternLine("rr ")
				.patternLine("  r")
				.key('r', ModItems.ROPE.get())
				.addCriterion("rope", InventoryChangeTrigger.Instance.forItems(ModItems.ROPE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "lead_from_rope"));
		ShapedRecipeBuilder.shapedRecipe(Items.PAINTING)
				.patternLine("sss")
				.patternLine("scs")
				.patternLine("sss")
				.key('s', Items.STICK)
				.key('c', ModItems.CANVAS.get())
				.addCriterion("canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "painting_from_canvas"));
		ShapedRecipeBuilder.shapedRecipe(Items.CAKE)
				.patternLine("mmm")
				.patternLine("ses")
				.patternLine("www")
				.key('m', ModItems.MILK_BOTTLE.get())
				.key('s', Items.SUGAR)
				.key('e', Items.EGG)
				.key('w', Items.WHEAT)
				.addCriterion("milk_bottle", InventoryChangeTrigger.Instance.forItems(ModItems.MILK_BOTTLE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_milk_bottle"));
		ShapelessRecipeBuilder.shapelessRecipe(Items.CAKE)
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addCriterion("cake_slice", InventoryChangeTrigger.Instance.forItems(ModItems.CAKE_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_slices"));
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
				.patternLine("iWi")
				.patternLine("iii")
				.key('b', Items.BRICK)
				.key('i', Items.IRON_INGOT)
				.key('S', Items.WOODEN_SHOVEL)
				.key('W', Items.WATER_BUCKET)
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
		ShapedRecipeBuilder.shapedRecipe(ModItems.SAFETY_NET.get(), 1)
				.patternLine("rr")
				.patternLine("rr")
				.key('r', ModItems.ROPE.get())
				.addCriterion("rope", InventoryChangeTrigger.Instance.forItems(ModItems.ROPE.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.RICE_BALE.get(), 1)
				.patternLine("rrr")
				.patternLine("rrr")
				.patternLine("rrr")
				.key('r', ModItems.RICE_PANICLE.get())
				.addCriterion("rice_panicle", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_PANICLE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ORGANIC_COMPOST.get(), 1)
				.addIngredient(Items.DIRT)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(ModItems.STRAW.get())
				.addIngredient(ModItems.STRAW.get())
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Items.BONE_MEAL)
				.addCriterion("rotten_flesh", InventoryChangeTrigger.Instance.forItems(Items.ROTTEN_FLESH))
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer, "organic_compost_from_rotten_flesh");
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ORGANIC_COMPOST.get(), 1)
				.addIngredient(Items.DIRT)
				.addIngredient(ModItems.STRAW.get())
				.addIngredient(ModItems.STRAW.get())
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(ModItems.TREE_BARK.get())
				.addIngredient(ModItems.TREE_BARK.get())
				.addIngredient(ModItems.TREE_BARK.get())
				.addIngredient(ModItems.TREE_BARK.get())
				.addCriterion("tree_bark", InventoryChangeTrigger.Instance.forItems(ModItems.TREE_BARK.get()))
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer, "organic_compost_from_tree_bark");
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
	}

	private void recipesMaterials(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.CANVAS.get())
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.STRAW.get())
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RICE_PANICLE.get(), 9)
				.addIngredient(ModItems.RICE_BALE.get())
				.addCriterion("rice_bale", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_BALE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RICE.get())
				.addIngredient(ModItems.RICE_PANICLE.get())
				.addCriterion("rice_panicle", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_PANICLE.get()))
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
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BOWL)
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.BARBECUE_STICK.get(), 2)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(Items.STICK)
				.addIngredient(Items.STICK)
				.addCriterion("barbecue", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CHICKEN_SANDWICH.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addCriterion("cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HAMBURGER.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addCriterion("hamburger", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
	}
	private void recipesCookedMeals(Consumer<IFinishedRecipe> consumer) {}
}
