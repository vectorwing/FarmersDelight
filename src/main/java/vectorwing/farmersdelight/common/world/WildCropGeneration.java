package vectorwing.farmersdelight.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;

public class WildCropGeneration
{
	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_CABBAGES = FeatureUtils.register("patch_wild_cabbages",
			Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(
					new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_CABBAGES.get()))), List.of(Blocks.SAND))));

	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_ONIONS = FeatureUtils.register("patch_wild_onions",
			Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(64, 4, 3, () ->
					Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_ONIONS.get())))
							.filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlock(Blocks.GRASS_BLOCK, new BlockPos(0, -1, 0)))))));

	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_TOMATOES = FeatureUtils.register("patch_wild_tomatoes",
			Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(64, 4, 3, () ->
					Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_TOMATOES.get())))
							.filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlocks(List.of(Blocks.GRASS_BLOCK, Blocks.RED_SAND, Blocks.SAND), new BlockPos(0, -1, 0)))))));

	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_CARROTS = FeatureUtils.register("patch_wild_carrots",
			Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(64, 4, 3, () ->
					Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_CARROTS.get())))
							.filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlock(Blocks.GRASS_BLOCK, new BlockPos(0, -1, 0)))))));

	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_POTATOES = FeatureUtils.register("patch_wild_potatoes",
			Feature.RANDOM_PATCH.configured(new RandomPatchConfiguration(64, 4, 3, () ->
					Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_POTATOES.get())))
							.filtered(BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.matchesBlock(Blocks.GRASS_BLOCK, new BlockPos(0, -1, 0)))))));

	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_BEETROOTS = FeatureUtils.register("patch_wild_beetroots",
			Feature.RANDOM_PATCH.configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(
					new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_BEETROOTS.get()))), List.of(Blocks.SAND))));

	public static final ConfiguredFeature<RandomPatchConfiguration, ?> FEATURE_PATCH_WILD_RICE = FeatureUtils.register("patch_wild_beetroots",
			ModBiomeFeatures.WILD_RICE.get().configured(FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(
					new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_RICE.get()))), List.of(Blocks.DIRT))));

	public static final PlacedFeature PATCH_WILD_CABBAGES = PlacementUtils.register("patch_wild_cabbages",
			FEATURE_PATCH_WILD_CABBAGES.placed(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	public static final PlacedFeature PATCH_WILD_ONIONS = PlacementUtils.register("patch_wild_onions",
			FEATURE_PATCH_WILD_ONIONS.placed(RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	public static final PlacedFeature PATCH_WILD_TOMATOES = PlacementUtils.register("patch_wild_tomatoes",
			FEATURE_PATCH_WILD_TOMATOES.placed(RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	public static final PlacedFeature PATCH_WILD_CARROTS = PlacementUtils.register("patch_wild_carrots",
			FEATURE_PATCH_WILD_CARROTS.placed(RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	public static final PlacedFeature PATCH_WILD_POTATOES = PlacementUtils.register("patch_wild_potatoes",
			FEATURE_PATCH_WILD_POTATOES.placed(RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	public static final PlacedFeature PATCH_WILD_BEETROOTS = PlacementUtils.register("patch_wild_beetroots",
			FEATURE_PATCH_WILD_BEETROOTS.placed(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	public static final PlacedFeature PATCH_WILD_RICE = PlacementUtils.register("patch_wild_rice",
			FEATURE_PATCH_WILD_RICE.placed(RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
}
