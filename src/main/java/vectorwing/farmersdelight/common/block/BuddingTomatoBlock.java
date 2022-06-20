package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Random;

public class BuddingTomatoBlock extends GrowingBushBlock implements BonemealableBlock
{
	public BuddingTomatoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getPlant(BlockGetter world, BlockPos pos) {
		return ModBlocks.BUDDING_TOMATO_CROP.get().defaultBlockState();
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level pLevel, Random pRandom, BlockPos pPos, BlockState pState) {
		return false;
	}

	@Override
	public void performBonemeal(ServerLevel pLevel, Random pRandom, BlockPos pPos, BlockState pState) {

	}
}
