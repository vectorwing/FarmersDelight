package vectorwing.farmersdelight.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Configuration {
    public static final String CATEGORY_WORLD = "world";
    public static final String CATEGORY_SETTINGS = "settings";

    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.BooleanValue CROPS_ON_SHIPWRECKS;
    public static final ForgeConfigSpec.BooleanValue CROPS_ON_VILLAGE_HOUSES;
    public static final ForgeConfigSpec.BooleanValue GENERATE_WILD_CABBAGES;
    public static final ForgeConfigSpec.IntValue FREQUENCY_WILD_CABBAGES;
    public static final ForgeConfigSpec.BooleanValue GENERATE_WILD_BEETROOTS;
    public static final ForgeConfigSpec.IntValue FREQUENCY_WILD_BEETROOTS;
    public static final ForgeConfigSpec.BooleanValue GENERATE_WILD_POTATOES;
    public static final ForgeConfigSpec.IntValue CHANCE_WILD_POTATOES;
    public static final ForgeConfigSpec.BooleanValue GENERATE_WILD_ONIONS;
    public static final ForgeConfigSpec.IntValue CHANCE_WILD_ONIONS;
    public static final ForgeConfigSpec.BooleanValue GENERATE_WILD_CARROTS;
    public static final ForgeConfigSpec.IntValue CHANCE_WILD_CARROTS;
    public static final ForgeConfigSpec.BooleanValue GENERATE_WILD_TOMATOES;
    public static final ForgeConfigSpec.IntValue CHANCE_WILD_TOMATOES;

    public static final ForgeConfigSpec.BooleanValue FARMERS_BUY_FD_CROPS;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Game settings").push(CATEGORY_SETTINGS);
        FARMERS_BUY_FD_CROPS = COMMON_BUILDER.comment("Should Novice and Apprentice Farmers buy this mod's crops? (May reduce chances of other trades appearing)").define("farmersBuyFDCrops", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("World generation").push(CATEGORY_WORLD);
        CROPS_ON_SHIPWRECKS = COMMON_BUILDER.comment("Generate crop items on a Shipwreck's supply chests").define("cropsOnShipwreckSupplyLoot", true);
        CROPS_ON_VILLAGE_HOUSES = COMMON_BUILDER.comment("Generate crop items on Village houses (all biomes)").define("cropsOnVillageHouseLoot", true);

        COMMON_BUILDER.comment("Wild Cabbage generation").push("wild_cabbages");
        GENERATE_WILD_CABBAGES = COMMON_BUILDER.comment("Generate wild cabbages on beaches").define("genWildCabbages", true);
        FREQUENCY_WILD_CABBAGES = COMMON_BUILDER.comment("Frequency of clusters. Larger value = more frequent.").defineInRange("frequency", 1, 0, 10);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Sea Beet generation").push("wild_beetroots");
        GENERATE_WILD_BEETROOTS = COMMON_BUILDER.comment("Generate sea beets on beaches").define("genWildBeetroots", true);
        FREQUENCY_WILD_BEETROOTS = COMMON_BUILDER.comment("Frequency of clusters. Larger value = more frequent.").defineInRange("frequency", 1, 0, 10);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Wild Potato generation").push("wild_potatoes");
        GENERATE_WILD_POTATOES = COMMON_BUILDER.comment("Generate wild potatoes on cold biomes (temperature between 0.0 and 0.3)").define("genWildPotatoes", true);
        CHANCE_WILD_POTATOES = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.").defineInRange("chance", 9, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Wild Carrot generation").push("wild_carrots");
        GENERATE_WILD_CARROTS = COMMON_BUILDER.comment("Generate wild carrots on temperate biomes (temperature between 0.4 and 0.9)").define("genWildCarrots", true);
        CHANCE_WILD_CARROTS = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.").defineInRange("chance", 9, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Wild Onion generation").push("wild_onions");
        GENERATE_WILD_ONIONS = COMMON_BUILDER.comment("Generate wild onions on temperate biomes (temperature between 0.4 and 0.9)").define("genWildOnions", true);
        CHANCE_WILD_ONIONS = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.").defineInRange("chance", 9, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Tomato Vines generation").push("wild_tomatoes");
        GENERATE_WILD_TOMATOES = COMMON_BUILDER.comment("Generate tomato vines on arid biomes (temperature 1.0 or higher)").define("genWildTomatoes", true);
        CHANCE_WILD_TOMATOES = COMMON_BUILDER.comment("Chance of generating clusters. Smaller value = more frequent.").defineInRange("chance", 9, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }

}
