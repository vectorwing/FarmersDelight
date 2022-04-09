package vectorwing.farmersdelight.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.item.Item.Properties;

public class RopeItem extends FuelBlockItem
{
	public RopeItem(Block blockIn, Properties properties) {
		super(blockIn, properties, 200);
	}

	@Override
	@Nullable
	public BlockItemUseContext updatePlacementContext(BlockItemUseContext context) {
		BlockPos blockpos = context.getClickedPos();
		World world = context.getLevel();
		BlockState state = world.getBlockState(blockpos);
		Block block = this.getBlock();

		if (state.getBlock() != block) {
			return context;
		} else {
			Direction direction;
			if (context.isSecondaryUseActive()) {
				direction = context.getClickedFace();
			} else {
				direction = Direction.DOWN;
			}

			int i = 0;
			BlockPos.Mutable blockpos$mutable = (new BlockPos.Mutable(blockpos.getX(), blockpos.getY(), blockpos.getZ())).move(direction);

			while (i < 256) {
				state = world.getBlockState(blockpos$mutable);
				if (state.getBlock() != this.getBlock()) {
					FluidState fluid = state.getFluidState();
					if (!fluid.is(FluidTags.WATER) && !fluid.isEmpty()) {
						return null;
					}
					if (state.canBeReplaced(context)) {
						return BlockItemUseContext.at(context, blockpos$mutable, direction);
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

	@Override
	protected boolean mustSurvive() {
		return false;
	}
}
