package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

public class CanvasSignBlockEntity extends SignBlockEntity
{
	public CanvasSignBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(pWorldPosition, pBlockState);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModBlockEntityTypes.CANVAS_SIGN.get();
	}
}
