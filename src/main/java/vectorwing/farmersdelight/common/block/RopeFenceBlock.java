package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class RopeFenceBlock extends CrossCollisionBlock
{
	public static final MapCodec<RopeFenceBlock> CODEC = simpleCodec(RopeFenceBlock::new);

	public static final VoxelShape POST = Block.box(7.0F, 0.0F, 7.0F, 9.0F, 16.0F, 9.0F);

	@Override
	protected MapCodec<? extends CrossCollisionBlock> codec() {
		return CODEC;
	}

	public RopeFenceBlock(Properties properties) {
		super(1.0F, 1.0F, 16.0F, 16.0F, 24.0F, properties);
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(NORTH, false)
			.setValue(EAST, false)
			.setValue(SOUTH, false)
			.setValue(WEST, false)
			.setValue(WATERLOGGED, false));
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return stack.is(Items.LEAD) ? ItemInteractionResult.SUCCESS : ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		} else {
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		return !level.isClientSide() ? LeadItem.bindPlayerMobs(player, level, pos) : InteractionResult.PASS;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return POST;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
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
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL
			? state.setValue(PROPERTY_BY_DIRECTION.get(facing), this.connectsTo(facingState, facingState.isFaceSturdy(level, facingPos, facing.getOpposite()), facing.getOpposite()))
			: super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
	}
}
