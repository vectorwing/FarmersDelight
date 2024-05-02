package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.item.SkilletItem;

public class SkilletItemRenderer extends BlockEntityWithoutLevelRenderer {

    public SkilletItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType pTransformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        //render block
        poseStack.pushPose();
        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();

        CompoundTag tag = stack.getTagElement("Cooking");
        ItemStack ingredientStack = ItemStack.of(tag);

        if (!ingredientStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1 / 16f, 0.5);

            long gameTime = Minecraft.getInstance().level.getGameTime();
            long time = stack.getOrCreateTag().getLong("FlipTimeStamp");
            if (time != 0) {
                float partialTicks = Minecraft.getInstance().getFrameTime();
                float animation = ((gameTime - time) + partialTicks) / SkilletItem.FLIP_TIME;
                float maxH = 1;
                poseStack.translate(0, maxH * Mth.sin(animation * Mth.PI), 0);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(360 * animation));
            }

            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            poseStack.scale(0.5F, 0.5F, 0.5F);

            var itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderStatic(ingredientStack, ItemTransforms.TransformType.FIXED, packedLight,
                    packedOverlay, poseStack, buffer, 0);

            poseStack.popPose();
        }


    }


}
