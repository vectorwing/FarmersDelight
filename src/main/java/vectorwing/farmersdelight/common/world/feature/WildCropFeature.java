package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import vectorwing.farmersdelight.common.world.configuration.WildCropPatchConfiguration;

import java.util.Random;

public class WildCropFeature extends Feature<WildCropPatchConfiguration>
{
	public WildCropFeature(Codec<WildCropPatchConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<WildCropPatchConfiguration> context) {
		WildCropPatchConfiguration config = context.config();
		BlockPos origin = context.origin();
		WorldGenLevel level = context.level();
		Random random = context.random();

		int i = 0;

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (int j = 0; j < 64; ++j) {
			int xzSpread = 5;
			int ySpread = 4;	// TODO: Just put these in your config, mate...
			mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
			if (config.primaryFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
				++i;
			}
		}

		for (int k = 0; k < 64; ++k) {
			int xzSpread = 5;
			int ySpread = 4;	// TODO: Just put these in your config, mate...
			mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
			if (config.secondaryFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
				++i;
			}
		}

		for (int k = 0; k < 64; ++k) {
			int xzSpread = 5;
			int ySpread = 4;	// TODO: Just put these in your config, mate...
			mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
			if (config.floorFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
				++i;
			}
		}

		return i > 0;
	}
}
