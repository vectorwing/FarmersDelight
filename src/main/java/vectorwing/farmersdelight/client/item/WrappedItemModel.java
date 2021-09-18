package vectorwing.farmersdelight.client.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraftforge.client.model.BakedModelWrapper;

public class WrappedItemModel<T extends IBakedModel> extends BakedModelWrapper<T>
{
	public WrappedItemModel(T originalModel) {
		super(originalModel);
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
		IBakedModel model = super.handlePerspective(cameraTransformType, mat);
		return model.equals(this) || model instanceof WrappedItemModel ? this : new WrappedItemModel<>(model);
	}
}
