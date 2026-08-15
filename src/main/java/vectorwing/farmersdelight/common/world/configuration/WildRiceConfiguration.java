package vectorwing.farmersdelight.common.world.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record WildRiceConfiguration(int tries, int xzSpread, int ySpread) implements FeatureConfiguration
{
	public static final Codec<WildRiceConfiguration> CODEC = RecordCodecBuilder.create(config -> config.group(
			ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(64).forGetter(WildRiceConfiguration::tries),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(4).forGetter(WildRiceConfiguration::xzSpread),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(WildRiceConfiguration::ySpread)
	).apply(config, WildRiceConfiguration::new));
}
