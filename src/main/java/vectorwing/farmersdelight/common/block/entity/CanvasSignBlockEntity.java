package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

public class CanvasSignBlockEntity extends SignBlockEntity
{
	public CanvasSignBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
		if (state.getBlock() instanceof CanvasSign canvasSign && canvasSign.isDarkBackground()) {
			this.frontText = createDefaultSignText().setColor(DyeColor.WHITE);
		}
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModBlockEntityTypes.CANVAS_SIGN.get();
	}
}
