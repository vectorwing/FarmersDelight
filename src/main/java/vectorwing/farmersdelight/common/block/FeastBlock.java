package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class FeastBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);
	public final Supplier<Item> servingItem;
	public final boolean hasLeftovers;

	protected static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
	};

	/**
	 * This block provides up to 4 servings of food to players who interact with it.
	 * If a leftover item is specified, the block lingers at 0 servings, and is destroyed on right-click.
	 *
	 * @param properties   Block properties.
	 * @param servingItem  The meal to be served.
	 * @param hasLeftovers Whether the block remains when out of servings. If false, the block vanishes once it runs out.
	 */
	public FeastBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties);
		this.servingItem = servingItem;
		this.hasLeftovers = hasLeftovers;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SERVINGS, 4));
	}

	public ItemStack getServingItem() {
		return new ItemStack(this.servingItem.get());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(SERVINGS)];
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (worldIn.isClientSide) {
			if (this.takeServing(worldIn, pos, state, player, handIn).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
		}

		return this.takeServing(worldIn, pos, state, player, handIn);
	}

	private InteractionResult takeServing(LevelAccessor worldIn, BlockPos pos, BlockState state, Player player, InteractionHand handIn) {
		int servings = state.getValue(SERVINGS);

		if (servings == 0) {
			worldIn.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
			worldIn.destroyBlock(pos, true);
			return InteractionResult.SUCCESS;
		}

		ItemStack serving = this.getServingItem();
		ItemStack heldStack = player.getItemInHand(handIn);

		if (servings > 0) {
			if (heldStack.sameItem(serving.getContainerItem())) {
				worldIn.setBlock(pos, state.setValue(SERVINGS, servings - 1), 3);
				if (!player.getAbilities().instabuild) {
					heldStack.shrink(1);
				}
				if (!player.getInventory().add(serving)) {
					player.drop(serving, false);
				}
				if (worldIn.getBlockState(pos).getValue(SERVINGS) == 0 && !this.hasLeftovers) {
					worldIn.removeBlock(pos, false);
				}
				worldIn.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			} else {
				player.displayClientMessage(TextUtils.getTranslation("block.feast.use_container", serving.getContainerItem().getHoverName()), true);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.below()).getMaterial().isSolid();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return blockState.getValue(SERVINGS);
	}

	public int getMaxServings() {
		return 4;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
		return false;
	}
}
