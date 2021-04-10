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

@SuppressWarnings("deprecation")
public class WildRiceBlock extends DoublePlantBlock implements IWaterLoggable, IGrowable
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WildRiceBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		FluidState fluid = worldIn.getFluidState(pos);
		BlockPos floorPos = pos.down();
		if (state.get(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
			return super.isValidPosition(state, worldIn, pos) && this.isValidGround(worldIn.getBlockState(floorPos), worldIn, floorPos) && fluid.isTagged(FluidTags.WATER) && fluid.getLevel() == 8;
		}
		return super.isValidPosition(state, worldIn, pos) && worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.WILD_RICE.get();
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote) {
			if (player.isCreative()) {
				removeBottomHalf(worldIn, pos, state, player);
			} else {
				spawnDrops(state, worldIn, pos, null, player, player.getHeldItemMainhand());
			}
		}

		worldIn.playEvent(player, 2001, pos, getStateId(state));
		if (this.isIn(BlockTags.GUARDED_BY_PIGLINS)) {
			PiglinTasks.func_234478_a_(player, false);
		}
	}

	protected static void removeBottomHalf(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf half = state.get(HALF);
		if (half == DoubleBlockHalf.UPPER) {
			BlockPos floorPos = pos.down();
			BlockState floorState = world.getBlockState(floorPos);
			if (floorState.getBlock() == state.getBlock() && floorState.get(HALF) == DoubleBlockHalf.LOWER) {
				world.setBlockState(floorPos, Blocks.WATER.getDefaultState(), 35);
				world.playEvent(player, 2001, floorPos, Block.getStateId(floorState));
			}
		}
	}

	@Override
	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
		worldIn.setBlockState(pos, this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), flags);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState blockstate = super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		DoubleBlockHalf half = stateIn.get(HALF);
		if (!blockstate.isAir()) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.getBlock() == this && facingState.get(HALF) != half) {
			return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getPos();
		FluidState fluid = context.getWorld().getFluidState(context.getPos());
		return pos.getY() < context.getWorld().getHeight() - 1
				&& fluid.isTagged(FluidTags.WATER)
				&& fluid.getLevel() == 8
				&& context.getWorld().getBlockState(pos.up()).isAir(context.getWorld(), pos.up())
				? super.getStateForPlacement(context) : null;
	}

	@Override
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return state.get(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER
				? Fluids.WATER.getStillFluidState(false)
				: Fluids.EMPTY.getDefaultState();
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return (double) rand.nextFloat() < 0.3F;
	}

	@Override
	public void grow(ServerWorld worldIn, Random random, BlockPos pos, BlockState state) {
		spawnAsEntity(worldIn, pos, new ItemStack(this));
	}
}
