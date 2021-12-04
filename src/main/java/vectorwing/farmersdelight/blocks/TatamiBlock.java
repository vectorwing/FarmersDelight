package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
	public BlockState getStateForPlacement(BlockItemUseContext context) {
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
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
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
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing.equals(stateIn.getValue(FACING)) && stateIn.getValue(PAIRED) && worldIn.getBlockState(facingPos).getBlock() != this) {
			return stateIn.setValue(PAIRED, false);
		}
		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
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
