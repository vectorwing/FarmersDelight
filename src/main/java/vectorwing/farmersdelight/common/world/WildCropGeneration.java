package vectorwing.farmersdelight.common.world;

import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;
import vectorwing.farmersdelight.common.world.filter.BiomeTagFilter;

import java.util.List;
import java.util.function.Supplier;

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

	public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		List<PlacementModifier> sandModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesTag(new Vec3i(0, -1, 0), BlockTags.SAND)
		)));
		List<PlacementModifier> dirtModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesTag(new Vec3i(0, -1, 0), BlockTags.DIRT)
		)));
		Holder<PlacedFeature> sandyShrubPlacedFeature = createSimplePlacedFeature(ModBlocks.SANDY_SHRUB, sandModifiers);
		ctx.register(FEATURE_PATCH_SANDY_SHRUB, new ConfiguredFeature<>(
				Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(
						32,
						2,
						3,
						sandyShrubPlacedFeature
				)
		));
		ctx.register(FEATURE_PATCH_WILD_CABBAGES, createWildCropConfiguredFeature(
				createSimplePlacedFeature(ModBlocks.WILD_CABBAGES, sandModifiers),
				sandyShrubPlacedFeature
		));
		ctx.register(FEATURE_PATCH_WILD_ONIONS, createSimpleWildCropConfiguredFeature(
				ModBlocks.WILD_ONIONS.get(), Blocks.ALLIUM, dirtModifiers
		));
		List<PlacementModifier> tomatoModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesBlocks(
						new Vec3i(0, -1, 0),
						Blocks.DIRT,
						Blocks.GRASS_BLOCK,
						Blocks.COARSE_DIRT,
						Blocks.SAND,
						Blocks.RED_SAND
				)
		)));
		ctx.register(FEATURE_PATCH_WILD_TOMATOES, createSimpleWildCropConfiguredFeature(
				ModBlocks.WILD_TOMATOES.get(), Blocks.DEAD_BUSH, tomatoModifiers
		));
		List<PlacementModifier> carrotCoarseDirtModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.replaceable(new Vec3i(0, 1, 0)),
				BlockPredicate.matchesTag(BlockTags.DIRT)
		)));
		ctx.register(FEATURE_PATCH_WILD_CARROTS, new ConfiguredFeature<>(
				ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(
						64,
						6,
						3,
						createSimplePlacedFeature(ModBlocks.WILD_CARROTS, dirtModifiers),
						createSimplePlacedFeature(Blocks.SHORT_GRASS, dirtModifiers),
						createSimplePlacedFeature(Blocks.COARSE_DIRT, carrotCoarseDirtModifiers)
				)
		));
		ctx.register(FEATURE_PATCH_WILD_POTATOES, createSimpleWildCropConfiguredFeature(
				ModBlocks.WILD_POTATOES.get(), Blocks.FERN, dirtModifiers
		));
		ctx.register(FEATURE_PATCH_WILD_BEETROOTS, createWildCropConfiguredFeature(
				createSimplePlacedFeature(ModBlocks.WILD_BEETROOTS, sandModifiers),
				sandyShrubPlacedFeature
		));
		List<PlacementModifier> riceModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.DIRT)
		)));
		ctx.register(FEATURE_PATCH_WILD_RICE, new ConfiguredFeature<>(
				ModBiomeFeatures.WILD_RICE.get(),
				new RandomPatchConfiguration(
						96,
						7,
						3,
						createSimplePlacedFeature(ModBlocks.WILD_RICE, riceModifiers)
				)
		));
		List<PlacementModifier> myceliumModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.MYCELIUM)
		)));
		ctx.register(FEATURE_PATCH_BROWN_MUSHROOM_COLONIES, createMushroomColonyConfiguredFeature(
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				Blocks.BROWN_MUSHROOM
		));
		ctx.register(FEATURE_PATCH_RED_MUSHROOM_COLONIES, createMushroomColonyConfiguredFeature(
				ModBlocks.RED_MUSHROOM_COLONY.get(),
				Blocks.RED_MUSHROOM
		));
	}
	
	private static ConfiguredFeature<?, ?> createMushroomColonyConfiguredFeature(Block colonyBlock, Block mushroomBlock) {
		final List<PlacementModifier> myceliumModifiers = List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.MYCELIUM)
		)));
		final Holder<PlacedFeature> colonyPlacedFeature = Holder.direct(new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
						Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(new RandomizedIntStateProvider(
								BlockStateProvider.simple(colonyBlock),
								MushroomColonyBlock.COLONY_AGE,
								UniformInt.of(0, 3)
						))
				)),
				myceliumModifiers
		));
		return createWildCropConfiguredFeature(
				colonyPlacedFeature,
				createSimplePlacedFeature(mushroomBlock, myceliumModifiers)
		);
	}

	private static ConfiguredFeature<?, ?> createSimpleWildCropConfiguredFeature(Block primaryBlock, Block secondaryBlock, List<PlacementModifier> placementModifiers) {
		return createWildCropConfiguredFeature(
				createSimplePlacedFeature(primaryBlock, placementModifiers),
				createSimplePlacedFeature(secondaryBlock, placementModifiers)
		);
	}
	
	private static ConfiguredFeature<?, ?> createWildCropConfiguredFeature(Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature) {
		return new ConfiguredFeature<>(
				ModBiomeFeatures.WILD_CROP.get(),
				new WildCropConfiguration(
						64,
						6,
						3,
						primaryFeature,
						secondaryFeature,
						null
				)
		);
	}
	
	private static Holder<PlacedFeature> createSimplePlacedFeature(Block block, List<PlacementModifier> modifiers) {
		return Holder.direct(new PlacedFeature(
				Holder.direct(new ConfiguredFeature<>(
						Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(SimpleStateProvider.simple(block))
				)),
				modifiers
		));
	}

	private static Holder<PlacedFeature> createSimplePlacedFeature(Supplier<Block> blockSupplier, List<PlacementModifier> modifiers) {
		return createSimplePlacedFeature(blockSupplier.get(), modifiers);
	}

	public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> ctx) {
		var configuredFeatureLookup = ctx.lookup(Registries.CONFIGURED_FEATURE);
		ctx.register(PATCH_WILD_CABBAGES, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_CABBAGES),
				30
		));
		ctx.register(PATCH_WILD_ONIONS, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_ONIONS),
				120
		));
		ctx.register(PATCH_WILD_TOMATOES, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_TOMATOES),
				100
		));
		ctx.register(PATCH_WILD_CARROTS, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_CARROTS),
				120
		));
		ctx.register(PATCH_WILD_POTATOES, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_POTATOES),
				100
		));
		ctx.register(PATCH_WILD_BEETROOTS, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_BEETROOTS),
				30
		));
		ctx.register(PATCH_WILD_RICE, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_WILD_RICE),
				20
		));
		ctx.register(PATCH_BROWN_MUSHROOM_COLONIES, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_BROWN_MUSHROOM_COLONIES),
				15
		));
		ctx.register(PATCH_RED_MUSHROOM_COLONIES, createPatchPlacedFeature(
				configuredFeatureLookup.getOrThrow(FEATURE_PATCH_RED_MUSHROOM_COLONIES),
				15
		));
	}
	
	private static PlacedFeature createPatchPlacedFeature(Holder<ConfiguredFeature<?, ?>> configuredFeature, int rarity) {
		return new PlacedFeature(
				configuredFeature,
				List.of(
						RarityFilter.onAverageOnceEvery(rarity),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
						BiomeFilter.biome(),
						BiomeTagFilter.biomeIsInTag(BiomeTags.IS_OVERWORLD)
				)
		);
	}

}
