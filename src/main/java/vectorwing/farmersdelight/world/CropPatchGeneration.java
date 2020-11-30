package vectorwing.farmersdelight.world;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.setup.Configuration;

public class CropPatchGeneration {
	public static final BlockClusterFeatureConfig CABBAGE_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_CABBAGES.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.SAND.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig ONION_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_ONIONS.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig TOMATO_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_TOMATOES.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock(), Blocks.RED_SAND.getBlock(), Blocks.SAND.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig CARROT_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_CARROTS.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig POTATO_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_POTATOES.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig BEETROOT_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_BEETROOTS.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(2).zSpread(2).whitelist(ImmutableSet.of(Blocks.SAND.getBlock())).func_227317_b_().build();
	public static final BlockClusterFeatureConfig RICE_PATCH_CONFIG = (new BlockClusterFeatureConfig.Builder(
			new SimpleBlockStateProvider(ModBlocks.WILD_RICE.get().getDefaultState()), new SimpleBlockPlacer())).tries(64).xSpread(4).zSpread(4).whitelist(ImmutableSet.of(Blocks.DIRT.getBlock())).func_227317_b_().build();

	public static final ConfiguredFeature<?, ?> PATCH_WILD_CABBAGES = register("patch_wild_cabbages", Feature.RANDOM_PATCH.withConfiguration(CABBAGE_PATCH_CONFIG)
			.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT.func_242729_a(Configuration.CHANCE_WILD_CABBAGES.get())));
	public static final ConfiguredFeature<?, ?> PATCH_WILD_ONIONS = register("patch_wild_onions", Feature.RANDOM_PATCH.withConfiguration(ONION_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT.func_242729_a(Configuration.CHANCE_WILD_ONIONS.get())));
	public static final ConfiguredFeature<?, ?> PATCH_WILD_TOMATOES = register("patch_wild_tomatoes", Feature.RANDOM_PATCH.withConfiguration(TOMATO_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT.func_242729_a(Configuration.CHANCE_WILD_TOMATOES.get())));
	public static final ConfiguredFeature<?, ?> PATCH_WILD_CARROTS = register("patch_wild_carrots", Feature.RANDOM_PATCH.withConfiguration(CARROT_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT.func_242729_a(Configuration.CHANCE_WILD_CARROTS.get())));
	public static final ConfiguredFeature<?, ?> PATCH_WILD_POTATOES = register("patch_wild_potatoes", Feature.RANDOM_PATCH.withConfiguration(POTATO_PATCH_CONFIG)
			.withPlacement(Features.Placements.PATCH_PLACEMENT.func_242729_a(Configuration.CHANCE_WILD_POTATOES.get())));
	public static final ConfiguredFeature<?, ?> PATCH_WILD_BEETROOTS = register("patch_wild_beetroots", Feature.RANDOM_PATCH.withConfiguration(BEETROOT_PATCH_CONFIG)
			.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242729_a(Configuration.CHANCE_WILD_BEETROOTS.get()));
	public static final ConfiguredFeature<?, ?> PATCH_WILD_RICE = register("patch_wild_rice", ModBiomeFeatures.RICE.get().withConfiguration(RICE_PATCH_CONFIG)
			.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).func_242729_a(Configuration.CHANCE_WILD_RICE.get()));

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, key, configuredFeature);
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.Climate climate = event.getClimate();

		if (event.getName().getPath().equals("beach")) {
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_CABBAGES);
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_BEETROOTS);
		}

		if (event.getCategory().equals(Biome.Category.SWAMP) || event.getCategory().equals(Biome.Category.JUNGLE)) {
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_RICE);
		}

		if (climate.temperature >= 1.0F) {
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_TOMATOES);
		}

		if (climate.temperature > 0.3F && climate.temperature < 1.0F) {
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_CARROTS);
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_ONIONS);
		}

		if (climate.temperature > 0.0F && climate.temperature <= 0.3F) {
			builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_WILD_POTATOES);
		}
	}
}
