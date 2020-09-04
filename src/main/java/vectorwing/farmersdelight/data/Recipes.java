package vectorwing.farmersdelight.data;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tags.ItemTags;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.utils.ForgeTags;
import vectorwing.farmersdelight.utils.ModTags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Recipes extends RecipeProvider {
	public Recipes(DataGenerator generator) {
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
		recipesFoodBlocks(consumer);
		recipesCraftedMeals(consumer);
	}

	private void foodSmeltingRecipes(String name, IItemProvider ingredient, IItemProvider result, float experience, Consumer<IFinishedRecipe> consumer) {
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
	 * If not, they fall on the minecraft namespace, overriding vanilla recipes instead of being alternatives.
	 */
	private void recipesVanillaAlternatives(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(Items.SCAFFOLDING, 6)
				.patternLine("b#b")
				.patternLine("b b")
				.patternLine("b b")
				.key('b', Items.BAMBOO)
				.key('#', ModItems.CANVAS.get())
				.addCriterion("canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
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
		ShapedRecipeBuilder.shapedRecipe(Items.PUMPKIN)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.PUMPKIN_SLICE.get())
				.addCriterion("pumpkin_slice", InventoryChangeTrigger.Instance.forItems(ModItems.PUMPKIN_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_from_slices"));
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
		ShapelessRecipeBuilder.shapelessRecipe(Items.BOOK)
				.addIngredient(Items.PAPER)
				.addIngredient(Items.PAPER)
				.addIngredient(Items.PAPER)
				.addIngredient(ModItems.CANVAS.get())
				.addCriterion("canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "book_from_canvas"));
	}

	private void recipesSmelting(Consumer<IFinishedRecipe> consumer) {
		foodSmeltingRecipes("fried_egg", Items.EGG, ModItems.FRIED_EGG.get(), 0.35F, consumer);
		foodSmeltingRecipes("beef_patty", ModItems.MINCED_BEEF.get(), ModItems.BEEF_PATTY.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_chicken_cuts", ModItems.CHICKEN_CUTS.get(), ModItems.COOKED_CHICKEN_CUTS.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_cod_slice", ModItems.COD_SLICE.get(), ModItems.COOKED_COD_SLICE.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_salmon_slice", ModItems.SALMON_SLICE.get(), ModItems.COOKED_SALMON_SLICE.get(), 0.35F, consumer);
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.IRON_KNIFE.get()),
				Items.IRON_NUGGET, 0.1F, 200)
				.addCriterion("has_iron_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_smelting_knife"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.GOLDEN_KNIFE.get()),
				Items.GOLD_NUGGET, 0.1F, 200)
				.addCriterion("has_golden_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_smelting_knife"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ModItems.IRON_KNIFE.get()),
				Items.IRON_NUGGET, 0.1F, 100)
				.addCriterion("has_iron_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_blasting_knife"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ModItems.GOLDEN_KNIFE.get()),
				Items.GOLD_NUGGET, 0.1F, 100)
				.addCriterion("has_golden_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_blasting_knife"));
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
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.CUTTING_BOARD.get())
				.patternLine(" K ")
				.patternLine("/##")
				.key('K', ModTags.KNIVES)
				.key('/', Items.STICK)
				.key('#', ItemTags.PLANKS)
				.addCriterion("stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.OAK_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.OAK_SLAB)
				.key('D', Items.OAK_TRAPDOOR)
				.addCriterion("oak_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.OAK_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BIRCH_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.BIRCH_SLAB)
				.key('D', Items.BIRCH_TRAPDOOR)
				.addCriterion("birch_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.BIRCH_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.SPRUCE_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.SPRUCE_SLAB)
				.key('D', Items.SPRUCE_TRAPDOOR)
				.addCriterion("spruce_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.SPRUCE_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.JUNGLE_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.JUNGLE_SLAB)
				.key('D', Items.JUNGLE_TRAPDOOR)
				.addCriterion("jungle_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.JUNGLE_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ACACIA_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.ACACIA_SLAB)
				.key('D', Items.ACACIA_TRAPDOOR)
				.addCriterion("acacia_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.ACACIA_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.DARK_OAK_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.DARK_OAK_SLAB)
				.key('D', Items.DARK_OAK_TRAPDOOR)
				.addCriterion("dark_oak_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.DARK_OAK_TRAPDOOR))
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
		ShapedRecipeBuilder.shapedRecipe(ModItems.CABBAGE_CRATE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.CABBAGE.get())
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.TOMATO_CRATE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.TOMATO.get())
				.addCriterion("tomato", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.ONION_CRATE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.ONION.get())
				.addCriterion("onion", InventoryChangeTrigger.Instance.forItems(ModItems.ONION.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.RICE_BALE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.RICE_PANICLE.get())
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
		SmithingRecipeBuilder.func_240502_a_(Ingredient.fromItems(ModItems.DIAMOND_KNIFE.get()), Ingredient.fromItems(Items.NETHERITE_INGOT), ModItems.NETHERITE_KNIFE.get())
				.func_240503_a_("netherite_ingot", InventoryChangeTrigger.Instance.forItems(Items.NETHERITE_INGOT))
				.func_240504_a_(consumer, FarmersDelight.MODID + ":netherite_knife_smithing");
	}

	private void recipesMaterials(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.CANVAS.get())
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.STRAW.get())
				.addCriterion("straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CABBAGE.get(), 9)
				.addIngredient(ModItems.CABBAGE_CRATE.get())
				.addCriterion("cabbage_crate", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE_CRATE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.TOMATO.get(), 9)
				.addIngredient(ModItems.TOMATO_CRATE.get())
				.addCriterion("tomato_crate", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO_CRATE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ONION.get(), 9)
				.addIngredient(ModItems.ONION_CRATE.get())
				.addCriterion("onion_crate", InventoryChangeTrigger.Instance.forItems(ModItems.ONION_CRATE.get()))
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
				.addIngredient(ForgeTags.KNIVES)
				.addCriterion("egg", InventoryChangeTrigger.Instance.forItems(Items.EGG))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.PIE_CRUST.get(), 1)
				.patternLine("wMw")
				.patternLine(" w ")
				.key('w', Items.WHEAT)
				.key('M', ForgeTags.MILK)
				.addCriterion("wheat", InventoryChangeTrigger.Instance.forItems(Items.WHEAT))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CAKE_SLICE.get(), 7)
				.addIngredient(Blocks.CAKE)
				.addIngredient(ForgeTags.KNIVES)
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

	private void recipesFoodBlocks(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.APPLE_PIE.get(), 1)
				.patternLine("###")
				.patternLine("aaa")
				.patternLine("xOx")
				.key('#', Items.WHEAT)
				.key('a', Items.APPLE)
				.key('x', Items.SUGAR)
				.key('O', ModItems.PIE_CRUST.get())
				.addCriterion("pie_crust", InventoryChangeTrigger.Instance.forItems(ModItems.PIE_CRUST.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.APPLE_PIE.get(), 1)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.APPLE_PIE_SLICE.get())
				.addCriterion("apple_pie_slice", InventoryChangeTrigger.Instance.forItems(ModItems.APPLE_PIE_SLICE.get()))
				.build(consumer, "apple_pie_from_slices");
		ShapedRecipeBuilder.shapedRecipe(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.patternLine("sss")
				.patternLine("sss")
				.patternLine("mOm")
				.key('s', Items.SWEET_BERRIES)
				.key('m', ForgeTags.MILK)
				.key('O', ModItems.PIE_CRUST.get())
				.addCriterion("pie_crust", InventoryChangeTrigger.Instance.forItems(ModItems.PIE_CRUST.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get())
				.addCriterion("sweet_berry_cheesecake_slice", InventoryChangeTrigger.Instance.forItems(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()))
				.build(consumer, "sweet_berry_cheesecake_from_slices");
		ShapedRecipeBuilder.shapedRecipe(ModItems.CHOCOLATE_PIE.get(), 1)
				.patternLine("ccc")
				.patternLine("mmm")
				.patternLine("xOx")
				.key('c', Items.COCOA_BEANS)
				.key('m', ForgeTags.MILK)
				.key('x', Items.SUGAR)
				.key('O', ModItems.PIE_CRUST.get())
				.addCriterion("pie_crust", InventoryChangeTrigger.Instance.forItems(ModItems.PIE_CRUST.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.CHOCOLATE_PIE.get(), 1)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.CHOCOLATE_PIE_SLICE.get())
				.addCriterion("chocolate_pie_slice", InventoryChangeTrigger.Instance.forItems(ModItems.CHOCOLATE_PIE_SLICE.get()))
				.build(consumer, "chocolate_pie_from_slices");
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
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.NETHER_SALAD.get())
				.addIngredient(Items.CRIMSON_FUNGUS)
				.addIngredient(Items.WARPED_FUNGUS)
				.addIngredient(Items.BOWL)
				.addCriterion("bowl", InventoryChangeTrigger.Instance.forItems(Items.BOWL))
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
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.EGG_SANDWICH.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ModItems.FRIED_EGG.get())
				.addIngredient(ModItems.FRIED_EGG.get())
				.addCriterion("fried_egg", InventoryChangeTrigger.Instance.forItems(ModItems.FRIED_EGG.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CHICKEN_SANDWICH.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ForgeTags.COOKED_CHICKEN)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addCriterion("cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HAMBURGER.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ModItems.BEEF_PATTY.get())
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addCriterion("hamburger", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.STUFFED_POTATO.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ForgeTags.COOKED_BEEF)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.MILK)
				.addCriterion("baked_potato", InventoryChangeTrigger.Instance.forItems(Items.BAKED_POTATO))
				.build(consumer);
	}
	private void recipesCookedMeals(Consumer<IFinishedRecipe> consumer) {}
}
