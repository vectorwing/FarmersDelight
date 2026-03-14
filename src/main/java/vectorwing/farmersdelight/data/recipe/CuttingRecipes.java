package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.Consumer;

public class CuttingRecipes
{
	public static void register(Consumer<FinishedRecipe> consumer) {
		// Knife
		cuttingAnimalItems(consumer);
		cuttingVegetables(consumer);
		cuttingFoods(consumer);
		cuttingFlowers(consumer);

		// Shovel
		diggingSediments(consumer);

		// Pickaxe
		salvagingMinerals(consumer);

		// Axe
		strippingWood(consumer);
		salvagingWoodenFurniture(consumer);

		// Shears
		salvagingUsingShears(consumer);
	}

	private static void cuttingAnimalItems(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BEEF), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.MINCED_BEEF.get(), 2)
				.setNamespace(FarmersDelight.MODID)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PORKCHOP), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.BACON.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CHICKEN), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_CHICKEN), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.COOKED_CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COD), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_COD), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.COOKED_COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_SALMON), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.COOKED_SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.HAM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.PORKCHOP, 2)
				.addResult(Items.BONE)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SMOKED_HAM.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.COOKED_PORKCHOP, 2)
				.addResult(Items.BONE)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MUTTON), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.MUTTON_CHOPS.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_MUTTON), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.COOKED_MUTTON_CHOPS.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.INK_SAC), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.BLACK_DYE, 2)
				.saveToFD(consumer);
	}

	private static void cuttingVegetables(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CABBAGE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.CABBAGE_LEAF.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RICE_PANICLE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.RICE.get(), 1)
				.addResult(ModItems.STRAW.get())
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MELON), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.MELON_SLICE, 9)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.PUMPKIN_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BROWN_MUSHROOM_COLONY.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.BROWN_MUSHROOM, 5)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RED_MUSHROOM_COLONY.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.RED_MUSHROOM, 5)
				.saveToFD(consumer);
	}

	private static void cuttingFoods(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(CommonTags.Items.DOUGH), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.RAW_PASTA.get(), 1)
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cutting/tag_dough"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.KELP_ROLL.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.KELP_ROLL_SLICE.get(), 3)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CAKE), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.CAKE_SLICE.get(), 7)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.APPLE_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.APPLE_PIE_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SWEET_BERRY_CHEESECAKE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CHOCOLATE_PIE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.CHOCOLATE_PIE_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN_PIE), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.PUMPKIN_PIE_SLICE.get(), 4)
				.saveToFD(consumer);
	}

	private static void cuttingFlowers(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WITHER_ROSE), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.BLACK_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CORNFLOWER), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.BLUE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLUE_ORCHID), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.LIGHT_BLUE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AZURE_BLUET), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.OXEYE_DAISY), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WHITE_TULIP), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.LIGHT_GRAY_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ALLIUM), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.MAGENTA_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ORANGE_TULIP), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.ORANGE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PINK_TULIP), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.PINK_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.RED_TULIP), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.RED_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.POPPY), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.RED_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LILY_OF_THE_VALLEY), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.WHITE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DANDELION), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.YELLOW_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.TORCHFLOWER), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.ORANGE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_BEETROOTS.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.BEETROOT_SEEDS, 1)
				.addResult(Items.RED_DYE)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CABBAGES.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.CABBAGE_SEEDS.get(), 1)
				.addResultWithChance(Items.YELLOW_DYE, 0.5F, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CARROTS.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.CARROT, 1)
				.addResultWithChance(Items.LIGHT_GRAY_DYE, 0.5F, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_ONIONS.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.ONION.get(), 1)
				.addResult(Items.MAGENTA_DYE, 2)
				.addResultWithChance(Items.LIME_DYE, 0.1F)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_POTATOES.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), Items.POTATO, 1)
				.addResultWithChance(Items.PURPLE_DYE, 0.5F, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_RICE.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.RICE.get(), 1)
				.addResultWithChance(ModItems.STRAW.get(), 0.5F)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_TOMATOES.get()), Ingredient.of(CommonTags.Items.TOOLS_KNIVES), ModItems.TOMATO_SEEDS.get(), 1)
				.addResultWithChance(ModItems.TOMATO.get(), 0.2F)
				.addResultWithChance(Items.GREEN_DYE, 0.1F)
				.saveToFD(consumer);
	}

	private static void salvagingMinerals(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BRICKS), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.BRICK, 4)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.NETHER_BRICKS), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.NETHER_BRICK, 4)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.STONE), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.COBBLESTONE, 1)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DEEPSLATE), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.COBBLED_DEEPSLATE, 1)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.QUARTZ_BLOCK), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.QUARTZ, 4)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AMETHYST_BLOCK), new ToolActionIngredient(ToolActions.PICKAXE_DIG), Items.AMETHYST_SHARD, 4)
				.salvaging()
				.saveToFD(consumer);
	}

	private static void strippingWood(Consumer<FinishedRecipe> consumer) {
		stripLogForBark(consumer, Items.OAK_LOG, Items.STRIPPED_OAK_LOG);
		stripLogForBark(consumer, Items.OAK_WOOD, Items.STRIPPED_OAK_WOOD);
		stripLogForBark(consumer, Items.SPRUCE_LOG, Items.STRIPPED_SPRUCE_LOG);
		stripLogForBark(consumer, Items.SPRUCE_WOOD, Items.STRIPPED_SPRUCE_WOOD);
		stripLogForBark(consumer, Items.BIRCH_LOG, Items.STRIPPED_BIRCH_LOG);
		stripLogForBark(consumer, Items.BIRCH_WOOD, Items.STRIPPED_BIRCH_WOOD);
		stripLogForBark(consumer, Items.JUNGLE_LOG, Items.STRIPPED_JUNGLE_LOG);
		stripLogForBark(consumer, Items.JUNGLE_WOOD, Items.STRIPPED_JUNGLE_WOOD);
		stripLogForBark(consumer, Items.ACACIA_LOG, Items.STRIPPED_ACACIA_LOG);
		stripLogForBark(consumer, Items.ACACIA_WOOD, Items.STRIPPED_ACACIA_WOOD);
		stripLogForBark(consumer, Items.DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_LOG);
		stripLogForBark(consumer, Items.DARK_OAK_WOOD, Items.STRIPPED_DARK_OAK_WOOD);
		stripLogForBark(consumer, Items.MANGROVE_LOG, Items.STRIPPED_MANGROVE_LOG);
		stripLogForBark(consumer, Items.MANGROVE_WOOD, Items.STRIPPED_MANGROVE_WOOD);
		stripLogForBark(consumer, Items.CHERRY_LOG, Items.STRIPPED_CHERRY_LOG);
		stripLogForBark(consumer, Items.CHERRY_WOOD, Items.STRIPPED_CHERRY_WOOD);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BAMBOO_BLOCK), new ToolActionIngredient(ToolActions.AXE_STRIP), Items.STRIPPED_BAMBOO_BLOCK)
				.addResult(ModItems.STRAW.get())
				.addSound(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP).toString()).saveToFD(consumer);
		stripLogForBark(consumer, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM);
		stripLogForBark(consumer, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE);
		stripLogForBark(consumer, Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM);
		stripLogForBark(consumer, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE);
	}

	private static void salvagingWoodenFurniture(Consumer<FinishedRecipe> consumer) {
		salvagePlankFromFurniture(consumer,WoodType.OAK,
				Items.OAK_PLANKS, Items.OAK_DOOR, Items.OAK_TRAPDOOR, Items.OAK_SIGN, Items.OAK_HANGING_SIGN, Items.OAK_FENCE, Items.OAK_FENCE_GATE,
				Items.OAK_PRESSURE_PLATE, Items.OAK_BUTTON, Items.OAK_BOAT, ModItems.OAK_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.SPRUCE,
				Items.SPRUCE_PLANKS, Items.SPRUCE_DOOR, Items.SPRUCE_TRAPDOOR, Items.SPRUCE_SIGN, Items.SPRUCE_HANGING_SIGN, Items.SPRUCE_FENCE, Items.SPRUCE_FENCE_GATE,
				Items.SPRUCE_PRESSURE_PLATE, Items.SPRUCE_BUTTON, Items.SPRUCE_BOAT, ModItems.SPRUCE_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.BIRCH,
				Items.BIRCH_PLANKS, Items.BIRCH_DOOR, Items.BIRCH_TRAPDOOR, Items.BIRCH_SIGN, Items.BIRCH_HANGING_SIGN, Items.BIRCH_FENCE, Items.BIRCH_FENCE_GATE,
				Items.BIRCH_PRESSURE_PLATE, Items.BIRCH_BUTTON, Items.BIRCH_BOAT, ModItems.BIRCH_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.JUNGLE,
				Items.JUNGLE_PLANKS, Items.JUNGLE_DOOR, Items.JUNGLE_TRAPDOOR, Items.JUNGLE_SIGN, Items.JUNGLE_HANGING_SIGN, Items.JUNGLE_FENCE, Items.JUNGLE_FENCE_GATE,
				Items.JUNGLE_PRESSURE_PLATE, Items.JUNGLE_BUTTON, Items.JUNGLE_BOAT, ModItems.JUNGLE_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.ACACIA,
				Items.ACACIA_PLANKS, Items.ACACIA_DOOR, Items.ACACIA_TRAPDOOR, Items.ACACIA_SIGN, Items.ACACIA_HANGING_SIGN, Items.ACACIA_FENCE, Items.ACACIA_FENCE_GATE,
				Items.ACACIA_PRESSURE_PLATE, Items.ACACIA_BUTTON, Items.ACACIA_BOAT, ModItems.ACACIA_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.DARK_OAK,
				Items.DARK_OAK_PLANKS, Items.DARK_OAK_DOOR, Items.DARK_OAK_TRAPDOOR, Items.DARK_OAK_SIGN, Items.DARK_OAK_HANGING_SIGN, Items.DARK_OAK_FENCE, Items.DARK_OAK_FENCE_GATE,
				Items.DARK_OAK_PRESSURE_PLATE, Items.DARK_OAK_BUTTON, Items.DARK_OAK_BOAT, ModItems.DARK_OAK_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.MANGROVE,
				Items.MANGROVE_PLANKS, Items.MANGROVE_DOOR, Items.MANGROVE_TRAPDOOR, Items.MANGROVE_SIGN, Items.MANGROVE_HANGING_SIGN, Items.MANGROVE_FENCE, Items.MANGROVE_FENCE_GATE,
				Items.MANGROVE_PRESSURE_PLATE, Items.MANGROVE_BUTTON, Items.MANGROVE_BOAT, ModItems.MANGROVE_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.CHERRY,
				Items.CHERRY_PLANKS, Items.CHERRY_DOOR, Items.CHERRY_TRAPDOOR, Items.CHERRY_SIGN, Items.CHERRY_HANGING_SIGN, Items.CHERRY_FENCE, Items.CHERRY_FENCE_GATE,
				Items.CHERRY_PRESSURE_PLATE, Items.CHERRY_BUTTON, Items.CHERRY_BOAT, ModItems.CHERRY_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.BAMBOO,
				Items.BAMBOO_PLANKS, Items.BAMBOO_DOOR, Items.BAMBOO_TRAPDOOR, Items.BAMBOO_SIGN, Items.BAMBOO_HANGING_SIGN, Items.BAMBOO_FENCE, Items.BAMBOO_FENCE_GATE,
				Items.BAMBOO_PRESSURE_PLATE, Items.BAMBOO_BUTTON, Items.BAMBOO_RAFT, ModItems.BAMBOO_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.CRIMSON,
				Items.CRIMSON_PLANKS, Items.CRIMSON_DOOR, Items.CRIMSON_TRAPDOOR, Items.CRIMSON_SIGN, Items.CRIMSON_HANGING_SIGN, Items.CRIMSON_FENCE, Items.CRIMSON_FENCE_GATE,
				Items.CRIMSON_PRESSURE_PLATE, Items.CRIMSON_BUTTON, ModItems.CRIMSON_CABINET.get());
		salvagePlankFromFurniture(consumer, WoodType.WARPED,
				Items.WARPED_PLANKS, Items.WARPED_DOOR, Items.WARPED_TRAPDOOR, Items.WARPED_SIGN, Items.WARPED_HANGING_SIGN, Items.WARPED_FENCE, Items.WARPED_FENCE_GATE,
				Items.WARPED_PRESSURE_PLATE, Items.WARPED_BUTTON, ModItems.WARPED_CABINET.get());
	}

	private static void diggingSediments(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CLAY), new ToolActionIngredient(ToolActions.SHOVEL_DIG), Items.CLAY_BALL, 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.GRAVEL), new ToolActionIngredient(ToolActions.SHOVEL_DIG), Items.GRAVEL, 1)
				.addResultWithChance(Items.FLINT, 0.1F)
				.saveToFD(consumer);
	}

	private static void salvagingUsingShears(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SADDLE), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 2)
				.addResultWithChance(Items.IRON_NUGGET, 0.5F, 2)
				.save(consumer, salvagingRecipe("saddle"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_HORSE_ARMOR), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 2)
				.save(consumer, salvagingRecipe("leather_horse_armor"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS), Ingredient.of(Tags.Items.SHEARS), Items.LEATHER, 1)
				.save(consumer, salvagingRecipe("leather_armor"));
	}

	/**
	 * Generates an axe-cutting recipe for wooded furniture items, with a chance to recover one plank of the given type.
	 */
	private static void salvagePlankFromFurniture(Consumer<FinishedRecipe> consumer, WoodType woodType, ItemLike plank, ItemLike... furniture) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(furniture), new ToolActionIngredient(ToolActions.AXE_DIG), plank, 1, 0.75F)
				.save(consumer, salvagingRecipe(woodType.name() + "_furniture"));
	}

	/**
	 * Generates an axe-stripping recipe for the pair of given logs, with custom sound and a Tree Bark result attached.
	 */
	private static void stripLogForBark(Consumer<FinishedRecipe> consumer, ItemLike log, ItemLike strippedLog) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), new ToolActionIngredient(ToolActions.AXE_STRIP), strippedLog)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP).toString())
				.saveToFD(consumer);
	}

	private static ResourceLocation salvagingRecipe(String name) {
		return new ResourceLocation(FarmersDelight.MODID, "salvaging/" + name);
	}
}
