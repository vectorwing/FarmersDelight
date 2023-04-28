package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BuddingTomatoBlock extends BuddingBushBlock implements BonemealableBlock
{
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)};
	private static final VoxelShape[] NARROW_SHAPE_BY_AGE = new VoxelShape[]{
			Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D),
			Block.box(4.0D, 0.0D, 4.0D, 12.0D, 6.0D, 12.0D),
			Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D),
			Block.box(2.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D),
			Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)};

	public BuddingTomatoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return Configuration.ENABLE_NARROW_CROP_HITBOXES.get() ? NARROW_SHAPE_BY_AGE[state.getValue(this.getAgeProperty())] : SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}

	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		return ModBlocks.BUDDING_TOMATO_CROP.get().defaultBlockState();
	}

	@Override
	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(ModBlocks.RICH_SOIL_FARMLAND.get()) || pState.is(Blocks.FARMLAND);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(BuddingBushBlock.AGE) == 4) {
			level.setBlock(currentPos, ModBlocks.TOMATO_CROP.get().defaultBlockState(), 3);
		}
		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canGrowPastMaxAge() {
		return true;
	}

	@Override
	public void growPastMaxAge(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		level.setBlockAndUpdate(pos, ModBlocks.TOMATO_CROP.get().defaultBlockState());
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(Level level) {
		return Mth.nextInt(level.random, 1, 4);
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int maxAge = getMaxAge();
		int ageGrowth = Math.min(getAge(state) + getBonemealAgeIncrease(level), 7);
		if (ageGrowth <= maxAge) {
			level.setBlockAndUpdate(pos, state.setValue(AGE, ageGrowth));
		} else {
			int remainingGrowth = ageGrowth - maxAge - 1;
			level.setBlockAndUpdate(pos, ModBlocks.TOMATO_CROP.get().defaultBlockState().setValue(TomatoVineBlock.VINE_AGE, remainingGrowth));
		}
	}
}
