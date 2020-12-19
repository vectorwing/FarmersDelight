package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class TatamiMatBlock extends HorizontalBlock
{
	public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

	public TatamiMatBlock() {
		super(Block.Properties.from(Blocks.WHITE_WOOL).hardnessAndResistance(0.3F));
		this.setDefaultState(this.getStateContainer().getBaseState().with(PART, BedPart.FOOT));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, PART);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == getDirectionToOther(stateIn.get(PART), stateIn.get(HORIZONTAL_FACING))) {
			return stateIn.isValidPosition(worldIn, currentPos) && facingState.isIn(this) && facingState.get(PART) != stateIn.get(PART) ? stateIn : Blocks.AIR.getDefaultState();
		} else {
			return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return !worldIn.isAirBlock(pos.down());
	}

	private static Direction getDirectionToOther(BedPart part, Direction direction) {
		return part == BedPart.FOOT ? direction : direction.getOpposite();
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote && player.isCreative()) {
			BedPart part = state.get(PART);
			if (part == BedPart.FOOT) {
				BlockPos pairPos = pos.offset(getDirectionToOther(part, state.get(HORIZONTAL_FACING)));
				BlockState pairState = worldIn.getBlockState(pairPos);
				if (pairState.getBlock() == this && pairState.get(PART) == BedPart.HEAD) {
					worldIn.setBlockState(pairPos, Blocks.AIR.getDefaultState(), 35);
					worldIn.playEvent(player, 2001, pairPos, Block.getStateId(pairState));
				}
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isRemote) {
			BlockPos facingPos = pos.offset(state.get(HORIZONTAL_FACING));
			worldIn.setBlockState(facingPos, state.with(PART, BedPart.HEAD), 3);
			worldIn.func_230547_a_(pos, Blocks.AIR);
			state.updateNeighbours(worldIn, pos, 3);
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction facing = context.getPlacementHorizontalFacing();
		BlockPos pos = context.getPos();
		BlockPos pairPos = pos.offset(facing);
		return context.getWorld().getBlockState(pairPos).isReplaceable(context) ? this.getDefaultState().with(HORIZONTAL_FACING, facing) : null;
	}
}
