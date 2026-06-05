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
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
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
	public void submit(CuttingBoardRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		if (state.itemRenderState == null) {
			return;
		}

		this.random.setSeed(state.seed);

		for (int i = 0; i < state.modelCount; i++) {
			poseStack.pushPose();

			float xOffset = state.modelCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
			float zOffset = state.modelCount == 1 ? 0 : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

			if (state.carvesBoard) {
				renderItemCarved(poseStack, state);
			} else if (state.isBlockItem && !state.rendersFlat) {
				renderBlock(poseStack, state, xOffset, i, zOffset);
			} else {
				renderItemLayingDown(poseStack, state, xOffset, i, zOffset);
			}

			state.itemRenderState.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	public void renderItemLayingDown(PoseStack matrixStackIn, CuttingBoardRenderState state, float xOffset, int yIndex, float zOffset) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D + xOffset, 0.08D + 0.03 * (yIndex + 1), 0.5D + zOffset);

		// Rotate item to face the cutting board's front side
		float f = -state.direction.toYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

		// Rotate item flat on the cutting board. Use X and Y from now on
		matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	public void renderBlock(PoseStack matrixStackIn, CuttingBoardRenderState state, float xOffset, int yIndex, float zOffset) {
		// Center block above the cutting board
		matrixStackIn.translate(0.5D + xOffset, 0.27D + 0.03 * (yIndex + 1), 0.5D + zOffset);

		// Rotate block to face the cutting board's front side
		float f = -state.direction.toYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

		// Resize the block
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}

	public void renderItemCarved(PoseStack matrixStackIn, CuttingBoardRenderState state) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.23D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -state.direction.toYRot() + 180;
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

		// Rotate item to be carved on the surface, A little less so for hoes and pickaxes.
		float poseAngle;
		if (state.isPickaxe || state.isHoe) {
			poseAngle = 225.0F;
		} else if (state.isTrident) {
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

	@Override
	public void extractRenderState(CuttingBoardBlockEntity cuttingBoard, CuttingBoardRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(cuttingBoard, state, partialTicks, cameraPosition, breakProgress);
		state.direction = cuttingBoard.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();

		ItemStack stack = cuttingBoard.getStoredItem();
		if (stack.isEmpty()) {
			state.itemRenderState = null;
		} else {
			state.itemRenderState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(
				state.itemRenderState,
				stack,
				ItemDisplayContext.FIXED,
				cuttingBoard.getLevel(),
				null,
				(int) cuttingBoard.getBlockPos().asLong()
			);


			// TODO this is a very hacky check for pickaxes.
			Item toolItem = stack.getItem();
			state.isPickaxe = toolItem.isCorrectToolForDrops(toolItem.getDefaultInstance(), Blocks.COBBLESTONE.defaultBlockState());
			state.isHoe = toolItem instanceof HoeItem;
			state.isTrident = toolItem instanceof TridentItem;

			state.rendersFlat = stack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD);
			// TODO this will not catch every block. I'm not familiar enough with the new way of doing things
			//      to know if there's a better way to catch this.
			state.isBlockItem = toolItem instanceof BlockItem;
		}

		state.seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
		state.modelCount = this.getModelCount(stack);

		state.carvesBoard = cuttingBoard.isItemCarvingBoard();
	}

	public static class CuttingBoardRenderState extends BlockEntityRenderState {
		public Direction direction;
		public ItemStackRenderState itemRenderState;
		public int seed;
		public int modelCount;
		public boolean rendersFlat = false;
		public boolean carvesBoard;
		public boolean isPickaxe = false;
		public boolean isHoe = false;
		public boolean isTrident = false;
		public boolean isBlockItem = false;
	}
}
