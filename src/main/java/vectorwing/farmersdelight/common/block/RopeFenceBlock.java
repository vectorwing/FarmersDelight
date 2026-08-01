package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class RopeFenceBlock extends CrossCollisionBlock
{
	public static final MapCodec<RopeFenceBlock> CODEC = simpleCodec(RopeFenceBlock::new);

	public static final VoxelShape POST = Block.box(7.0F, 0.0F, 7.0F, 9.0F, 16.0F, 9.0F);
	public static final VoxelShape NORTH_SIDE = Block.box(7.0F, 4.0F, 0.0F, 9.0F, 14.0F, 9.0F);
	public static final VoxelShape EAST_SIDE = Block.box(7.0F, 4.0F, 7.0F, 16.0F, 14.0F, 9.0F);
	public static final VoxelShape SOUTH_SIDE = Block.box(7.0F, 4.0F, 7.0F, 9.0F, 14.0F, 16.0F);
	public static final VoxelShape WEST_SIDE = Block.box(0.0F, 4.0F, 7.0F, 9.0F, 14.0F, 9.0F);

	@Override
	protected MapCodec<? extends CrossCollisionBlock> codec() {
		return CODEC;
	}

	public RopeFenceBlock(Properties properties) {
		super(2.0F, 16.0F, 2.0F, 14.0F, 24.0F, properties);
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(NORTH, false)
			.setValue(EAST, false)
			.setValue(SOUTH, false)
			.setValue(WEST, false)
			.setValue(WATERLOGGED, false));
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.isClientSide()) {
			return stack.is(Items.LEAD) ? InteractionResult.SUCCESS : InteractionResult.PASS;
		} else {
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		return !level.isClientSide() ? LeadItem.bindPlayerMobs(player, level, pos) : InteractionResult.PASS;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		VoxelShape shape = POST;
		if (state.getValue(NORTH)) {
			shape = Shapes.or(shape, NORTH_SIDE);
		}
		if (state.getValue(EAST)) {
			shape = Shapes.or(shape, EAST_SIDE);
		}
		if (state.getValue(SOUTH)) {
			shape = Shapes.or(shape, SOUTH_SIDE);
		}
		if (state.getValue(WEST)) {
			shape = Shapes.or(shape, WEST_SIDE);
		}
		return shape;
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.getShape(state, level, pos, context);
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState state) {
		return POST;
	}

	public boolean connectsTo(BlockState state, boolean isSideSolid, Direction direction) {
		boolean isRopeFenceGate = state.is(ModBlocks.ROPE_FENCE_GATE.get()) && FenceGateBlock.connectsToDirection(state, direction);
		return !isExceptionForConnection(state) && isSideSolid || this.isSameFence(state) || isRopeFenceGate;
	}

	protected boolean isSameFence(BlockState state) {
		return state.is(ModBlocks.ROPE_FENCE.get());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
		BlockPos northPos = pos.north();
		BlockPos eastPos = pos.east();
		BlockPos southPos = pos.south();
		BlockPos westPos = pos.west();
		BlockState northState = level.getBlockState(northPos);
		BlockState eastState = level.getBlockState(eastPos);
		BlockState southState = level.getBlockState(southPos);
		BlockState westState = level.getBlockState(westPos);
		return super.getStateForPlacement(context)
			.setValue(NORTH, this.connectsTo(northState, northState.isFaceSturdy(level, northPos, Direction.SOUTH), Direction.SOUTH))
			.setValue(EAST, this.connectsTo(eastState, eastState.isFaceSturdy(level, eastPos, Direction.WEST), Direction.WEST))
			.setValue(SOUTH, this.connectsTo(southState, southState.isFaceSturdy(level, southPos, Direction.NORTH), Direction.NORTH))
			.setValue(WEST, this.connectsTo(westState, westState.isFaceSturdy(level, westPos, Direction.EAST), Direction.EAST))
			.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL
			? state.setValue(PROPERTY_BY_DIRECTION.get(facing), this.connectsTo(facingState, facingState.isFaceSturdy(level, facingPos, facing.getOpposite()), facing.getOpposite()))
			: super.updateShape(state, level, scheduledTickAccess, currentPos, facing, facingPos, facingState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}
}
