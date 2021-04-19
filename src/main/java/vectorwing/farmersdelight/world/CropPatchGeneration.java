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
			new SimpleBlockStateProvider(ModBlocks.WILD_CABBAGES.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.SAND.getBlock())).preventProjection().build();
	public static final BlockClusterFeatureConfig ONION_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_ONIONS.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).preventProjection().build();
	public static final BlockClusterFeatureConfig TOMATO_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_TOMATOES.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock(), Blocks.RED_SAND.getBlock(), Blocks.SAND.getBlock())).preventProjection().build();
	public static final BlockClusterFeatureConfig CARROT_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_CARROTS.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).preventProjection().build();
	public static final BlockClusterFeatureConfig POTATO_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_POTATOES.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).preventProjection().build();
	public static final BlockClusterFeatureConfig BEETROOT_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_BEETROOTS.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.SAND.getBlock())).preventProjection().build();
	public static final BlockClusterFeatureConfig RICE_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_RICE.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(4).zSpread(4).whitelist(ImmutableSet.of(Blocks.DIRT.getBlock())).preventProjection().build();

	public static final ConfiguredFeature<?, ?> PATCH_WILD_CABBAGES = Feature.RANDOM_PATCH.withConfiguration(CABBAGE_PATCH_CONFIG)
			.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(Configuration.CHANCE_WILD_CABBAGES.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_ONIONS = Feature.RANDOM_PATCH.withConfiguration(ONION_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT).chance(Configuration.CHANCE_WILD_ONIONS.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_TOMATOES = Feature.RANDOM_PATCH.withConfiguration(TOMATO_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT).chance(Configuration.CHANCE_WILD_TOMATOES.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_CARROTS = Feature.RANDOM_PATCH.withConfiguration(CARROT_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT).chance(Configuration.CHANCE_WILD_CARROTS.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_POTATOES = Feature.RANDOM_PATCH.withConfiguration(POTATO_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT).chance(Configuration.CHANCE_WILD_POTATOES.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_BEETROOTS = Feature.RANDOM_PATCH.withConfiguration(BEETROOT_PATCH_CONFIG)
			.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(Configuration.CHANCE_WILD_BEETROOTS.get());
	public static final ConfiguredFeature<?, ?> PATCH_WILD_RICE = ModBiomeFeatures.RICE.get().withConfiguration(RICE_PATCH_CONFIG)
			.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(Configuration.CHANCE_WILD_RICE.get());

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
