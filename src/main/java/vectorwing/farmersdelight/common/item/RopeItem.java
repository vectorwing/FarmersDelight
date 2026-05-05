package vectorwing.farmersdelight.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;

public class RopeItem extends BlockItem
{
	public RopeItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	@Nullable
	public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		BlockState state = level.getBlockState(pos);

		if (!state.is(this.getBlock())) {
			return context;
		}

		Direction direction;
		if (context.isSecondaryUseActive()) {
			direction = context.getClickedFace();
		} else {
			direction = Direction.DOWN;
		}

		BlockPos.MutableBlockPos mutablePos = pos.mutable().move(direction);

		while (mutablePos.getY() >= level.getMinBuildHeight()) {
			state = level.getBlockState(mutablePos);
			if (!state.is(this.getBlock())) {
				FluidState fluid = state.getFluidState();
				if (!fluid.is(FluidTags.WATER) && !fluid.isEmpty()) {
					return null;
				}
				if (state.canBeReplaced(context)) {
					return BlockPlaceContext.at(context, mutablePos, direction);
				}
				break;
			}

			if (direction != Direction.DOWN) {
				return null;
			}

			mutablePos.move(direction);
		}

		return null;
	}

	@Override
	protected boolean mustSurvive() {
		return false;
	}
}
