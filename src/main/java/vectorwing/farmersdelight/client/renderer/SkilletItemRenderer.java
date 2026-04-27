package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IArmPoseTransformer;
import vectorwing.farmersdelight.common.item.SkilletItem;

public class SkilletItemRenderer extends BlockEntityWithoutLevelRenderer {
    public SkilletItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        //render block
        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();

        CompoundTag tag = stack.getTagElement("Cooking");
        ItemStack ingredientStack = ItemStack.of(tag);

        float animation = 0;

        if (!ingredientStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1 / 16f, 0.5);

            long gameTime = Minecraft.getInstance().level.getGameTime();
            long time = stack.getOrCreateTag().getLong("FlipTimeStamp");
            if (time != 0) {
                float partialTicks = Minecraft.getInstance().getFrameTime();
                animation = ((gameTime - time) + partialTicks) / SkilletItem.FLIP_TIME;
                animation = Mth.clamp(animation, 0, 1);
                float maxH = 0.4F;
                poseStack.translate(0, maxH * Mth.sin(animation * Mth.PI), 0);
                float rotationAnimation = stack.getOrCreateTag().getBoolean("Flipped") ? animation + 1.0F : animation;
                poseStack.mulPose(Axis.XP.rotationDegrees(180 * rotationAnimation));
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees(stack.getOrCreateTag().getBoolean("Flipped") ? 180 : 0));
            }

            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.scale(0.5F, 0.5F, 0.5F);

            if (displayContext != ItemDisplayContext.GUI) {
                var itemRenderer = Minecraft.getInstance().getItemRenderer();
                itemRenderer.renderStatic(ingredientStack, ItemDisplayContext.FIXED, packedLight,
                        packedOverlay, poseStack, buffer, null, 0);
            }

            poseStack.popPose();
        }

        poseStack.pushPose();

        if (animation != 0 && displayContext.firstPerson()) {
            poseStack.translate(0, 0, 1);
            poseStack.mulPose(Axis.XN.rotationDegrees(Mth.sin(animation * Mth.TWO_PI) * 6));
            poseStack.translate(0F, 0, -1);
            poseStack.translate(0, 0, -Mth.sin(animation * Mth.PI) * 0.2);
        }
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);

        poseStack.popPose();

    }

    public static class ArmPoseTransformer implements IArmPoseTransformer {
        @Override
        public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
            ItemStack stack = entity.getUseItem();
            long time = stack.getOrCreateTag().getLong("FlipTimeStamp");
            if (time > 0) {
                float partialTicks = Minecraft.getInstance().getFrameTime();
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
