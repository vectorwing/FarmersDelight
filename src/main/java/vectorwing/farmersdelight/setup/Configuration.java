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

	static
	{
		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

		COMMON_BUILDER.comment("World settings").push(CATEGORY_WORLD);
		CROPS_ON_SHIPWRECKS = COMMON_BUILDER.comment("Generate crop items on a Shipwreck's supply chests").define("cropsOnShipwreckSupplyLoot", true);
		CROPS_ON_VILLAGE_HOUSES = COMMON_BUILDER.comment("Generate crop items on Village houses (all biomes)").define("cropsOnVillageHouseLoot", true);
		COMMON_BUILDER.pop();

		COMMON_CONFIG = COMMON_BUILDER.build();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) { }

	@SubscribeEvent
	public static void onReload(final ModConfig.Reloading configEvent) { }

}
