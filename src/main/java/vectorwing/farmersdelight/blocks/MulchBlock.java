package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.PlantType;

public class MulchBlock extends Block
{
	public MulchBlock()
	{
		super(Properties.from(Blocks.DIRT));
	}

	public MulchBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType type = plantable.getPlantType(world, pos.offset(facing));
		return type != PlantType.Crop && type != PlantType.Nether;
	}
}
