package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

/**
 * 26.1 port note:
 * <p>
 * The skillet used to render its in-hand / dropped-item visuals (skillet block + cooked ingredient + flip animation)
 * through a {@code BlockEntityWithoutLevelRenderer} wired up via {@code IClientItemExtensions#getCustomRenderer()}.
 * Both of those APIs were removed in the 1.21.4+ item-model rework. The replacement is a data-driven {@code special}
 * item model backed by a {@link net.minecraft.client.renderer.special.SpecialModelRenderer} registered through
 * {@code RegisterSpecialModelRendererEvent}. That migration (the new renderer + its {@code Unbaked} codec + the
 * skillet item-model JSON) is owned by datagen and is intentionally NOT recreated here.
 * <p>
 * What survives client-side is the arm-pose transformer below, which is still referenced by
 * {@link vectorwing.farmersdelight.common.EnumParameters#PROXY_SKILLET_FLIP} and remains valid under the
 * {@code IClientItemExtensions#getArmPose(...)} extension. NOTE that {@link IArmPoseTransformer} is now render-state
 * based: {@code applyTransform} receives a {@link HumanoidRenderState} instead of the {@code LivingEntity}, so the
 * held skillet stack is read from the render state and the flip timing uses the client game time.
 */
public class SkilletItemRenderer
{
	private SkilletItemRenderer() {
	}

	/**
	 * Returns the skillet flip animation progress in the range [0, 1] for the given stack, or 0 if not flipping.
	 * Mirrors the timing used by the (now data-driven) skillet item renderer and the arm-pose transformer.
	 */
	public static float getFlipAnimationProgress(ItemStack stack) {
		if (!stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
			return 0.0F;
		}
		Minecraft minecraft = Minecraft.getInstance();
		Level level = minecraft.level;
		if (level == null) {
			return 0.0F;
		}
		long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
		float partialTicks = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
		float animation = ((level.getGameTime() - time) + partialTicks) / SkilletItem.FLIP_TIME;
		return Mth.clamp(animation, 0, 1);
	}

	public static class ArmPoseTransformer implements IArmPoseTransformer
	{
		@Override
		public void applyTransform(HumanoidModel<?> model, HumanoidRenderState entity, HumanoidArm arm) {
			ItemStack stack = entity.getUseItemStackForArm(arm);
			if (stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
				float animation = getFlipAnimationProgress(stack);

				if (arm == HumanoidArm.LEFT) {
					model.leftArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
				} else {
					model.rightArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
				}
			}
		}
	}
}
