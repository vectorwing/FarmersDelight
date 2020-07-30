package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import vectorwing.farmersdelight.init.ModBlocks;

import java.util.Random;

public class OrganicCompostBlock extends Block
{
	public OrganicCompostBlock()
	{
		super(Properties.from(Blocks.DIRT));
	}

	public OrganicCompostBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (worldIn.getRandom().nextFloat() <= 0.1F) {
			worldIn.setBlockState(pos, ModBlocks.MULCH.get().getDefaultState(), 2);
		}
	}
}
