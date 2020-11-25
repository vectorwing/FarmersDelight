package vectorwing.farmersdelight.world;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.registry.ModBiomeFeatures;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.setup.Configuration;

public class CropPatchGeneration
{
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

	public static void generateCrop() {
		for (Biome biome : ForgeRegistries.BIOMES)
		{
			if (biome.getDefaultTemperature() >= 1.0F)
			{
				if (Configuration.GENERATE_WILD_TOMATOES.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
							.withConfiguration(TOMATO_PATCH_CONFIG)
							.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(Configuration.CHANCE_WILD_TOMATOES.get()))));
				}
			}
			if (biome == Biomes.BEACH)
			{
				if (Configuration.GENERATE_WILD_CABBAGES.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
							.withConfiguration(CABBAGE_PATCH_CONFIG)
							.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(Configuration.FREQUENCY_WILD_CABBAGES.get()))));
				}
				if (Configuration.GENERATE_WILD_BEETROOTS.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
							.withConfiguration(BEETROOT_PATCH_CONFIG)
							.withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(Configuration.FREQUENCY_WILD_BEETROOTS.get()))));
				}
			}
			if (biome == Biomes.SWAMP)
			{
				if (Configuration.GENERATE_WILD_RICE.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, ModBiomeFeatures.RICE.get()
							.withConfiguration(RICE_PATCH_CONFIG)
							.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(Configuration.CHANCE_WILD_RICE.get()))));
				}
			}
			if (biome.getDefaultTemperature() > 0.3 && biome.getDefaultTemperature() < 1.0) {
				if (Configuration.GENERATE_WILD_CARROTS.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
							.withConfiguration(CARROT_PATCH_CONFIG)
							.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(Configuration.CHANCE_WILD_CARROTS.get()))));
				}
				if (Configuration.GENERATE_WILD_ONIONS.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
							.withConfiguration(ONION_PATCH_CONFIG)
							.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(Configuration.CHANCE_WILD_ONIONS.get()))));
				}
			}
			if (biome.getDefaultTemperature() > 0.0 && biome.getDefaultTemperature() <= 0.3) {
				if (Configuration.GENERATE_WILD_POTATOES.get())
				{
					biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
							.withConfiguration(POTATO_PATCH_CONFIG)
							.withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(Configuration.CHANCE_WILD_POTATOES.get()))));
				}
			}
		}
	}
}
