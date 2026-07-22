package vectorwing.farmersdelight.common.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * Registry keys for the world-generation JSON shipped with Farmer's Delight.
 * Minecraft 26.1 moved patch predicates into placed-feature data, so the old
 * Java bootstrap implementation is no longer part of the runtime port.
 */
public final class WildCropGeneration {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_SANDY_SHRUB = configured("patch_sandy_shrub");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CABBAGES = configured("patch_wild_cabbages");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_ONIONS = configured("patch_wild_onions");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_TOMATOES = configured("patch_wild_tomatoes");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CARROTS = configured("patch_wild_carrots");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_POTATOES = configured("patch_wild_potatoes");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_BEETROOTS = configured("patch_wild_beetroots");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_RICE = configured("patch_wild_rice");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_BROWN_MUSHROOM_COLONIES = configured("patch_brown_mushroom_colony");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_RED_MUSHROOM_COLONIES = configured("patch_red_mushroom_colony");

    public static final ResourceKey<PlacedFeature> PATCH_WILD_CABBAGES = placed("patch_wild_cabbages");
    public static final ResourceKey<PlacedFeature> PATCH_SANDY_SHRUB = placed("patch_sandy_shrub");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_ONIONS = placed("patch_wild_onions");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_TOMATOES = placed("patch_wild_tomatoes");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_CARROTS = placed("patch_wild_carrots");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_POTATOES = placed("patch_wild_potatoes");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_BEETROOTS = placed("patch_wild_beetroots");
    public static final ResourceKey<PlacedFeature> PATCH_WILD_RICE = placed("patch_wild_rice");
    public static final ResourceKey<PlacedFeature> PATCH_BROWN_MUSHROOM_COLONIES = placed("patch_brown_mushroom_colony");
    public static final ResourceKey<PlacedFeature> PATCH_RED_MUSHROOM_COLONIES = placed("patch_red_mushroom_colony");

    private WildCropGeneration() {}

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name));
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name));
    }
}
