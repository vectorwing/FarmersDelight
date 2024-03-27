package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Random;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity>
{
	private final Random random = new Random();

	public CuttingBoardRenderer(BlockEntityRendererProvider.Context pContext) {
	}

	@Override
	public void render(CuttingBoardBlockEntity cuttingBoardEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		Direction direction = cuttingBoardEntity.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();
		ItemStack itemStack = cuttingBoardEntity.getStoredItem();
		int posLong = (int) cuttingBoardEntity.getBlockPos().asLong();

		int seed = itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) + itemStack.getDamageValue();
		this.random.setSeed(seed);

		if (!itemStack.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance()
					.getItemRenderer();

			int itemRenderCount = this.getModelCount(itemStack);
			for (int i = 0; i < itemRenderCount; i++) {
				poseStack.pushPose();

				poseStack.pushPose();
				boolean isBlockItem = itemRenderer.getModel(itemStack, cuttingBoardEntity.getLevel(), null, 0).applyTransform(ItemTransforms.TransformType.FIXED, poseStack, false).isGui3d();
				poseStack.popPose();

				float xOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float zOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

				if (cuttingBoardEntity.isItemCarvingBoard()) {
					renderItemCarved(poseStack, direction, itemStack);
				} else if (isBlockItem && !itemStack.is(ModTags.FLAT_ON_CUTTING_BOARD)) {
					renderBlock(poseStack, direction, xOffset, i, zOffset);
				} else {
					renderItemLayingDown(poseStack, direction, xOffset, i, zOffset);
				}

				Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffer, posLong);
				poseStack.popPose();
			}
		}
	}

	public void renderItemLayingDown(PoseStack matrixStackIn, Direction direction, float xOffset, int yIndex, float zOffset) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D + xOffset, 0.08D + 0.03 * (yIndex + 1), 0.5D + zOffset);

		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot();
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));

		// Rotate item flat on the cutting board. Use X and Y from now on
		matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	public void renderBlock(PoseStack matrixStackIn, Direction direction, float xOffset, int yIndex, float zOffset) {
		// Center block above the cutting board
		matrixStackIn.translate(0.5D + xOffset, 0.27D + 0.03 * (yIndex + 1), 0.5D + zOffset);

		// Rotate block to face the cutting board's front side
		float f = -direction.toYRot();
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));

		// Resize the block
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}

	public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.23D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot() + 180;
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f));

		// Rotate item to be carved on the surface, A little less so for hoes and pickaxes.
		Item toolItem = itemStack.getItem();
		float poseAngle;
		if (toolItem instanceof PickaxeItem || toolItem instanceof HoeItem) {
			poseAngle = 225.0F;
		} else if (toolItem instanceof TridentItem) {
			poseAngle = 135.0F;
		} else {
			poseAngle = 180.0F;
		}
		matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(poseAngle));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	protected int getModelCount(ItemStack stack) {
		if (stack.getCount() > 48) {
			return 5;
		} else if (stack.getCount() > 32) {
			return 4;
		} else if (stack.getCount() > 16) {
			return 3;
		} else if (stack.getCount() > 1) {
			return 2;
		}
		return 1;
	}
}
