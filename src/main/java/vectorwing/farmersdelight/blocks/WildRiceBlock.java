package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vectorwing.farmersdelight.registry.ModBlocks;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class WildRiceBlock extends DoublePlantBlock implements IWaterLoggable, IGrowable
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WildRiceBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, true).setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		FluidState fluid = worldIn.getFluidState(pos);
		BlockPos floorPos = pos.below();
		if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
			return super.canSurvive(state, worldIn, pos) && this.mayPlaceOn(worldIn.getBlockState(floorPos), worldIn, floorPos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
		}
		return super.canSurvive(state, worldIn, pos) && worldIn.getBlockState(pos.below()).getBlock() == ModBlocks.WILD_RICE.get();
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(WATERLOGGED, false).setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isClientSide) {
			if (player.isCreative()) {
				removeBottomHalf(worldIn, pos, state, player);
			} else {
				dropResources(state, worldIn, pos, null, player, player.getMainHandItem());
			}
		}

		worldIn.levelEvent(player, 2001, pos, getId(state));
		if (this.is(BlockTags.GUARDED_BY_PIGLINS)) {
			PiglinTasks.angerNearbyPiglins(player, false);
		}
	}

	protected static void removeBottomHalf(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf half = state.getValue(HALF);
		if (half == DoubleBlockHalf.UPPER) {
			BlockPos floorPos = pos.below();
			BlockState floorState = world.getBlockState(floorPos);
			if (floorState.getBlock() == state.getBlock() && floorState.getValue(HALF) == DoubleBlockHalf.LOWER) {
				world.setBlock(floorPos, Blocks.WATER.defaultBlockState(), 35);
				world.levelEvent(player, 2001, floorPos, Block.getId(floorState));
			}
		}
	}

	@Override
	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
		worldIn.setBlock(pos, this.defaultBlockState().setValue(WATERLOGGED, true).setValue(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(WATERLOGGED, false).setValue(HALF, DoubleBlockHalf.UPPER), flags);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState currentState = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		DoubleBlockHalf half = stateIn.getValue(HALF);
		if (!currentState.isAir()) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}
		if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.getBlock() == this && facingState.getValue(HALF) != half) {
			return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
		} else {
			return Blocks.AIR.defaultBlockState();
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getClickedPos();
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return pos.getY() < context.getLevel().getMaxBuildHeight() - 1
				&& fluid.is(FluidTags.WATER)
				&& fluid.getAmount() == 8
				&& context.getLevel().getBlockState(pos.above()).isAir(context.getLevel(), pos.above())
				? super.getStateForPlacement(context) : null;
	}

	@Override
	public boolean canPlaceLiquid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER
				? Fluids.WATER.getSource(false)
				: Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return (double) rand.nextFloat() < 0.3F;
	}

	@Override
	public void performBonemeal(ServerWorld worldIn, Random random, BlockPos pos, BlockState state) {
		popResource(worldIn, pos, new ItemStack(this));
	}
}
