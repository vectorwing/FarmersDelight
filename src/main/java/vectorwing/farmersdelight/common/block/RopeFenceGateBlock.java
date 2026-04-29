package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class RopeFenceGateBlock extends FenceGateBlock
{
	protected static final VoxelShape Z_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 16.0D, 14.0D, 9.0D);
	protected static final VoxelShape X_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 14.0D, 16.0D);
	protected static final VoxelShape Z_COLLISION_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 16.0D, 24.0D, 9.0D);
	protected static final VoxelShape X_COLLISION_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 24.0D, 16.0D);

	public RopeFenceGateBlock(Properties props) {
		super(props, ModSounds.BLOCK_ROPE_FENCE_GATE_OPEN.get(), ModSounds.BLOCK_ROPE_FENCE_GATE_CLOSE.get());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		Direction.Axis axis = facing.getAxis();
		if (state.getValue(FACING).getClockWise().getAxis() != axis) {
			return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
		} else {
			boolean isBorderedByWalls = this.isWall(facingState) && this.isWall(level.getBlockState(currentPos.relative(facing.getOpposite())));
			return state.setValue(IN_WALL, isBorderedByWalls);
		}
	}

	protected boolean isWall(BlockState state) {
		return state.is(BlockTags.WALLS);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(OPEN)) {
			return Shapes.empty();
		} else {
			return state.getValue(FACING).getAxis() == Direction.Axis.Z ? Z_COLLISION_SHAPE : X_COLLISION_SHAPE;
		}
	}
}
