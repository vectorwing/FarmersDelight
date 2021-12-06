package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class TatamiBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty PAIRED = BooleanProperty.create("paired");

	public TatamiBlock() {
		super(Block.Properties.copy(Blocks.WHITE_WOOL));
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.DOWN).setValue(PAIRED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction face = context.getClickedFace();
		BlockPos targetPos = context.getClickedPos().relative(face.getOpposite());
		BlockState targetState = context.getLevel().getBlockState(targetPos);
		boolean pairing = false;

		if (context.getPlayer() != null && !context.getPlayer().isShiftKeyDown() && targetState.getBlock() == this && !targetState.getValue(PAIRED)) {
			pairing = true;
		}

		return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite()).setValue(PAIRED, pairing);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isClientSide) {
			if (placer != null && placer.isShiftKeyDown()) {
				return;
			}
			BlockPos facingPos = pos.relative(state.getValue(FACING));
			BlockState facingState = worldIn.getBlockState(facingPos);
			if (facingState.getBlock() == this && !facingState.getValue(PAIRED)) {
				worldIn.setBlock(facingPos, state.setValue(FACING, state.getValue(FACING).getOpposite()).setValue(PAIRED, true), 3);
				worldIn.blockUpdated(pos, Blocks.AIR);
				state.updateNeighbourShapes(worldIn, pos, 3);
			}
		}
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing.equals(stateIn.getValue(FACING)) && stateIn.getValue(PAIRED) && worldIn.getBlockState(facingPos).getBlock() != this) {
			return stateIn.setValue(PAIRED, false);
		}
		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PAIRED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
}
