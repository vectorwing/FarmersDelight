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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultStoveRenderer implements BlockEntityRenderer<StoveBlockEntity, DefaultStoveRenderer.StoveRenderState>
{
	private final ItemModelResolver itemModelResolver;

	public DefaultStoveRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public StoveRenderState createRenderState() {
		return new StoveRenderState();
	}

	@Override
	public void extractRenderState(StoveBlockEntity stove, StoveRenderState state, float partialTicks, Vec3 cameraPosition,
			ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(stove, state, partialTicks, cameraPosition, breakProgress);
		state.direction = stove.getBlockState().getValue(AbstractStoveBlock.FACING).getOpposite();

		Level level = stove.getLevel();
		var inventory = stove.getItems();
		List<RenderedItem> items = new ArrayList<>();
		for (int i = 0; i < inventory.size(); ++i) {
			ItemStack stack = inventory.getStack(i);
			if (stack.isEmpty()) {
				continue;
			}
			ItemStackRenderState itemState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.FIXED, level, null, (int) stove.getBlockPos().asLong() + i);
			items.add(new RenderedItem(itemState, stove.getStoveItemOffset(i)));
		}
		state.items = items.isEmpty() ? Collections.emptyList() : items;
	}

	@Override
	public void submit(StoveRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		for (RenderedItem item : state.items) {
			poseStack.pushPose();
			poseStack.translate(0.5D, 1.02D, 0.5D);
			poseStack.mulPose(Axis.YP.rotationDegrees(-state.direction.toYRot()));
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			poseStack.translate(item.offset.x, item.offset.y, 0.0D);
			poseStack.scale(0.375F, 0.375F, 0.375F);
			item.itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	public static class StoveRenderState extends BlockEntityRenderState
	{
		public List<RenderedItem> items = Collections.emptyList();
		public Direction direction = Direction.NORTH;
	}

	public record RenderedItem(ItemStackRenderState itemState, Vec2 offset) {}
}
