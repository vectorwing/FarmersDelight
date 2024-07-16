package vectorwing.farmersdelight.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
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
import net.neoforged.neoforge.common.CommonHooks;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RiceBlock extends BushBlock implements BonemealableBlock, LiquidBlockContainer
{
	public static final MapCodec<RiceBlock> CODEC = simpleCodec(RiceBlock::new);

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
	protected MapCodec<? extends BushBlock> codec() {
		return CODEC;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.tick(state, level, pos, random);
		if (!level.isAreaLoaded(pos, 1)) return;
		if (level.getRawBrightness(pos.above(), 0) >= 6) {
			int age = this.getAge(state);
			if (age <= this.getMaxAge()) {
				float chance = 10;
				if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25.0F / chance) + 1) == 0)) {
					if (age == this.getMaxAge()) {
						RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
						if (riceUpper.defaultBlockState().canSurvive(level, pos.above()) && level.isEmptyBlock(pos.above())) {
							level.setBlockAndUpdate(pos.above(), riceUpper.defaultBlockState());
							CommonHooks.fireCropGrowPost(level, pos, state);
						}
					} else {
						level.setBlock(pos, this.withAge(age + 1), 2);
						CommonHooks.fireCropGrowPost(level, pos, state);
					}
				}
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		FluidState fluid = level.getFluidState(pos);
		return super.canSurvive(state, level, pos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.DIRT);
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
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return new ItemStack(ModItems.RICE.get());
	}

	public BlockState withAge(int age) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), age);
	}

//	public boolean isMaxAge(BlockState state) {
//		return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
//	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE, SUPPORTING);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		BlockState state = super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
		if (!state.isAir()) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
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
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		BlockState upperState = level.getBlockState(pos.above());
		if (upperState.getBlock() instanceof RicePaniclesBlock) {
			return !((RicePaniclesBlock) upperState.getBlock()).isMaxAge(upperState);
		}
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(Level level) {
		return Mth.nextInt(level.random, 1, 4);
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), 7);
		if (ageGrowth <= this.getMaxAge()) {
			level.setBlockAndUpdate(pos, state.setValue(AGE, ageGrowth));
		} else {
			BlockState top = level.getBlockState(pos.above());
			if (top.getBlock() == ModBlocks.RICE_CROP_PANICLES.get()) {
				BonemealableBlock growable = (BonemealableBlock) level.getBlockState(pos.above()).getBlock();
				if (growable.isValidBonemealTarget(level, pos.above(), top)) {
					growable.performBonemeal(level, level.random, pos.above(), top);
				}
			} else {
				RicePaniclesBlock riceUpper = (RicePaniclesBlock) ModBlocks.RICE_CROP_PANICLES.get();
				int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
				if (riceUpper.defaultBlockState().canSurvive(level, pos.above()) && level.isEmptyBlock(pos.above())) {
					level.setBlockAndUpdate(pos, state.setValue(AGE, this.getMaxAge()));
					level.setBlock(pos.above(), riceUpper.defaultBlockState().setValue(RicePaniclesBlock.RICE_AGE, remainingGrowth), 2);
				}
			}
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluidIn) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		return false;
	}
}
