package vectorwing.farmersdelight.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;
import vectorwing.farmersdelight.common.world.filter.BiomeTagFilter;

import java.util.List;

public class WildCropGeneration
{
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_WILD_CABBAGES;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_WILD_ONIONS;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_WILD_TOMATOES;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_WILD_CARROTS;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_WILD_POTATOES;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_WILD_BEETROOTS;
	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_WILD_RICE;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_BROWN_MUSHROOM_COLONIES;
	public static Holder<ConfiguredFeature<WildCropConfiguration, ?>> FEATURE_PATCH_RED_MUSHROOM_COLONIES;

	public static Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> FEATURE_PATCH_SANDY_SHRUB_BONEMEAL;

	public static Holder<PlacedFeature> PATCH_WILD_CABBAGES;
	public static Holder<PlacedFeature> PATCH_WILD_ONIONS;
	public static Holder<PlacedFeature> PATCH_WILD_TOMATOES;
	public static Holder<PlacedFeature> PATCH_WILD_CARROTS;
	public static Holder<PlacedFeature> PATCH_WILD_POTATOES;
	public static Holder<PlacedFeature> PATCH_WILD_BEETROOTS;
	public static Holder<PlacedFeature> PATCH_WILD_RICE;
	public static Holder<PlacedFeature> PATCH_BROWN_MUSHROOM_COLONIES;
	public static Holder<PlacedFeature> PATCH_RED_MUSHROOM_COLONIES;

