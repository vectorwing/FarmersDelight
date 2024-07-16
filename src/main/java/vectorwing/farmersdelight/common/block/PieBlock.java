package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PieBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);

	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);

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
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (heldStack.is(ModTags.KNIVES)) {
			return cutSlice(level, pos, state, player);
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
	protected InteractionResult consumeBite(Level level, BlockPos pos, BlockState state, Player playerIn) {
		if (!playerIn.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			ItemStack sliceStack = this.getPieSliceItem();
			FoodProperties sliceFood = sliceStack.getItem().getFoodProperties(sliceStack, playerIn);

			if (sliceFood != null) {
				playerIn.getFoodData().eat(sliceFood);
				for (FoodProperties.PossibleEffect effect : sliceFood.effects()) {
					if (!level.isClientSide && effect != null && level.random.nextFloat() < effect.probability()) {
						playerIn.addEffect(effect.effect());
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
			return InteractionResult.SUCCESS;
		}
	}

	/**
	 * Cuts off a bite and drops a slice item, without feeding the player.
	 */
	protected ItemInteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
		int bites = state.getValue(BITES);
		if (bites < getMaxBites() - 1) {
			level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
		} else {
			level.removeBlock(pos, false);
		}

		Direction direction = player.getDirection().getOpposite();
		ItemUtils.spawnItemEntity(level, this.getPieSliceItem(), pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
				direction.getStepX() * 0.15, 0.05, direction.getStepZ() * 0.15);
		level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
		return ItemInteractionResult.SUCCESS;
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
		builder.add(FACING, BITES);
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
		return getMaxBites() - blockState.getValue(BITES);
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
