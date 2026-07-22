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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.client.renderer.state.CuttingBoardRenderState;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity, CuttingBoardRenderState>
{
    private final ItemModelResolver itemModelResolver;

	public CuttingBoardRenderer(BlockEntityRendererProvider.Context pContext) {
		itemModelResolver = pContext.itemModelResolver();
	}

	@Override
	public CuttingBoardRenderState createRenderState() {
		return new CuttingBoardRenderState();
	}

	@Override
	public void extractRenderState(CuttingBoardBlockEntity cuttingBoardEntity, CuttingBoardRenderState renderState, float partialTick, @NonNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(cuttingBoardEntity, renderState, partialTick, cameraPosition, breakProgress);

		renderState.direction = cuttingBoardEntity.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();

		ItemStack boardStack = cuttingBoardEntity.getStoredItem();
		renderState.boardStack = boardStack.copy();

		int posLong = (int) cuttingBoardEntity.getBlockPos().asLong();
		int seed = boardStack.isEmpty() ? 187 : Item.getId(boardStack.getItem()) + boardStack.getDamageValue();
		renderState.random.setSeed(seed);

		renderState.displayItem = new ItemStackRenderState();
		this.itemModelResolver.updateForTopItem(renderState.displayItem, boardStack, ItemDisplayContext.FIXED, cuttingBoardEntity.getLevel(), null, posLong);
		renderState.isItemCarvingBoard = cuttingBoardEntity.isItemCarvingBoard();
	}

    @Override
    public void submit(CuttingBoardRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector nodeCollector, @NonNull CameraRenderState camera) {
        Direction direction = state.direction;
        ItemStack boardStack = state.boardStack;

        int itemRenderCount = this.getModelCount(boardStack);

        for (int i = 0; i < itemRenderCount; i++) {
            poseStack.pushPose();

			poseStack.pushPose();

			boolean isBlockItem = state.displayItem.usesBlockLight();

			float xOffset = itemRenderCount == 1 ? 0 : (state.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
			float zOffset = itemRenderCount == 1 ? 0 : (state.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

			if (state.isItemCarvingBoard) {
				renderItemCarved(poseStack, direction, boardStack);
			} else if (isBlockItem && !boardStack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD)) {
				renderBlock(poseStack, direction, xOffset, i, zOffset);
			} else {
				renderItemLayingDown(poseStack, direction, xOffset, i, zOffset);
			}


			state.displayItem.submit(poseStack, nodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();


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
		if (itemStack.is(ItemTags.PICKAXES)|| toolItem instanceof HoeItem) {
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

