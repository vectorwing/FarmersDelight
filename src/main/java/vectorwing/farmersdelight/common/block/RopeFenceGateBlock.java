package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class RopeFenceGateBlock extends FenceGateBlock
{
	public RopeFenceGateBlock(Properties props) {
		super(props, ModSounds.BLOCK_ROPE_FENCE_GATE_OPEN.get(), ModSounds.BLOCK_ROPE_FENCE_GATE_CLOSE.get());
	}

	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		Direction.Axis direction$axis = pFacing.getAxis();
		if (pState.getValue(FACING).getClockWise().getAxis() != direction$axis) {
			return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
		} else {
			boolean isBorderedByWalls = this.isWall(pFacingState) && this.isWall(pLevel.getBlockState(pCurrentPos.relative(pFacing.getOpposite())));
			return pState.setValue(IN_WALL, isBorderedByWalls);
		}
	}

	protected boolean isWall(BlockState pState) {
		return pState.is(BlockTags.WALLS);
	}
}
