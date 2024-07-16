package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(getServingsProperty(), getMaxServings()));
	}

	public IntegerProperty getServingsProperty() {
		return SERVINGS;
	}

	public int getMaxServings() {
		return 4;
	}

	public ItemStack getServingItem(BlockState state) {
		return new ItemStack(this.servingItem.get());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(SERVINGS)];
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			if (this.takeServing(level, pos, state, player, hand).consumesAction()) {
				return ItemInteractionResult.SUCCESS;
			}
		}

		return this.takeServing(level, pos, state, player, hand);
	}

	protected ItemInteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
		int servings = state.getValue(getServingsProperty());

		if (servings == 0) {
			level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
			level.destroyBlock(pos, true);
			return ItemInteractionResult.SUCCESS;
		}

		ItemStack serving = this.getServingItem(state);
		ItemStack heldStack = player.getItemInHand(hand);

		if (servings > 0) {
			if (!serving.hasCraftingRemainingItem() || ItemStack.isSameItem(heldStack, serving.getCraftingRemainingItem())) {
				level.setBlock(pos, state.setValue(getServingsProperty(), servings - 1), 3);
				if (!player.getAbilities().instabuild && serving.hasCraftingRemainingItem()) {
					heldStack.shrink(1);
				}
				if (!player.getInventory().add(serving)) {
					player.drop(serving, false);
				}
				if (level.getBlockState(pos).getValue(getServingsProperty()) == 0 && !this.hasLeftovers) {
					level.removeBlock(pos, false);
				}
				level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
				return ItemInteractionResult.SUCCESS;
			} else {
				player.displayClientMessage(TextUtils.getTranslation("block.feast.use_container", serving.getCraftingRemainingItem().getHoverName()), true);
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isSolid();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return blockState.getValue(getServingsProperty());
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
