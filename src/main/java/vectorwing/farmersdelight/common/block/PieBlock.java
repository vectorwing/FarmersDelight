package vectorwing.farmersdelight.common.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
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
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack heldStack = player.getItemInHand(handIn);
		if (worldIn.isClientSide) {
			if (heldStack.is(ModTags.KNIVES)) {
				return cutSlice(worldIn, pos, state);
			}

			if (this.consumeBite(worldIn, pos, state, player) == InteractionResult.SUCCESS) {
				return InteractionResult.SUCCESS;
			}

			if (heldStack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}

		if (heldStack.is(ModTags.KNIVES)) {
			return cutSlice(worldIn, pos, state);
		}
		return this.consumeBite(worldIn, pos, state, player);
	}

	/**
	 * Eats a slice from the pie, feeding the player.
	 */
	protected InteractionResult consumeBite(Level worldIn, BlockPos pos, BlockState state, Player playerIn) {
		if (!playerIn.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			ItemStack sliceStack = this.getPieSliceItem();
			FoodProperties sliceFood = sliceStack.getItem().getFoodProperties();

			playerIn.getFoodData().eat(sliceStack.getItem(), sliceStack);
			if (this.getPieSliceItem().getItem().isEdible() && sliceFood != null) {
				for (Pair<MobEffectInstance, Float> pair : sliceFood.getEffects()) {
					if (!worldIn.isClientSide && pair.getFirst() != null && worldIn.random.nextFloat() < pair.getSecond()) {
						playerIn.addEffect(new MobEffectInstance(pair.getFirst()));
					}
				}
			}

			int bites = state.getValue(BITES);
			if (bites < getMaxBites() - 1) {
				worldIn.setBlock(pos, state.setValue(BITES, bites + 1), 3);
			} else {
				worldIn.removeBlock(pos, false);
			}
			worldIn.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
			return InteractionResult.SUCCESS;
		}
	}

	/**
	 * Cuts off a bite and drops a slice item, without feeding the player.
	 */
	protected InteractionResult cutSlice(Level worldIn, BlockPos pos, BlockState state) {
		int bites = state.getValue(BITES);
		if (bites < getMaxBites() - 1) {
			worldIn.setBlock(pos, state.setValue(BITES, bites + 1), 3);
		} else {
			worldIn.removeBlock(pos, false);
		}
		Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), this.getPieSliceItem());
		worldIn.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
		return InteractionResult.SUCCESS;
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
		builder.add(FACING, BITES);
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return getMaxBites() - blockState.getValue(BITES);
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
