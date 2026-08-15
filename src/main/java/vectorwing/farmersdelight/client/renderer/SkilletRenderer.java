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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity, SkilletRenderer.SkilletRenderState>
{
	private final ItemModelResolver itemModelResolver;
	private final Random random = new Random();

	public SkilletRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public SkilletRenderState createRenderState() {
		return new SkilletRenderState();
	}

	@Override
	public void extractRenderState(SkilletBlockEntity skillet, SkilletRenderState state, float partialTicks, Vec3 cameraPosition,
			ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(skillet, state, partialTicks, cameraPosition, breakProgress);
		state.facing = skillet.getBlockState().getValue(SkilletBlock.FACING);

		ItemStack stack = skillet.getStoredStack();
		if (stack.isEmpty()) {
			state.items = Collections.emptyList();
			return;
		}

		int modelCount = this.getModelCount(stack);
		int seed = Item.getId(stack.getItem()) + stack.getDamageValue();
		this.random.setSeed(seed);

		List<RenderedItem> items = new ArrayList<>(modelCount);
		for (int i = 0; i < modelCount; ++i) {
			ItemStackRenderState itemState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.FIXED, skillet.getLevel(), null, seed + i);
			items.add(new RenderedItem(itemState, this.randomOffset(), this.randomOffset()));
		}
		state.items = items;
	}

	@Override
	public void submit(SkilletRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		for (int i = 0; i < state.items.size(); ++i) {
			RenderedItem item = state.items.get(i);
			poseStack.pushPose();
			poseStack.translate(0.5D + item.xOffset, 0.1D + 0.03D * (i + 1), 0.5D + item.zOffset);
			poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			poseStack.scale(0.5F, 0.5F, 0.5F);
			item.itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	private float randomOffset() {
		return (this.random.nextFloat() - 0.5F) * 0.2F;
	}

	private int getModelCount(ItemStack stack) {
		int count = stack.getCount();
		if (count > 48) {
			return 5;
		} else if (count > 32) {
			return 4;
		} else if (count > 16) {
			return 3;
		} else {
			return count > 1 ? 2 : 1;
		}
	}

	public static class SkilletRenderState extends BlockEntityRenderState
	{
		public List<RenderedItem> items = Collections.emptyList();
		public Direction facing = Direction.NORTH;
	}

	public record RenderedItem(ItemStackRenderState itemState, float xOffset, float zOffset) {}
}
