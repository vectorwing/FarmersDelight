package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
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
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Random;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity, CuttingBoardRenderer.CuttingBoardRenderState>
{
	private final Random random = new Random();
	private final ItemModelResolver itemModelResolver;

	public CuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public CuttingBoardRenderState createRenderState() {
		return new CuttingBoardRenderState();
	}

	@Override
	public void extractRenderState(CuttingBoardBlockEntity cuttingBoard, CuttingBoardRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(cuttingBoard, state, partialTicks, cameraPosition, breakProgress);

		ItemStack itemStack = cuttingBoard.getStoredItem();
		state.items.clear();
		if (itemStack.isEmpty()) {
			return;
		}

		state.direction = cuttingBoard.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();
		state.isCarving = cuttingBoard.isItemCarvingBoard();
		state.isFlatItem = itemStack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD);
		state.carvedAngle = getCarvedAngle(itemStack.getItem());

		// Item-model seed: the original renderer used the same block-position seed for every stacked copy.
		int seed = (int) cuttingBoard.getBlockPos().asLong();
		int modelSeed = Item.getId(itemStack.getItem()) + itemStack.getDamageValue();
		this.random.setSeed(modelSeed);

		int itemRenderCount = this.getModelCount(itemStack);
		for (int i = 0; i < itemRenderCount; i++) {
			ItemStackRenderState itemState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(itemState, itemStack, ItemDisplayContext.FIXED, cuttingBoard.getLevel(), null, seed);

			float xOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
			float zOffset = itemRenderCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

			state.items.add(new RenderedItem(itemState, xOffset, zOffset, itemState.usesBlockLight()));
		}
	}

	@Override
	public void submit(CuttingBoardRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		for (int i = 0; i < state.items.size(); i++) {
			RenderedItem rendered = state.items.get(i);
			if (rendered.itemState().isEmpty()) {
				continue;
			}

			poseStack.pushPose();

			if (state.isCarving) {
				renderItemCarved(poseStack, state.direction, state.carvedAngle);
			} else if (rendered.isBlock() && !state.isFlatItem) {
				renderBlock(poseStack, state.direction, rendered.xOffset(), i, rendered.zOffset());
			} else {
				renderItemLayingDown(poseStack, state.direction, rendered.xOffset(), i, rendered.zOffset());
			}

			rendered.itemState().submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
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

	public void renderItemCarved(PoseStack matrixStackIn, Direction direction, float poseAngle) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.23D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot() + 180;
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

		// Rotate item to be carved on the surface, A little less so for hoes and pickaxes.
		matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	private static float getCarvedAngle(Item toolItem) {
		if (toolItem.builtInRegistryHolder().is(ItemTags.PICKAXES) || toolItem instanceof HoeItem) {
			return 225.0F;
		} else if (toolItem instanceof TridentItem) {
			return 135.0F;
		} else {
			return 180.0F;
		}
	}

	protected int getModelCount(ItemStack stack) {
		int modelCount = 1;

		if (stack.getCount() > 1) {
			modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4);
		}

		return modelCount;
	}

	public static class CuttingBoardRenderState extends BlockEntityRenderState
	{
		public final java.util.List<RenderedItem> items = new java.util.ArrayList<>();
		public Direction direction = Direction.NORTH;
		public boolean isCarving;
		public boolean isFlatItem;
		public float carvedAngle = 180.0F;
	}

	public record RenderedItem(ItemStackRenderState itemState, float xOffset, float zOffset, boolean isBlock) {}
}
