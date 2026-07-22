package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BuddingTomatoBlock extends BuddingBushBlock implements BonemealableBlock
{
	public BuddingTomatoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState updateShape(
		BlockState state,
		@NonNull LevelReader level,
		@NonNull ScheduledTickAccess scheduledTickAccess,
		@NonNull BlockPos pos,
		@NonNull Direction direction,
		@NonNull BlockPos neighborPos,
		@NonNull BlockState neighborState,
		@NonNull RandomSource random) {
		if (state.getValue(BuddingBushBlock.AGE) == 4) {
			return ModBlocks.TOMATO_CROP.get().defaultBlockState();
		}
		return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
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


