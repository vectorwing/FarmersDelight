package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.SkilletRenderState;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import java.util.Random;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity, SkilletRenderState>
{
	private final Random random = new Random();
    private final ItemModelResolver itemModelResolver;

	public SkilletRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
	}

    @Override
    public SkilletRenderState createRenderState() {
        return new SkilletRenderState();
    }

    @Override
    public void extractRenderState(@NonNull SkilletBlockEntity skilletEntity, @NonNull SkilletRenderState renderState, float partialTick, @NonNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(skilletEntity, renderState, partialTick, cameraPosition, breakProgress);

        renderState.direction = skilletEntity.getBlockState().getValue(StoveBlock.FACING);
        ItemStack stack = skilletEntity.getInventory().getStackInSlot(0);
        renderState.stack = stack.copy();
        int posLong = (int) skilletEntity.getBlockPos().asLong();
        renderState.displayItem = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(renderState.displayItem, stack, ItemDisplayContext.FIXED, skilletEntity.getLevel(), null, posLong);
    }

    @Override
    public void submit(SkilletRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector nodeCollector, @NonNull CameraRenderState camera) {
        Direction direction = state.direction;

        ItemStack stack = state.stack;
        int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
        this.random.setSeed(seed);

        if (!stack.isEmpty()) {
            int itemRenderCount = this.getModelCount(stack);
            for (int i = 0; i < itemRenderCount; i++) {
                poseStack.pushPose();

                // Stack up items in the skillet, with a slight offset per item
                float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                poseStack.translate(0.5D + xOffset, 0.1D + 0.03 * (i + 1), 0.5D + zOffset);

                // Rotate item to face the skillet's front side
                float degrees = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(degrees));

                // Rotate item flat on the skillet. Use X and Y from now on
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

                // Resize the items
                poseStack.scale(0.5F, 0.5F, 0.5F);

                if (state.displayItem != null) {
                    state.displayItem.submit(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                }
                poseStack.popPose();
            }
        }
    }

    protected int getModelCount(ItemStack stack) {
        int modelCount = 1;

        if (stack.getCount() > 1) {
            modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4);
        }

        return modelCount;
    }
}

