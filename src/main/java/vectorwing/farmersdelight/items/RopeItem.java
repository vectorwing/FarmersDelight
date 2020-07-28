package vectorwing.farmersdelight.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RopeItem extends BlockItem
{
	public RopeItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}

	@Nullable
	public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		World world = context.getWorld();
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = this.getBlock();

		if (blockstate.getBlock() != block) {
			return context;
		} else {
			Direction direction;
			if (context.func_225518_g_()) {
				direction = context.getFace();
			} else {
				direction = Direction.DOWN;
			}

			int i = 0;
			BlockPos.Mutable blockpos$mutable = (new BlockPos.Mutable(blockpos)).move(direction);

			while(i < 256) {
				blockstate = world.getBlockState(blockpos$mutable);
				if (blockstate.getBlock() != this.getBlock()) {
					if (blockstate.isReplaceable(context)) {
						return BlockItemUseContext.func_221536_a(context, blockpos$mutable, direction);
					}
					break;
				}

				if (direction != Direction.DOWN) {
					return context;
				}

				blockpos$mutable.move(direction);
				++i;
			}

			return null;
		}
	}

	protected boolean checkPosition() {
		return false;
	}
}
