package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

public class StoveRenderer implements BlockEntityRenderer<StoveBlockEntity>
{
	public StoveRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(StoveBlockEntity stoveEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = stoveEntity.getBlockState().getValue(StoveBlock.FACING).getOpposite();

		ItemStackHandler inventory = stoveEntity.getInventory();
		int posLong = (int) stoveEntity.getBlockPos().asLong();

		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack stoveStack = inventory.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				poseStack.pushPose();

				// Center item above the stove
				poseStack.translate(0.5D, 1.02D, 0.5D);

				// Rotate item to face the stove's front side
				float f = -direction.toYRot();
				poseStack.mulPose(Axis.YP.rotationDegrees(f));

				// Rotate item flat on the stove. Use X and Y from now on
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

				// Neatly align items according to their index
				Vec2 itemOffset = stoveEntity.getStoveItemOffset(i);
				poseStack.translate(itemOffset.x, itemOffset.y, 0.0D);

				// Resize the items
				poseStack.scale(0.375F, 0.375F, 0.375F);

				if (stoveEntity.getLevel() != null)
					Minecraft.getInstance().getItemRenderer().renderStatic(stoveStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(stoveEntity.getLevel(), stoveEntity.getBlockPos().above()), combinedOverlayIn, poseStack, buffer, stoveEntity.getLevel(), posLong + i);
				poseStack.popPose();
			}
		}
	}
}