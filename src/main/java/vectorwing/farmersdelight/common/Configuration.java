package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableList;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Supplier;

public class Configuration
{
	public static ModConfigSpec COMMON_CONFIG;
	public static ModConfigSpec CLIENT_CONFIG;

	// COMMON
	public static final String CATEGORY_SETTINGS = "settings";
	public static Supplier<Boolean> ENABLE_FARMERS_BUY_FD_CROPS;
	public static Supplier<Boolean> ENABLE_WANDERING_TRADER_SELLS_FD_ITEMS;
	public static Supplier<Boolean> ENABLE_ROPE_REELING;
	public static Supplier<List<? extends String>> CANVAS_SIGN_DARK_BACKGROUND_LIST;

	public static final String CATEGORY_FARMING = "farming";
	public static Supplier<Double> RICH_SOIL_BOOST_CHANCE;
	public static Supplier<Boolean> ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES;
	public static Supplier<String> DEFAULT_TOMATO_VINE_ROPE;

	public static final String CATEGORY_CRAFTING = "crafting";
	public static Supplier<Boolean> ENABLE_COOKING_POT_RECIPE_BOOK;
	public static Supplier<Boolean> ENABLE_VANILLA_CROP_CRATES;
	public static Supplier<Double> CUTTING_BOARD_FORTUNE_BONUS;

	public static final String CATEGORY_OVERRIDES = "overrides";
	public static Supplier<Boolean> ENABLE_VANILLA_SOUP_EXTRA_EFFECTS;
	public static Supplier<Boolean> ENABLE_RABBIT_STEW_BUFF;
	public static Supplier<Boolean> ENABLE_PUMPKIN_PIE_SNEAK_TO_PLACE;
	public static Supplier<Boolean> ENABLE_DISPENSER_TOOLS_CUTTING_BOARD;

	public static final String CATEGORY_OVERRIDES_STACK_SIZE = "stack_size";
	public static Supplier<Boolean> ENABLE_STACKABLE_SOUP_ITEMS;
	public static Supplier<List<? extends String>> SOUP_ITEM_LIST;

	public static final String CATEGORY_WORLD = "world";
	public static Supplier<Boolean> GENERATE_FD_CHEST_LOOT;
	public static Supplier<Boolean> GENERATE_VILLAGE_COMPOST_HEAPS;
	public static Supplier<Boolean> GENERATE_VILLAGE_FARM_FD_CROPS;

	public static final String CATEGORY_DEBUG = "debug";
	public static Supplier<Boolean> ENABLE_TOMATO_ROPE_PERMANENCE;

	// CLIENT
	public static Supplier<Boolean> ENABLE_NOURISHMENT_HUNGER_OVERLAY;
	public static Supplier<Boolean> ENABLE_COMFORT_HEALTH_OVERLAY;
	public static Supplier<Boolean> ENABLE_FOOD_EFFECT_TOOLTIP;

