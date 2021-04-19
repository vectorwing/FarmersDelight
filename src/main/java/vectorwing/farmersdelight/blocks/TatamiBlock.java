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
		super(Block.Properties.from(Blocks.WHITE_WOOL));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.DOWN).with(PAIRED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction face = context.getFace();
		BlockPos targetPos = context.getPos().offset(face.getOpposite());
		BlockState targetState = context.getWorld().getBlockState(targetPos);
		boolean pairing = false;

		if (context.getPlayer() != null && !context.getPlayer().isSneaking() && targetState.getBlock() == this && !targetState.get(PAIRED)) {
			pairing = true;
		}

		return this.getDefaultState().with(FACING, context.getFace().getOpposite()).with(PAIRED, pairing);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote) {
			if (placer != null && placer.isSneaking()) {
				return;
			}
			BlockPos facingPos = pos.offset(state.get(FACING));
			BlockState facingState = worldIn.getBlockState(facingPos);
			if (facingState.getBlock() == this && !facingState.get(PAIRED)) {
				worldIn.setBlockState(facingPos, state.with(FACING, state.get(FACING).getOpposite()).with(PAIRED, true), 3);
				worldIn.updateBlock(pos, Blocks.AIR);
				state.updateNeighbours(worldIn, pos, 3);
			}
		}
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing.equals(stateIn.get(FACING)) && stateIn.get(PAIRED) && worldIn.getBlockState(facingPos).getBlock() != this) {
			return stateIn.with(PAIRED, false);
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, PAIRED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
}
