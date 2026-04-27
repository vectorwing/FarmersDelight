package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class SkilletItemRenderer extends BlockEntityWithoutLevelRenderer
{
	public SkilletItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		//render block
		BlockItem item = ((BlockItem) stack.getItem());
		BlockState state = item.getBlock().defaultBlockState();


		Minecraft mc = Minecraft.getInstance();

		ItemStackWrapper stackWrapper = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY);
		ItemStack ingredientStack = stackWrapper.getStack();

		float animation = 0;

		if (!ingredientStack.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0.5, 1 / 16f, 0.5);

			long gameTime = mc.level.getGameTime();
			if (stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get()) && displayContext != ItemDisplayContext.GUI) {
				long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
				float partialTicks = mc.getTimer().getGameTimeDeltaPartialTick(false);
				animation = ((gameTime - time) + partialTicks) / SkilletItem.FLIP_TIME;
				animation = Mth.clamp(animation, 0, 1);
				float maxH = 0.4F;
				poseStack.translate(0, maxH * Mth.sin(animation * Mth.PI), 0);
				float rotationAnimation = stack.getOrDefault(ModDataComponents.SKILLET_FLIPPED.get(), false) ? animation + 1.0F : animation;
				poseStack.mulPose(Axis.XP.rotationDegrees(180 * rotationAnimation));
			} else {
				poseStack.mulPose(Axis.XP.rotationDegrees(stack.getOrDefault(ModDataComponents.SKILLET_FLIPPED.get(), false) ? 180 : 0));
			}

			poseStack.mulPose(Axis.XP.rotationDegrees(90));
			poseStack.scale(0.5F, 0.5F, 0.5F);

			if (displayContext != ItemDisplayContext.GUI) {
				var itemRenderer = mc.getItemRenderer();
				itemRenderer.renderStatic(ingredientStack, ItemDisplayContext.FIXED, packedLight,
						packedOverlay, poseStack, buffer, null, 0);
			}

			poseStack.popPose();
		}

		poseStack.pushPose();

		if (animation != 0 && displayContext.firstPerson()) {
			poseStack.translate(0, 0, 1);
			poseStack.mulPose(Axis.XN.rotationDegrees(Mth.sin(animation * Mth.TWO_PI) * 15));
			poseStack.translate(0F, 0, -1);
			poseStack.translate(0, 0, -Mth.sin(animation * Mth.PI) * 0.2);
		}
		mc.getBlockRenderer().renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);

		poseStack.popPose();
	}

	public static class ArmPoseTransformer implements IArmPoseTransformer {
		@Override
		public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
			ItemStack stack = entity.getUseItem();
			if (stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
				long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
				float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
				float animation = ((entity.level().getGameTime() - time) + partialTicks) / SkilletItem.FLIP_TIME;
				animation = Mth.clamp(animation, 0, 1);

				if (arm == HumanoidArm.LEFT) {
					model.leftArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
				} else {
					model.rightArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
				}
            }
		}
	}
}
