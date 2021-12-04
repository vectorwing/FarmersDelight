package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import net.minecraft.block.AbstractBlock.OffsetType;
import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class WildPatchBlock extends BushBlock implements IGrowable
{
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

	public WildPatchBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.RED_SAND;
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return OffsetType.XZ;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return (double) rand.nextFloat() < 0.8F;
	}

	@Override
	public void performBonemeal(ServerWorld worldIn, Random random, BlockPos pos, BlockState state) {
		int wildCropLimit = 10;

		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
			if (worldIn.getBlockState(nearbyPos).is(this)) {
				--wildCropLimit;
				if (wildCropLimit <= 0) {
					return;
				}
			}
		}

		BlockPos randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

		for (int k = 0; k < 4; ++k) {
			if (worldIn.isEmptyBlock(randomPos) && state.canSurvive(worldIn, randomPos)) {
				pos = randomPos;
			}

			randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
		}

		if (worldIn.isEmptyBlock(randomPos) && state.canSurvive(worldIn, randomPos)) {
			worldIn.setBlock(randomPos, state, 2);
		}
	}
}
