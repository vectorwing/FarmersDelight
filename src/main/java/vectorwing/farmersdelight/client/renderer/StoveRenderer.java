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
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

public class StoveRenderer implements BlockEntityRenderer<StoveBlockEntity>
{
	public StoveRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(StoveBlockEntity stove, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		Direction direction = stove.getBlockState().getValue(StoveBlock.FACING).getOpposite();

		ItemStackHandler inventory = stove.getInventory();
		int posLong = (int) stove.getBlockPos().asLong();

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
				Vec2 itemOffset = stove.getStoveItemOffset(i);
				poseStack.translate(itemOffset.x, itemOffset.y, 0.0D);

				// Resize the items
				poseStack.scale(0.375F, 0.375F, 0.375F);

				if (stove.getLevel() != null)
					Minecraft.getInstance().getItemRenderer().renderStatic(stoveStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(stove.getLevel(), stove.getBlockPos().above()), packedOverlay, poseStack, buffer, stove.getLevel(), posLong + i);
				poseStack.popPose();
			}
		}
	}
}