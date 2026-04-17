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
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.ShapeUtils;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PieBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);

	protected static final VoxelShape[] SHAPES = {
			Block.box(2, 0, 2, 14, 4, 14),
			Shapes.join(Block.box(2, 0, 8, 8, 4, 14), Block.box(2, 0, 2, 14, 4, 8), BooleanOp.OR),
			Block.box(2, 0, 2, 14, 4, 8),
			Block.box(8, 0, 2, 14, 4, 8)
	};

	private static final VoxelShape[][] ROTATED_SHAPES = buildShapes();

	public final Supplier<Item> pieSlice;

	public PieBlock(Properties properties, Supplier<Item> pieSlice) {
		super(properties);
		this.pieSlice = pieSlice;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BITES, 0));
	}

	public ItemStack getPieSliceItem() {
		return new ItemStack(this.pieSlice.get());
	}

	public int getMaxBites() {
		return 4;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return ROTATED_SHAPES[state.getValue(BITES)][state.getValue(FACING).get2DDataValue()];
	}

	private static VoxelShape[][] buildShapes() {
		VoxelShape[][] result = new VoxelShape[SHAPES.length][4];
		for (int i = 0; i < SHAPES.length; i++) {
			Map<Direction, VoxelShape> rotated = ShapeUtils.getShapesRotatedFromNorth(SHAPES[i]);
			for (Map.Entry<Direction, VoxelShape> entry : rotated.entrySet()) {
				result[i][entry.getKey().get2DDataValue()] = entry.getValue();
			}
		}
		return result;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (ItemUtils.isKnife(heldStack)) {
			return cutSlice(level, pos, state, player, heldStack.getItem());
		}

		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			if (consumeBite(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}

			if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}

		return consumeBite(level, pos, state, player);
	}

	/**
	 * Eats a slice from the pie, feeding the player.
	 */
	protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			ItemStack sliceStack = this.getPieSliceItem();
			FoodProperties sliceFood = sliceStack.getItem().getFoodProperties(sliceStack, player);

			if (sliceFood != null) {
				player.getFoodData().eat(sliceFood);
				for (FoodProperties.PossibleEffect effect : sliceFood.effects()) {
					if (!level.isClientSide && effect != null && level.random.nextFloat() < effect.probability()) {
						player.addEffect(effect.effect());
					}
				}
			}

			int bites = state.getValue(BITES);
			if (bites < getMaxBites() - 1) {
				level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
			} else {
				level.removeBlock(pos, false);
			}
			level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.001D);
			}
			return InteractionResult.SUCCESS;
		}
	}

	/**
	 * Cuts off a bite and drops a slice item, without feeding the player.
	 */
	protected ItemInteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player, Item knife) {
		int bites = state.getValue(BITES);
		if (bites < getMaxBites() - 1) {
			level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
		} else {
			level.removeBlock(pos, false);
		}

		Direction direction = player.getDirection().getOpposite();
		ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
				direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
		level.playSound(null, pos, ModSounds.BLOCK_FOOD_SLICE.get(), SoundSource.PLAYERS, 0.8F, 0.8F);
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.001D);
		}
		player.awardStat(Stats.ITEM_USED.get(knife));

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSupportRigidBlock(level, pos.below());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, BITES);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return getMaxBites() - state.getValue(BITES);
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
