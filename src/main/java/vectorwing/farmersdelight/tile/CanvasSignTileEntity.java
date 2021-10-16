package vectorwing.farmersdelight.tile;

import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

public class CanvasSignTileEntity extends SignBlockEntity
{
	@Override
	public BlockEntityType<?> getType() {
		return ModTileEntityTypes.CANVAS_SIGN_TILE.get();
	}
}
