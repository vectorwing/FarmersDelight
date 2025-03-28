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
	public RopeItem(Block block, Properties properties) {
		super(block, properties, 200);
	}

	@Override
	@Nullable
	public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		BlockState state = level.getBlockState(pos);
		Block block = this.getBlock();

		if (state.getBlock() != block) return context;
		Direction direction;
		if (context.isSecondaryUseActive()) {
			direction = context.getClickedFace();
		} else {
			direction = Direction.DOWN;
		}

		int i = 0;
		BlockPos.MutableBlockPos blockpos$mutable = (new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ())).move(direction);

		while (i < 256) {
			state = level.getBlockState(blockpos$mutable);
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

	@Override
	protected boolean mustSurvive() {
		return false;
	}
}
