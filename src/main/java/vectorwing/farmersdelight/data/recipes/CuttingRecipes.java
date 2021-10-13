package vectorwing.farmersdelight.data.recipes;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import vectorwing.farmersdelight.crafting.ingredients.ToolIngredient;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;

import java.util.function.Consumer;

public class CuttingRecipes
{
	public static void register(Consumer<IFinishedRecipe> consumer) {
		// Knife
		cuttingMeats(consumer);
		cuttingVegetables(consumer);
		cuttingPastries(consumer);
		cuttingFlowers(consumer);

		// Pickaxe
		salvagingBricks(consumer);

		// Axe
		strippingWood(consumer);
		salvagingWoodenFurniture(consumer);

		// Shovel
		diggingSediments(consumer);

		// Shears
		salvagingUsingShears(consumer);
	}

	private static void cuttingMeats(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BEEF), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.MINCED_BEEF.get(), 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.PORKCHOP), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.BACON.get(), 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CHICKEN), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_CHICKEN), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COD), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_COD), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SALMON), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_SALMON), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.HAM.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.PORKCHOP, 2)
				.addResult(Items.BONE)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.SMOKED_HAM.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.COOKED_PORKCHOP, 2)
				.addResult(Items.BONE)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.MUTTON), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.MUTTON_CHOPS.get(), 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_MUTTON), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.COOKED_MUTTON_CHOPS.get(), 2)
				.build(consumer);
	}

	private static void cuttingVegetables(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.CABBAGE.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.CABBAGE_LEAF.get(), 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.RICE_PANICLE.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.RICE.get(), 1)
				.addResult(ModItems.STRAW.get())
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.MELON), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.MELON_SLICE, 9)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.PUMPKIN), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.PUMPKIN_SLICE.get(), 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.BROWN_MUSHROOM_COLONY.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.BROWN_MUSHROOM, 5)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.RED_MUSHROOM_COLONY.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.RED_MUSHROOM, 5)
				.build(consumer);
	}

	private static void cuttingPastries(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CAKE), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.CAKE_SLICE.get(), 7)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.APPLE_PIE.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.APPLE_PIE_SLICE.get(), 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.SWEET_BERRY_CHEESECAKE.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.CHOCOLATE_PIE.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.CHOCOLATE_PIE_SLICE.get(), 4)
				.build(consumer);
	}

	private static void cuttingFlowers(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WITHER_ROSE), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.BLACK_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CORNFLOWER), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.BLUE_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BLUE_ORCHID), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.LIGHT_BLUE_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.AZURE_BLUET), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.OXEYE_DAISY), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WHITE_TULIP), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ALLIUM), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.MAGENTA_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ORANGE_TULIP), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.ORANGE_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.PINK_TULIP), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.PINK_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.RED_TULIP), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.RED_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.POPPY), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.RED_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LILY_OF_THE_VALLEY), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.WHITE_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.DANDELION), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.YELLOW_DYE, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_BEETROOTS.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.BEETROOT_SEEDS, 1)
				.addResult(Items.RED_DYE)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_CABBAGES.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.CABBAGE_SEEDS.get(), 1)
				.addResultWithChance(Items.YELLOW_DYE, 0.5F, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_CARROTS.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.CARROT, 1)
				.addResultWithChance(Items.LIGHT_GRAY_DYE, 0.5F, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_ONIONS.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.ONION.get(), 1)
				.addResult(Items.MAGENTA_DYE, 2)
				.addResultWithChance(Items.LIME_DYE, 0.1F)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_POTATOES.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), Items.POTATO, 1)
				.addResultWithChance(Items.PURPLE_DYE, 0.5F, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_RICE.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.RICE.get(), 1)
				.addResultWithChance(ModItems.STRAW.get(), 0.5F)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.WILD_TOMATOES.get()), Ingredient.fromTag(ForgeTags.TOOLS_KNIVES), ModItems.TOMATO_SEEDS.get(), 1)
				.addResultWithChance(ModItems.TOMATO.get(), 0.2F)
				.addResultWithChance(Items.GREEN_DYE, 0.1F)
				.build(consumer);
	}

	private static void salvagingBricks(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BRICKS), new ToolIngredient(ToolType.PICKAXE), Items.BRICK, 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.NETHER_BRICKS), new ToolIngredient(ToolType.PICKAXE), Items.NETHER_BRICK, 4)
				.build(consumer);
	}

	private static void strippingWood(Consumer<IFinishedRecipe> consumer) {
		stripLogForBark(consumer, Items.OAK_LOG, Items.STRIPPED_OAK_LOG);
		stripLogForBark(consumer, Items.OAK_WOOD, Items.STRIPPED_OAK_WOOD);
		stripLogForBark(consumer, Items.BIRCH_LOG, Items.STRIPPED_BIRCH_LOG);
		stripLogForBark(consumer, Items.BIRCH_WOOD, Items.STRIPPED_BIRCH_WOOD);
		stripLogForBark(consumer, Items.SPRUCE_LOG, Items.STRIPPED_SPRUCE_LOG);
		stripLogForBark(consumer, Items.SPRUCE_WOOD, Items.STRIPPED_SPRUCE_WOOD);
		stripLogForBark(consumer, Items.JUNGLE_LOG, Items.STRIPPED_JUNGLE_LOG);
		stripLogForBark(consumer, Items.JUNGLE_WOOD, Items.STRIPPED_JUNGLE_WOOD);
		stripLogForBark(consumer, Items.ACACIA_LOG, Items.STRIPPED_ACACIA_LOG);
		stripLogForBark(consumer, Items.ACACIA_WOOD, Items.STRIPPED_ACACIA_WOOD);
		stripLogForBark(consumer, Items.DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_LOG);
		stripLogForBark(consumer, Items.DARK_OAK_WOOD, Items.STRIPPED_DARK_OAK_WOOD);
		stripLogForBark(consumer, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM);
		stripLogForBark(consumer, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE);
		stripLogForBark(consumer, Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM);
		stripLogForBark(consumer, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE);
	}

	private static void salvagingWoodenFurniture(Consumer<IFinishedRecipe> consumer) {
		salvagePlankFromFurniture(consumer, Items.OAK_PLANKS, Items.OAK_DOOR, Items.OAK_TRAPDOOR, Items.OAK_SIGN);
		salvagePlankFromFurniture(consumer, Items.BIRCH_PLANKS, Items.BIRCH_DOOR, Items.BIRCH_TRAPDOOR, Items.BIRCH_SIGN);
		salvagePlankFromFurniture(consumer, Items.SPRUCE_PLANKS, Items.SPRUCE_DOOR, Items.SPRUCE_TRAPDOOR, Items.SPRUCE_SIGN);
		salvagePlankFromFurniture(consumer, Items.JUNGLE_PLANKS, Items.JUNGLE_DOOR, Items.JUNGLE_TRAPDOOR, Items.JUNGLE_SIGN);
		salvagePlankFromFurniture(consumer, Items.ACACIA_PLANKS, Items.ACACIA_DOOR, Items.ACACIA_TRAPDOOR, Items.ACACIA_SIGN);
		salvagePlankFromFurniture(consumer, Items.DARK_OAK_PLANKS, Items.DARK_OAK_DOOR, Items.DARK_OAK_TRAPDOOR, Items.DARK_OAK_SIGN);
		salvagePlankFromFurniture(consumer, Items.CRIMSON_PLANKS, Items.CRIMSON_DOOR, Items.CRIMSON_TRAPDOOR, Items.CRIMSON_SIGN);
		salvagePlankFromFurniture(consumer, Items.WARPED_PLANKS, Items.WARPED_DOOR, Items.WARPED_TRAPDOOR, Items.WARPED_SIGN);
	}

	private static void diggingSediments(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CLAY), new ToolIngredient(ToolType.SHOVEL), Items.CLAY_BALL, 4)
				.build(consumer);
	}

	private static void salvagingUsingShears(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SADDLE), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 2)
				.addResultWithChance(Items.IRON_NUGGET, 0.5F, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_HORSE_ARMOR), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_HELMET), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_CHESTPLATE), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_LEGGINGS), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_BOOTS), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
	}


	/**
	 * Generates an axe-cutting recipe for each furniture, resulting in one plank of the given type.
	 */
	private static void salvagePlankFromFurniture(Consumer<IFinishedRecipe> consumer, IItemProvider plank, IItemProvider door, IItemProvider trapdoor, IItemProvider sign) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(door), new ToolIngredient(ToolType.AXE), plank).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(trapdoor), new ToolIngredient(ToolType.AXE), plank).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(sign), new ToolIngredient(ToolType.AXE), plank).build(consumer);
	}

	/**
	 * Generates an axe-stripping recipe for the pair of given logs, with custom sound and a Tree Bark result attached.
	 */
	private static void stripLogForBark(Consumer<IFinishedRecipe> consumer, IItemProvider log, IItemProvider strippedLog) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(log), new ToolIngredient(ToolType.AXE), strippedLog)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
	}
}
