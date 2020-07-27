package vectorwing.farmersdelight.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Configuration
{
	public static final String CATEGORY_WORLD = "world";

	public static ForgeConfigSpec COMMON_CONFIG;

	public static ForgeConfigSpec.BooleanValue CROPS_ON_SHIPWRECKS;
	public static ForgeConfigSpec.BooleanValue CROPS_ON_VILLAGE_HOUSES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_CABBAGES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_ONIONS;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_TOMATOES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_CARROTS;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_POTATOES;
	public static ForgeConfigSpec.BooleanValue GENERATE_WILD_BEETROOTS;

	static
	{
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

		COMMON_BUILDER.comment("World settings").push(CATEGORY_WORLD);
		CROPS_ON_SHIPWRECKS = COMMON_BUILDER.comment("Generate crop items on a Shipwreck's supply chests").define("cropsOnShipwreckSupplyLoot", true);
		CROPS_ON_VILLAGE_HOUSES = COMMON_BUILDER.comment("Generate crop items on Village houses (all biomes)").define("cropsOnVillageHouseLoot", true);
		GENERATE_WILD_CABBAGES = COMMON_BUILDER.comment("Generate wild cabbages on beaches").define("genWildCabbages", true);
		GENERATE_WILD_BEETROOTS = COMMON_BUILDER.comment("Generate sea beets on beaches").define("genWildBeetroots", true);
		GENERATE_WILD_POTATOES = COMMON_BUILDER.comment("Generate wild potatoes on cold biomes (temperature 0.3 or lower)").define("genWildPotatoes", true);
		GENERATE_WILD_CARROTS = COMMON_BUILDER.comment("Generate wild carrots on temperate biomes (temperature between 0.3 and 1.0)").define("genWildCarrots", true);
		GENERATE_WILD_ONIONS = COMMON_BUILDER.comment("Generate wild onions on temperate biomes (temperature between 0.3 and 1.0)").define("genWildOnions", true);
		GENERATE_WILD_TOMATOES = COMMON_BUILDER.comment("Generate tomato vines on arid biomes (temperature 1.0 or higher)").define("genWildTomatoes", true);
		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) { }

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) { }

}
