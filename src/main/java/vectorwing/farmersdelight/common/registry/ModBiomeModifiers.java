package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
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
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.world.WildCropGeneration;
import vectorwing.farmersdelight.common.world.modifier.AddFeaturesByFilterBiomeModifier;

import java.util.Optional;

public class ModBiomeModifiers
{
	public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, FarmersDelight.MODID);

	public static RegistryObject<Codec<AddFeaturesByFilterBiomeModifier>> ADD_FEATURES_BY_FILTER = BIOME_MODIFIER_SERIALIZERS.register("add_features_by_filter", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.fieldOf("allowed_biomes").forGetter(AddFeaturesByFilterBiomeModifier::allowedBiomes),
					Biome.LIST_CODEC.optionalFieldOf("denied_biomes").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::deniedBiomes),
					Codec.FLOAT.optionalFieldOf("min_temperature").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::minimumTemperature),
					Codec.FLOAT.optionalFieldOf("max_temperature").orElse(Optional.empty()).forGetter(AddFeaturesByFilterBiomeModifier::maximumTemperature),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesByFilterBiomeModifier::features),
					GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesByFilterBiomeModifier::step)
			).apply(builder, AddFeaturesByFilterBiomeModifier::new)));

	public static ResourceKey<BiomeModifier> WILD_CABBAGES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_cabbages"));
	public static ResourceKey<BiomeModifier> WILD_ONIONS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_onions"));
	public static ResourceKey<BiomeModifier> WILD_TOMATOES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_tomatoes"));
	public static ResourceKey<BiomeModifier> WILD_CARROTS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_carrots"));
	public static ResourceKey<BiomeModifier> WILD_POTATOES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_potatoes"));
	public static ResourceKey<BiomeModifier> WILD_BEETROOTS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_beetroots"));
	public static ResourceKey<BiomeModifier> WILD_RICE = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "wild_rice"));
	public static ResourceKey<BiomeModifier> BROWN_MUSHROOM_COLONIES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "brown_mushroom_colony"));
	public static ResourceKey<BiomeModifier> RED_MUSHROOM_COLONIES = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FarmersDelight.MODID, "red_mushroom_colony"));

	public static void bootstrapBiomeModifiers(BootstapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedFeatureGetter = context.lookup(Registries.PLACED_FEATURE);

		context.register(WILD_CABBAGES, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(BiomeTags.IS_BEACH),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_CABBAGES)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(WILD_ONIONS, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
				Optional.of(HolderSet.direct(
						biomeGetter.getOrThrow(Biomes.LUSH_CAVES),
						biomeGetter.getOrThrow(Biomes.MUSHROOM_FIELDS)
				)),
				Optional.of(0.4f),
				Optional.of(0.9f),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_ONIONS)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(WILD_TOMATOES, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(Tags.Biomes.IS_HOT_OVERWORLD),
				Optional.of(biomeGetter.getOrThrow(Tags.Biomes.IS_WET)),
				Optional.empty(),
				Optional.empty(),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_TOMATOES)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(WILD_CARROTS, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
				Optional.of(HolderSet.direct(
						biomeGetter.getOrThrow(Biomes.LUSH_CAVES),
						biomeGetter.getOrThrow(Biomes.MUSHROOM_FIELDS)
				)),
				Optional.of(0.4f),
				Optional.of(0.9f),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_CARROTS)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(WILD_POTATOES, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(BiomeTags.IS_OVERWORLD),
				Optional.of(biomeGetter.getOrThrow(Tags.Biomes.IS_UNDERGROUND)),
				Optional.of(0.1f),
				Optional.of(0.3f),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_POTATOES)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(WILD_BEETROOTS, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(BiomeTags.IS_BEACH),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_BEETROOTS)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(WILD_RICE, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(Tags.Biomes.IS_WET_OVERWORLD),
				Optional.of(biomeGetter.getOrThrow(Tags.Biomes.IS_UNDERGROUND)),
				Optional.empty(),
				Optional.empty(),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_WILD_RICE)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(BROWN_MUSHROOM_COLONIES, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(Tags.Biomes.IS_MUSHROOM),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_BROWN_MUSHROOM_COLONIES)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
		context.register(RED_MUSHROOM_COLONIES, new AddFeaturesByFilterBiomeModifier(
				biomeGetter.getOrThrow(Tags.Biomes.IS_MUSHROOM),
				Optional.empty(),
				Optional.empty(),
				Optional.empty(),
				HolderSet.direct(placedFeatureGetter.getOrThrow(WildCropGeneration.PATCH_RED_MUSHROOM_COLONIES)),
				GenerationStep.Decoration.VEGETAL_DECORATION
		));
	}

	private static BiomeModifier createSimpleModifier(Holder<Biome> biomeHolder, Holder<PlacedFeature> placedFeatureHolder) {
		return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomeHolder),
				HolderSet.direct(placedFeatureHolder),
				GenerationStep.Decoration.VEGETAL_DECORATION
		);
	}
}
