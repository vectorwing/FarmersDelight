package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import vectorwing.farmersdelight.client.renderer.state.CanvasSignRenderState;

/**
 * Minecraft 26.2 renders sign bodies as normal block models.
 * This renderer is therefore responsible only for sign text, via vanilla's
 * {@link AbstractSignRenderer}.
 */
public abstract class AbstractCanvasSignRenderer<S extends CanvasSignRenderState> extends AbstractSignRenderer<S>
{
    protected AbstractCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}