//package vectorwing.farmersdelight.common.world;
//
//import com.google.common.collect.ImmutableSet;
//import net.minecraft.core.Registry;
//import net.minecraft.data.BuiltinRegistries;
//import net.minecraft.data.worldgen.Features;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//import net.minecraft.world.level.levelgen.feature.Feature;
//import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
//import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
//import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
//import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
//import vectorwing.farmersdelight.common.registry.ModBiomeFeatures;
//import vectorwing.farmersdelight.common.registry.ModBlocks;
//import vectorwing.farmersdelight.common.Configuration;
//
//public class WildCropGeneration
//{
//	public static final RandomPatchConfiguration CABBAGE_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_CABBAGES.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.SAND)).noProjection().build();
//	public static final RandomPatchConfiguration ONION_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_ONIONS.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).noProjection().build();
//	public static final RandomPatchConfiguration TOMATO_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_TOMATOES.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, Blocks.RED_SAND, Blocks.SAND)).noProjection().build();
//	public static final RandomPatchConfiguration CARROT_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_CARROTS.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).noProjection().build();
//	public static final RandomPatchConfiguration POTATO_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_POTATOES.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).noProjection().build();
//	public static final RandomPatchConfiguration BEETROOT_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_BEETROOTS.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(2).zspread(2).whitelist(ImmutableSet.of(Blocks.SAND)).noProjection().build();
//	public static final RandomPatchConfiguration RICE_PATCH_CONFIG = (new RandomPatchConfiguration.GrassConfigurationBuilder(
//			new SimpleStateProvider(ModBlocks.WILD_RICE.get().defaultBlockState()), new SimpleBlockPlacer())).tries(64).xspread(4).zspread(4).whitelist(ImmutableSet.of(Blocks.DIRT)).noProjection().build();
//
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_CABBAGES = Feature.RANDOM_PATCH.configured(CABBAGE_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(Configuration.CHANCE_WILD_CABBAGES.get());
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_ONIONS = Feature.RANDOM_PATCH.configured(ONION_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(Configuration.CHANCE_WILD_ONIONS.get());
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_TOMATOES = Feature.RANDOM_PATCH.configured(TOMATO_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(Configuration.CHANCE_WILD_TOMATOES.get());
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_CARROTS = Feature.RANDOM_PATCH.configured(CARROT_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(Configuration.CHANCE_WILD_CARROTS.get());
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_POTATOES = Feature.RANDOM_PATCH.configured(POTATO_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_DOUBLE_SQUARE).rarity(Configuration.CHANCE_WILD_POTATOES.get());
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_BEETROOTS = Feature.RANDOM_PATCH.configured(BEETROOT_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(Configuration.CHANCE_WILD_BEETROOTS.get());
//	public static final ConfiguredFeature<?, ?> PATCH_WILD_RICE = ModBiomeFeatures.WILD_RICE.get().configured(RICE_PATCH_CONFIG)
//			.decorated(Features.Decorators.HEIGHTMAP_SQUARE).rarity(Configuration.CHANCE_WILD_RICE.get());
//
//	private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
//		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key, configuredFeature);
//	}
//
//	public static void registerConfiguredFeatures() {
//		register("patch_wild_cabbages", PATCH_WILD_CABBAGES);
//		register("patch_wild_onions", PATCH_WILD_ONIONS);
//		register("patch_wild_tomatoes", PATCH_WILD_TOMATOES);
//		register("patch_wild_carrots", PATCH_WILD_CARROTS);
//		register("patch_wild_potatoes", PATCH_WILD_POTATOES);
//		register("patch_wild_beetroots", PATCH_WILD_BEETROOTS);
//		register("patch_wild_rice", PATCH_WILD_RICE);
//	}
//}
