package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class TatamiBlock extends Block {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public TatamiBlock() {
		super(Block.Properties.from(Blocks.WHITE_WOOL));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.DOWN));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction face = context.getFace();
		BlockPos targetPos = context.getPos().offset(face.getOpposite());
		BlockState targetState = context.getWorld().getBlockState(targetPos);

		Direction direction;

		if (targetState.getBlock() == this && targetState.get(FACING) == face) {
			direction = face.getOpposite();
		} else {
			direction = context.getNearestLookingDirection().getOpposite();
		}

		return this.getDefaultState().with(FACING, direction);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
