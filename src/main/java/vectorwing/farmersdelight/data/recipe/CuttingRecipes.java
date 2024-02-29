package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.ToolActions;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

public class CuttingRecipes
{
	public static void register(RecipeOutput output) {
		// Knife
		cuttingAnimalItems(output);
		cuttingVegetables(output);
		cuttingFoods(output);
		cuttingFlowers(output);

		// Pickaxe
		salvagingMinerals(output);

		// Axe
		strippingWood(output);
		salvagingWoodenFurniture(output);

		// Shovel
		diggingSediments(output);

		// Shears
		salvagingUsingShears(output);
	}

	private static void cuttingAnimalItems(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BEEF), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.MINCED_BEEF.get(), 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PORKCHOP), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.BACON.get(), 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CHICKEN), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_CHICKEN), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COD), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_COD), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_SALMON), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.HAM.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.PORKCHOP, 2)
				.addResult(Items.BONE)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SMOKED_HAM.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.COOKED_PORKCHOP, 2)
				.addResult(Items.BONE)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MUTTON), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.MUTTON_CHOPS.get(), 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_MUTTON), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_MUTTON_CHOPS.get(), 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.INK_SAC), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.BLACK_DYE, 2)
				.build(output);
	}

	private static void cuttingVegetables(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CABBAGE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.CABBAGE_LEAF.get(), 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RICE_PANICLE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.RICE.get(), 1)
				.addResult(ModItems.STRAW.get())
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MELON), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.MELON_SLICE, 9)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.PUMPKIN_SLICE.get(), 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BROWN_MUSHROOM_COLONY.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.BROWN_MUSHROOM, 5)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RED_MUSHROOM_COLONY.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.RED_MUSHROOM, 5)
				.build(output);
	}

	private static void cuttingFoods(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ForgeTags.DOUGH), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.RAW_PASTA.get(), 1)
				.build(output, new ResourceLocation(FarmersDelight.MODID, "cutting/tag_dough"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.KELP_ROLL.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.KELP_ROLL_SLICE.get(), 3)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CAKE), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.CAKE_SLICE.get(), 7)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.APPLE_PIE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.APPLE_PIE_SLICE.get(), 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SWEET_BERRY_CHEESECAKE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CHOCOLATE_PIE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.CHOCOLATE_PIE_SLICE.get(), 4)
				.build(output);
	}

	private static void cuttingFlowers(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WITHER_ROSE), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.BLACK_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CORNFLOWER), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.BLUE_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLUE_ORCHID), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.LIGHT_BLUE_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AZURE_BLUET), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.OXEYE_DAISY), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WHITE_TULIP), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ALLIUM), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.MAGENTA_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ORANGE_TULIP), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.ORANGE_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PINK_TULIP), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.PINK_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.RED_TULIP), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.RED_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.POPPY), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.RED_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LILY_OF_THE_VALLEY), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.WHITE_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DANDELION), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.YELLOW_DYE, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_BEETROOTS.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.BEETROOT_SEEDS, 1)
				.addResult(Items.RED_DYE)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CABBAGES.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.CABBAGE_SEEDS.get(), 1)
				.addResultWithChance(Items.YELLOW_DYE, 0.5F, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CARROTS.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.CARROT, 1)
				.addResultWithChance(Items.LIGHT_GRAY_DYE, 0.5F, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_ONIONS.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.ONION.get(), 1)
				.addResult(Items.MAGENTA_DYE, 2)
				.addResultWithChance(Items.LIME_DYE, 0.1F)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_POTATOES.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), Items.POTATO, 1)
				.addResultWithChance(Items.PURPLE_DYE, 0.5F, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_RICE.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.RICE.get(), 1)
				.addResultWithChance(ModItems.STRAW.get(), 0.5F)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_TOMATOES.get()), Ingredient.of(ForgeTags.TOOLS_KNIVES), ModItems.TOMATO_SEEDS.get(), 1)
				.addResultWithChance(ModItems.TOMATO.get(), 0.2F)
				.addResultWithChance(Items.GREEN_DYE, 0.1F)
				.build(output);
	}

	private static void salvagingMinerals(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BRICKS), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.BRICK, 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.NETHER_BRICKS), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.NETHER_BRICK, 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.STONE), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.COBBLESTONE, 1)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DEEPSLATE), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.COBBLED_DEEPSLATE, 1)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.QUARTZ_BLOCK), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.QUARTZ, 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AMETHYST_BLOCK), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.AMETHYST_SHARD, 4)
				.build(output);
	}

	private static void strippingWood(RecipeOutput output) {
		stripLogForBark(output, Items.OAK_LOG, Items.STRIPPED_OAK_LOG);
		stripLogForBark(output, Items.OAK_WOOD, Items.STRIPPED_OAK_WOOD);
		stripLogForBark(output, Items.SPRUCE_LOG, Items.STRIPPED_SPRUCE_LOG);
		stripLogForBark(output, Items.SPRUCE_WOOD, Items.STRIPPED_SPRUCE_WOOD);
		stripLogForBark(output, Items.BIRCH_LOG, Items.STRIPPED_BIRCH_LOG);
		stripLogForBark(output, Items.BIRCH_WOOD, Items.STRIPPED_BIRCH_WOOD);
		stripLogForBark(output, Items.JUNGLE_LOG, Items.STRIPPED_JUNGLE_LOG);
		stripLogForBark(output, Items.JUNGLE_WOOD, Items.STRIPPED_JUNGLE_WOOD);
		stripLogForBark(output, Items.ACACIA_LOG, Items.STRIPPED_ACACIA_LOG);
		stripLogForBark(output, Items.ACACIA_WOOD, Items.STRIPPED_ACACIA_WOOD);
		stripLogForBark(output, Items.DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_LOG);
		stripLogForBark(output, Items.DARK_OAK_WOOD, Items.STRIPPED_DARK_OAK_WOOD);
		stripLogForBark(output, Items.MANGROVE_LOG, Items.STRIPPED_MANGROVE_LOG);
		stripLogForBark(output, Items.MANGROVE_WOOD, Items.STRIPPED_MANGROVE_WOOD);
		stripLogForBark(output, Items.CHERRY_LOG, Items.STRIPPED_CHERRY_LOG);
		stripLogForBark(output, Items.CHERRY_WOOD, Items.STRIPPED_CHERRY_WOOD);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BAMBOO_BLOCK), new ToolActionIngredient(ToolActions.AXE_STRIP), Items.STRIPPED_BAMBOO_BLOCK)
				.addResult(ModItems.STRAW.get())
				.addSound(SoundEvents.AXE_STRIP).build(output);
		stripLogForBark(output, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM);
		stripLogForBark(output, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE);
		stripLogForBark(output, Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM);
		stripLogForBark(output, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE);
	}

	private static void salvagingWoodenFurniture(RecipeOutput output) {
		salvagePlankFromFurniture(output, Items.OAK_PLANKS, Items.OAK_DOOR, Items.OAK_TRAPDOOR, Items.OAK_SIGN, Items.OAK_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.SPRUCE_PLANKS, Items.SPRUCE_DOOR, Items.SPRUCE_TRAPDOOR, Items.SPRUCE_SIGN, Items.SPRUCE_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.BIRCH_PLANKS, Items.BIRCH_DOOR, Items.BIRCH_TRAPDOOR, Items.BIRCH_SIGN, Items.BIRCH_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.JUNGLE_PLANKS, Items.JUNGLE_DOOR, Items.JUNGLE_TRAPDOOR, Items.JUNGLE_SIGN, Items.JUNGLE_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.ACACIA_PLANKS, Items.ACACIA_DOOR, Items.ACACIA_TRAPDOOR, Items.ACACIA_SIGN, Items.ACACIA_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.DARK_OAK_PLANKS, Items.DARK_OAK_DOOR, Items.DARK_OAK_TRAPDOOR, Items.DARK_OAK_SIGN, Items.DARK_OAK_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.MANGROVE_PLANKS, Items.MANGROVE_DOOR, Items.MANGROVE_TRAPDOOR, Items.MANGROVE_SIGN, Items.MANGROVE_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.CHERRY_PLANKS, Items.CHERRY_DOOR, Items.CHERRY_TRAPDOOR, Items.CHERRY_SIGN, Items.CHERRY_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.BAMBOO_PLANKS, Items.BAMBOO_DOOR, Items.BAMBOO_TRAPDOOR, Items.BAMBOO_SIGN, Items.BAMBOO_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.CRIMSON_PLANKS, Items.CRIMSON_DOOR, Items.CRIMSON_TRAPDOOR, Items.CRIMSON_SIGN, Items.CRIMSON_HANGING_SIGN);
		salvagePlankFromFurniture(output, Items.WARPED_PLANKS, Items.WARPED_DOOR, Items.WARPED_TRAPDOOR, Items.WARPED_SIGN, Items.WARPED_HANGING_SIGN);
	}

	private static void diggingSediments(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CLAY), new ToolActionIngredient(ToolActions.SHOVEL_DIG), Items.CLAY_BALL, 4)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.GRAVEL), new ToolActionIngredient(ToolActions.SHOVEL_DIG), Items.GRAVEL, 1)
				.addResultWithChance(Items.FLINT, 0.1F)
				.build(output);
	}

	private static void salvagingUsingShears(RecipeOutput output) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SADDLE), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 2)
				.addResultWithChance(Items.IRON_NUGGET, 0.5F, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_HORSE_ARMOR), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 2)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_HELMET), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_CHESTPLATE), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_LEGGINGS), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_BOOTS), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(output);
	}


	/**
	 * Generates an axe-cutting recipe for each furniture, resulting in one plank of the given type.
	 */
	private static void salvagePlankFromFurniture(RecipeOutput output, ItemLike plank, ItemLike door, ItemLike trapdoor, ItemLike sign, ItemLike hangingSign) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(door), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(trapdoor), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(sign), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(output);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(hangingSign), new ToolActionIngredient(ToolActions.AXE_DIG), plank).build(output);
	}

	/**
	 * Generates an axe-stripping recipe for the pair of given logs, with custom sound and a Tree Bark result attached.
	 */
	private static void stripLogForBark(RecipeOutput output, ItemLike log, ItemLike strippedLog) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), new ToolActionIngredient(ToolActions.AXE_STRIP), strippedLog)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.AXE_STRIP).build(output);
	}
}