	public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);
	public static final BlockPos BLOCK_ABOVE = new BlockPos(0, 1, 0);

	public static final BiomeTagFilter TAGGED_IS_OVERWORLD = BiomeTagFilter.biomeIsInTag(BiomeTags.IS_OVERWORLD);

	public static void registerWildCropGeneration() {
		FEATURE_PATCH_WILD_CABBAGES = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_cabbages"),
				ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_CABBAGES.get(), ModBlocks.SANDY_SHRUB.get(), BlockPredicate.matchesBlocks(BLOCK_BELOW, Blocks.SAND)));

		FEATURE_PATCH_WILD_ONIONS = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_onions"),
				ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_ONIONS.get(), Blocks.ALLIUM, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)));

		FEATURE_PATCH_WILD_TOMATOES = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_tomatoes"),
				ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_TOMATOES.get(), Blocks.DEAD_BUSH, BlockPredicate.matchesBlocks(BLOCK_BELOW, List.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.RED_SAND, Blocks.SAND))));

		FEATURE_PATCH_WILD_CARROTS = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_carrots"),
				ModBiomeFeatures.WILD_CROP.get(), wildCropWithFloorConfig(ModBlocks.WILD_CARROTS.get(), Blocks.GRASS, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT), Blocks.COARSE_DIRT, BlockPredicate.matchesTag(BlockTags.DIRT)));

		FEATURE_PATCH_WILD_POTATOES = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_potatoes"),
				ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_POTATOES.get(), Blocks.FERN, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)));

		FEATURE_PATCH_WILD_BEETROOTS = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_beetroots"),
				ModBiomeFeatures.WILD_CROP.get(), wildCropConfig(ModBlocks.WILD_BEETROOTS.get(), ModBlocks.SANDY_SHRUB.get(), BlockPredicate.matchesBlocks(BLOCK_BELOW, Blocks.SAND)));

		FEATURE_PATCH_WILD_RICE = register(new ResourceLocation(FarmersDelight.MODID, "patch_wild_rice"),
				ModBiomeFeatures.WILD_RICE.get(), FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.WILD_RICE.get())), List.of(Blocks.DIRT)));

		FEATURE_PATCH_BROWN_MUSHROOM_COLONIES = register(new ResourceLocation(FarmersDelight.MODID, "patch_brown_mushroom_colonies"),
				ModBiomeFeatures.WILD_CROP.get(), mushroomColonyConfig(ModBlocks.BROWN_MUSHROOM_COLONY.get(), Blocks.BROWN_MUSHROOM, BlockPredicate.matchesBlock(Blocks.MYCELIUM, BLOCK_BELOW)));

		FEATURE_PATCH_RED_MUSHROOM_COLONIES = register(new ResourceLocation(FarmersDelight.MODID, "patch_red_mushroom_colonies"),
				ModBiomeFeatures.WILD_CROP.get(), mushroomColonyConfig(ModBlocks.RED_MUSHROOM_COLONY.get(), Blocks.RED_MUSHROOM, BlockPredicate.matchesBlock(Blocks.MYCELIUM, BLOCK_BELOW)));

		FEATURE_PATCH_SANDY_SHRUB_BONEMEAL = register(new ResourceLocation(FarmersDelight.MODID, "patch_sandy_shrub"),
				Feature.RANDOM_PATCH, randomPatchConfig(ModBlocks.SANDY_SHRUB.get(), 32, 2, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.SAND)));

		PATCH_WILD_CABBAGES = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_cabbages"),
				FEATURE_PATCH_WILD_CABBAGES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_CABBAGES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_WILD_ONIONS = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_onions"),
				FEATURE_PATCH_WILD_ONIONS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_ONIONS.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_WILD_TOMATOES = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_tomatoes"),
				FEATURE_PATCH_WILD_TOMATOES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_TOMATOES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_WILD_CARROTS = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_carrots"),
				FEATURE_PATCH_WILD_CARROTS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_CARROTS.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_WILD_POTATOES = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_potatoes"),
				FEATURE_PATCH_WILD_POTATOES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_POTATOES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_WILD_BEETROOTS = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_beetroots"),
				FEATURE_PATCH_WILD_BEETROOTS, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_BEETROOTS.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_WILD_RICE = registerPlacement(new ResourceLocation(FarmersDelight.MODID, "patch_wild_rice"),
				FEATURE_PATCH_WILD_RICE, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_WILD_RICE.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_BROWN_MUSHROOM_COLONIES = registerPlacement(new ResourceLocation("patch_brown_mushroom_colonies"),
				FEATURE_PATCH_BROWN_MUSHROOM_COLONIES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_BROWN_MUSHROOM_COLONIES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

		PATCH_RED_MUSHROOM_COLONIES = registerPlacement(new ResourceLocation("patch_red_mushroom_colonies"),
				FEATURE_PATCH_RED_MUSHROOM_COLONIES, RarityFilter.onAverageOnceEvery(Configuration.CHANCE_RED_MUSHROOM_COLONIES.get()), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);
	}

	public static RandomPatchConfiguration randomPatchConfig(Block block, int tries, int xzSpread, BlockPredicate plantedOn) {
		return new RandomPatchConfiguration(tries, xzSpread, 3, PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
				BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)));
	}

	public static WildCropConfiguration wildCropConfig(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
		return new WildCropConfiguration(64, 6, 3, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
	}

	public static WildCropConfiguration wildCropWithFloorConfig(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn, Block floorBlock, BlockPredicate replaces) {
		return new WildCropConfiguration(64, 6, 3, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), floorBlockConfig(floorBlock, replaces));
	}

	public static WildCropConfiguration mushroomColonyConfig(Block colonyBlock, Block secondaryBlock, BlockPredicate plantedOn) {
		return new WildCropConfiguration(64, 6, 3, colonyBlockConfig(colonyBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
	}

	public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
		return PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
				BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
	}

	public static Holder<PlacedFeature> colonyBlockConfig(Block block, BlockPredicate plantedOn) {
		return PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new RandomizedIntStateProvider(BlockStateProvider.simple(block), MushroomColonyBlock.COLONY_AGE, UniformInt.of(0, 3))),
				BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
	}

	public static Holder<PlacedFeature> floorBlockConfig(Block block, BlockPredicate replaces) {
		return PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
				BlockPredicate.allOf(BlockPredicate.replaceable(BLOCK_ABOVE), replaces));
	}

	// Registry stuff

	static Holder<PlacedFeature> registerPlacement(ResourceLocation id, Holder<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
	}

	protected static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(ResourceLocation id, F feature, FC featureConfig) {
		return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
	}

	private static <V extends T, T> Holder<V> register(Registry<T> registry, ResourceLocation id, V value) {
		return (Holder<V>) BuiltinRegistries.<T>register(registry, id, value);
	}
}
