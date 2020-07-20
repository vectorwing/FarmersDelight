package vectorwing.farmersdelight.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TallRiceCropBlock extends BushBlock implements IGrowable
{
	protected TallRiceCropBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return false;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{

	}
}
