package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import vectorwing.farmersdelight.common.world.configuration.WildCropConfiguration;

public class WildCropFeature extends Feature<WildCropConfiguration>
{
	public WildCropFeature(Codec<WildCropConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<WildCropConfiguration> context) {
		WildCropConfiguration config = context.config();
		BlockPos origin = context.origin();
		WorldGenLevel level = context.level();
		RandomSource random = context.random();

		int i = 0;
		int tries = config.tries();
		int xzSpread = config.xzSpread() + 1;
		int ySpread = config.ySpread() + 1;

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		Holder<PlacedFeature> floorFeature = config.floorFeature();
		if (floorFeature != null) {
			for (int j = 0; j < tries; ++j) {
				mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
				if (config.floorFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
					++i;
				}
			}
		}

		for (int k = 0; k < tries; ++k) {
			int shorterXZ = xzSpread - 2;
			mutablePos.setWithOffset(origin, random.nextInt(shorterXZ) - random.nextInt(shorterXZ), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(shorterXZ) - random.nextInt(shorterXZ));
			if (config.primaryFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
				++i;
			}
		}

		for (int l = 0; l < tries; ++l) {
			mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
			if (config.secondaryFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
				++i;
			}
		}

		return i > 0;
	}
}
