package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

public class ModChestLootTables {
    public static final ResourceKey<LootTable> ABANDONED_MINESHAFT = register("chests/fd_abandoned_mineshaft");
    public static final ResourceKey<LootTable> BASTION_HOGLIN_STABLE = register("chests/fd_bastion_hoglin_stable");
    public static final ResourceKey<LootTable> BASTION_TREASURE = register("chests/fd_bastion_treasure");
    public static final ResourceKey<LootTable> END_CITY_TREASURE = register("chests/fd_end_city_treasure");
    public static final ResourceKey<LootTable> PILLAGER_OUTPOST = register("chests/fd_pillager_outpost");
    public static final ResourceKey<LootTable> RUINED_PORTAL = register("chests/fd_ruined_portal");
    public static final ResourceKey<LootTable> SHIPWRECK_SUPPLY = register("chests/fd_shipwreck_supply");
    public static final ResourceKey<LootTable> SIMPLE_DUNGEON = register("chests/fd_simple_dungeon");
    public static final ResourceKey<LootTable> VILLAGE_BUTCHER = register("chests/fd_village_butcher");
    public static final ResourceKey<LootTable> VILLAGE_DESERT_HOUSE = register("chests/fd_village_desert_house");
    public static final ResourceKey<LootTable> VILLAGE_PLAINS_HOUSE = register("chests/fd_village_plains_house");
    public static final ResourceKey<LootTable> VILLAGE_SAVANNA_HOUSE = register("chests/fd_village_savanna_house");
    public static final ResourceKey<LootTable> VILLAGE_SNOWY_HOUSE = register("chests/fd_village_snowy_house");
    public static final ResourceKey<LootTable> VILLAGE_TAIGA_HOUSE = register("chests/fd_village_taiga_house");

    private static ResourceKey<LootTable> register(String id) {
//        return ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, id);
        return ResourceKey.create(Registries.LOOT_TABLE, RecipeUtils.FDLocation(id));
    }
}
