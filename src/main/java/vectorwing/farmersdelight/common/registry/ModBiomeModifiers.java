package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.world.WildCropGeneration;
import vectorwing.farmersdelight.common.world.modifier.AddFeaturesByFilterBiomeModifier;

import java.util.Optional;

public class ModBiomeModifiers
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, FarmersDelight.MODID);

	public static final RegistryObject<Codec<AddFeaturesByFilterBiomeModifier>> ADD_FEATURES_BY_FILTER = BIOME_MODIFIER_SERIALIZERS.register("add_features_by_filter", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(AddFeaturesByFilterBiomeModifier::allowedBiomes),
					Biome.LIST_CODEC.optionalFieldOf("denied_biomes").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::deniedBiomes),
					Codec.FLOAT.optionalFieldOf("min_temperature").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::minimumTemperature),
					Codec.FLOAT.optionalFieldOf("max_temperature").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::maximumTemperature),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesByFilterBiomeModifier::features),
					GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesByFilterBiomeModifier::step)
			).apply(builder, AddFeaturesByFilterBiomeModifier::new)));

	public static final ResourceKey<BiomeModifier> BROWN_MUSHROOM_COLONY = registerKey("brown_mushroom_colony");
	public static final ResourceKey<BiomeModifier> RED_MUSHROOM_COLONY = registerKey("red_mushroom_colony");
	public static final ResourceKey<BiomeModifier> WILD_BEETROOTS = registerKey("wild_beetroots");
	public static final ResourceKey<BiomeModifier> WILD_CABBAGES = registerKey("wild_cabbages");
	public static final ResourceKey<BiomeModifier> WILD_CARROTS = registerKey("wild_carrots");
	public static final ResourceKey<BiomeModifier> WILD_ONIONS = registerKey("wild_onions");
	public static final ResourceKey<BiomeModifier> WILD_POTATOES = registerKey("wild_potatoes");
	public static final ResourceKey<BiomeModifier> WILD_RICE = registerKey("wild_rice");
	public static final ResourceKey<BiomeModifier> WILD_TOMATOES = registerKey("wild_tomatoes");

	private static ResourceKey<BiomeModifier> registerKey(String name) {
		return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, name));
	}

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);
		context.register(BROWN_MUSHROOM_COLONY, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(Tags.Biomes.IS_MUSHROOM), HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_BROWN_MUSHROOM_COLONY)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(RED_MUSHROOM_COLONY, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(Tags.Biomes.IS_MUSHROOM), HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_RED_MUSHROOM_COLONY)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_BEETROOTS, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(BiomeTags.IS_BEACH), HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_BEETROOTS)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_CABBAGES, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(BiomeTags.IS_BEACH), HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_CABBAGES)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_CARROTS, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD), HolderSet.direct(biomeGetter.getOrThrow(Biomes.LUSH_CAVES), biomeGetter.getOrThrow(Biomes.MUSHROOM_FIELDS)), 0.4F, 0.9F, HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_CARROTS)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_ONIONS, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD), biomeGetter.getOrThrow(Tags.Biomes.IS_UNDERGROUND), 0.1F, 0.3F, HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_ONIONS)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_POTATOES, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD), biomeGetter.getOrThrow(Tags.Biomes.IS_UNDERGROUND), 0.1F, 0.3F, HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_POTATOES)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_RICE, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(Tags.Biomes.IS_WET_OVERWORLD), Optional.of(biomeGetter.getOrThrow(Tags.Biomes.IS_UNDERGROUND)), Optional.empty(), Optional.empty(), HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_RICE)), GenerationStep.Decoration.VEGETAL_DECORATION));
		context.register(WILD_TOMATOES, new AddFeaturesByFilterBiomeModifier(biomeGetter.getOrThrow(Tags.Biomes.IS_HOT_OVERWORLD), Optional.of(biomeGetter.getOrThrow(Tags.Biomes.IS_WET)), Optional.empty(), Optional.empty(), HolderSet.direct(featureGetter.getOrThrow(WildCropGeneration.PLACED_WILD_TOMATOES)), GenerationStep.Decoration.VEGETAL_DECORATION));
	}
}
