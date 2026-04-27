package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HayBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StrawBaleBlock extends HayBlock
{
	public StrawBaleBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
		return 20;
	}
}
