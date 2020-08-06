package vectorwing.farmersdelight.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class RedMushroomColonyBlock extends MushroomColonyBlock
{
	public RedMushroomColonyBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(COLONY_AGE, 0));
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(Items.RED_MUSHROOM);
	}
}
