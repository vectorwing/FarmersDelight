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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.StoveRenderState;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

public abstract class AbstractStoveRenderer<T extends AbstractStoveBlockEntity, S extends StoveRenderState> implements BlockEntityRenderer<T, S>
{
	private static final float SIZE = 0.375F;
	private final ItemModelResolver itemModelResolver;

	public AbstractStoveRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public void extractRenderState(@NonNull T blockEntity, @NonNull S state, float partialTicks, @NonNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

		ItemStackRenderState[] stacks = new ItemStackRenderState[blockEntity.getItems().getSlots()];
		BlockPos pos = blockEntity.getBlockPos();
		for (int i = 0; i < blockEntity.getItems().getSlots(); ++i) {
			ItemStack stack = blockEntity.getItems().getStackInSlot(i);
			ItemStackRenderState stackRenderState = new ItemStackRenderState();
			itemModelResolver.updateForTopItem(stackRenderState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, (int) pos.asLong() + i);
			stacks[i] = stackRenderState;
		}

		state.stoveStacks = stacks;
		state.facing = blockEntity.getBlockState().getValue(AbstractStoveBlock.FACING).getOpposite();
	}

	@Override
	public void submit(S state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
		Direction direction = state.facing.getOpposite();

		var items = state.stoveStacks;

		for (int i = 0; i < items.length; ++i) {
			ItemStackRenderState stoveStack = items[i];
			if (stoveStack.isEmpty()) continue;

			poseStack.pushPose();

			// Center item above the stove
			poseStack.translate(0.5D, 1.02D, 0.5D);

			// Rotate item to face the stove's front side
			float f = -direction.toYRot();
			poseStack.mulPose(Axis.YP.rotationDegrees(f));

			// Rotate item flat on the stove. Use X and Y from now on
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

			// Neatly align items according to their index
			Vec2 itemOffset = getStoveItemOffset(i);
			poseStack.translate(itemOffset.x, itemOffset.y, 0.0D);

			// Resize the items
			poseStack.scale(SIZE, SIZE, SIZE);

			stoveStack.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	public abstract Vec2 getStoveItemOffset(int index);
}
