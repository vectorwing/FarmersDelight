package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class WildCropsBlock extends WildPatchBlock
{
	public WildCropsBlock(Properties properties)
	{
		super(properties);
	}

	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}
}
