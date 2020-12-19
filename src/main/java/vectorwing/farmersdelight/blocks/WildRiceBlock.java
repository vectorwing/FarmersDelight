package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import vectorwing.farmersdelight.registry.ModBlocks;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class WildRiceBlock extends DoublePlantBlock implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public WildRiceBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		FluidState fluid = worldIn.getFluidState(pos);
		BlockPos floorPos = pos.down();
		if (state.get(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
			return super.isValidPosition(state, worldIn, pos) && this.isValidGround(worldIn.getBlockState(floorPos), worldIn, floorPos) && fluid.isTagged(FluidTags.WATER) && fluid.getLevel() == 8;
		}
		return super.isValidPosition(state, worldIn, pos) && worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.WILD_RICE.get();
	}

	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
		worldIn.setBlockState(pos, this.getDefaultState().with(WATERLOGGED, true).with(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, false).with(HALF, DoubleBlockHalf.UPPER), flags);
	}

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
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn)	{
		return state.get(HALF) == DoubleBlockHalf.LOWER;
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER
				? Fluids.WATER.getStillFluidState(false)
				: Fluids.EMPTY.getDefaultState();
	}
}
