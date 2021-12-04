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
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

	public TatamiMatBlock() {
		super(Block.Properties.copy(Blocks.WHITE_WOOL).strength(0.3F));
		this.registerDefaultState(this.getStateDefinition().any().setValue(PART, BedPart.FOOT));
	}

	private static Direction getDirectionToOther(BedPart part, Direction direction) {
		return part == BedPart.FOOT ? direction : direction.getOpposite();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == getDirectionToOther(stateIn.getValue(PART), stateIn.getValue(FACING))) {
			return stateIn.canSurvive(worldIn, currentPos) && facingState.is(this) && facingState.getValue(PART) != stateIn.getValue(PART) ? stateIn : Blocks.AIR.defaultBlockState();
		} else {
			return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return !worldIn.isEmptyBlock(pos.below());
	}

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isClientSide && player.isCreative()) {
			BedPart part = state.getValue(PART);
			if (part == BedPart.FOOT) {
				BlockPos pairPos = pos.relative(getDirectionToOther(part, state.getValue(FACING)));
				BlockState pairState = worldIn.getBlockState(pairPos);
				if (pairState.getBlock() == this && pairState.getValue(PART) == BedPart.HEAD) {
					worldIn.setBlock(pairPos, Blocks.AIR.defaultBlockState(), 35);
					worldIn.levelEvent(player, 2001, pairPos, Block.getId(pairState));
				}
			}
		}

		super.playerWillDestroy(worldIn, pos, state, player);
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		if (!worldIn.isClientSide) {
			BlockPos facingPos = pos.relative(state.getValue(FACING));
			worldIn.setBlock(facingPos, state.setValue(PART, BedPart.HEAD), 3);
			worldIn.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(worldIn, pos, 3);
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction facing = context.getHorizontalDirection();
		BlockPos pos = context.getClickedPos();
		BlockPos pairPos = pos.relative(facing);
		return context.getLevel().getBlockState(pairPos).canBeReplaced(context) ? this.defaultBlockState().setValue(FACING, facing) : null;
	}
}
