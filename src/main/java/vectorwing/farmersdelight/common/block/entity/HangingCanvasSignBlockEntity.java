package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

public class HangingCanvasSignBlockEntity extends HangingSignBlockEntity
{
	public HangingCanvasSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModBlockEntityTypes.HANGING_CANVAS_SIGN.get();
	}

	public int getTextLineHeight() {
		return 9;
	}

	public int getMaxTextLineWidth() {
		return 60;
	}
}
