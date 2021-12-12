package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class TatamiMatBlock extends HorizontalDirectionalBlock
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PART);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == getDirectionToOther(stateIn.getValue(PART), stateIn.getValue(FACING))) {
			return stateIn.canSurvive(worldIn, currentPos) && facingState.is(this) && facingState.getValue(PART) != stateIn.getValue(PART) ? stateIn : Blocks.AIR.defaultBlockState();
		} else {
			return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return !worldIn.isEmptyBlock(pos.below());
	}

	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
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
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getHorizontalDirection();
		BlockPos pos = context.getClickedPos();
		BlockPos pairPos = pos.relative(facing);
		return context.getLevel().getBlockState(pairPos).canBeReplaced(context) ? this.defaultBlockState().setValue(FACING, facing) : null;
	}
}
