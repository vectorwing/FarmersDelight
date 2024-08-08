package vectorwing.farmersdelight.common.world.modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import vectorwing.farmersdelight.common.registry.ModBiomeModifiers;

import java.util.Optional;

public record AddFeaturesByFilterBiomeModifier(
		HolderSet<Biome> allowedBiomes,
		Optional<HolderSet<Biome>> deniedBiomes,
		Optional<Float> minimumTemperature,
		Optional<Float> maximumTemperature,
		HolderSet<PlacedFeature> features,
		GenerationStep.Decoration step
) implements BiomeModifier
{

	public AddFeaturesByFilterBiomeModifier(HolderSet<Biome> allowedBiomes, HolderSet<Biome> deniedBiomes, float minTemp, float maxTemp, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) {
		this(allowedBiomes, Optional.of(deniedBiomes), Optional.of(minTemp), Optional.of(maxTemp), features, step);
	}

	public AddFeaturesByFilterBiomeModifier(HolderSet<Biome> allowedBiomes, HolderSet<PlacedFeature> features, GenerationStep.Decoration step) {
		this(allowedBiomes, Optional.empty(), Optional.empty(), Optional.empty(), features, step);
	}

	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD && this.allowedBiomes.contains(biome)) {
			if (deniedBiomes.isPresent() && this.deniedBiomes.get().contains(biome)) {
				return;
			}
			if (minimumTemperature.isPresent() && biome.get().getBaseTemperature() < minimumTemperature.get()) {
				return;
			}
			if (maximumTemperature.isPresent() && biome.get().getBaseTemperature() > maximumTemperature.get()) {
				return;
			}
			BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
			this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
		}
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return ModBiomeModifiers.ADD_FEATURES_BY_FILTER.get();
	}
}
