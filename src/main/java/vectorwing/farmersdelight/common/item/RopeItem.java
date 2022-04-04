package vectorwing.farmersdelight.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public class RopeItem extends FuelBlockItem
{
	public RopeItem(Block blockIn, Properties properties) {
		super(blockIn, properties, 200);
	}

	@Override
	@Nullable
	public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level world = context.getLevel();
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
			BlockPos.MutableBlockPos blockpos$mutable = (new BlockPos.MutableBlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ())).move(direction);

			while (i < 256) {
				state = world.getBlockState(blockpos$mutable);
				if (state.getBlock() != this.getBlock()) {
					FluidState fluid = state.getFluidState();
					if (!fluid.is(FluidTags.WATER) && !fluid.isEmpty()) {
						return null;
					}
					if (state.canBeReplaced(context)) {
						return BlockPlaceContext.at(context, blockpos$mutable, direction);
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
