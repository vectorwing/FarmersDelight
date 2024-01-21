package vectorwing.farmersdelight.common.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;
import vectorwing.farmersdelight.common.world.filter.BiomeTagFilter;

import java.util.List;

public class WildCropGeneration
{
	public static final ResourceKey<ConfiguredFeature<?, ?>> BROWN_MUSHROOM_COLONY = registerConfiguredFeatureKey("patch_brown_mushroom_colony");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_MUSHROOM_COLONY = registerConfiguredFeatureKey("patch_red_mushroom_colony");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SANDY_SHRUB = registerConfiguredFeatureKey("patch_sandy_shrub");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_BEETROOTS = registerConfiguredFeatureKey("patch_wild_beetroots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_CABBAGES = registerConfiguredFeatureKey("patch_wild_cabbages");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_CARROTS = registerConfiguredFeatureKey("patch_wild_carrots");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_ONIONS = registerConfiguredFeatureKey("patch_wild_onions");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_POTATOES = registerConfiguredFeatureKey("patch_wild_potatoes");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_RICE = registerConfiguredFeatureKey("patch_wild_rice");
	public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_TOMATOES = registerConfiguredFeatureKey("patch_wild_tomatoes");

	public static final ResourceKey<PlacedFeature> PLACED_BROWN_MUSHROOM_COLONY = registerKey("patch_brown_mushroom_colony");
	public static final ResourceKey<PlacedFeature> PLACED_RED_MUSHROOM_COLONY = registerKey("patch_red_mushroom_colony");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_BEETROOTS = registerKey("patch_wild_beetroots");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_CABBAGES = registerKey("patch_wild_cabbages");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_CARROTS = registerKey("patch_wild_carrots");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_ONIONS = registerKey("patch_wild_onions");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_POTATOES = registerKey("patch_wild_potatoes");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_RICE = registerKey("patch_wild_rice");
	public static final ResourceKey<PlacedFeature> PLACED_WILD_TOMATOES = registerKey("patch_wild_tomatoes");


	private static ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredFeatureKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(FarmersDelight.MODID, name));
	}

	private static ResourceKey<PlacedFeature> registerKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(FarmersDelight.MODID, name));
	}

	public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
		context.register(BROWN_MUSHROOM_COLONY, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createColonyFeature(ModBlocks.BROWN_MUSHROOM_COLONY.get(), Blocks.MYCELIUM),
						createPlacedFeature(Blocks.BROWN_MUSHROOM, Blocks.MYCELIUM),
						null)));

		context.register(RED_MUSHROOM_COLONY, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createColonyFeature(ModBlocks.RED_MUSHROOM_COLONY.get(), Blocks.MYCELIUM),
						createPlacedFeature(Blocks.RED_MUSHROOM, Blocks.MYCELIUM),
						null)));

		context.register(SANDY_SHRUB, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(32, 2, 3,
						createPlacedFeature(ModBlocks.SANDY_SHRUB.get(), BlockTags.SAND))));

		context.register(WILD_BEETROOTS, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createPlacedFeature(ModBlocks.WILD_BEETROOTS.get(), BlockTags.SAND),
						createPlacedFeature(ModBlocks.SANDY_SHRUB.get(), BlockTags.SAND),
						null)));

		context.register(WILD_CABBAGES, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createPlacedFeature(ModBlocks.WILD_CABBAGES.get(), BlockTags.SAND),
						createPlacedFeature(ModBlocks.SANDY_SHRUB.get(), BlockTags.SAND),
						null)));

		context.register(WILD_CARROTS, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createPlacedFeature(ModBlocks.WILD_CARROTS.get(), BlockTags.DIRT),
						createPlacedFeature(Blocks.FERN, BlockTags.DIRT),
						createFloorFeature(Blocks.COARSE_DIRT, BlockTags.DIRT))));

		context.register(WILD_ONIONS, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createPlacedFeature(ModBlocks.WILD_ONIONS.get(), BlockTags.DIRT),
						createPlacedFeature(Blocks.ALLIUM, BlockTags.DIRT),
						null)));

		context.register(WILD_POTATOES, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createPlacedFeature(ModBlocks.WILD_POTATOES.get(), BlockTags.DIRT),
						createPlacedFeature(Blocks.FERN, BlockTags.DIRT),
						null)));

		context.register(WILD_RICE, new ConfiguredFeature<>(ModBiomeFeatures.WILD_RICE.get(),
				new RandomPatchConfiguration(96, 7, 3,
						createPlacedFeature(ModBlocks.WILD_RICE.get(), BlockTags.DIRT))));

		context.register(WILD_TOMATOES, new ConfiguredFeature<>(ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3,
						createPlacedFeature(ModBlocks.WILD_TOMATOES.get(), ModTags.WILD_TOMATO_GROWS_ON),
						createPlacedFeature(Blocks.DEAD_BUSH, ModTags.WILD_TOMATO_GROWS_ON),
						null)));
	}

	public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> featureGetter = context.lookup(Registries.CONFIGURED_FEATURE);
		context.register(PLACED_BROWN_MUSHROOM_COLONY, createPlacedFeature(featureGetter, BROWN_MUSHROOM_COLONY, 15));
		context.register(PLACED_RED_MUSHROOM_COLONY, createPlacedFeature(featureGetter, RED_MUSHROOM_COLONY, 15));
		context.register(PLACED_WILD_BEETROOTS, createPlacedFeature(featureGetter, WILD_BEETROOTS, 30));
		context.register(PLACED_WILD_CABBAGES, createPlacedFeature(featureGetter, WILD_CABBAGES, 30));
		context.register(PLACED_WILD_CARROTS, createPlacedFeature(featureGetter, WILD_CARROTS, 120));
		context.register(PLACED_WILD_ONIONS, createPlacedFeature(featureGetter, WILD_ONIONS, 120));
		context.register(PLACED_WILD_POTATOES, createPlacedFeature(featureGetter, WILD_POTATOES, 100));
		context.register(PLACED_WILD_RICE, createPlacedFeature(featureGetter, WILD_RICE, 20));
		context.register(PLACED_WILD_TOMATOES, createPlacedFeature(featureGetter, WILD_TOMATOES, 100));
	}


	private static Holder<PlacedFeature> createPlacedFeature(Block blockToPlace, Block blockToPlaceOn) {
		return Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(blockToPlace)))), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), blockToPlaceOn))))));
	}

	private static Holder<PlacedFeature> createPlacedFeature(Block blockToPlace, TagKey<Block> blockToPlaceOn) {
		return Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(blockToPlace)))), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesTag(new Vec3i(0, -1, 0), blockToPlaceOn))))));
	}

	private static Holder<PlacedFeature> createFloorFeature(Block blockToPlace, TagKey<Block> blockToPlaceIn) {
		return Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(blockToPlace)))), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.replaceable(new Vec3i(0, 1, 0)), BlockPredicate.matchesTag(blockToPlaceIn))))));
	}

	private static Holder<PlacedFeature> createColonyFeature(Block block, Block blockToPlaceOn) {
		return Holder.direct(new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new RandomizedIntStateProvider(BlockStateProvider.simple(block), MushroomColonyBlock.COLONY_AGE, UniformInt.of(0, 3))))), List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), blockToPlaceOn))))));
	}

	private static PlacedFeature createPlacedFeature(HolderGetter<ConfiguredFeature<?, ?>> featureGetter, ResourceKey<ConfiguredFeature<?, ?>> feature, int rarity) {
		return new PlacedFeature(featureGetter.getOrThrow(feature), List.of(
				RarityFilter.onAverageOnceEvery(rarity),
				InSquarePlacement.spread(),
				HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
				BiomeFilter.biome(),
				BiomeTagFilter.biomeIsInTag(BiomeTags.IS_OVERWORLD)
		));
	}
}