	static {
		ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

		COMMON_BUILDER.push(CATEGORY_SETTINGS);
		ENABLE_FARMERS_BUY_FD_CROPS = COMMON_BUILDER.comment("If enabled, Novice and Apprentice Farmer villagers will have a chance to buy crops from this mod.")
			.define("enableFarmerFDTrades", true);
		ENABLE_WANDERING_TRADER_SELLS_FD_ITEMS = COMMON_BUILDER.comment("If enabled, the Wandering Trader will have a chance to sell seeds and plantables from this mod.")
			.define("enableWanderingTraderFDTrades", true);
		ENABLE_ROPE_REELING = COMMON_BUILDER.comment("If enabled, players will be able to reel back rope, bottom to top, when sneak-using with an empty hand on them.")
			.define("enableRopeReeling", true);
		CANVAS_SIGN_DARK_BACKGROUND_LIST = COMMON_BUILDER.comment("A list of dye colors that, when used as the background color of a Canvas Sign, should default to white text when placed.",
				"Dyes: [\"white\", \"orange\", \"magenta\", \"light_blue\", \"yellow\", \"lime\", \"pink\", \"gray\", \"light_gray\", \"cyan\", \"purple\", \"blue\", \"brown\", \"green\", \"red\", \"black\"]")
			.defineList("canvasSignDarkBackgroundList", ImmutableList.of("gray", "purple", "blue", "brown", "green", "red", "black"), () -> "", obj -> true);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.push(CATEGORY_FARMING);
		RICH_SOIL_BOOST_CHANCE = COMMON_BUILDER.comment("The chance (in decimal percentage) of Rich Soil providing a bone meal boost to a crop planted on it. Set to 0.0 to disable.")
			.defineInRange("richSoilBoostChance", 0.2, 0.0, 1.0);
		ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES = COMMON_BUILDER.comment("If enabled, tomato crops will be able to climb any rope tagged as farmersdelight:ropes. If disabled, this will be restricted to this mod's rope only.")
			.comment("Beware: this will convert these blocks into the block specified in defaultTomatoVineRope.")
			.define("enableTomatoVineClimbingTaggedRopes", true);
		DEFAULT_TOMATO_VINE_ROPE = COMMON_BUILDER.comment("Which rope should Tomato Vines leave behind when broken?")
			.define("defaultTomatoVineRope", "farmersdelight:rope");
		COMMON_BUILDER.pop();

		COMMON_BUILDER.push(CATEGORY_CRAFTING);
		ENABLE_COOKING_POT_RECIPE_BOOK = COMMON_BUILDER.comment("If enabled, the Cooking Pot will provide a Recipe Book on its interface, similar to the one on Crafting Tables and Furnaces.")
			.define("enableCookingPotRecipeBook", true);
		ENABLE_VANILLA_CROP_CRATES = COMMON_BUILDER.comment("If enabled, storage crates (3x3) for vanilla crops (similar to Quark and Thermal Cultivation) will become craftable.")
			.define("enableVanillaCropCrates", true);
		CUTTING_BOARD_FORTUNE_BONUS = COMMON_BUILDER.comment("How much (in decimal percentage) should each level of Fortune increase the odds of obtaining rare results on the Cutting Board? Set to 0.0 to disable.")
			.defineInRange("cuttingBoardFortuneBonus", 0.1, 0.0, 1.0);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.push(CATEGORY_OVERRIDES);
		ENABLE_VANILLA_SOUP_EXTRA_EFFECTS = COMMON_BUILDER.comment("If enabled, soups and stews from Minecraft will grant Nourishment when eaten, similar to meals from this mod.")
			.define("enableVanillaSoupExtraEffects", true);
		ENABLE_RABBIT_STEW_BUFF = COMMON_BUILDER.comment("If enabled, Rabbit Stew will be given improved food stats, to match its crafting cost.")
			.define("enableRabbitStewBuff", true);
		ENABLE_PUMPKIN_PIE_SNEAK_TO_PLACE = COMMON_BUILDER.comment("If enabled, Pumpkin Pie will require the user to sneak to place it down as a block.")
			.define("enablePumpkinPieSneakToPlace", false);
		ENABLE_DISPENSER_TOOLS_CUTTING_BOARD = COMMON_BUILDER.comment("If enabled, the Dispenser will be able to operate a Cutting Board in front of itself, using stored items as cutting tools.")
			.define("enableCuttingBoardDispenserBehavior", true);

		COMMON_BUILDER.push(CATEGORY_OVERRIDES_STACK_SIZE);
		ENABLE_STACKABLE_SOUP_ITEMS = COMMON_BUILDER.comment("If enabled, any BowlFoodItem in the following list will become stackable to 16, much like Farmer's Delight's meals.")
			.define("enableStackableSoupItems", true);
		SOUP_ITEM_LIST = COMMON_BUILDER.comment("List of targeted food items. They must extend the BowlFoodItem class in code to be affected. Defaults to vanilla soups and stews.")
			.defineList("soupItemList", ImmutableList.of("minecraft:mushroom_stew", "minecraft:beetroot_soup", "minecraft:rabbit_stew"), () -> "", obj -> true);
		COMMON_BUILDER.pop();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.push(CATEGORY_WORLD);
		GENERATE_FD_CHEST_LOOT = COMMON_BUILDER.comment("If enabled, new loot pools will be added to chest loot across the game, containing items from this mod (ropes, crops, tools etc).")
			.define("generateFDChestLoot", true);
		GENERATE_VILLAGE_COMPOST_HEAPS = COMMON_BUILDER.comment("If enabled, Compost Heaps (structures containing Organic Compost piles) will sometimes generate across Villages.")
			.define("generateVillageCompostHeaps", true);
		GENERATE_VILLAGE_FARM_FD_CROPS = COMMON_BUILDER.comment("If enabled, crops from Farmer's Delight will sometimes replace standard crops in village farm plots.")
			.define("generateFDCropsOnVillageFarms", true);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.push(CATEGORY_DEBUG);
		ENABLE_TOMATO_ROPE_PERMANENCE = COMMON_BUILDER
			.comment("Tomato crops hanging on rope will force-place a Rope block when broken. Due to technical limitations, this affects things such as commands, making them fail to 'setblock' at first.")
			.comment("Disable this if this block is preventing you from editing a region in creative or with commands. For normal gameplay, I recommend leaving this enabled.")
			.define("enableTomatoRopePermanence", true);
		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();

		ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

		ENABLE_NOURISHMENT_HUNGER_OVERLAY = CLIENT_BUILDER.comment("If enabled, a gilded overlay will be shown over the food meter when the player has the Nourishment effect.")
			.define("enableNourishmentHungerOverlay", true);
		ENABLE_COMFORT_HEALTH_OVERLAY = CLIENT_BUILDER.comment("If enabled, a scrolling overlay will be shown over the health meter when the player has the Comfort effect.")
			.define("enableComfortHealthOverlay", true);
		ENABLE_FOOD_EFFECT_TOOLTIP = CLIENT_BUILDER.comment("If enabled, food items will display tooltips showing which effects they grant when eaten, if any. Applies to foods from both Minecraft and this mod.")
			.define("enableFoodEffectTooltip", true);

		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}

