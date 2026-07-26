package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BuddingTomatoBlock extends BuddingBushBlock implements BonemealableBlock
{
	public BuddingTomatoBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (state.getValue(BuddingBushBlock.AGE) == 4) {
			if (level instanceof LevelAccessor levelAccessor) {
				levelAccessor.setBlock(currentPos, ModBlocks.TOMATO_CROP.get().defaultBlockState(), 3);
			}
		}
		return super.updateShape(state, level, tickAccess, currentPos, facing, facingPos, facingState, random);
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
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(Level level) {
		return Mth.nextInt(level.getRandom(), 1, 4);
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int maxAge = getMaxAge();
		int ageGrowth = Math.min(getAge(state) + getBonemealAgeIncrease(level), 7);
		if (ageGrowth <= maxAge) {
			level.setBlockAndUpdate(pos, state.setValue(AGE, ageGrowth));
		} else {
			int remainingGrowth = ageGrowth - maxAge - 1;
			level.setBlockAndUpdate(pos, ModBlocks.TOMATO_CROP.get().defaultBlockState().setValue(TomatoBlock.VINE_AGE, remainingGrowth));
		}
	}
}
