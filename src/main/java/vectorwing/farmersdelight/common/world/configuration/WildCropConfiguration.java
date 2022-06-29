package vectorwing.farmersdelight.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.Optional;

public record WildCropConfiguration(Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature
) implements FeatureConfiguration
{
	public static final Codec<WildCropConfiguration> CODEC = RecordCodecBuilder.create((config) -> config.group(
			PlacedFeature.CODEC.fieldOf("primary_feature").forGetter(WildCropConfiguration::primaryFeature),
			PlacedFeature.CODEC.fieldOf("secondary_feature").forGetter(WildCropConfiguration::secondaryFeature),
			PlacedFeature.CODEC.optionalFieldOf("floor_feature").forGetter(floorConfig -> Optional.ofNullable(floorConfig.secondaryFeature))
	).apply(config, (primary, secondary, floor) -> floor.map(placedFeatureHolder -> new WildCropConfiguration(primary, secondary, placedFeatureHolder)).orElseGet(() -> new WildCropConfiguration(primary, secondary, null))));

	public WildCropConfiguration(Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature) {
		this.primaryFeature = primaryFeature;
		this.secondaryFeature = secondaryFeature;
		this.floorFeature = floorFeature;
	}

	public Holder<PlacedFeature> primaryFeature() {
		return this.primaryFeature;
	}

	public Holder<PlacedFeature> secondaryFeature() {
		return this.secondaryFeature;
	}

	public Holder<PlacedFeature> floorFeature() {
		return this.floorFeature;
	}
}
