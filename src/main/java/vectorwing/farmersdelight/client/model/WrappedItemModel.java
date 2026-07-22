package vectorwing.farmersdelight.client.model;

/*
public class WrappedItemModel<T extends BakedModel> extends BakedModelWrapper<T>
{
	private final List<BakedModel> renderPasses = List.of(this);

	public WrappedItemModel(T originalModel) {
		super(originalModel);
	}

	@Override
	public boolean isCustomRenderer() {
		return true;
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext cameraTransformType, PoseStack poseStack, boolean applyLeftHandTransform) {
		BakedModel model = super.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
		return model.equals(this) || model instanceof WrappedItemModel ? this : new WrappedItemModel<>(model);
	}

	@Override
	public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
		return renderPasses;
	}
}*/

