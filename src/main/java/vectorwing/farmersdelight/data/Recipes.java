package vectorwing.farmersdelight.data;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.data.recipes.CookingRecipes;
import vectorwing.farmersdelight.data.recipes.CuttingRecipes;
import vectorwing.farmersdelight.data.recipes.SmeltingRecipes;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Recipes extends RecipeProvider
{
	public Recipes(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
		recipesVanillaAlternatives(consumer);
		recipesBlocks(consumer);
		recipesCanvasSigns(consumer);
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
		ShapelessRecipeBuilder.shapeless(Items.PUMPKIN_SEEDS)
				.requires(ModItems.PUMPKIN_SLICE.get())
				.unlockedBy("has_pumpkin_slice", InventoryChangeTrigger.Instance.hasItems(ModItems.PUMPKIN_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_seeds_from_slice"));
		ShapedRecipeBuilder.shaped(Items.SCAFFOLDING, 6)
				.pattern("b#b")
				.pattern("b b")
				.pattern("b b")
				.define('b', Items.BAMBOO)
				.define('#', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "scaffolding_from_canvas"));
		ShapedRecipeBuilder.shaped(Items.LEAD)
				.pattern("rr ")
				.pattern("rr ")
				.pattern("  r")
				.define('r', ModItems.ROPE.get())
				.unlockedBy("has_rope", InventoryChangeTrigger.Instance.hasItems(ModItems.ROPE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "lead_from_rope"));
		ShapedRecipeBuilder.shaped(Items.PAINTING)
				.pattern("sss")
				.pattern("scs")
				.pattern("sss")
				.define('s', Items.STICK)
				.define('c', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "painting_from_canvas"));
		ShapedRecipeBuilder.shaped(Items.PUMPKIN)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.PUMPKIN_SLICE.get())
				.unlockedBy("has_pumpkin_slice", InventoryChangeTrigger.Instance.hasItems(ModItems.PUMPKIN_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_from_slices"));
		ShapedRecipeBuilder.shaped(Items.CAKE)
				.pattern("mmm")
				.pattern("ses")
				.pattern("www")
				.define('m', ForgeTags.MILK)
				.define('s', Items.SUGAR)
				.define('e', ForgeTags.EGGS)
				.define('w', Items.WHEAT)
				.unlockedBy("has_milk_bottle", InventoryChangeTrigger.Instance.hasItems(ModItems.MILK_BOTTLE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_milk_bottle"));
		ShapelessRecipeBuilder.shapeless(Items.CAKE)
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.unlockedBy("has_cake_slice", InventoryChangeTrigger.Instance.hasItems(ModItems.CAKE_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_slices"));
		ShapelessRecipeBuilder.shapeless(Items.BOOK)
				.requires(Items.PAPER)
				.requires(Items.PAPER)
				.requires(Items.PAPER)
				.requires(ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "book_from_canvas"));
		ShapelessRecipeBuilder.shapeless(Items.MILK_BUCKET)
				.requires(Items.BUCKET)
				.requires(ModItems.MILK_BOTTLE.get())
				.requires(ModItems.MILK_BOTTLE.get())
				.requires(ModItems.MILK_BOTTLE.get())
				.unlockedBy("has_milk_bottle", InventoryChangeTrigger.Instance.hasItems(ModItems.MILK_BOTTLE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "milk_bucket_from_bottles"));
		ShapelessRecipeBuilder.shapeless(Items.PAPER)
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.unlockedBy("has_tree_bark", InventoryChangeTrigger.Instance.hasItems(ModItems.TREE_BARK.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "paper_from_tree_bark"));
	}

	private void recipesCanvasSigns(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.CANVAS_SIGN.get(), 3)
				.pattern("w#w")
				.pattern("w#w")
				.pattern(" / ")
				.define('w', ItemTags.PLANKS)
				.define('#', ModItems.CANVAS.get())
				.define('/', Items.STICK)
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.WHITE_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_WHITE)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.ORANGE_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_ORANGE)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.MAGENTA_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_MAGENTA)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.LIGHT_BLUE_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_LIGHT_BLUE)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.YELLOW_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_YELLOW)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.LIME_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_LIME)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.PINK_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_PINK)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.GRAY_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_GRAY)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.LIGHT_GRAY_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_LIGHT_GRAY)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.CYAN_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_CYAN)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.PURPLE_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_PURPLE)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.BLUE_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_BLUE)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.BROWN_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_BROWN)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.GREEN_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_GREEN)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.RED_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_RED)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.BLACK_CANVAS_SIGN.get(), 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(Tags.Items.DYES_BLACK)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("canvas_sign")
				.save(consumer);
	}

	private void recipesBlocks(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModBlocks.STOVE.get())
				.pattern("iii")
				.pattern("B B")
				.pattern("BCB")
				.define('i', Tags.Items.INGOTS_IRON)
				.define('B', Blocks.BRICKS)
				.define('C', Blocks.CAMPFIRE)
				.unlockedBy("has_campfire", InventoryChangeTrigger.Instance.hasItems(Blocks.CAMPFIRE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.COOKING_POT.get())
				.pattern("bSb")
				.pattern("iWi")
				.pattern("iii")
				.define('b', Items.BRICK)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('S', Items.WOODEN_SHOVEL)
				.define('W', Items.WATER_BUCKET)
				.unlockedBy("has_iron_ingot", InventoryChangeTrigger.Instance.hasItems(Items.IRON_INGOT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.BASKET.get())
				.pattern("b b")
				.pattern("# #")
				.pattern("b#b")
				.define('b', Items.BAMBOO)
				.define('#', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.CUTTING_BOARD.get())
				.pattern("/##")
				.pattern("/##")
				.define('/', Items.STICK)
				.define('#', ItemTags.PLANKS)
				.unlockedBy("has_stick", InventoryChangeTrigger.Instance.hasItems(Items.STICK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.SKILLET.get())
				.pattern(" ##")
				.pattern(" ##")
				.pattern("/  ")
				.define('/', Items.BRICK)
				.define('#', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_brick", InventoryChangeTrigger.Instance.hasItems(Items.BRICK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.OAK_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.OAK_SLAB)
				.define('D', Items.OAK_TRAPDOOR)
				.unlockedBy("has_oak_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.OAK_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.BIRCH_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.BIRCH_SLAB)
				.define('D', Items.BIRCH_TRAPDOOR)
				.unlockedBy("has_birch_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.BIRCH_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.SPRUCE_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.SPRUCE_SLAB)
				.define('D', Items.SPRUCE_TRAPDOOR)
				.unlockedBy("has_spruce_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.SPRUCE_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.JUNGLE_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.JUNGLE_SLAB)
				.define('D', Items.JUNGLE_TRAPDOOR)
				.unlockedBy("has_jungle_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.JUNGLE_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.ACACIA_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.ACACIA_SLAB)
				.define('D', Items.ACACIA_TRAPDOOR)
				.unlockedBy("has_acacia_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.ACACIA_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.DARK_OAK_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.DARK_OAK_SLAB)
				.define('D', Items.DARK_OAK_TRAPDOOR)
				.unlockedBy("has_dark_oak_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.DARK_OAK_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.CRIMSON_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.CRIMSON_SLAB)
				.define('D', Items.CRIMSON_TRAPDOOR)
				.unlockedBy("has_crimson_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.CRIMSON_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModBlocks.WARPED_PANTRY.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.WARPED_SLAB)
				.define('D', Items.WARPED_TRAPDOOR)
				.unlockedBy("has_warped_trapdoor", InventoryChangeTrigger.Instance.hasItems(Items.WARPED_TRAPDOOR))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.ROPE.get(), 3)
				.pattern("s")
				.pattern("s")
				.pattern("s")
				.define('s', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.Instance.hasItems(ModItems.STRAW.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.SAFETY_NET.get(), 1)
				.pattern("rr")
				.pattern("rr")
				.define('r', ModItems.ROPE.get())
				.unlockedBy("has_rope", InventoryChangeTrigger.Instance.hasItems(ModItems.ROPE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.CABBAGE_CRATE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.CABBAGE.get())
				.unlockedBy("has_cabbage", InventoryChangeTrigger.Instance.hasItems(ModItems.CABBAGE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.TOMATO_CRATE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.TOMATO.get())
				.unlockedBy("has_tomato", InventoryChangeTrigger.Instance.hasItems(ModItems.TOMATO.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.ONION_CRATE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.ONION.get())
				.unlockedBy("has_onion", InventoryChangeTrigger.Instance.hasItems(ModItems.ONION.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.RICE_BALE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.RICE_PANICLE.get())
				.unlockedBy("has_rice_panicle", InventoryChangeTrigger.Instance.hasItems(ModItems.RICE_PANICLE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.RICE_BAG.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.RICE.get())
				.unlockedBy("has_rice", InventoryChangeTrigger.Instance.hasItems(ModItems.RICE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.STRAW_BALE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.Instance.hasItems(ModItems.STRAW.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.CANVAS_RUG.get(), 2)
				.requires(ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.CANVAS.get(), 1)
				.requires(ModItems.CANVAS_RUG.get())
				.requires(ModItems.CANVAS_RUG.get())
				.unlockedBy("has_canvas_rug", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS_RUG.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "canvas_from_canvas_rug"));
		ShapelessRecipeBuilder.shapeless(ModItems.ORGANIC_COMPOST.get(), 1)
				.requires(Items.DIRT)
				.requires(Items.ROTTEN_FLESH)
				.requires(Items.ROTTEN_FLESH)
				.requires(ModItems.STRAW.get())
				.requires(ModItems.STRAW.get())
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.unlockedBy("has_rotten_flesh", InventoryChangeTrigger.Instance.hasItems(Items.ROTTEN_FLESH))
				.unlockedBy("has_straw", InventoryChangeTrigger.Instance.hasItems(ModItems.STRAW.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "organic_compost_from_rotten_flesh"));
		ShapelessRecipeBuilder.shapeless(ModItems.ORGANIC_COMPOST.get(), 1)
				.requires(Items.DIRT)
				.requires(ModItems.STRAW.get())
				.requires(ModItems.STRAW.get())
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.unlockedBy("has_tree_bark", InventoryChangeTrigger.Instance.hasItems(ModItems.TREE_BARK.get()))
				.unlockedBy("has_straw", InventoryChangeTrigger.Instance.hasItems(ModItems.STRAW.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "organic_compost_from_tree_bark"));
		ShapedRecipeBuilder.shaped(ModItems.TATAMI.get(), 2)
				.pattern("cs")
				.pattern("sc")
				.define('c', ModItems.CANVAS.get())
				.define('s', ModItems.STRAW.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);

		// BREAKING DOWN
		ShapelessRecipeBuilder.shapeless(ModItems.FULL_TATAMI_MAT.get(), 2)
				.requires(ModItems.TATAMI.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.HALF_TATAMI_MAT.get(), 2)
				.requires(ModItems.FULL_TATAMI_MAT.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);

		// COMBINING BACK
		ShapelessRecipeBuilder.shapeless(ModItems.FULL_TATAMI_MAT.get(), 1)
				.requires(ModItems.HALF_TATAMI_MAT.get())
				.requires(ModItems.HALF_TATAMI_MAT.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "full_tatami_mat_from_halves"));
		ShapelessRecipeBuilder.shapeless(ModItems.TATAMI.get(), 1)
				.requires(ModItems.FULL_TATAMI_MAT.get())
				.requires(ModItems.FULL_TATAMI_MAT.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.Instance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "tatami_block_from_full"));
	}

	private void recipesTools(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.FLINT_KNIFE.get())
				.pattern(" m")
				.pattern("s ")
				.define('m', Items.FLINT)
				.define('s', Items.STICK)
				.unlockedBy("has_stick", InventoryChangeTrigger.Instance.hasItems(Items.STICK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.IRON_KNIFE.get())
				.pattern(" m")
				.pattern("s ")
				.define('m', Tags.Items.INGOTS_IRON)
				.define('s', Items.STICK)
				.unlockedBy("has_iron_ingot", InventoryChangeTrigger.Instance.hasItems(Items.IRON_INGOT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.DIAMOND_KNIFE.get())
				.pattern(" m")
				.pattern("s ")
				.define('m', Items.DIAMOND)
				.define('s', Items.STICK)
				.unlockedBy("has_diamond", InventoryChangeTrigger.Instance.hasItems(Items.DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.GOLDEN_KNIFE.get())
				.pattern(" m")
				.pattern("s ")
				.define('m', Items.GOLD_INGOT)
				.define('s', Items.STICK)
				.unlockedBy("has_gold_ingot", InventoryChangeTrigger.Instance.hasItems(Items.GOLD_INGOT))
				.save(consumer);
		SmithingRecipeBuilder.smithing(Ingredient.of(ModItems.DIAMOND_KNIFE.get()), Ingredient.of(Items.NETHERITE_INGOT), ModItems.NETHERITE_KNIFE.get())
				.unlocks("has_netherite_ingot", InventoryChangeTrigger.Instance.hasItems(Items.NETHERITE_INGOT))
				.save(consumer, FarmersDelight.MODID + ":netherite_knife_smithing");
	}

	private void recipesMaterials(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.CANVAS.get())
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.Instance.hasItems(ModItems.STRAW.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(Items.CARROT, 9)
				.requires(ModItems.CARROT_CRATE.get())
				.unlockedBy("has_carrot_crate", InventoryChangeTrigger.Instance.hasItems(ModItems.CARROT_CRATE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "carrot_from_crate"));
		ShapelessRecipeBuilder.shapeless(Items.POTATO, 9)
				.requires(ModItems.POTATO_CRATE.get())
				.unlockedBy("has_potato_crate", InventoryChangeTrigger.Instance.hasItems(ModItems.POTATO_CRATE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "potato_from_crate"));
		ShapelessRecipeBuilder.shapeless(Items.BEETROOT, 9)
				.requires(ModItems.BEETROOT_CRATE.get())
				.unlockedBy("has_beetroot_crate", InventoryChangeTrigger.Instance.hasItems(ModItems.BEETROOT_CRATE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "beetroot_from_crate"));
		ShapelessRecipeBuilder.shapeless(ModItems.CABBAGE.get(), 9)
				.requires(ModItems.CABBAGE_CRATE.get())
				.unlockedBy("has_cabbage_crate", InventoryChangeTrigger.Instance.hasItems(ModItems.CABBAGE_CRATE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.TOMATO.get(), 9)
				.requires(ModItems.TOMATO_CRATE.get())
				.unlockedBy("has_tomato_crate", InventoryChangeTrigger.Instance.hasItems(ModItems.TOMATO_CRATE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.ONION.get(), 9)
				.requires(ModItems.ONION_CRATE.get())
				.unlockedBy("has_onion_crate", InventoryChangeTrigger.Instance.hasItems(ModItems.ONION_CRATE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.RICE_PANICLE.get(), 9)
				.requires(ModItems.RICE_BALE.get())
				.unlockedBy("has_rice_bale", InventoryChangeTrigger.Instance.hasItems(ModItems.RICE_BALE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.RICE.get(), 9)
				.requires(ModItems.RICE_BAG.get())
				.unlockedBy("has_rice_bag", InventoryChangeTrigger.Instance.hasItems(ModItems.RICE_BAG.get()))
				.save(consumer, FarmersDelight.MODID + ":rice_from_bag");
		ShapelessRecipeBuilder.shapeless(ModItems.STRAW.get(), 9)
				.requires(ModItems.STRAW_BALE.get())
				.unlockedBy("has_straw_bale", InventoryChangeTrigger.Instance.hasItems(ModItems.STRAW_BALE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.RICE.get())
				.requires(ModItems.RICE_PANICLE.get())
				.unlockedBy("has_rice_panicle", InventoryChangeTrigger.Instance.hasItems(ModItems.RICE_PANICLE.get()))
				.save(consumer);
	}

	private void recipesFoodstuffs(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(ModItems.TOMATO_SEEDS.get())
				.requires(Ingredient.of(ModItems.TOMATO.get(), ModItems.ROTTEN_TOMATO.get()))
				.unlockedBy("has_tomato", InventoryChangeTrigger.Instance.hasItems(ModItems.TOMATO.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.MILK_BOTTLE.get(), 3)
				.requires(Items.MILK_BUCKET)
				.requires(Items.GLASS_BOTTLE)
				.requires(Items.GLASS_BOTTLE)
				.requires(Items.GLASS_BOTTLE)
				.unlockedBy("has_milk_bucket", InventoryChangeTrigger.Instance.hasItems(Items.MILK_BUCKET))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.MELON_JUICE.get(), 1)
				.requires(Items.MELON_SLICE)
				.requires(Items.MELON_SLICE)
				.requires(Items.SUGAR)
				.requires(Items.MELON_SLICE)
				.requires(Items.MELON_SLICE)
				.requires(Items.GLASS_BOTTLE)
				.unlockedBy("has_melon_slice", InventoryChangeTrigger.Instance.hasItems(Items.MELON_SLICE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.WHEAT_DOUGH.get(), 3)
				.requires(Items.WATER_BUCKET)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_wheat", InventoryChangeTrigger.Instance.hasItems(Items.WHEAT))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "wheat_dough_from_water"));
		ShapelessRecipeBuilder.shapeless(ModItems.WHEAT_DOUGH.get(), 3)
				.requires(ForgeTags.EGGS)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_wheat", InventoryChangeTrigger.Instance.hasItems(Items.WHEAT))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "wheat_dough_from_eggs"));
		ShapedRecipeBuilder.shaped(ModItems.PIE_CRUST.get(), 1)
				.pattern("wMw")
				.pattern(" w ")
				.define('w', Items.WHEAT)
				.define('M', ForgeTags.MILK)
				.unlockedBy("has_wheat", InventoryChangeTrigger.Instance.hasItems(Items.WHEAT))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.SWEET_BERRY_COOKIE.get(), 8)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_sweet_berries", InventoryChangeTrigger.Instance.hasItems(Items.SWEET_BERRIES))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.HONEY_COOKIE.get(), 8)
				.requires(Items.HONEY_BOTTLE)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_honey_bottle", InventoryChangeTrigger.Instance.hasItems(Items.HONEY_BOTTLE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.CABBAGE.get())
				.requires(ModItems.CABBAGE_LEAF.get())
				.requires(ModItems.CABBAGE_LEAF.get())
				.unlockedBy("has_cabbage_leaf", InventoryChangeTrigger.Instance.hasItems(ModItems.CABBAGE_LEAF.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cabbage_from_leaves"));
		ShapelessRecipeBuilder.shapeless(ModItems.HORSE_FEED.get(), 1)
				.requires(Ingredient.of(Items.HAY_BLOCK, ModItems.RICE_BALE.get()))
				.requires(Items.APPLE)
				.requires(Items.APPLE)
				.requires(Items.GOLDEN_CARROT)
				.unlockedBy("has_golden_carrot", InventoryChangeTrigger.Instance.hasItems(Items.GOLDEN_CARROT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.MELON_POPSICLE.get(), 1)
				.pattern(" mm")
				.pattern("imm")
				.pattern("-i ")
				.define('m', Items.MELON_SLICE)
				.define('i', Items.ICE)
				.define('-', Items.STICK)
				.unlockedBy("has_melon", InventoryChangeTrigger.Instance.hasItems(Items.MELON_SLICE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.FRUIT_SALAD.get(), 1)
				.requires(Items.APPLE)
				.requires(Items.MELON_SLICE)
				.requires(Items.MELON_SLICE)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.SWEET_BERRIES)
				.requires(ModItems.PUMPKIN_SLICE.get())
				.requires(Items.BOWL)
				.unlockedBy("has_fruits", InventoryChangeTrigger.Instance.hasItems(Items.MELON_SLICE, Items.SWEET_BERRIES, Items.APPLE, ModItems.PUMPKIN_SLICE.get()))
				.save(consumer);
	}

	private void recipesFoodBlocks(Consumer<IFinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.APPLE_PIE.get(), 1)
				.pattern("###")
				.pattern("aaa")
				.pattern("xOx")
				.define('#', Items.WHEAT)
				.define('a', Items.APPLE)
				.define('x', Items.SUGAR)
				.define('O', ModItems.PIE_CRUST.get())
				.unlockedBy("has_pie_crust", InventoryChangeTrigger.Instance.hasItems(ModItems.PIE_CRUST.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.APPLE_PIE.get(), 1)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.APPLE_PIE_SLICE.get())
				.unlockedBy("has_apple_pie_slice", InventoryChangeTrigger.Instance.hasItems(ModItems.APPLE_PIE_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "apple_pie_from_slices"));
		ShapedRecipeBuilder.shaped(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.pattern("sss")
				.pattern("sss")
				.pattern("mOm")
				.define('s', Items.SWEET_BERRIES)
				.define('m', ForgeTags.MILK)
				.define('O', ModItems.PIE_CRUST.get())
				.unlockedBy("has_pie_crust", InventoryChangeTrigger.Instance.hasItems(ModItems.PIE_CRUST.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get())
				.unlockedBy("has_sweet_berry_cheesecake_slice", InventoryChangeTrigger.Instance.hasItems(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "sweet_berry_cheesecake_from_slices"));
		ShapedRecipeBuilder.shaped(ModItems.CHOCOLATE_PIE.get(), 1)
				.pattern("ccc")
				.pattern("mmm")
				.pattern("xOx")
				.define('c', Items.COCOA_BEANS)
				.define('m', ForgeTags.MILK)
				.define('x', Items.SUGAR)
				.define('O', ModItems.PIE_CRUST.get())
				.unlockedBy("has_pie_crust", InventoryChangeTrigger.Instance.hasItems(ModItems.PIE_CRUST.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ModItems.CHOCOLATE_PIE.get(), 1)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.CHOCOLATE_PIE_SLICE.get())
				.unlockedBy("has_chocolate_pie_slice", InventoryChangeTrigger.Instance.hasItems(ModItems.CHOCOLATE_PIE_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "chocolate_pie_from_slices"));
	}

	private void recipesCraftedMeals(Consumer<IFinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(ModItems.MIXED_SALAD.get())
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_TOMATO)
				.requires(ForgeTags.CROPS_ONION)
				.requires(Items.BEETROOT)
				.requires(Items.BOWL)
				.unlockedBy("has_bowl", InventoryChangeTrigger.Instance.hasItems(Items.BOWL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.NETHER_SALAD.get())
				.requires(Items.CRIMSON_FUNGUS)
				.requires(Items.WARPED_FUNGUS)
				.requires(Items.BOWL)
				.unlockedBy("has_bowl", InventoryChangeTrigger.Instance.hasItems(Items.BOWL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.BARBECUE_STICK.get(), 2)
				.requires(ForgeTags.CROPS_TOMATO)
				.requires(ForgeTags.CROPS_ONION)
				.requires(Items.COOKED_BEEF)
				.requires(Items.COOKED_CHICKEN)
				.requires(Items.STICK)
				.requires(Items.STICK)
				.unlockedBy("has_cooked_beef", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_BEEF))
				.unlockedBy("has_cooked_chicken", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_CHICKEN))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.EGG_SANDWICH.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_EGGS)
				.requires(ForgeTags.COOKED_EGGS)
				.unlockedBy("has_fried_egg", InventoryChangeTrigger.Instance.hasItems(ModItems.FRIED_EGG.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.CHICKEN_SANDWICH.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_CHICKEN)
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(Items.CARROT)
				.unlockedBy("has_cooked_chicken", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_CHICKEN))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.HAMBURGER.get())
				.requires(ForgeTags.BREAD)
				.requires(ModItems.BEEF_PATTY.get())
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_TOMATO)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_beef_patty", InventoryChangeTrigger.Instance.hasItems(ModItems.BEEF_PATTY.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.BACON_SANDWICH.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_BACON)
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_TOMATO)
				.unlockedBy("has_bacon", InventoryChangeTrigger.Instance.hasItems(ModItems.COOKED_BACON.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.MUTTON_WRAP.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_mutton", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_MUTTON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.STUFFED_POTATO.get())
				.requires(Items.BAKED_POTATO)
				.requires(ForgeTags.COOKED_BEEF)
				.requires(Items.CARROT)
				.requires(ForgeTags.MILK)
				.unlockedBy("has_baked_potato", InventoryChangeTrigger.Instance.hasItems(Items.BAKED_POTATO))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.GRILLED_SALMON.get())
				.requires(ForgeTags.COOKED_FISHES_SALMON)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.BOWL)
				.requires(ForgeTags.CROPS_CABBAGE)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_salmon", InventoryChangeTrigger.Instance.hasItems(Items.SALMON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.STEAK_AND_POTATOES.get())
				.requires(Items.BAKED_POTATO)
				.requires(Items.COOKED_BEEF)
				.requires(Items.BOWL)
				.requires(ForgeTags.CROPS_ONION)
				.requires(ModItems.COOKED_RICE.get())
				.unlockedBy("has_baked_potato", InventoryChangeTrigger.Instance.hasItems(Items.BAKED_POTATO))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.ROASTED_MUTTON_CHOPS.get())
				.requires(ModItems.COOKED_MUTTON_CHOPS.get())
				.requires(Items.BEETROOT)
				.requires(Items.BOWL)
				.requires(ModItems.COOKED_RICE.get())
				.requires(ForgeTags.CROPS_TOMATO)
				.unlockedBy("has_mutton", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_MUTTON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.BACON_AND_EGGS.get())
				.requires(ModItems.COOKED_BACON.get())
				.requires(ModItems.COOKED_BACON.get())
				.requires(Items.BOWL)
				.requires(ForgeTags.COOKED_EGGS)
				.requires(ForgeTags.COOKED_EGGS)
				.unlockedBy("has_bacon", InventoryChangeTrigger.Instance.hasItems(ModItems.COOKED_BACON.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.ROAST_CHICKEN_BLOCK.get())
				.requires(ForgeTags.CROPS_ONION)
				.requires(ForgeTags.EGGS)
				.requires(Items.BREAD)
				.requires(Items.CARROT)
				.requires(Items.COOKED_CHICKEN)
				.requires(Items.BAKED_POTATO)
				.requires(Items.CARROT)
				.requires(Items.BOWL)
				.requires(Items.BAKED_POTATO)
				.unlockedBy("has_cooked_chicken", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_CHICKEN))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.SHEPHERDS_PIE_BLOCK.get())
				.requires(Items.BAKED_POTATO)
				.requires(ForgeTags.MILK)
				.requires(Items.BAKED_POTATO)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.CROPS_ONION)
				.requires(Items.BOWL)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_cooked_mutton", InventoryChangeTrigger.Instance.hasItems(Items.COOKED_MUTTON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ModItems.HONEY_GLAZED_HAM_BLOCK.get())
				.requires(Items.SWEET_BERRIES)
				.requires(Items.HONEY_BOTTLE)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.SWEET_BERRIES)
				.requires(ModItems.SMOKED_HAM.get())
				.requires(Items.SWEET_BERRIES)
				.requires(ModItems.COOKED_RICE.get())
				.requires(Items.BOWL)
				.requires(ModItems.COOKED_RICE.get())
				.unlockedBy("has_smoked_ham", InventoryChangeTrigger.Instance.hasItems(ModItems.SMOKED_HAM.get()))
				.save(consumer);
	}
}
