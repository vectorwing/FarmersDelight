package vectorwing.farmersdelight.common.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
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
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;
import vectorwing.farmersdelight.common.world.filter.BiomeTagFilter;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class WildCropGeneration
{
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_SANDY_SHRUB = registerConfiguredFeatureKey("patch_sandy_shrub");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CABBAGES = registerConfiguredFeatureKey("patch_wild_cabbages");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_ONIONS = registerConfiguredFeatureKey("patch_wild_onions");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_TOMATOES = registerConfiguredFeatureKey("patch_wild_tomatoes");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_CARROTS = registerConfiguredFeatureKey("patch_wild_carrots");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_POTATOES = registerConfiguredFeatureKey("patch_wild_potatoes");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_BEETROOTS = registerConfiguredFeatureKey("patch_wild_beetroots");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_WILD_RICE = registerConfiguredFeatureKey("patch_wild_rice");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_BROWN_MUSHROOM_COLONIES = registerConfiguredFeatureKey("patch_brown_mushroom_colony");
	public static ResourceKey<ConfiguredFeature<?, ?>> FEATURE_PATCH_RED_MUSHROOM_COLONIES = registerConfiguredFeatureKey("patch_red_mushroom_colony");

	public static ResourceKey<PlacedFeature> PATCH_WILD_CABBAGES = registerPlacedFeatureKey("patch_wild_cabbages");
	public static ResourceKey<PlacedFeature> PATCH_WILD_ONIONS = registerPlacedFeatureKey("patch_wild_onions");
	public static ResourceKey<PlacedFeature> PATCH_WILD_TOMATOES = registerPlacedFeatureKey("patch_wild_tomatoes");
	public static ResourceKey<PlacedFeature> PATCH_WILD_CARROTS = registerPlacedFeatureKey("patch_wild_carrots");
	public static ResourceKey<PlacedFeature> PATCH_WILD_POTATOES = registerPlacedFeatureKey("patch_wild_potatoes");
	public static ResourceKey<PlacedFeature> PATCH_WILD_BEETROOTS = registerPlacedFeatureKey("patch_wild_beetroots");
	public static ResourceKey<PlacedFeature> PATCH_WILD_RICE = registerPlacedFeatureKey("patch_wild_rice");
	public static ResourceKey<PlacedFeature> PATCH_BROWN_MUSHROOM_COLONIES = registerPlacedFeatureKey("patch_brown_mushroom_colony");
	public static ResourceKey<PlacedFeature> PATCH_RED_MUSHROOM_COLONIES = registerPlacedFeatureKey("patch_red_mushroom_colony");

	private static ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredFeatureKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, name));
	}

	private static ResourceKey<PlacedFeature> registerPlacedFeatureKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, name));
	}

	public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
		context.register(FEATURE_PATCH_SANDY_SHRUB, new ConfiguredFeature<>(
				Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(32, 2, 3,
						plantPlacedFeature(ModBlocks.SANDY_SHRUB.get(), BlockTags.SAND))
		));
		context.register(FEATURE_PATCH_WILD_CABBAGES, wildCropConfiguredFeature(
				ModBlocks.WILD_CABBAGES.get(),
				ModBlocks.SANDY_SHRUB.get(),
				BlockTags.SAND
		));
		context.register(FEATURE_PATCH_WILD_BEETROOTS, wildCropConfiguredFeature(
				ModBlocks.WILD_BEETROOTS.get(),
				ModBlocks.SANDY_SHRUB.get(),
				BlockTags.SAND
		));
		context.register(FEATURE_PATCH_WILD_CARROTS, wildCropConfiguredFeature(
				ModBlocks.WILD_CARROTS.get(),
				Blocks.SHORT_GRASS,
				Blocks.COARSE_DIRT,
				BlockTags.DIRT
		));
		context.register(FEATURE_PATCH_WILD_ONIONS, wildCropConfiguredFeature(
				ModBlocks.WILD_ONIONS.get(),
				Blocks.ALLIUM,
				BlockTags.DIRT
		));
		context.register(FEATURE_PATCH_WILD_POTATOES, wildCropConfiguredFeature(
				ModBlocks.WILD_POTATOES.get(),
				Blocks.FERN,
				BlockTags.DIRT
		));
		context.register(FEATURE_PATCH_WILD_TOMATOES, wildCropConfiguredFeature(
				ModBlocks.WILD_TOMATOES.get(), Blocks.DEAD_BUSH, ModTags.Blocks.TERRAIN
		));
		context.register(FEATURE_PATCH_WILD_RICE, new ConfiguredFeature<>(
				ModBiomeFeatures.WILD_RICE.get(),
				new RandomPatchConfiguration(96, 7, 3,
						plantPlacedFeature(ModBlocks.WILD_RICE.get(), BlockTags.DIRT)
				)
		));
		context.register(FEATURE_PATCH_BROWN_MUSHROOM_COLONIES, mushroomColonyConfiguredFeature(
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				Blocks.BROWN_MUSHROOM
		));
		context.register(FEATURE_PATCH_RED_MUSHROOM_COLONIES, mushroomColonyConfiguredFeature(
				ModBlocks.RED_MUSHROOM_COLONY.get(),
				Blocks.RED_MUSHROOM
		));
	}

	public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureLookup = context.lookup(Registries.CONFIGURED_FEATURE);
		context.register(PATCH_WILD_CABBAGES, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_CABBAGES, 30));
		context.register(PATCH_WILD_ONIONS, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_ONIONS, 120));
		context.register(PATCH_WILD_TOMATOES, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_TOMATOES, 100));
		context.register(PATCH_WILD_CARROTS, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_CARROTS, 120));
		context.register(PATCH_WILD_POTATOES, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_POTATOES, 100));
		context.register(PATCH_WILD_BEETROOTS, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_BEETROOTS, 30));
		context.register(PATCH_WILD_RICE, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_WILD_RICE, 20));
		context.register(PATCH_BROWN_MUSHROOM_COLONIES, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_BROWN_MUSHROOM_COLONIES, 15));
		context.register(PATCH_RED_MUSHROOM_COLONIES, createPlacedFeature(configuredFeatureLookup, FEATURE_PATCH_RED_MUSHROOM_COLONIES, 15));
	}

	private static ConfiguredFeature<?, ?> wildCropConfiguredFeature(Block primaryBlock, Block secondaryBlock, TagKey<Block> blocksToTarget) {
		return defaultWildCropConfiguredFeature(
				plantPlacedFeature(primaryBlock, blocksToTarget),
				plantPlacedFeature(secondaryBlock, blocksToTarget),
				null
		);
	}

	private static ConfiguredFeature<?, ?> wildCropConfiguredFeature(Block primaryBlock, Block secondaryBlock, Block floorBlock, TagKey<Block> blocksToTarget) {
		return defaultWildCropConfiguredFeature(
				plantPlacedFeature(primaryBlock, blocksToTarget),
				plantPlacedFeature(secondaryBlock, blocksToTarget),
				floorPlacedFeature(floorBlock, blocksToTarget)
		);
	}

	private static ConfiguredFeature<?, ?> mushroomColonyConfiguredFeature(Block colonyBlock, Block mushroomBlock) {
		return defaultWildCropConfiguredFeature(
				Holder.direct(new PlacedFeature(
						Holder.direct(new ConfiguredFeature<>(
								Feature.SIMPLE_BLOCK,
								new SimpleBlockConfiguration(new RandomizedIntStateProvider(
										BlockStateProvider.simple(colonyBlock),
										MushroomColonyBlock.COLONY_AGE,
										UniformInt.of(0, 3)
								))
						)),
						placeOnTopOfModifier(Blocks.MYCELIUM)
				)),
				plantPlacedFeature(mushroomBlock, Blocks.MYCELIUM),
				null
		);
	}

	private static ConfiguredFeature<?, ?> defaultWildCropConfiguredFeature(Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature) {
		return new ConfiguredFeature<>(
				ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(64, 6, 3, primaryFeature, secondaryFeature, floorFeature)
		);
	}

	private static Holder<PlacedFeature> plantPlacedFeature(Block block, Block blocksToPlaceOn) {
		return Holder.direct(new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
						Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(SimpleStateProvider.simple(block))
				)),
				placeOnTopOfModifier(blocksToPlaceOn)
		));
	}

	private static Holder<PlacedFeature> plantPlacedFeature(Block block, TagKey<Block> blocksToPlaceOn) {
		return Holder.direct(new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
						Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(SimpleStateProvider.simple(block))
				)),
				placeOnTopOfModifier(blocksToPlaceOn)
		));
	}

	private static Holder<PlacedFeature> floorPlacedFeature(Block block, TagKey<Block> blocksToReplace) {
		return Holder.direct(new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
						Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(SimpleStateProvider.simple(block))
				)),
				replaceBlockModifier(blocksToReplace)
		));
	}

	private static List<PlacementModifier> placeOnTopOfModifier(Block blockToPlaceOn) {
		return List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), blockToPlaceOn)
		)));
	}

	private static List<PlacementModifier> placeOnTopOfModifier(TagKey<Block> blocksToPlaceOn) {
		return List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesTag(new Vec3i(0, -1, 0), blocksToPlaceOn)
		)));
	}

	private static List<PlacementModifier> replaceBlockModifier(TagKey<Block> blocksToReplace) {
		return List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.replaceable(new Vec3i(0, 1, 0)),
				BlockPredicate.matchesTag(blocksToReplace)
		)));
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