	// Backwards compatibility configs.
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue FARMERS_BUY_FD_CROPS = (ModConfigSpec.BooleanValue) ENABLE_FARMERS_BUY_FD_CROPS;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue WANDERING_TRADER_SELLS_FD_ITEMS = (ModConfigSpec.BooleanValue) ENABLE_WANDERING_TRADER_SELLS_FD_ITEMS;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue ENABLE_RECIPE_BOOK_COOKING_POT = (ModConfigSpec.BooleanValue) ENABLE_COOKING_POT_RECIPE_BOOK;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue VANILLA_SOUP_EXTRA_EFFECTS = (ModConfigSpec.BooleanValue) ENABLE_VANILLA_SOUP_EXTRA_EFFECTS;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue RABBIT_STEW_BUFF = (ModConfigSpec.BooleanValue) ENABLE_RABBIT_STEW_BUFF;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue DISPENSER_TOOLS_CUTTING_BOARD = (ModConfigSpec.BooleanValue) ENABLE_DISPENSER_TOOLS_CUTTING_BOARD;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue NOURISHMENT_HUNGER_OVERLAY = (ModConfigSpec.BooleanValue) ENABLE_NOURISHMENT_HUNGER_OVERLAY;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue COMFORT_HEALTH_OVERLAY = (ModConfigSpec.BooleanValue) ENABLE_COMFORT_HEALTH_OVERLAY;
	@Deprecated(forRemoval = true)
	public static ModConfigSpec.BooleanValue FOOD_EFFECT_TOOLTIP = (ModConfigSpec.BooleanValue) ENABLE_FOOD_EFFECT_TOOLTIP;
}
