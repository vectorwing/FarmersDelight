package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Random;

public class BuddingTomatoBlock extends BuddingBushBlock implements BonemealableBlock
{
	public BuddingTomatoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		return ModBlocks.BUDDING_TOMATO_CROP.get().defaultBlockState();
	}

	protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
		return pState.is(ModBlocks.RICH_SOIL_FARMLAND.get()) || pState.is(Blocks.FARMLAND);
	}

	public boolean canGrowPastMaxAge() {
		return true;
	}

	public void growPastMaxAge(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		level.setBlockAndUpdate(pos, ModBlocks.TOMATO_CROP.get().defaultBlockState());
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	protected int getBonemealAgeIncrease(Level level) {
		return Mth.nextInt(level.random, 1, 4);
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
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
