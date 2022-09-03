package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class Configuration
{
	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec CLIENT_CONFIG;

	// COMMON
	public static final String CATEGORY_SETTINGS = "settings";
	public static ForgeConfigSpec.BooleanValue ENABLE_VANILLA_CROP_CRATES;
	public static ForgeConfigSpec.BooleanValue FARMERS_BUY_FD_CROPS;
	public static ForgeConfigSpec.BooleanValue WANDERING_TRADER_SELLS_FD_ITEMS;
	public static ForgeConfigSpec.DoubleValue RICH_SOIL_BOOST_CHANCE;
	public static ForgeConfigSpec.DoubleValue CUTTING_BOARD_FORTUNE_BONUS;
	public static ForgeConfigSpec.BooleanValue ENABLE_ROPE_REELING;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> CANVAS_SIGN_DARK_BACKGROUND_LIST;

	public static final String CATEGORY_RECIPE_BOOK = "recipe_book";
	public static ForgeConfigSpec.BooleanValue ENABLE_RECIPE_BOOK_COOKING_POT;

	public static final String CATEGORY_OVERRIDES = "overrides";
	public static ForgeConfigSpec.BooleanValue VANILLA_SOUP_EXTRA_EFFECTS;
	public static ForgeConfigSpec.BooleanValue RABBIT_STEW_JUMP_BOOST;
	public static ForgeConfigSpec.BooleanValue DISPENSER_TOOLS_CUTTING_BOARD;

	public static final String CATEGORY_OVERRIDES_STACK_SIZE = "stack_size";
	public static ForgeConfigSpec.BooleanValue ENABLE_STACKABLE_SOUP_ITEMS;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> SOUP_ITEM_LIST;
	public static ForgeConfigSpec.BooleanValue OVERRIDE_ALL_SOUP_ITEMS;

	public static final String CATEGORY_WORLD = "world";
	public static ForgeConfigSpec.BooleanValue GENERATE_FD_CHEST_LOOT;
	public static ForgeConfigSpec.BooleanValue GENERATE_VILLAGE_COMPOST_HEAPS;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_CABBAGES;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_CABBAGES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_BEETROOTS;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_BEETROOTS;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_POTATOES;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_POTATOES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_ONIONS;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_ONIONS;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_CARROTS;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_CARROTS;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_TOMATOES;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_TOMATOES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_RICE;
	public static ForgeConfigSpec.IntValue CHANCE_WILD_RICE;

	// CLIENT
	public static final String CATEGORY_CLIENT = "client";

	public static ForgeConfigSpec.BooleanValue NOURISHED_HUNGER_OVERLAY;
	public static ForgeConfigSpec.BooleanValue COMFORT_HEALTH_OVERLAY;
	public static ForgeConfigSpec.BooleanValue FOOD_EFFECT_TOOLTIP;

	static {
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

		COMMON_BUILDER.comment("Game settings").push(CATEGORY_SETTINGS);
		ENABLE_VANILLA_CROP_CRATES = COMMON_BUILDER.comment("Farmer's Delight adds crates (3x3) for vanilla crops, similar to Quark and Thermal Cultivation. Should they be craftable?")
				.define("enableVanillaCropCrates", true);
		FARMERS_BUY_FD_CROPS = COMMON_BUILDER.comment("Should Novice and Apprentice Farmers buy this mod's crops? (May reduce chances of other trades appearing)")
				.define("farmersBuyFDCrops", true);
		WANDERING_TRADER_SELLS_FD_ITEMS = COMMON_BUILDER.comment("Should the Wandering Trader sell some of this mod's items? (Currently includes crop seeds and onions)")
				.define("wanderingTraderSellsFDItems", true);
		RICH_SOIL_BOOST_CHANCE = COMMON_BUILDER.comment("How often (in percentage) should Rich Soil succeed in boosting a plant's growth at each random tick? Set it to 0.0 to disable this.")
				.defineInRange("richSoilBoostChance", 0.2, 0.0, 1.0);
		CUTTING_BOARD_FORTUNE_BONUS = COMMON_BUILDER.comment("How much of a bonus (in percentage) should each level of Fortune grant to Cutting Board chances? Set it to 0.0 to disable this.")
				.defineInRange("cuttingBoardFortuneBonus", 0.1, 0.0, 1.0);
		ENABLE_ROPE_REELING = COMMON_BUILDER.comment("Should players be able to reel back rope, bottom to top, when sneak-using with an empty hand on them?")
				.define("enableRopeReeling", true);
		CANVAS_SIGN_DARK_BACKGROUND_LIST = COMMON_BUILDER.comment("A list of dye colors that, when used as the background of a Canvas Sign, should default to white text when placed.",
						"Dyes: [\"white\", \"orange\", \"magenta\", \"light_blue\", \"yellow\", \"lime\", \"pink\", \"gray\", \"light_gray\", \"cyan\", \"purple\", \"blue\", \"brown\", \"green\", \"red\", \"black\"]")
				.defineList("canvasSignDarkBackgroundList", ImmutableList.of("gray", "purple", "blue", "brown", "green", "red", "black"), obj -> true);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Recipe book").push(CATEGORY_RECIPE_BOOK);
		ENABLE_RECIPE_BOOK_COOKING_POT = COMMON_BUILDER.comment("Should the Cooking Pot have a Recipe Book available on its interface?")
				.define("enableRecipeBookCookingPot", true);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Vanilla item overrides").push(CATEGORY_OVERRIDES);
		VANILLA_SOUP_EXTRA_EFFECTS = COMMON_BUILDER.comment("Should soups and stews from vanilla Minecraft grant additional effects, like meals from this mod?")
				.define("vanillaSoupExtraEffects", true);
		RABBIT_STEW_JUMP_BOOST = COMMON_BUILDER.comment("Should Rabbit Stew grant users the jumping prowess of a rabbit when eaten?")
				.define("rabbitStewJumpBoost", true);
		DISPENSER_TOOLS_CUTTING_BOARD = COMMON_BUILDER.comment("Should most vanilla tools register a dispenser behavior when facing a Cutting Board?")
				.define("dispenserUsesToolsOnCuttingBoard", true);

		COMMON_BUILDER.comment("Stack size overrides").push(CATEGORY_OVERRIDES_STACK_SIZE);
		ENABLE_STACKABLE_SOUP_ITEMS = COMMON_BUILDER.comment("Should SoupItems in the following list become stackable to 16, much like Farmer's Delight's meals?")
				.define("enableStackableSoupItems", true);
		SOUP_ITEM_LIST = COMMON_BUILDER.comment("List of SoupItems. Default: vanilla soups and stews.")
				.defineList("soupItemList", ImmutableList.of("minecraft:mushroom_stew", "minecraft:beetroot_soup", "minecraft:rabbit_stew"), obj -> true);
		OVERRIDE_ALL_SOUP_ITEMS = COMMON_BUILDER.comment("Toggle this setting to instead make ALL SoupItems stackable, except the ones on the list (deny-list). This affects items from other mods, so be careful!")
				.define("overrideAllSoupItems", false);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("World generation").push(CATEGORY_WORLD);
		GENERATE_FD_CHEST_LOOT = COMMON_BUILDER.comment("Should this mod add some of its items (ropes, seeds, knives, meals etc.) as extra chest loot across Minecraft?")
				.define("generateFDChestLoot", true);
		GENERATE_VILLAGE_COMPOST_HEAPS = COMMON_BUILDER.comment("Generate Compost Heaps across all village biomes")
				.define("genVillageCompostHeaps", true);

		COMMON_BUILDER.comment("Wild Cabbage generation").push("wild_cabbages");
		CHANCE_WILD_CABBAGES = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 30, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Sea Beet generation").push("wild_beetroots");
		CHANCE_WILD_BEETROOTS = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 30, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Wild Potato generation").push("wild_potatoes");
		CHANCE_WILD_POTATOES = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 100, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Wild Carrot generation").push("wild_carrots");
		CHANCE_WILD_CARROTS = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 120, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Wild Onion generation").push("wild_onions");
		CHANCE_WILD_ONIONS = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 120, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Tomato Vines generation").push("wild_tomatoes");
		CHANCE_WILD_TOMATOES = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 100, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Wild Rice generation").push("wild_rice");
		CHANCE_WILD_RICE = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.")
				.defineInRange("chance", 20, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();

		ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

		CLIENT_BUILDER.comment("Client settings").push(CATEGORY_CLIENT);
		NOURISHED_HUNGER_OVERLAY = CLIENT_BUILDER.comment("Should the hunger bar have a gilded overlay when the player has the Nourishment effect?")
				.define("nourishmentHungerOverlay", true);
		COMFORT_HEALTH_OVERLAY = CLIENT_BUILDER.comment("Should the health bar have a silver sheen when the player has the Comfort effect?")
				.define("comfortHealthOverlay", true);
		FOOD_EFFECT_TOOLTIP = CLIENT_BUILDER.comment("Should meal and drink tooltips display which effects they provide?")
				.define("foodEffectTooltip", true);
		CLIENT_BUILDER.pop();

		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
}
