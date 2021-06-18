package vectorwing.farmersdelight.tile;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

public class CanvasSignTileEntity extends SignTileEntity
{
	@Override
	public TileEntityType<?> getType() {
		return ModTileEntityTypes.CANVAS_SIGN_TILE.get();
	}
}
