package vectorwing.farmersdelight.data.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CraftingRecipes
{
	public static void register(Consumer<FinishedRecipe> consumer) {
		recipesVanillaAlternatives(consumer);
		recipesBlocks(consumer);
		recipesCanvasSigns(consumer);
		recipesTools(consumer);
		recipesMaterials(consumer);
		recipesFoodstuffs(consumer);
		recipesFoodBlocks(consumer);
		recipesCraftedMeals(consumer);
		SpecialRecipeBuilder.special(ModRecipeSerializers.FOOD_SERVING.get()).save(consumer, "food_serving");
	}

	public static void canvasSignDyeing(Consumer<FinishedRecipe> consumer, ItemLike canvasSign, ItemLike hangingCanvasSign, TagKey<Item> dyeTag) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, canvasSign, 1)
				.requires(ModTags.CANVAS_SIGNS)
				.requires(dyeTag)
				.unlockedBy("has_canvas_sign", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS_SIGN.get()))
				.group("fd_canvas_sign")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, hangingCanvasSign, 1)
				.requires(ModTags.HANGING_CANVAS_SIGNS)
				.requires(dyeTag)
				.unlockedBy("has_hanging_canvas_sign", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HANGING_CANVAS_SIGN.get()))
				.group("fd_hanging_canvas_sign")
				.save(consumer);
	}

	/**
	 * The following recipes should ALWAYS define a custom save location.
	 * If not, they fall on the minecraft namespace, overriding vanilla recipes instead of being alternatives.
	 */
	private static void recipesVanillaAlternatives(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PUMPKIN_SEEDS)
				.requires(ModItems.PUMPKIN_SLICE.get())
				.unlockedBy("has_pumpkin_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PUMPKIN_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_seeds_from_slice"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SCAFFOLDING, 6)
				.pattern("b#b")
				.pattern("b b")
				.pattern("b b")
				.define('b', Items.BAMBOO)
				.define('#', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "scaffolding_from_canvas"));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.LEAD)
				.pattern("ss ")
				.pattern("ss ")
				.pattern("  s")
				.define('s', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "lead_from_straw"));
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.PAINTING)
				.pattern("sss")
				.pattern("scs")
				.pattern("sss")
				.define('s', Items.STICK)
				.define('c', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "painting_from_canvas"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Items.PUMPKIN)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.PUMPKIN_SLICE.get())
				.unlockedBy("has_pumpkin_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PUMPKIN_SLICE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "pumpkin_from_slices"));
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Items.CAKE)
				.pattern("mmm")
				.pattern("ses")
				.pattern("www")
				.define('m', ForgeTags.MILK)
				.define('s', Items.SUGAR)
				.define('e', ForgeTags.EGGS)
				.define('w', Items.WHEAT)
				.unlockedBy("has_milk_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MILK_BOTTLE.get()))
				.group("cake")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_milk_bottle"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.CAKE)
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.requires(ModItems.CAKE_SLICE.get())
				.unlockedBy("has_cake_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CAKE_SLICE.get()))
				.group("cake")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cake_from_slices"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BOOK)
				.requires(Items.PAPER)
				.requires(Items.PAPER)
				.requires(Items.PAPER)
				.requires(ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "book_from_canvas"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MILK_BUCKET)
				.requires(Items.BUCKET)
				.requires(ModItems.MILK_BOTTLE.get())
				.requires(ModItems.MILK_BOTTLE.get())
				.requires(ModItems.MILK_BOTTLE.get())
				.requires(ModItems.MILK_BOTTLE.get())
				.unlockedBy("has_milk_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.MILK_BOTTLE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "milk_bucket_from_bottles"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER)
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.unlockedBy("has_tree_bark", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TREE_BARK.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "paper_from_tree_bark"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Items.PACKED_MUD, 2)
				.requires(ModItems.STRAW.get())
				.requires(Items.MUD)
				.requires(Items.MUD)
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "packed_mud_from_straw"));
	}

	private static void recipesCanvasSigns(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.CANVAS_SIGN.get(), 3)
				.pattern("w#w")
				.pattern("w#w")
				.pattern(" / ")
				.define('w', ItemTags.PLANKS)
				.define('#', ModItems.CANVAS.get())
				.define('/', Items.STICK)
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.HANGING_CANVAS_SIGN.get(), 6)
				.pattern("X X")
				.pattern("w#w")
				.pattern("w#w")
				.define('X', Items.CHAIN)
				.define('w', ItemTags.LOGS)
				.define('#', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);

		canvasSignDyeing(consumer, ModItems.WHITE_CANVAS_SIGN.get(), ModItems.WHITE_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_WHITE);
		canvasSignDyeing(consumer, ModItems.ORANGE_CANVAS_SIGN.get(), ModItems.ORANGE_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_ORANGE);
		canvasSignDyeing(consumer, ModItems.MAGENTA_CANVAS_SIGN.get(), ModItems.MAGENTA_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_MAGENTA);
		canvasSignDyeing(consumer, ModItems.LIGHT_BLUE_CANVAS_SIGN.get(), ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_LIGHT_BLUE);
		canvasSignDyeing(consumer, ModItems.YELLOW_CANVAS_SIGN.get(), ModItems.YELLOW_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_YELLOW);
		canvasSignDyeing(consumer, ModItems.LIME_CANVAS_SIGN.get(), ModItems.LIME_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_LIME);
		canvasSignDyeing(consumer, ModItems.PINK_CANVAS_SIGN.get(), ModItems.PINK_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_PINK);
		canvasSignDyeing(consumer, ModItems.GRAY_CANVAS_SIGN.get(), ModItems.GRAY_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_GRAY);
		canvasSignDyeing(consumer, ModItems.LIGHT_GRAY_CANVAS_SIGN.get(), ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_LIGHT_GRAY);
		canvasSignDyeing(consumer, ModItems.CYAN_CANVAS_SIGN.get(), ModItems.CYAN_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_CYAN);
		canvasSignDyeing(consumer, ModItems.PURPLE_CANVAS_SIGN.get(), ModItems.PURPLE_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_PURPLE);
		canvasSignDyeing(consumer, ModItems.BLUE_CANVAS_SIGN.get(), ModItems.BLUE_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_BLUE);
		canvasSignDyeing(consumer, ModItems.BROWN_CANVAS_SIGN.get(), ModItems.BROWN_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_BROWN);
		canvasSignDyeing(consumer, ModItems.GREEN_CANVAS_SIGN.get(), ModItems.GREEN_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_GREEN);
		canvasSignDyeing(consumer, ModItems.RED_CANVAS_SIGN.get(), ModItems.RED_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_RED);
		canvasSignDyeing(consumer, ModItems.BLACK_CANVAS_SIGN.get(), ModItems.BLACK_HANGING_CANVAS_SIGN.get(), Tags.Items.DYES_BLACK);
	}

	private static void recipesBlocks(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.STOVE.get())
				.pattern("iii")
				.pattern("B B")
				.pattern("BCB")
				.define('i', Tags.Items.INGOTS_IRON)
				.define('B', Blocks.BRICKS)
				.define('C', Blocks.CAMPFIRE)
				.unlockedBy("has_campfire", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CAMPFIRE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.COOKING_POT.get())
				.pattern("bSb")
				.pattern("iWi")
				.pattern("iii")
				.define('b', Items.BRICK)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('S', Items.WOODEN_SHOVEL)
				.define('W', Items.WATER_BUCKET)
				.unlockedBy("has_iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BASKET.get())
				.pattern("b b")
				.pattern("# #")
				.pattern("b#b")
				.define('b', Items.BAMBOO)
				.define('#', ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CUTTING_BOARD.get())
				.pattern("/##")
				.pattern("/##")
				.define('/', Items.STICK)
				.define('#', ItemTags.PLANKS)
				.unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SKILLET.get())
				.pattern(" ##")
				.pattern(" ##")
				.pattern("/  ")
				.define('/', Items.BRICK)
				.define('#', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_brick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BRICK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.OAK_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.OAK_SLAB)
				.define('D', Items.OAK_TRAPDOOR)
				.unlockedBy("has_oak_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.OAK_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BIRCH_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.BIRCH_SLAB)
				.define('D', Items.BIRCH_TRAPDOOR)
				.unlockedBy("has_birch_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BIRCH_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.SPRUCE_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.SPRUCE_SLAB)
				.define('D', Items.SPRUCE_TRAPDOOR)
				.unlockedBy("has_spruce_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SPRUCE_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.JUNGLE_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.JUNGLE_SLAB)
				.define('D', Items.JUNGLE_TRAPDOOR)
				.unlockedBy("has_jungle_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.JUNGLE_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ACACIA_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.ACACIA_SLAB)
				.define('D', Items.ACACIA_TRAPDOOR)
				.unlockedBy("has_acacia_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ACACIA_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.DARK_OAK_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.DARK_OAK_SLAB)
				.define('D', Items.DARK_OAK_TRAPDOOR)
				.unlockedBy("has_dark_oak_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DARK_OAK_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.MANGROVE_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.MANGROVE_SLAB)
				.define('D', Items.MANGROVE_TRAPDOOR)
				.unlockedBy("has_mangrove_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MANGROVE_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CHERRY_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.CHERRY_SLAB)
				.define('D', Items.CHERRY_TRAPDOOR)
				.unlockedBy("has_cherry_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CHERRY_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BAMBOO_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.BAMBOO_SLAB)
				.define('D', Items.BAMBOO_TRAPDOOR)
				.unlockedBy("has_bamboo_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAMBOO_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CRIMSON_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.CRIMSON_SLAB)
				.define('D', Items.CRIMSON_TRAPDOOR)
				.unlockedBy("has_crimson_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRIMSON_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.WARPED_CABINET.get())
				.pattern("___")
				.pattern("D D")
				.pattern("___")
				.define('_', Items.WARPED_SLAB)
				.define('D', Items.WARPED_TRAPDOOR)
				.unlockedBy("has_warped_trapdoor", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WARPED_TRAPDOOR))
				.group("fd_cabinet")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.ROPE.get(), 4)
				.pattern("s")
				.pattern("s")
				.define('s', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.group("fd_rope")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SAFETY_NET.get(), 1)
				.pattern("rr")
				.pattern("rr")
				.define('r', ModItems.ROPE.get())
				.unlockedBy("has_rope", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ROPE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.ROPE.get(), 4)
				.requires(ModItems.SAFETY_NET.get())
				.unlockedBy("has_safety_net", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SAFETY_NET.get()))
				.group("fd_rope")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "rope_from_safety_net"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.CABBAGE_CRATE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.CABBAGE.get())
				.unlockedBy("has_cabbage", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CABBAGE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.TOMATO_CRATE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.TOMATO.get())
				.unlockedBy("has_tomato", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.ONION_CRATE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.ONION.get())
				.unlockedBy("has_onion", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ONION.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RICE_BALE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.RICE_PANICLE.get())
				.unlockedBy("has_rice_panicle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE_PANICLE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.RICE_BAG.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.RICE.get())
				.unlockedBy("has_rice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.STRAW_BALE.get(), 1)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.CANVAS_RUG.get(), 2)
				.requires(ModItems.CANVAS.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CANVAS.get(), 1)
				.requires(ModItems.CANVAS_RUG.get())
				.requires(ModItems.CANVAS_RUG.get())
				.unlockedBy("has_canvas_rug", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS_RUG.get()))
				.group("fd_canvas")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "canvas_from_canvas_rug"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ORGANIC_COMPOST.get(), 1)
				.requires(Items.DIRT)
				.requires(Items.ROTTEN_FLESH)
				.requires(Items.ROTTEN_FLESH)
				.requires(ModItems.STRAW.get())
				.requires(ModItems.STRAW.get())
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.unlockedBy("has_rotten_flesh", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ROTTEN_FLESH))
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.group("fd_organic_compost")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "organic_compost_from_rotten_flesh"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.ORGANIC_COMPOST.get(), 1)
				.requires(Items.DIRT)
				.requires(ModItems.STRAW.get())
				.requires(ModItems.STRAW.get())
				.requires(Items.BONE_MEAL)
				.requires(Items.BONE_MEAL)
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.requires(ModItems.TREE_BARK.get())
				.unlockedBy("has_tree_bark", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TREE_BARK.get()))
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.group("fd_organic_compost")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "organic_compost_from_tree_bark"));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.TATAMI.get(), 2)
				.pattern("cs")
				.pattern("sc")
				.define('c', ModItems.CANVAS.get())
				.define('s', ModItems.STRAW.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.group("fd_tatami")
				.save(consumer);

		// BREAKING DOWN
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.FULL_TATAMI_MAT.get(), 2)
				.requires(ModItems.TATAMI.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.group("fd_full_tatami_mat")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.HALF_TATAMI_MAT.get(), 2)
				.requires(ModItems.FULL_TATAMI_MAT.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.save(consumer);

		// COMBINING BACK
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.FULL_TATAMI_MAT.get(), 1)
				.requires(ModItems.HALF_TATAMI_MAT.get())
				.requires(ModItems.HALF_TATAMI_MAT.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.group("fd_full_tatami_mat")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "full_tatami_mat_from_halves"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.TATAMI.get(), 1)
				.requires(ModItems.FULL_TATAMI_MAT.get())
				.requires(ModItems.FULL_TATAMI_MAT.get())
				.unlockedBy("has_canvas", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CANVAS.get()))
				.group("fd_tatami")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "tatami_block_from_full"));
	}

	private static void recipesTools(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.FLINT_KNIFE.get())
				.pattern("m")
				.pattern("s")
				.define('m', Items.FLINT)
				.define('s', Items.STICK)
				.unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.IRON_KNIFE.get())
				.pattern("m")
				.pattern("s")
				.define('m', Tags.Items.INGOTS_IRON)
				.define('s', Items.STICK)
				.unlockedBy("has_iron_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_KNIFE.get())
				.pattern("m")
				.pattern("s")
				.define('m', Items.DIAMOND)
				.define('s', Items.STICK)
				.unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_KNIFE.get())
				.pattern("m")
				.pattern("s")
				.define('m', Items.GOLD_INGOT)
				.define('s', Items.STICK)
				.unlockedBy("has_gold_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
				.save(consumer);
		SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(ModItems.DIAMOND_KNIFE.get()), Ingredient.of(Items.NETHERITE_INGOT), RecipeCategory.COMBAT, ModItems.NETHERITE_KNIFE.get())
				.unlocks("has_netherite_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
				.save(consumer, FarmersDelight.MODID + ":netherite_knife_smithing");
	}

	private static void recipesMaterials(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CANVAS.get())
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.STRAW.get())
				.unlockedBy("has_straw", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW.get()))
				.group("fd_canvas")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.CARROT, 9)
				.requires(ModItems.CARROT_CRATE.get())
				.unlockedBy("has_carrot_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CARROT_CRATE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "carrot_from_crate"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.POTATO, 9)
				.requires(ModItems.POTATO_CRATE.get())
				.unlockedBy("has_potato_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.POTATO_CRATE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "potato_from_crate"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, Items.BEETROOT, 9)
				.requires(ModItems.BEETROOT_CRATE.get())
				.unlockedBy("has_beetroot_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BEETROOT_CRATE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "beetroot_from_crate"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CABBAGE.get(), 9)
				.requires(ModItems.CABBAGE_CRATE.get())
				.unlockedBy("has_cabbage_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CABBAGE_CRATE.get()))
				.group("fd_cabbage")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.TOMATO.get(), 9)
				.requires(ModItems.TOMATO_CRATE.get())
				.unlockedBy("has_tomato_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO_CRATE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ONION.get(), 9)
				.requires(ModItems.ONION_CRATE.get())
				.unlockedBy("has_onion_crate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ONION_CRATE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RICE_PANICLE.get(), 9)
				.requires(ModItems.RICE_BALE.get())
				.unlockedBy("has_rice_bale", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE_BALE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RICE.get(), 9)
				.requires(ModItems.RICE_BAG.get())
				.unlockedBy("has_rice_bag", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE_BAG.get()))
				.group("fd_rice")
				.save(consumer, FarmersDelight.MODID + ":rice_from_bag");
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.STRAW.get(), 9)
				.requires(ModItems.STRAW_BALE.get())
				.unlockedBy("has_straw_bale", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAW_BALE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RICE.get())
				.requires(ModItems.RICE_PANICLE.get())
				.unlockedBy("has_rice_panicle", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RICE_PANICLE.get()))
				.group("fd_rice")
				.save(consumer);
	}

	private static void recipesFoodstuffs(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TOMATO_SEEDS.get())
				.requires(Ingredient.of(ModItems.TOMATO.get(), ModItems.ROTTEN_TOMATO.get()))
				.unlockedBy("has_tomato", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MILK_BOTTLE.get(), 4)
				.requires(Items.MILK_BUCKET)
				.requires(Items.GLASS_BOTTLE)
				.requires(Items.GLASS_BOTTLE)
				.requires(Items.GLASS_BOTTLE)
				.requires(Items.GLASS_BOTTLE)
				.unlockedBy("has_milk_bucket", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MILK_BUCKET))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MELON_JUICE.get(), 1)
				.requires(Items.MELON_SLICE)
				.requires(Items.MELON_SLICE)
				.requires(Items.SUGAR)
				.requires(Items.MELON_SLICE)
				.requires(Items.MELON_SLICE)
				.requires(Items.GLASS_BOTTLE)
				.unlockedBy("has_melon_slice", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MELON_SLICE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.WHEAT_DOUGH.get(), 3)
				.requires(Items.WATER_BUCKET)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
				.group("fd_wheat_dough")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "wheat_dough_from_water"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.WHEAT_DOUGH.get(), 3)
				.requires(ForgeTags.EGGS)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
				.group("fd_wheat_dough")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "wheat_dough_from_eggs"));
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.PIE_CRUST.get(), 1)
				.pattern("wMw")
				.pattern(" w ")
				.define('w', Items.WHEAT)
				.define('M', ForgeTags.MILK)
				.unlockedBy("has_wheat", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHEAT))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SWEET_BERRY_COOKIE.get(), 8)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_sweet_berries", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SWEET_BERRIES))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.HONEY_COOKIE.get(), 8)
				.requires(Items.HONEY_BOTTLE)
				.requires(Items.WHEAT)
				.requires(Items.WHEAT)
				.unlockedBy("has_honey_bottle", InventoryChangeTrigger.TriggerInstance.hasItems(Items.HONEY_BOTTLE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CABBAGE.get())
				.requires(ModItems.CABBAGE_LEAF.get())
				.requires(ModItems.CABBAGE_LEAF.get())
				.unlockedBy("has_cabbage_leaf", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CABBAGE_LEAF.get()))
				.group("fd_cabbage")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cabbage_from_leaves"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.HORSE_FEED.get(), 1)
				.requires(Ingredient.of(Items.HAY_BLOCK, ModItems.RICE_BALE.get()))
				.requires(Items.APPLE)
				.requires(Items.APPLE)
				.requires(Items.GOLDEN_CARROT)
				.unlockedBy("has_golden_carrot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLDEN_CARROT))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.MELON_POPSICLE.get(), 1)
				.pattern(" mm")
				.pattern("imm")
				.pattern("-i ")
				.define('m', Items.MELON_SLICE)
				.define('i', Items.ICE)
				.define('-', Items.STICK)
				.unlockedBy("has_melon", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MELON_SLICE))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.FRUIT_SALAD.get(), 1)
				.requires(Items.APPLE)
				.requires(Items.MELON_SLICE)
				.requires(Items.MELON_SLICE)
				.requires(ForgeTags.BERRIES)
				.requires(ForgeTags.BERRIES)
				.requires(ModItems.PUMPKIN_SLICE.get())
				.requires(Items.BOWL)
				.unlockedBy("has_fruits", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MELON_SLICE, Items.SWEET_BERRIES, Items.APPLE, ModItems.PUMPKIN_SLICE.get()))
				.save(consumer);
	}

	private static void recipesFoodBlocks(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.APPLE_PIE.get(), 1)
				.pattern("###")
				.pattern("aaa")
				.pattern("xOx")
				.define('#', Items.WHEAT)
				.define('a', Items.APPLE)
				.define('x', Items.SUGAR)
				.define('O', ModItems.PIE_CRUST.get())
				.unlockedBy("has_pie_crust", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PIE_CRUST.get()))
				.group("fd_apple_pie")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.APPLE_PIE.get(), 1)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.APPLE_PIE_SLICE.get())
				.unlockedBy("has_apple_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.APPLE_PIE_SLICE.get()))
				.group("fd_apple_pie")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "apple_pie_from_slices"));
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.pattern("sss")
				.pattern("sss")
				.pattern("mOm")
				.define('s', Items.SWEET_BERRIES)
				.define('m', ForgeTags.MILK)
				.define('O', ModItems.PIE_CRUST.get())
				.unlockedBy("has_pie_crust", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PIE_CRUST.get()))
				.group("fd_sweet_berry_cheesecake")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.SWEET_BERRY_CHEESECAKE.get(), 1)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get())
				.unlockedBy("has_sweet_berry_cheesecake_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()))
				.group("fd_sweet_berry_cheesecake")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "sweet_berry_cheesecake_from_slices"));
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.CHOCOLATE_PIE.get(), 1)
				.pattern("ccc")
				.pattern("mmm")
				.pattern("xOx")
				.define('c', Items.COCOA_BEANS)
				.define('m', ForgeTags.MILK)
				.define('x', Items.SUGAR)
				.define('O', ModItems.PIE_CRUST.get())
				.unlockedBy("has_pie_crust", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PIE_CRUST.get()))
				.group("fd_chocolate_pie")
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.CHOCOLATE_PIE.get(), 1)
				.pattern("##")
				.pattern("##")
				.define('#', ModItems.CHOCOLATE_PIE_SLICE.get())
				.unlockedBy("has_chocolate_pie_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CHOCOLATE_PIE_SLICE.get()))
				.group("fd_chocolate_pie")
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "chocolate_pie_from_slices"));
	}

	private static void recipesCraftedMeals(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MIXED_SALAD.get())
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_TOMATO)
				.requires(Items.BEETROOT)
				.requires(Items.BOWL)
				.unlockedBy("has_bowl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BOWL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.NETHER_SALAD.get())
				.requires(Items.CRIMSON_FUNGUS)
				.requires(Items.WARPED_FUNGUS)
				.requires(Items.BOWL)
				.unlockedBy("has_bowl", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BOWL))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BARBECUE_STICK.get())
				.requires(ForgeTags.CROPS_TOMATO)
				.requires(ForgeTags.CROPS_ONION)
				.requires(Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(ForgeTags.COOKED_BEEF),
						new Ingredient.TagValue(ForgeTags.COOKED_PORK),
						new Ingredient.TagValue(ForgeTags.COOKED_CHICKEN),
						new Ingredient.TagValue(ForgeTags.COOKED_MUTTON),
						new Ingredient.TagValue(ForgeTags.COOKED_FISHES),
						new Ingredient.ItemValue(new ItemStack(Items.COOKED_RABBIT))
				)))
				.requires(Items.STICK)
				.unlockedBy("has_tomato", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO.get()))
				.unlockedBy("has_onion", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ONION.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.EGG_SANDWICH.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_EGGS)
				.requires(ForgeTags.COOKED_EGGS)
				.unlockedBy("has_fried_egg", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FRIED_EGG.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHICKEN_SANDWICH.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_CHICKEN)
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(Items.CARROT)
				.unlockedBy("has_cooked_chicken", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_CHICKEN))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.HAMBURGER.get())
				.requires(ForgeTags.BREAD)
				.requires(ModItems.BEEF_PATTY.get())
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_TOMATO)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_beef_patty", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BEEF_PATTY.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BACON_SANDWICH.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_BACON)
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_TOMATO)
				.unlockedBy("has_bacon", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COOKED_BACON.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.MUTTON_WRAP.get())
				.requires(ForgeTags.BREAD)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.SALAD_INGREDIENTS)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_mutton", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_MUTTON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STUFFED_POTATO.get())
				.requires(Items.BAKED_POTATO)
				.requires(ForgeTags.COOKED_BEEF)
				.requires(ForgeTags.MILK)
				.unlockedBy("has_baked_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAKED_POTATO))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SALMON_ROLL.get(), 2)
				.requires(ModItems.SALMON_SLICE.get())
				.requires(ModItems.SALMON_SLICE.get())
				.requires(ModItems.COOKED_RICE.get())
				.unlockedBy("has_salmon_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALMON_SLICE.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.COD_ROLL.get(), 2)
				.requires(ModItems.COD_SLICE.get())
				.requires(ModItems.COD_SLICE.get())
				.requires(ModItems.COOKED_RICE.get())
				.unlockedBy("has_cod_slice", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COD_SLICE.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.KELP_ROLL.get(), 1)
				.pattern("RXR")
				.pattern("###")
				.define('#', Items.DRIED_KELP)
				.define('R', ModItems.COOKED_RICE.get())
				.define('X', Items.CARROT)
				.unlockedBy("has_dried_kelp", InventoryChangeTrigger.TriggerInstance.hasItems(Items.DRIED_KELP))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.GRILLED_SALMON.get())
				.requires(ForgeTags.COOKED_FISHES_SALMON)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.BOWL)
				.requires(ForgeTags.CROPS_CABBAGE)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_salmon", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SALMON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STEAK_AND_POTATOES.get())
				.requires(Items.BAKED_POTATO)
				.requires(Items.COOKED_BEEF)
				.requires(Items.BOWL)
				.requires(ForgeTags.CROPS_ONION)
				.requires(ModItems.COOKED_RICE.get())
				.unlockedBy("has_baked_potato", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BAKED_POTATO))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ROASTED_MUTTON_CHOPS.get())
				.requires(ModItems.COOKED_MUTTON_CHOPS.get())
				.requires(Items.BEETROOT)
				.requires(Items.BOWL)
				.requires(ModItems.COOKED_RICE.get())
				.requires(ForgeTags.CROPS_TOMATO)
				.unlockedBy("has_mutton", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_MUTTON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BACON_AND_EGGS.get())
				.requires(ModItems.COOKED_BACON.get())
				.requires(ModItems.COOKED_BACON.get())
				.requires(Items.BOWL)
				.requires(ForgeTags.COOKED_EGGS)
				.requires(ForgeTags.COOKED_EGGS)
				.unlockedBy("has_bacon", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COOKED_BACON.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ROAST_CHICKEN_BLOCK.get())
				.requires(ForgeTags.CROPS_ONION)
				.requires(ForgeTags.EGGS)
				.requires(Items.BREAD)
				.requires(Items.CARROT)
				.requires(Items.COOKED_CHICKEN)
				.requires(Items.BAKED_POTATO)
				.requires(Items.CARROT)
				.requires(Items.BOWL)
				.requires(Items.BAKED_POTATO)
				.unlockedBy("has_cooked_chicken", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_CHICKEN))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SHEPHERDS_PIE_BLOCK.get())
				.requires(Items.BAKED_POTATO)
				.requires(ForgeTags.MILK)
				.requires(Items.BAKED_POTATO)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.COOKED_MUTTON)
				.requires(ForgeTags.CROPS_ONION)
				.requires(Items.BOWL)
				.requires(ForgeTags.CROPS_ONION)
				.unlockedBy("has_cooked_mutton", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COOKED_MUTTON))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.HONEY_GLAZED_HAM_BLOCK.get())
				.requires(Items.SWEET_BERRIES)
				.requires(Items.HONEY_BOTTLE)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.SWEET_BERRIES)
				.requires(ModItems.SMOKED_HAM.get())
				.requires(Items.SWEET_BERRIES)
				.requires(ModItems.COOKED_RICE.get())
				.requires(Items.BOWL)
				.requires(ModItems.COOKED_RICE.get())
				.unlockedBy("has_smoked_ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SMOKED_HAM.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RICE_ROLL_MEDLEY_BLOCK.get())
				.requires(ModItems.KELP_ROLL_SLICE.get())
				.requires(ModItems.KELP_ROLL_SLICE.get())
				.requires(ModItems.KELP_ROLL_SLICE.get())
				.requires(ModItems.SALMON_ROLL.get())
				.requires(ModItems.SALMON_ROLL.get())
				.requires(ModItems.SALMON_ROLL.get())
				.requires(ModItems.COD_ROLL.get())
				.requires(Items.BOWL)
				.requires(ModItems.COD_ROLL.get())
				.unlockedBy("has_rice_roll", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALMON_ROLL.get(), ModItems.COD_ROLL.get(), ModItems.KELP_ROLL_SLICE.get()))
				.save(consumer);
	}
}
