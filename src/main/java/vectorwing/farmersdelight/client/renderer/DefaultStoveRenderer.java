package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec2;
import vectorwing.farmersdelight.client.renderer.state.StoveRenderState;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

public class DefaultStoveRenderer<T extends AbstractStoveBlockEntity> extends AbstractStoveRenderer<T, StoveRenderState>
{
	public DefaultStoveRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public StoveRenderState createRenderState() {
		return new StoveRenderState();
	}

	@Override
	public Vec2 getStoveItemOffset(int index) {
		final float X_OFFSET = 0.3F;
		final float Y_OFFSET = 0.2F;
		final Vec2[] OFFSETS = {
			new Vec2(X_OFFSET, Y_OFFSET),
			new Vec2(0.0F, Y_OFFSET),
			new Vec2(-X_OFFSET, Y_OFFSET),
			new Vec2(X_OFFSET, -Y_OFFSET),
			new Vec2(0.0F, -Y_OFFSET),
			new Vec2(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}
}
