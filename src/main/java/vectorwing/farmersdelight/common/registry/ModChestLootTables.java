package vectorwing.farmersdelight.common.registry;

import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class ModChestLootTables {
    public static final ResourceLocation ABANDONED_MINESHAFT = register("chests/fd_abandoned_mineshaft");
    public static final ResourceLocation BASTION_HOGLIN_STABLE = register("chests/fd_bastion_hoglin_stable");
    public static final ResourceLocation BASTION_TREASURE = register("chests/fd_bastion_treasure");
    public static final ResourceLocation END_CITY_TREASURE = register("chests/fd_end_city_treasure");
    public static final ResourceLocation PILLAGER_OUTPOST = register("chests/fd_pillager_outpost");
    public static final ResourceLocation RUINED_PORTAL = register("chests/fd_ruined_portal");
    public static final ResourceLocation SHIPWRECK_SUPPLY = register("chests/fd_shipwreck_supply");
    public static final ResourceLocation SIMPLE_DUNGEON = register("chests/fd_simple_dungeon");
    public static final ResourceLocation VILLAGE_BUTCHER = register("chests/fd_village_butcher");
    public static final ResourceLocation VILLAGE_DESERT_HOUSE = register("chests/fd_village_desert_house");
    public static final ResourceLocation VILLAGE_PLAINS_HOUSE = register("chests/fd_village_plains_house");
    public static final ResourceLocation VILLAGE_SAVANNA_HOUSE = register("chests/fd_village_savanna_house");
    public static final ResourceLocation VILLAGE_SNOWY_HOUSE = register("chests/fd_village_snowy_house");
    public static final ResourceLocation VILLAGE_TAIGA_HOUSE = register("chests/fd_village_taiga_house");

    private static ResourceLocation register(String id) {
        return new ResourceLocation(FarmersDelight.MODID, id);
    }
}
