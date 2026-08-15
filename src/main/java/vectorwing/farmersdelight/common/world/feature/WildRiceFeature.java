package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import vectorwing.farmersdelight.common.block.WildRiceBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.world.configuration.WildRiceConfiguration;

public class WildRiceFeature extends Feature<WildRiceConfiguration>
{
	public WildRiceFeature(Codec<WildRiceConfiguration> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<WildRiceConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		WildRiceConfiguration config = context.config();
		RandomSource random = context.random();

		BlockPos blockpos = level.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, origin);

		int i = 0;
		BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

		for (int j = 0; j < config.tries(); ++j) {
			blockpos$mutable.set(blockpos).move(
					random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1),
					random.nextInt(config.ySpread() + 1) - random.nextInt(config.ySpread() + 1),
					random.nextInt(config.xzSpread() + 1) - random.nextInt(config.xzSpread() + 1));

			if (level.getBlockState(blockpos$mutable).getBlock() == Blocks.WATER && level.getBlockState(blockpos$mutable.above()).getBlock() == Blocks.AIR) {
				BlockState bottomRiceState = ModBlocks.WILD_RICE.get().defaultBlockState().setValue(WildRiceBlock.HALF, DoubleBlockHalf.LOWER);
				if (bottomRiceState.canSurvive(level, blockpos$mutable)) {
					DoublePlantBlock.placeAt(level, bottomRiceState, blockpos$mutable, 2);
					++i;
				}
			}
		}

		return i > 0;
	}
}
