package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Random;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity>
{
	private final Random random = new Random();

	public CuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(CuttingBoardBlockEntity cuttingBoard, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		ItemStack itemStack = cuttingBoard.getStoredItem();
		if (itemStack.isEmpty()) {
			return;
		}

		Direction direction = cuttingBoard.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();
		int posLong = (int) cuttingBoard.getBlockPos().asLong();
		int seed = itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) + itemStack.getDamageValue();
		this.random.setSeed(seed);

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		int itemRenderCount = this.getModelCount(itemStack);

		for (int i = 0; i < itemRenderCount; i++) {
			poseStack.pushPose();

			poseStack.pushPose();
			boolean isBlockItem = itemRenderer.getModel(itemStack, cuttingBoard.getLevel(), null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
			poseStack.popPose();

			float xOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
			float zOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

			if (cuttingBoard.isItemCarvingBoard()) {
				renderItemCarved(poseStack, direction, itemStack);
			} else if (isBlockItem && !itemStack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD)) {
				renderBlock(poseStack, direction, xOffset, i, zOffset);
			} else {
				renderItemLayingDown(poseStack, direction, xOffset, i, zOffset);
			}

			Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, cuttingBoard.getLevel(), posLong);
			poseStack.popPose();
		}
	}

	public void renderItemLayingDown(PoseStack matrixStackIn, Direction direction, float xOffset, int yIndex, float zOffset) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D + xOffset, 0.08D + 0.03 * (yIndex + 1), 0.5D + zOffset);

		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

		// Rotate item flat on the cutting board. Use X and Y from now on
		matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	public void renderBlock(PoseStack matrixStackIn, Direction direction, float xOffset, int yIndex, float zOffset) {
		// Center block above the cutting board
		matrixStackIn.translate(0.5D + xOffset, 0.27D + 0.03 * (yIndex + 1), 0.5D + zOffset);

		// Rotate block to face the cutting board's front side
		float f = -direction.toYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

		// Resize the block
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}

	public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.23D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot() + 180;
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

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
		matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	protected int getModelCount(ItemStack stack) {
		int modelCount = 1;

		if (stack.getCount() > 1) {
			modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4);
		}

		return modelCount;
	}
}
