package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.Consumer;

public class CuttingRecipes
{
	public static Ingredient KNIVES = matchesTool(KnifeItem.KNIFE_DIG, CommonTags.Items.TOOLS_KNIVES);
	public static Ingredient PICKAXES = matchesTool(ToolActions.PICKAXE_DIG, ItemTags.PICKAXES);
	public static Ingredient AXES = matchesTool(ToolActions.AXE_DIG, ItemTags.AXES);
	public static Ingredient AXES_STRIP = matchesTool(ToolActions.AXE_STRIP, ItemTags.AXES);
	public static Ingredient SHOVELS = matchesTool(ToolActions.SHOVEL_DIG, ItemTags.SHOVELS);
	public static Ingredient SHEARS = matchesTool(ToolActions.SHEARS_DIG, Tags.Items.SHEARS);

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
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BEEF), KNIVES, ModItems.MINCED_BEEF.get(), 2)
				.setNamespace(FarmersDelight.MODID)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PORKCHOP), KNIVES, ModItems.BACON.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CHICKEN), KNIVES, ModItems.CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_CHICKEN), KNIVES, ModItems.COOKED_CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COD), KNIVES, ModItems.COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_COD), KNIVES, ModItems.COOKED_COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON), KNIVES, ModItems.SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_SALMON), KNIVES, ModItems.COOKED_SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.HAM.get()), KNIVES, Items.PORKCHOP, 2)
				.addResult(Items.BONE)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SMOKED_HAM.get()), KNIVES, Items.COOKED_PORKCHOP, 2)
				.addResult(Items.BONE)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MUTTON), KNIVES, ModItems.MUTTON_CHOPS.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_MUTTON), KNIVES, ModItems.COOKED_MUTTON_CHOPS.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.INK_SAC), KNIVES, Items.BLACK_DYE, 2)
				.saveToFD(consumer);
	}

	private static void cuttingVegetables(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CABBAGE.get()), KNIVES, ModItems.CABBAGE_LEAF.get(), 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RICE_PANICLE.get()), KNIVES, ModItems.RICE.get(), 1)
				.addResult(ModItems.STRAW.get())
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MELON), KNIVES, Items.MELON_SLICE, 9)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN), KNIVES, ModItems.PUMPKIN_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BROWN_MUSHROOM_COLONY.get()), KNIVES, Items.BROWN_MUSHROOM, 5)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RED_MUSHROOM_COLONY.get()), KNIVES, Items.RED_MUSHROOM, 5)
				.saveToFD(consumer);
	}

	private static void cuttingFoods(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(CommonTags.Items.DOUGH), KNIVES, ModItems.RAW_PASTA.get(), 1)
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "cutting/tag_dough"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.KELP_ROLL.get()), KNIVES, ModItems.KELP_ROLL_SLICE.get(), 3)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CAKE), KNIVES, ModItems.CAKE_SLICE.get(), 7)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.APPLE_PIE.get()), KNIVES, ModItems.APPLE_PIE_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SWEET_BERRY_CHEESECAKE.get()), KNIVES, ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CHOCOLATE_PIE.get()), KNIVES, ModItems.CHOCOLATE_PIE_SLICE.get(), 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN_PIE), KNIVES, ModItems.PUMPKIN_PIE_SLICE.get(), 4)
				.saveToFD(consumer);
	}

	private static void cuttingFlowers(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WITHER_ROSE), KNIVES, Items.BLACK_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CORNFLOWER), KNIVES, Items.BLUE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLUE_ORCHID), KNIVES, Items.LIGHT_BLUE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AZURE_BLUET), KNIVES, Items.LIGHT_GRAY_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.OXEYE_DAISY), KNIVES, Items.LIGHT_GRAY_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WHITE_TULIP), KNIVES, Items.LIGHT_GRAY_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ALLIUM), KNIVES, Items.MAGENTA_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ORANGE_TULIP), KNIVES, Items.ORANGE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PINK_TULIP), KNIVES, Items.PINK_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.RED_TULIP), KNIVES, Items.RED_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.POPPY), KNIVES, Items.RED_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LILY_OF_THE_VALLEY), KNIVES, Items.WHITE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DANDELION), KNIVES, Items.YELLOW_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.TORCHFLOWER), KNIVES, Items.ORANGE_DYE, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_BEETROOTS.get()), KNIVES, Items.BEETROOT_SEEDS, 1)
				.addResult(Items.RED_DYE)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CABBAGES.get()), KNIVES, ModItems.CABBAGE_SEEDS.get(), 1)
				.addResultWithChance(Items.YELLOW_DYE, 0.5F, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CARROTS.get()), KNIVES, Items.CARROT, 1)
				.addResultWithChance(Items.LIGHT_GRAY_DYE, 0.5F, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_ONIONS.get()), KNIVES, ModItems.ONION.get(), 1)
				.addResult(Items.MAGENTA_DYE, 2)
				.addResultWithChance(Items.LIME_DYE, 0.1F)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_POTATOES.get()), KNIVES, Items.POTATO, 1)
				.addResultWithChance(Items.PURPLE_DYE, 0.5F, 2)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_RICE.get()), KNIVES, ModItems.RICE.get(), 1)
				.addResultWithChance(ModItems.STRAW.get(), 0.5F)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_TOMATOES.get()), KNIVES, ModItems.TOMATO_SEEDS.get(), 1)
				.addResultWithChance(ModItems.TOMATO.get(), 0.2F)
				.addResultWithChance(Items.GREEN_DYE, 0.1F)
				.saveToFD(consumer);
	}

	private static void salvagingMinerals(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BRICKS), PICKAXES, Items.BRICK, 4)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.NETHER_BRICKS), PICKAXES, Items.NETHER_BRICK, 4)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.STONE), PICKAXES, Items.COBBLESTONE, 1)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DEEPSLATE), PICKAXES, Items.COBBLED_DEEPSLATE, 1)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.QUARTZ_BLOCK), PICKAXES, Items.QUARTZ, 4)
				.salvaging()
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AMETHYST_BLOCK), PICKAXES, Items.AMETHYST_SHARD, 4)
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
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BAMBOO_BLOCK), AXES_STRIP, Items.STRIPPED_BAMBOO_BLOCK)
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
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WOODEN_BASKET.get()), AXES, ModItems.CANVAS.get())
				.addResult(Items.STICK)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BAMBOO_BASKET.get()), AXES, ModItems.CANVAS.get())
				.addResult(Items.BAMBOO)
				.saveToFD(consumer);
	}

	private static void diggingSediments(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CLAY), SHOVELS, Items.CLAY_BALL, 4)
				.saveToFD(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.GRAVEL), SHOVELS, Items.GRAVEL, 1)
				.addResultWithChance(Items.FLINT, 0.1F)
				.saveToFD(consumer);
	}

	private static void salvagingUsingShears(Consumer<FinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SADDLE), SHEARS, Items.LEATHER, 2)
				.addResultWithChance(Items.IRON_NUGGET, 0.5F, 2)
				.save(consumer, salvagingRecipe("saddle"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_HORSE_ARMOR), SHEARS, Items.LEATHER, 2)
				.save(consumer, salvagingRecipe("leather_horse_armor"));
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS), SHEARS, Items.LEATHER, 1)
				.save(consumer, salvagingRecipe("leather_armor"));
	}

	/**
	 * Generates an axe-cutting recipe for wooded furniture items, with a chance to recover one plank of the given type.
	 */
	private static void salvagePlankFromFurniture(Consumer<FinishedRecipe> consumer, WoodType woodType, ItemLike plank, ItemLike... furniture) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(furniture), AXES, plank, 1, 0.75F)
				.save(consumer, salvagingRecipe(woodType.name() + "_furniture"));
	}

	/**
	 * Generates an axe-stripping recipe for the pair of given logs, with custom sound and a Tree Bark result attached.
	 */
	private static void stripLogForBark(Consumer<FinishedRecipe> consumer, ItemLike log, ItemLike strippedLog) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), AXES_STRIP, strippedLog)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP).toString())
				.saveToFD(consumer);
	}

	private static Ingredient matchesTool(ToolAction toolAction, TagKey<Item> fallbackTag) {
		return CompoundIngredient.of(new ToolActionIngredient(toolAction), Ingredient.of(fallbackTag));
	}

	private static ResourceLocation salvagingRecipe(String name) {
		return new ResourceLocation(FarmersDelight.MODID, "salvaging/" + name);
	}
}
