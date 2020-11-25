package vectorwing.farmersdelight.world.features;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import vectorwing.farmersdelight.blocks.WildRiceBlock;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.Random;
import java.util.function.Function;

public class RiceCropFeature extends Feature<BlockClusterFeatureConfig> {
	public RiceCropFeature(Function<Dynamic<?>, ? extends BlockClusterFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
		BlockPos blockpos = worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos);;

		int i = 0;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for(int j = 0; j < config.tryCount; ++j) {
			blockpos$mutable.setPos(blockpos).move(
					rand.nextInt(config.xSpread + 1) - rand.nextInt(config.xSpread + 1),
					rand.nextInt(config.ySpread + 1) - rand.nextInt(config.ySpread + 1),
					rand.nextInt(config.zSpread + 1) - rand.nextInt(config.zSpread + 1));

			if (worldIn.getBlockState(blockpos$mutable).getBlock() == Blocks.WATER && worldIn.getBlockState(blockpos$mutable.up()).getBlock() == Blocks.AIR) {
				BlockState bottomRiceState = ModBlocks.WILD_RICE.get().getDefaultState().with(WildRiceBlock.HALF, DoubleBlockHalf.LOWER);
				if (bottomRiceState.isValidPosition(worldIn, blockpos$mutable)) {
					((WildRiceBlock)bottomRiceState.getBlock()).placeAt(worldIn, blockpos$mutable, 2);
					++i;
				}
			}
		}

		return i > 0;
	}
}
