package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.data.recipes.CookingRecipes;
import vectorwing.farmersdelight.data.recipes.CuttingRecipes;
import vectorwing.farmersdelight.data.recipes.SmeltingRecipes;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;

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
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		recipesVanillaAlternatives(consumer);
		recipesBlocks(consumer);
		recipesTools(consumer);
		recipesMaterials(consumer);
		recipesFoodstuffs(consumer);
		recipesFoodBlocks(consumer);
		recipesCraftedMeals(consumer);

		SmeltingRecipes.register(consumer);
		CookingRecipes.register(consumer);
		CuttingRecipes.register(consumer);
	}

	/**
	 * The following recipes should ALWAYS define a custom save location.
	 * If not, they fall on the minecraft namespace, overriding vanilla recipes instead of being alternatives.
	 */
	private void recipesVanillaAlternatives(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(Items.PUMPKIN_SEEDS)
				.addIngredient(ModItems.PUMPKIN_SLICE.get())
				.addCriterion("has_pumpkin_slice", InventoryChangeTrigger.Instance.forItems(ModItems.PUMPKIN_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_seeds_from_slice"));
		ShapedRecipeBuilder.shapedRecipe(Items.SCAFFOLDING, 6)
				.patternLine("b#b")
				.patternLine("b b")
				.patternLine("b b")
				.key('b', Items.BAMBOO)
				.key('#', ModItems.CANVAS.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "scaffolding_from_canvas"));
		ShapedRecipeBuilder.shapedRecipe(Items.LEAD)
				.patternLine("rr ")
				.patternLine("rr ")
				.patternLine("  r")
				.key('r', ModItems.ROPE.get())
				.addCriterion("has_rope", InventoryChangeTrigger.Instance.forItems(ModItems.ROPE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "lead_from_rope"));
		ShapedRecipeBuilder.shapedRecipe(Items.PAINTING)
				.patternLine("sss")
				.patternLine("scs")
				.patternLine("sss")
				.key('s', Items.STICK)
				.key('c', ModItems.CANVAS.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "painting_from_canvas"));
		ShapedRecipeBuilder.shapedRecipe(Items.PUMPKIN)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.PUMPKIN_SLICE.get())
				.addCriterion("has_pumpkin_slice", InventoryChangeTrigger.Instance.forItems(ModItems.PUMPKIN_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_from_slices"));
		ShapedRecipeBuilder.shapedRecipe(Items.CAKE)
				.patternLine("mmm")
				.patternLine("ses")
				.patternLine("www")
				.key('m', ForgeTags.MILK)
				.key('s', Items.SUGAR)
				.key('e', ForgeTags.EGGS)
				.key('w', Items.WHEAT)
				.addCriterion("has_milk_bottle", InventoryChangeTrigger.Instance.forItems(ModItems.MILK_BOTTLE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_milk_bottle"));
		ShapelessRecipeBuilder.shapelessRecipe(Items.CAKE)
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addIngredient(ModItems.CAKE_SLICE.get())
				.addCriterion("has_cake_slice", InventoryChangeTrigger.Instance.forItems(ModItems.CAKE_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_slices"));
		ShapelessRecipeBuilder.shapelessRecipe(Items.BOOK)
				.addIngredient(Items.PAPER)
				.addIngredient(Items.PAPER)
				.addIngredient(Items.PAPER)
				.addIngredient(ModItems.CANVAS.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "book_from_canvas"));
		ShapelessRecipeBuilder.shapelessRecipe(Items.MILK_BUCKET)
				.addIngredient(Items.BUCKET)
				.addIngredient(ModItems.MILK_BOTTLE.get())
				.addIngredient(ModItems.MILK_BOTTLE.get())
				.addIngredient(ModItems.MILK_BOTTLE.get())
				.addCriterion("has_milk_bottle", InventoryChangeTrigger.Instance.forItems(ModItems.MILK_BOTTLE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "milk_bucket_from_bottles"));
	}

	private void recipesBlocks(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.STOVE.get())
				.patternLine("iii")
				.patternLine("B B")
				.patternLine("BCB")
				.key('i', Items.IRON_INGOT)
				.key('B', Blocks.BRICKS)
				.key('C', Blocks.CAMPFIRE)
				.addCriterion("has_campfire", InventoryChangeTrigger.Instance.forItems(Blocks.CAMPFIRE))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.COOKING_POT.get())
				.patternLine("bSb")
				.patternLine("iWi")
				.patternLine("iii")
				.key('b', Items.BRICK)
				.key('i', Items.IRON_INGOT)
				.key('S', Items.WOODEN_SHOVEL)
				.key('W', Items.WATER_BUCKET)
				.addCriterion("has_iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASKET.get())
				.patternLine("b b")
				.patternLine("# #")
				.patternLine("b#b")
				.key('b', Items.BAMBOO)
				.key('#', ModItems.CANVAS.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.CUTTING_BOARD.get())
				.patternLine("/##")
				.patternLine("/##")
				.key('/', Items.STICK)
				.key('#', ItemTags.PLANKS)
				.addCriterion("has_stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.OAK_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.OAK_SLAB)
				.key('D', Items.OAK_TRAPDOOR)
				.addCriterion("has_oak_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.OAK_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BIRCH_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.BIRCH_SLAB)
				.key('D', Items.BIRCH_TRAPDOOR)
				.addCriterion("has_birch_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.BIRCH_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.SPRUCE_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.SPRUCE_SLAB)
				.key('D', Items.SPRUCE_TRAPDOOR)
				.addCriterion("has_spruce_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.SPRUCE_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.JUNGLE_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.JUNGLE_SLAB)
				.key('D', Items.JUNGLE_TRAPDOOR)
				.addCriterion("has_jungle_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.JUNGLE_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ACACIA_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.ACACIA_SLAB)
				.key('D', Items.ACACIA_TRAPDOOR)
				.addCriterion("has_acacia_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.ACACIA_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.DARK_OAK_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.DARK_OAK_SLAB)
				.key('D', Items.DARK_OAK_TRAPDOOR)
				.addCriterion("has_dark_oak_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.DARK_OAK_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.CRIMSON_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.CRIMSON_SLAB)
				.key('D', Items.CRIMSON_TRAPDOOR)
				.addCriterion("has_crimson_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.CRIMSON_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.WARPED_PANTRY.get())
				.patternLine("___")
				.patternLine("D D")
				.patternLine("___")
				.key('_', Items.WARPED_SLAB)
				.key('D', Items.WARPED_TRAPDOOR)
				.addCriterion("has_warped_trapdoor", InventoryChangeTrigger.Instance.forItems(Items.WARPED_TRAPDOOR))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.ROPE.get(), 3)
				.patternLine("s")
				.patternLine("s")
				.patternLine("s")
				.key('s', ModItems.STRAW.get())
				.addCriterion("has_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.SAFETY_NET.get(), 1)
				.patternLine("rr")
				.patternLine("rr")
				.key('r', ModItems.ROPE.get())
				.addCriterion("has_rope", InventoryChangeTrigger.Instance.forItems(ModItems.ROPE.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.CABBAGE_CRATE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.CABBAGE.get())
				.addCriterion("has_cabbage", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.TOMATO_CRATE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.TOMATO.get())
				.addCriterion("has_tomato", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.ONION_CRATE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.ONION.get())
				.addCriterion("has_onion", InventoryChangeTrigger.Instance.forItems(ModItems.ONION.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.RICE_BALE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.RICE_PANICLE.get())
				.addCriterion("has_rice_panicle", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_PANICLE.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.RICE_BAG.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.RICE.get())
				.addCriterion("has_rice", InventoryChangeTrigger.Instance.forItems(ModItems.RICE.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.STRAW_BALE.get(), 1)
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ModItems.STRAW.get())
				.addCriterion("has_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
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
				.addCriterion("has_rotten_flesh", InventoryChangeTrigger.Instance.forItems(Items.ROTTEN_FLESH))
				.addCriterion("has_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "organic_compost_from_rotten_flesh"));
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
				.addCriterion("has_tree_bark", InventoryChangeTrigger.Instance.forItems(ModItems.TREE_BARK.get()))
				.addCriterion("has_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "organic_compost_from_tree_bark"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.TATAMI.get(), 2)
				.patternLine("cs")
				.patternLine("sc")
				.key('c', ModItems.CANVAS.get())
				.key('s', ModItems.STRAW.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer);

		// BREAKING DOWN
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.FULL_TATAMI_MAT.get(), 2)
				.addIngredient(ModItems.TATAMI.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HALF_TATAMI_MAT.get(), 2)
				.addIngredient(ModItems.FULL_TATAMI_MAT.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer);

		// COMBINING BACK
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.FULL_TATAMI_MAT.get(), 1)
				.addIngredient(ModItems.HALF_TATAMI_MAT.get())
				.addIngredient(ModItems.HALF_TATAMI_MAT.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "full_tatami_mat_from_halves"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.TATAMI.get(), 1)
				.addIngredient(ModItems.FULL_TATAMI_MAT.get())
				.addIngredient(ModItems.FULL_TATAMI_MAT.get())
				.addCriterion("has_canvas", InventoryChangeTrigger.Instance.forItems(ModItems.CANVAS.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "tatami_block_from_full"));
	}

	private void recipesTools(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.FLINT_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.FLINT)
				.key('s', Items.STICK)
				.addCriterion("has_stick", InventoryChangeTrigger.Instance.forItems(Items.STICK))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.IRON_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.IRON_INGOT)
				.key('s', Items.STICK)
				.addCriterion("has_iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.DIAMOND_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.DIAMOND)
				.key('s', Items.STICK)
				.addCriterion("has_diamond", InventoryChangeTrigger.Instance.forItems(Items.DIAMOND))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.GOLDEN_KNIFE.get())
				.patternLine(" m")
				.patternLine("s ")
				.key('m', Items.GOLD_INGOT)
				.key('s', Items.STICK)
				.addCriterion("has_gold_ingot", InventoryChangeTrigger.Instance.forItems(Items.GOLD_INGOT))
				.build(consumer);
		SmithingRecipeBuilder.smithingRecipe(Ingredient.fromItems(ModItems.DIAMOND_KNIFE.get()), Ingredient.fromItems(Items.NETHERITE_INGOT), ModItems.NETHERITE_KNIFE.get())
				.addCriterion("has_netherite_ingot", InventoryChangeTrigger.Instance.forItems(Items.NETHERITE_INGOT))
				.build(consumer, FarmersDelight.MODID + ":netherite_knife_smithing");
	}

	private void recipesMaterials(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shapedRecipe(ModItems.CANVAS.get())
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.STRAW.get())
				.addCriterion("has_straw", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(Items.CARROT, 9)
				.addIngredient(ModItems.CARROT_CRATE.get())
				.addCriterion("has_carrot_crate", InventoryChangeTrigger.Instance.forItems(ModItems.CARROT_CRATE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "carrot_from_crate"));
		ShapelessRecipeBuilder.shapelessRecipe(Items.POTATO, 9)
				.addIngredient(ModItems.POTATO_CRATE.get())
				.addCriterion("has_potato_crate", InventoryChangeTrigger.Instance.forItems(ModItems.POTATO_CRATE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "potato_from_crate"));
		ShapelessRecipeBuilder.shapelessRecipe(Items.BEETROOT, 9)
				.addIngredient(ModItems.BEETROOT_CRATE.get())
				.addCriterion("has_beetroot_crate", InventoryChangeTrigger.Instance.forItems(ModItems.BEETROOT_CRATE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "beetroot_from_crate"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CABBAGE.get(), 9)
				.addIngredient(ModItems.CABBAGE_CRATE.get())
				.addCriterion("has_cabbage_crate", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE_CRATE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.TOMATO.get(), 9)
				.addIngredient(ModItems.TOMATO_CRATE.get())
				.addCriterion("has_tomato_crate", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO_CRATE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ONION.get(), 9)
				.addIngredient(ModItems.ONION_CRATE.get())
				.addCriterion("has_onion_crate", InventoryChangeTrigger.Instance.forItems(ModItems.ONION_CRATE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RICE_PANICLE.get(), 9)
				.addIngredient(ModItems.RICE_BALE.get())
				.addCriterion("has_rice_bale", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_BALE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RICE.get(), 9)
				.addIngredient(ModItems.RICE_BAG.get())
				.addCriterion("has_rice_bag", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_BAG.get()))
				.build(consumer, FarmersDelight.MODID + ":rice_from_bag");
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.STRAW.get(), 9)
				.addIngredient(ModItems.STRAW_BALE.get())
				.addCriterion("has_straw_bale", InventoryChangeTrigger.Instance.forItems(ModItems.STRAW_BALE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RICE.get())
				.addIngredient(ModItems.RICE_PANICLE.get())
				.addCriterion("has_rice_panicle", InventoryChangeTrigger.Instance.forItems(ModItems.RICE_PANICLE.get()))
				.build(consumer);
	}

	private void recipesFoodstuffs(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.TOMATO_SEEDS.get())
				.addIngredient(ModItems.TOMATO.get())
				.addCriterion("has_tomato", InventoryChangeTrigger.Instance.forItems(ModItems.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.MILK_BOTTLE.get(), 3)
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addCriterion("has_milk_bucket", InventoryChangeTrigger.Instance.forItems(Items.MILK_BUCKET))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.RAW_PASTA.get())
				.addIngredient(Ingredient.merge(Sets.newHashSet(
						Ingredient.fromTag(ForgeTags.EGGS),
						Ingredient.fromItems(Items.WATER_BUCKET)
				)))
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.WHEAT)
				.addCriterion("has_wheat", InventoryChangeTrigger.Instance.forItems(Items.WHEAT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.WHEAT_DOUGH.get(), 3)
				.patternLine("www")
				.patternLine(" b ")
				.key('w', Items.WHEAT)
				.key('b', Items.WATER_BUCKET)
				.addCriterion("has_wheat", InventoryChangeTrigger.Instance.forItems(Items.WHEAT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.PIE_CRUST.get(), 1)
				.patternLine("wMw")
				.patternLine(" w ")
				.key('w', Items.WHEAT)
				.key('M', ForgeTags.MILK)
				.addCriterion("has_wheat", InventoryChangeTrigger.Instance.forItems(Items.WHEAT))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.SWEET_BERRY_COOKIE.get(), 8)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.WHEAT)
				.addCriterion("has_sweet_berries", InventoryChangeTrigger.Instance.forItems(Items.SWEET_BERRIES))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HONEY_COOKIE.get(), 8)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.WHEAT)
				.addIngredient(Items.WHEAT)
				.addCriterion("has_honey_bottle", InventoryChangeTrigger.Instance.forItems(Items.HONEY_BOTTLE))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CABBAGE.get())
				.addIngredient(ModItems.CABBAGE_LEAF.get())
				.addIngredient(ModItems.CABBAGE_LEAF.get())
				.addCriterion("has_cabbage_leaf", InventoryChangeTrigger.Instance.forItems(ModItems.CABBAGE_LEAF.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "cabbage_from_leaves"));
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HORSE_FEED.get(), 1)
				.addIngredient(Ingredient.fromItems(Items.HAY_BLOCK, ModItems.RICE_BALE.get()))
				.addIngredient(Items.APPLE)
				.addIngredient(Items.APPLE)
				.addIngredient(Items.GOLDEN_CARROT)
				.addCriterion("has_golden_carrot", InventoryChangeTrigger.Instance.forItems(Items.GOLDEN_CARROT))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.MELON_POPSICLE.get(), 1)
				.patternLine(" mm")
				.patternLine("imm")
				.patternLine("-i ")
				.key('m', Items.MELON_SLICE)
				.key('i', Items.ICE)
				.key('-', Items.STICK)
				.addCriterion("has_melon", InventoryChangeTrigger.Instance.forItems(Items.MELON_SLICE))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.FRUIT_SALAD.get(), 1)
				.addIngredient(Items.APPLE)
				.addIngredient(Items.MELON_SLICE)
				.addIngredient(Items.MELON_SLICE)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(ModItems.PUMPKIN_SLICE.get())
				.addIngredient(Items.BOWL)
				.addCriterion("has_fruits", InventoryChangeTrigger.Instance.forItems(Items.MELON_SLICE, Items.SWEET_BERRIES, Items.APPLE, ModItems.PUMPKIN_SLICE.get()))
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
				.addCriterion("has_pie_crust", InventoryChangeTrigger.Instance.forItems(ModItems.PIE_CRUST.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.APPLE_PIE.get(), 1)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.APPLE_PIE_SLICE.get())
				.addCriterion("has_apple_pie_slice", InventoryChangeTrigger.Instance.forItems(ModItems.APPLE_PIE_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "apple_pie_from_slices"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.patternLine("sss")
				.patternLine("sss")
				.patternLine("mOm")
				.key('s', Items.SWEET_BERRIES)
				.key('m', ForgeTags.MILK)
				.key('O', ModItems.PIE_CRUST.get())
				.addCriterion("has_pie_crust", InventoryChangeTrigger.Instance.forItems(ModItems.PIE_CRUST.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get())
				.addCriterion("has_sweet_berry_cheesecake_slice", InventoryChangeTrigger.Instance.forItems(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "sweet_berry_cheesecake_from_slices"));
		ShapedRecipeBuilder.shapedRecipe(ModItems.CHOCOLATE_PIE.get(), 1)
				.patternLine("ccc")
				.patternLine("mmm")
				.patternLine("xOx")
				.key('c', Items.COCOA_BEANS)
				.key('m', ForgeTags.MILK)
				.key('x', Items.SUGAR)
				.key('O', ModItems.PIE_CRUST.get())
				.addCriterion("has_pie_crust", InventoryChangeTrigger.Instance.forItems(ModItems.PIE_CRUST.get()))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ModItems.CHOCOLATE_PIE.get(), 1)
				.patternLine("##")
				.patternLine("##")
				.key('#', ModItems.CHOCOLATE_PIE_SLICE.get())
				.addCriterion("has_chocolate_pie_slice", InventoryChangeTrigger.Instance.forItems(ModItems.CHOCOLATE_PIE_SLICE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "chocolate_pie_from_slices"));
	}

	private void recipesCraftedMeals(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.MIXED_SALAD.get())
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BOWL)
				.addCriterion("has_bowl", InventoryChangeTrigger.Instance.forItems(Items.BOWL))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.NETHER_SALAD.get())
				.addIngredient(Items.CRIMSON_FUNGUS)
				.addIngredient(Items.WARPED_FUNGUS)
				.addIngredient(Items.BOWL)
				.addCriterion("has_bowl", InventoryChangeTrigger.Instance.forItems(Items.BOWL))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.BARBECUE_STICK.get(), 2)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(Items.STICK)
				.addIngredient(Items.STICK)
				.addCriterion("has_cooked_beef", InventoryChangeTrigger.Instance.forItems(Items.COOKED_BEEF))
				.addCriterion("has_cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.EGG_SANDWICH.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ForgeTags.COOKED_EGGS)
				.addIngredient(ForgeTags.COOKED_EGGS)
				.addCriterion("has_fried_egg", InventoryChangeTrigger.Instance.forItems(ModItems.FRIED_EGG.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.CHICKEN_SANDWICH.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ForgeTags.COOKED_CHICKEN)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(Items.CARROT)
				.addCriterion("has_cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HAMBURGER.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ModItems.BEEF_PATTY.get())
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addCriterion("has_beef_patty", InventoryChangeTrigger.Instance.forItems(ModItems.BEEF_PATTY.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.BACON_SANDWICH.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(ForgeTags.COOKED_BACON)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addCriterion("has_bacon", InventoryChangeTrigger.Instance.forItems(ModItems.COOKED_BACON.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.MUTTON_WRAP.get())
				.addIngredient(ForgeTags.BREAD)
				.addIngredient(Items.COOKED_MUTTON)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.MILK)
				.addCriterion("has_mutton", InventoryChangeTrigger.Instance.forItems(Items.COOKED_MUTTON))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.STUFFED_POTATO.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ForgeTags.COOKED_BEEF)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.MILK)
				.addCriterion("has_baked_potato", InventoryChangeTrigger.Instance.forItems(Items.BAKED_POTATO))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.GRILLED_SALMON.get())
				.addIngredient(ForgeTags.COOKED_FISHES_SALMON)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(Items.BOWL)
				.addIngredient(ForgeTags.CROPS_CABBAGE)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addCriterion("has_salmon", InventoryChangeTrigger.Instance.forItems(Items.SALMON))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.STEAK_AND_POTATOES.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(Items.COOKED_BEEF)
				.addIngredient(Items.BOWL)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(ModItems.COOKED_RICE.get())
				.addCriterion("has_baked_potato", InventoryChangeTrigger.Instance.forItems(Items.BAKED_POTATO))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ROASTED_LAMB_CHOPS.get())
				.addIngredient(Items.MUTTON)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BOWL)
				.addIngredient(ModItems.COOKED_RICE.get())
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addCriterion("has_mutton", InventoryChangeTrigger.Instance.forItems(Items.COOKED_MUTTON))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.ROAST_CHICKEN_BLOCK.get())
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(Items.BREAD)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.COOKED_CHICKEN)
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.BOWL)
				.addIngredient(Items.BAKED_POTATO)
				.addCriterion("has_cooked_chicken", InventoryChangeTrigger.Instance.forItems(Items.COOKED_CHICKEN))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.SHEPHERDS_PIE_BLOCK.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ForgeTags.MILK)
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(Items.COOKED_MUTTON)
				.addIngredient(Items.COOKED_MUTTON)
				.addIngredient(Items.COOKED_MUTTON)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.BOWL)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addCriterion("has_cooked_mutton", InventoryChangeTrigger.Instance.forItems(Items.COOKED_MUTTON))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ModItems.HONEY_GLAZED_HAM_BLOCK.get())
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(ModItems.SMOKED_HAM.get())
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(ModItems.COOKED_RICE.get())
				.addIngredient(Items.BOWL)
				.addIngredient(ModItems.COOKED_RICE.get())
				.addCriterion("has_smoked_ham", InventoryChangeTrigger.Instance.forItems(ModItems.SMOKED_HAM.get()))
				.build(consumer);
	}
}
