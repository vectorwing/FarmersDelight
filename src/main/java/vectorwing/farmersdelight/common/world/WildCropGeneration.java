package vectorwing.farmersdelight.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;

public class WildCropGeneration
{
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_CABBAGES;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_ONIONS;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_TOMATOES;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_CARROTS;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_POTATOES;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_BEETROOTS;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_RICE;

	public static Holder<PlacedFeature> PATCH_WILD_CABBAGES;
	public static Holder<PlacedFeature> PATCH_WILD_ONIONS;
	public static Holder<PlacedFeature> PATCH_WILD_TOMATOES;
	public static Holder<PlacedFeature> PATCH_WILD_CARROTS;
	public static Holder<PlacedFeature> PATCH_WILD_POTATOES;
	public static Holder<PlacedFeature> PATCH_WILD_BEETROOTS;
	public static Holder<PlacedFeature> PATCH_WILD_RICE;

	public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);

	public static RandomPatchConfiguration getWildCropConfiguration(Block block, int tries, int xzSpread, BlockPredicate plantedOn) {
		return new RandomPatchConfiguration(tries, xzSpread, 3, PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
						BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)));
	}

	static Holder<PlacedFeature> registerPlacement(ResourceLocation id, Holder<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
	}

	protected static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(ResourceLocation id, F feature, FC featureConfig) {
		return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	private static <V extends T, T> Holder<V> register(Registry<T> registry, ResourceLocation id, V value) {
		return (Holder<V>) BuiltinRegistries.<T>register(registry, id, value);
	}

	public static void registerWildCropGeneration() {
		FEATURE_PATCH_WILD_CABBAGES = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_cabbages"),
				Feature.RANDOM_PATCH, getWildCropConfiguration(ModBlocks.WILD_CABBAGES.get(), 64, 4, BlockPredicate.matchesBlock(Blocks.SAND, BLOCK_BELOW)));

		FEATURE_PATCH_WILD_ONIONS = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_onions"),
				Feature.RANDOM_PATCH, getWildCropConfiguration(ModBlocks.WILD_ONIONS.get(), 64, 4, BlockPredicate.matchesTag(BlockTags.DIRT, BLOCK_BELOW)));

		FEATURE_PATCH_WILD_TOMATOES = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_tomatoes"),
				Feature.RANDOM_PATCH, getWildCropConfiguration(ModBlocks.WILD_TOMATOES.get(), 64, 4, BlockPredicate.matchesBlocks(List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.RED_SAND, Blocks.SAND), BLOCK_BELOW)));

		FEATURE_PATCH_WILD_CARROTS = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_carrots"),
				Feature.RANDOM_PATCH, getWildCropConfiguration(ModBlocks.WILD_CARROTS.get(), 64, 4, BlockPredicate.matchesTag(BlockTags.DIRT, BLOCK_BELOW)));

		FEATURE_PATCH_WILD_POTATOES = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_potatoes"),
				Feature.RANDOM_PATCH, getWildCropConfiguration(ModBlocks.WILD_POTATOES.get(), 64, 4, BlockPredicate.matchesTag(BlockTags.DIRT, BLOCK_BELOW)));

		FEATURE_PATCH_WILD_BEETROOTS = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_beetroots"),
				Feature.RANDOM_PATCH, getWildCropConfiguration(ModBlocks.WILD_BEETROOTS.get(), 64, 4, BlockPredicate.matchesBlock(Blocks.SAND, BLOCK_BELOW)));

		FEATURE_PATCH_WILD_RICE = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_rice"),
				ModBiomeFeatures.WILD_RICE.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_RICE.get())), List.of(Blocks.DIRT)));

		PATCH_WILD_CABBAGES = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_cabbages"),
				FEATURE_PATCH_WILD_CABBAGES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_CABBAGES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		PATCH_WILD_ONIONS = registerPlacement(new ResourceLocation("patch_wild_onions"),
				FEATURE_PATCH_WILD_ONIONS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_ONIONS.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		PATCH_WILD_TOMATOES = registerPlacement(new ResourceLocation("patch_wild_tomatoes"),
				FEATURE_PATCH_WILD_TOMATOES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_TOMATOES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		PATCH_WILD_CARROTS = registerPlacement(new ResourceLocation("patch_wild_carrots"),
				FEATURE_PATCH_WILD_CARROTS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_CARROTS.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		PATCH_WILD_POTATOES = registerPlacement(new ResourceLocation("patch_wild_potatoes"),
				FEATURE_PATCH_WILD_POTATOES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_POTATOES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		PATCH_WILD_BEETROOTS = registerPlacement(new ResourceLocation("patch_wild_beetroots"),
				FEATURE_PATCH_WILD_BEETROOTS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_BEETROOTS.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		PATCH_WILD_RICE = registerPlacement(new ResourceLocation("patch_wild_rice"),
				FEATURE_PATCH_WILD_RICE, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_RICE.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}
}
