package vectorwing.farmersdelight.client.extension;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.client.renderer.item.GlassJugItemRenderer;

public class GlassJugItemClientExtension implements IClientItemExtensions
{
	BlockEntityWithoutLevelRenderer renderer = new GlassJugItemRenderer();

	@Override
	public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
		return renderer;
	}
}
