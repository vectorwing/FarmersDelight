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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity, CuttingBoardRenderer.CuttingBoardRenderState>
{
	private final ItemModelResolver itemModelResolver;
	private final Random random = new Random();

	public CuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public CuttingBoardRenderState createRenderState() {
		return new CuttingBoardRenderState();
	}

	@Override
	public void extractRenderState(CuttingBoardBlockEntity cuttingBoard, CuttingBoardRenderState state, float partialTicks, Vec3 cameraPosition,
			ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(cuttingBoard, state, partialTicks, cameraPosition, breakProgress);
		state.direction = cuttingBoard.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();

		ItemStack stack = cuttingBoard.getStoredItem();
		if (stack.isEmpty()) {
			state.items = Collections.emptyList();
			return;
		}

		int modelCount = this.getModelCount(stack);
		int seed = Item.getId(stack.getItem()) + stack.getDamageValue();
		this.random.setSeed(seed);

		List<RenderedItem> items = new ArrayList<>(modelCount);
		for (int i = 0; i < modelCount; ++i) {
			float xOffset = modelCount == 1 ? 0.0F : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
			float zOffset = modelCount == 1 ? 0.0F : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

			ItemStackRenderState itemState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.FIXED, cuttingBoard.getLevel(), null, seed + i);
			items.add(new RenderedItem(itemState, getPoseType(stack, cuttingBoard.isItemCarvingBoard()), xOffset, zOffset, getCarvedToolPoseAngle(stack)));
		}
		state.items = items;
	}

	@Override
	public void submit(CuttingBoardRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		for (int i = 0; i < state.items.size(); ++i) {
			RenderedItem item = state.items.get(i);
			poseStack.pushPose();
			this.applyPose(poseStack, state.direction, item, i);
			item.itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	private static PoseType getPoseType(ItemStack stack, boolean isItemCarvingBoard) {
		if (isItemCarvingBoard) {
			return PoseType.CARVED_TOOL;
		}
		if (stack.getItem() instanceof BlockItem && !stack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD)) {
			return PoseType.BLOCK;
		}
		return PoseType.FLAT_ITEM;
	}

	private void applyPose(PoseStack poseStack, Direction direction, RenderedItem item, int index) {
		switch (item.poseType) {
			case CARVED_TOOL -> this.applyCarvedToolPose(poseStack, direction, item.poseAngle);
			case BLOCK -> this.applyBlockPose(poseStack, direction, item.xOffset, index, item.zOffset);
			case FLAT_ITEM -> this.applyFlatItemPose(poseStack, direction, item.xOffset, index, item.zOffset);
		}
	}

	private void applyFlatItemPose(PoseStack poseStack, Direction direction, float xOffset, int yIndex, float zOffset) {
		poseStack.translate(0.5D + xOffset, 0.08D + 0.03D * (yIndex + 1), 0.5D + zOffset);
		poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		poseStack.scale(0.6F, 0.6F, 0.6F);
	}

	private void applyBlockPose(PoseStack poseStack, Direction direction, float xOffset, int yIndex, float zOffset) {
		poseStack.translate(0.5D + xOffset, 0.27D + 0.03D * (yIndex + 1), 0.5D + zOffset);
		poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
		poseStack.scale(0.8F, 0.8F, 0.8F);
	}

	private void applyCarvedToolPose(PoseStack poseStack, Direction direction, float poseAngle) {
		poseStack.translate(0.5D, 0.23D, 0.5D);
		poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot() + 180.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(poseAngle));
		poseStack.scale(0.6F, 0.6F, 0.6F);
	}

	private static float getCarvedToolPoseAngle(ItemStack stack) {
		Item item = stack.getItem();
		if (stack.is(ItemTags.PICKAXES) || stack.is(ItemTags.HOES)) {
			return 225.0F;
		}
		return item instanceof TridentItem ? 135.0F : 180.0F;
	}

	private int getModelCount(ItemStack stack) {
		int modelCount = 1;
		if (stack.getCount() > 1) {
			modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4.0F);
		}
		return modelCount;
	}

	public static class CuttingBoardRenderState extends BlockEntityRenderState
	{
		public List<RenderedItem> items = Collections.emptyList();
		public Direction direction = Direction.NORTH;
	}

	public record RenderedItem(ItemStackRenderState itemState, PoseType poseType, float xOffset, float zOffset, float poseAngle) {}

	public enum PoseType
	{
		FLAT_ITEM,
		BLOCK,
		CARVED_TOOL
	}
}
