package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import vectorwing.farmersdelight.init.ModItems;

public class CabbagesBlock extends CropsBlock
{
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D,  8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D)};


	public CabbagesBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	protected IItemProvider getSeedsItem() {
		return ModItems.CABBAGE_SEEDS.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}
}
