package vectorwing.farmersdelight.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

public class CanvasSignTileEntity extends SignBlockEntity
{
	public CanvasSignTileEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(pWorldPosition, pBlockState);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModTileEntityTypes.CANVAS_SIGN_TILE.get();
	}
}
