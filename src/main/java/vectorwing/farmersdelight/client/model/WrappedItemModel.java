package vectorwing.farmersdelight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

public class WrappedItemModel<T extends BakedModel> extends BakedModelWrapper<T>
{
	public WrappedItemModel(T originalModel) {
		super(originalModel);
	}

	@Override
	public boolean isCustomRenderer() {
		return true;
	}

	@Override
	public BakedModel applyTransform(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
		BakedModel model = super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
		return model.equals(this) || model instanceof WrappedItemModel ? this : new WrappedItemModel<>(model);
	}
}
