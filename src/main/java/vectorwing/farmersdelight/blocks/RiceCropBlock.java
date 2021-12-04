package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class RiceCropBlock extends BushBlock implements IGrowable, ILiquidContainer
{
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final BooleanProperty SUPPORTING = BooleanProperty.create("supporting");
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)};

	public RiceCropBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(SUPPORTING, false));
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1)) return;
		if (worldIn.getRawBrightness(pos.above(), 0) >= 6) {
			int age = this.getAge(state);
			if (age <= this.getMaxAge()) {
				float chance = 10;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / chance) + 1) == 0)) {
					if (age == this.getMaxAge()) {
						RiceUpperCropBlock riceUpper = (RiceUpperCropBlock) ModBlocks.RICE_UPPER_CROP.get();
						if (riceUpper.defaultBlockState().canSurvive(worldIn, pos.above()) && worldIn.isEmptyBlock(pos.above())) {
							worldIn.setBlockAndUpdate(pos.above(), riceUpper.defaultBlockState());
							net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
						}
					} else {
						worldIn.setBlock(pos, this.withAge(age + 1), 2);
						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
					}
				}
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		FluidState fluid = worldIn.getFluidState(pos);
		return super.canSurvive(state, worldIn, pos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return super.mayPlaceOn(state, worldIn, pos) || Tags.Blocks.DIRT.contains(state.getBlock());
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	protected int getAge(BlockState state) {
		return state.getValue(this.getAgeProperty());
	}

	public int getMaxAge() {
		return 3;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.RICE.get());
	}

	public BlockState withAge(int age) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE, SUPPORTING);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState state = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		if (!state.isAir()) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			if (facing == Direction.UP) {
				return state.setValue(SUPPORTING, isSupportingRiceUpper(facingState));
			}
		}

		return state;
	}

	public boolean isSupportingRiceUpper(BlockState topState) {
		return topState.getBlock() == ModBlocks.RICE_UPPER_CROP.get();
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.getStateForPlacement(context) : null;
	}

	@Override
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		BlockState upperState = worldIn.getBlockState(pos.above());
		if (upperState.getBlock() instanceof RiceUpperCropBlock) {
			return !((RiceUpperCropBlock) upperState.getBlock()).isMaxAge(upperState);
		}
		return true;
	}

	@Override
	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(World worldIn) {
		return MathHelper.nextInt(worldIn.random, 1, 4);
	}

	@Override
	public void performBonemeal(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(worldIn), 7);
		if (ageGrowth <= this.getMaxAge()) {
			worldIn.setBlockAndUpdate(pos, state.setValue(AGE, ageGrowth));
		} else {
			BlockState top = worldIn.getBlockState(pos.above());
			if (top.getBlock() == ModBlocks.RICE_UPPER_CROP.get()) {
				IGrowable growable = (IGrowable) worldIn.getBlockState(pos.above()).getBlock();
				if (growable.isValidBonemealTarget(worldIn, pos.above(), top, false)) {
					growable.performBonemeal(worldIn, worldIn.random, pos.above(), top);
				}
			} else {
				RiceUpperCropBlock riceUpper = (RiceUpperCropBlock) ModBlocks.RICE_UPPER_CROP.get();
				int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
				if (riceUpper.defaultBlockState().canSurvive(worldIn, pos.above()) && worldIn.isEmptyBlock(pos.above())) {
					worldIn.setBlockAndUpdate(pos, state.setValue(AGE, this.getMaxAge()));
					worldIn.setBlock(pos.above(), riceUpper.defaultBlockState().setValue(RiceUpperCropBlock.RICE_AGE, remainingGrowth), 2);
				}
			}
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	public boolean canPlaceLiquid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return false;
	}

	@Override
	public boolean placeLiquid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return false;
	}
}
