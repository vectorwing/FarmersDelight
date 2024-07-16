package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@SuppressWarnings("deprecation")
public class RopeBlock extends IronBarsBlock
{
	public static final BooleanProperty TIED_TO_BELL = BooleanProperty.create("tied_to_bell");
	protected static final VoxelShape LOWER_SUPPORT_AABB = Block.box(7, 0, 7, 9, 1, 9);

	public RopeBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(NORTH, false)
				.setValue(SOUTH, false)
				.setValue(EAST, false)
				.setValue(WEST, false)
				.setValue(TIED_TO_BELL, false)
				.setValue(WATERLOGGED, false)
		);
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockGetter world = context.getLevel();
		BlockPos posAbove = context.getClickedPos().above();
		BlockState state = super.getStateForPlacement(context);
		return state != null ? state.setValue(TIED_TO_BELL, world.getBlockState(posAbove).getBlock() == Blocks.BELL) : null;
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (Configuration.ENABLE_ROPE_REELING.get() && player.isSecondaryUseActive()) {
			if (player.getAbilities().mayBuild && (player.getAbilities().instabuild || player.getInventory().add(new ItemStack(this.asItem())))) {
				BlockPos.MutableBlockPos reelingPos = pos.mutable().move(Direction.DOWN);
				int minBuildHeight = level.getMinBuildHeight();

				while (reelingPos.getY() >= minBuildHeight) {
					BlockState blockStateBelow = level.getBlockState(reelingPos);
					if (blockStateBelow.is(this)) {
						reelingPos.move(Direction.DOWN);
					} else {
						reelingPos.move(Direction.UP);
						level.destroyBlock(reelingPos, false, player);
						return InteractionResult.sidedSuccess(level.isClientSide);
					}
				}
			}
		} else {
			BlockPos.MutableBlockPos bellRingingPos = pos.mutable().move(Direction.UP);

			for (int i = 0; i < 24; i++) {
				BlockState blockStateAbove = level.getBlockState(bellRingingPos);
				Block blockAbove = blockStateAbove.getBlock();
				if (blockAbove == Blocks.BELL) {
					((BellBlock) blockAbove).attemptToRing(level, bellRingingPos, blockStateAbove.getValue(BellBlock.FACING).getClockWise());
					return InteractionResult.SUCCESS;
				} else if (blockAbove == ModBlocks.ROPE.get()) {
					bellRingingPos.move(Direction.UP);
				} else {
					return InteractionResult.PASS;
				}
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
		return LOWER_SUPPORT_AABB;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return useContext.getItemInHand().getItem() == this.asItem();
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		boolean tiedToBell = state.getValue(TIED_TO_BELL);
		if (facing == Direction.UP) {
			tiedToBell = level.getBlockState(facingPos).getBlock() == Blocks.BELL;
		}

		return facing.getAxis().isHorizontal()
				? state.setValue(TIED_TO_BELL, tiedToBell).setValue(PROPERTY_BY_DIRECTION.get(facing), this.attachsTo(facingState, facingState.isFaceSturdy(level, facingPos, facing.getOpposite())))
				: super.updateShape(state.setValue(TIED_TO_BELL, tiedToBell), facing, facingState, level, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, TIED_TO_BELL);
	}
}