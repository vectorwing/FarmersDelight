package vectorwing.farmersdelight.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import vectorwing.farmersdelight.blocks.WildRiceBlock;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.Random;

public class RiceCropFeature extends Feature<BlockClusterFeatureConfig>
{
	public RiceCropFeature(Codec<BlockClusterFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
		BlockPos blockpos = worldIn.getHeightmapPos(Heightmap.Type.OCEAN_FLOOR_WG, pos);

		int i = 0;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for (int j = 0; j < config.tries; ++j) {
			blockpos$mutable.set(blockpos).move(
					rand.nextInt(config.xspread + 1) - rand.nextInt(config.xspread + 1),
					rand.nextInt(config.yspread + 1) - rand.nextInt(config.yspread + 1),
					rand.nextInt(config.zspread + 1) - rand.nextInt(config.zspread + 1));

			if (worldIn.getBlockState(blockpos$mutable).getBlock() == Blocks.WATER && worldIn.getBlockState(blockpos$mutable.above()).getBlock() == Blocks.AIR) {
				BlockState bottomRiceState = ModBlocks.WILD_RICE.get().defaultBlockState().setValue(WildRiceBlock.HALF, DoubleBlockHalf.LOWER);
				if (bottomRiceState.canSurvive(worldIn, blockpos$mutable)) {
					((WildRiceBlock) bottomRiceState.getBlock()).placeAt(worldIn, blockpos$mutable, 2);
					++i;
				}
			}
		}

		return i > 0;
	}
}
