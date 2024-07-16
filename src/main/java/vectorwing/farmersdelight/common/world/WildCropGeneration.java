package vectorwing.farmersdelight.common.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import vectorwing.farmersdelight.FarmersDelight;

@SuppressWarnings("unused")
public class WildCropGeneration
{
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_SANDY_SHRUB = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_sandy_shrub"));

	// Those are unused, but kept for reference just in case
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CABBAGES = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_cabbages"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_ONIONS = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_onions"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_TOMATOES = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_tomatoes"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CARROTS = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_carrots"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_POTATOES = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_potatoes"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_BEETROOTS = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_beetroots"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_RICE = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_rice"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_BROWN_MUSHROOM_COLONIES = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_brown_mushroom_colony"));
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_RED_MUSHROOM_COLONIES = ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_red_mushroom_colony"));

	public static ResourceKey<PlacedFeature> PATCH_WILD_CABBAGES = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_cabbages"));
	public static ResourceKey<PlacedFeature> PATCH_WILD_ONIONS = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_onions"));
	public static ResourceKey<PlacedFeature> PATCH_WILD_TOMATOES = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_tomatoes"));
	public static ResourceKey<PlacedFeature> PATCH_WILD_CARROTS = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_carrots"));
	public static ResourceKey<PlacedFeature> PATCH_WILD_POTATOES = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_potatoes"));
	public static ResourceKey<PlacedFeature> PATCH_WILD_BEETROOTS = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_beetroots"));
	public static ResourceKey<PlacedFeature> PATCH_WILD_RICE = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_wild_rice"));
	public static ResourceKey<PlacedFeature> PATCH_BROWN_MUSHROOM_COLONIES = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_brown_mushroom_colony"));
	public static ResourceKey<PlacedFeature> PATCH_RED_MUSHROOM_COLONIES = ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "patch_red_mushroom_colony"));

	public static void load() {
	}
}
