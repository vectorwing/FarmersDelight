package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@SuppressWarnings("deprecation")
public class RopeBlock extends IronBarsBlock
{
	public static final BooleanProperty TIED_TO_BELL = BooleanProperty.create("tied_to_bell");

	public RopeBlock() {
		super(Properties.of(Material.CLOTH_DECORATION).noCollission().noOcclusion().strength(0.2F).sound(SoundType.WOOL));
		this.registerDefaultState(this.stateDefinition.any().setValue(TIED_TO_BELL, false));
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
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
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (player.getItemInHand(handIn).isEmpty()) {
			BlockPos.MutableBlockPos blockpos$mutable = pos.mutable().move(Direction.UP);

			for (int i = 0; i < 24; i++) {
				BlockState blockStateAbove = worldIn.getBlockState(blockpos$mutable);
				Block blockAbove = blockStateAbove.getBlock();
				if (blockAbove == Blocks.BELL) {
					((BellBlock) blockAbove).attemptToRing(worldIn, blockpos$mutable, blockStateAbove.getValue(BellBlock.FACING).getClockWise());
					return InteractionResult.SUCCESS;
				} else if (blockAbove == ModBlocks.ROPE.get()) {
					blockpos$mutable.move(Direction.UP);
				} else {
					return InteractionResult.PASS;
				}
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return useContext.getItemInHand().getItem() == this.asItem();
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		boolean tiedToBell = stateIn.getValue(TIED_TO_BELL);
		if (facing == Direction.UP) {
			tiedToBell = worldIn.getBlockState(facingPos).getBlock() == Blocks.BELL;
		}

		return facing.getAxis().isHorizontal()
				? stateIn.setValue(TIED_TO_BELL, tiedToBell).setValue(PROPERTY_BY_DIRECTION.get(facing), this.attachsTo(facingState, facingState.isFaceSturdy(worldIn, facingPos, facing.getOpposite())))
				: super.updateShape(stateIn.setValue(TIED_TO_BELL, tiedToBell), facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, TIED_TO_BELL);
	}

	@Override
	public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, net.minecraft.world.entity.LivingEntity entity) {
		return true;
	}
}