package vectorwing.farmersdelight.world;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import vectorwing.farmersdelight.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.setup.Configuration;

public class CropPatchGeneration
{
	public static final BlockClusterFeatureConfig CABBAGE_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_CABBAGES.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.SAND.getBlock())).noProjection().build();
	public static final BlockClusterFeatureConfig ONION_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_ONIONS.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).noProjection().build();
	public static final BlockClusterFeatureConfig TOMATO_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_TOMATOES.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock(), Blocks.RED_SAND.getBlock(), Blocks.SAND.getBlock())).noProjection().build();
	public static final BlockClusterFeatureConfig CARROT_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_CARROTS.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).noProjection().build();
	public static final BlockClusterFeatureConfig POTATO_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_POTATOES.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).noProjection().build();
	public static final BlockClusterFeatureConfig BEETROOT_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_BEETROOTS.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.SAND.getBlock())).noProjection().build();
	public static final BlockClusterFeatureConfig RICE_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_RICE.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(4).zspread(4).whitelist(ImmutableSet.of(Blocks.DIRT.getBlock())).noProjection().build();

	public static final ConfiguredFeature<?, ?> PATCH_WILD_CABBAGES = Feature.RANDOM_PATCH.configured(CABBAGE_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(Configuration.CHANCE_WILD_CABBAGES.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_ONIONS = Feature.RANDOM_PATCH.configured(ONION_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(Configuration.CHANCE_WILD_ONIONS.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_TOMATOES = Feature.RANDOM_PATCH.configured(TOMATO_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(Configuration.CHANCE_WILD_TOMATOES.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_CARROTS = Feature.RANDOM_PATCH.configured(CARROT_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(Configuration.CHANCE_WILD_CARROTS.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_POTATOES = Feature.RANDOM_PATCH.configured(POTATO_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(Configuration.CHANCE_WILD_POTATOES.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_BEETROOTS = Feature.RANDOM_PATCH.configured(BEETROOT_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(Configuration.CHANCE_WILD_BEETROOTS.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_RICE = ModBiomeFeatures.RICE.get().configured(RICE_PATCH_CONFIG)
			.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(Configuration.CHANCE_WILD_RICE.get());

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
	}

	public static void registerConfiguredFeatures() {
		register("patch_wild_cabbages", PATCH_WILD_CABBAGES);
		register("patch_wild_onions", PATCH_WILD_ONIONS);
		register("patch_wild_tomatoes", PATCH_WILD_TOMATOES);
		register("patch_wild_carrots", PATCH_WILD_CARROTS);
		register("patch_wild_potatoes", PATCH_WILD_POTATOES);
		register("patch_wild_beetroots", PATCH_WILD_BEETROOTS);
		register("patch_wild_rice", PATCH_WILD_RICE);
	}
}
