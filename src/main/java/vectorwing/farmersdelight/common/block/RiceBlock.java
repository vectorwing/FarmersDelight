package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class RiceBlock extends BushBlock implements BonemealableBlock, LiquidBlockContainer
{
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final BooleanProperty SUPPORTING = BooleanProperty.create("supporting");
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)};

	public RiceBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(SUPPORTING, false));
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);
		if (!worldIn.isAreaLoaded(pos, 1)) return;
		if (worldIn.getRawBrightness(pos.above(), 0) >= 6) {
			int age = this.getAge(state);
			if (age <= this.getMaxAge()) {
				float chance = 10;
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / chance) + 1) == 0)) {
					if (age == this.getMaxAge()) {
						RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
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
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		FluidState fluid = worldIn.getFluidState(pos);
		return super.canSurvive(state, worldIn, pos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return super.mayPlaceOn(state, worldIn, pos) || state.is(BlockTags.DIRT);
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
	public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.RICE.get());
	}

	public BlockState withAge(int age) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), age);
	}

	public boolean isMaxAge(BlockState state) {
		return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, SUPPORTING);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		BlockState state = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		if (!state.isAir()) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
			if (facing == Direction.UP) {
				return state.setValue(SUPPORTING, isSupportingRiceUpper(facingState));
			}
		}

		return state;
	}

	public boolean isSupportingRiceUpper(BlockState topState) {
		return topState.getBlock() == ModBlocks.RICE_CROP_PANICLES.get();
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.getStateForPlacement(context) : null;
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		BlockState upperState = worldIn.getBlockState(pos.above());
		if (upperState.getBlock() instanceof RicePaniclesBlock) {
			return !((RicePaniclesBlock) upperState.getBlock()).isMaxAge(upperState);
		}
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(Level worldIn) {
		return Mth.nextInt(worldIn.random, 1, 4);
	}

	@Override
	public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
		int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(worldIn), 7);
		if (ageGrowth <= this.getMaxAge()) {
			worldIn.setBlockAndUpdate(pos, state.setValue(AGE, ageGrowth));
		} else {
			BlockState top = worldIn.getBlockState(pos.above());
			if (top.getBlock() == ModBlocks.RICE_CROP_PANICLES.get()) {
				BonemealableBlock growable = (BonemealableBlock) worldIn.getBlockState(pos.above()).getBlock();
				if (growable.isValidBonemealTarget(worldIn, pos.above(), top, false)) {
					growable.performBonemeal(worldIn, worldIn.random, pos.above(), top);
				}
			} else {
				RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
				int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
				if (riceUpper.defaultBlockState().canSurvive(worldIn, pos.above()) && worldIn.isEmptyBlock(pos.above())) {
					worldIn.setBlockAndUpdate(pos, state.setValue(AGE, this.getMaxAge()));
					worldIn.setBlock(pos.above(), riceUpper.defaultBlockState().setValue(RicePaniclesBlock.RICE_AGE, remainingGrowth), 2);
				}
			}
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return false;
	}
}
