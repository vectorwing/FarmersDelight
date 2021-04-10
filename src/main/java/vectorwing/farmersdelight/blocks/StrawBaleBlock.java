package vectorwing.farmersdelight.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.HayBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class StrawBaleBlock extends HayBlock
{
	public StrawBaleBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 20;
	}
}
