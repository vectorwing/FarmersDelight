package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModSounds;
import net.minecraft.util.RandomSource;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class FeastBlock extends Block
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 4);

	public final Supplier<Item> servingItem;
	public final boolean hasLeftovers;
	public final boolean hasServingParticles;

	protected static final VoxelShape[] SHAPES = new VoxelShape[]{
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D),
	};

	/**
	 * This block provides up to 4 servings of food to players who interact with it.
	 * If a leftover item is specified, the block lingers at 0 servings, and is destroyed on right-click.
	 *
	 * @param properties   Block properties.
	 * @param servingItem  The meal to be served.
	 * @param hasLeftovers Whether the block remains when out of servings. If false, the block vanishes once it runs out.
	 */
	public FeastBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers, boolean hasServingParticles) {
		super(properties);
		this.servingItem = servingItem;
		this.hasLeftovers = hasLeftovers;
		this.hasServingParticles = hasServingParticles;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(getServingsProperty(), getMaxServings()));
	}

	public FeastBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		this(properties, servingItem, hasLeftovers, true);
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
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(SERVINGS)];
	}

	@Override
	protected InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide()) {
			if (this.takeServing(level, pos, state, player, hand).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
		}

		return this.takeServing(level, pos, state, player, hand);
	}

	protected InteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
		int servings = state.getValue(getServingsProperty());

		if (servings == 0) {
			level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
			level.destroyBlock(pos, true);
			return InteractionResult.SUCCESS;
		}

		ItemStack serving = this.getServingItem(state);
		ItemStack heldStack = player.getItemInHand(hand);

		if (servings > 0) {
			ItemStack craftingRemainingItem = ItemUtils.getCraftingRemainingItem(serving);
			if (craftingRemainingItem.isEmpty() || ItemStack.isSameItem(heldStack, craftingRemainingItem)) {
				level.setBlock(pos, state.setValue(getServingsProperty(), servings - 1), 3);
				player.awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
				if (!player.getAbilities().instabuild && !craftingRemainingItem.isEmpty()) {
					heldStack.shrink(1);
				}
				if (!player.getInventory().add(serving)) {
					player.drop(serving, false);
				}
				if (level.getBlockState(pos).getValue(getServingsProperty()) == 0 && !this.hasLeftovers) {
					level.destroyBlock(pos, true);
				}
				level.playSound(null, pos, ModSounds.BLOCK_FOOD_TAKE_PORTION.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
				if (hasServingParticles && level instanceof ServerLevel serverLevel) {
					serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.001D);
				}
				return InteractionResult.SUCCESS;
			} else {
				player.sendOverlayMessage(TextUtils.block("feast.use_container", craftingRemainingItem.getHoverName()));
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
		return facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, scheduledTickAccess, currentPos, facing, facingPos, facingState, random);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSupportRigidBlock(level, pos.below());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SERVINGS);
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return state.getValue(getServingsProperty());
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
