package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

public class DefaultStoveRenderer<T extends AbstractStoveBlockEntity> implements BlockEntityRenderer<T, DefaultStoveRenderer.AbstractStoveRenderState>
{
	private static final float SIZE = 0.375F;
	private final ItemModelResolver itemModelResolver;

	public DefaultStoveRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public AbstractStoveRenderState createRenderState() {
		return new AbstractStoveRenderState();
	}

	@Override
	public void submit(AbstractStoveRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		for (int i = 0; i < state.slotCount; ++i) {
			ItemStackRenderState stackRenderState = state.itemRenderStates[i];
			if (stackRenderState == null) continue;

			poseStack.pushPose();

			// Center item above the stove
			poseStack.translate(0.5D, 1.02D, 0.5D);

			// Rotate item to face the stove's front side
			float f = -state.direction.toYRot();
			poseStack.mulPose(Axis.YP.rotationDegrees(f));

			// Rotate item flat on the stove. Use X and Y from now on
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

			// Neatly align items according to their index
			Vec2 itemOffset = state.offsets[i];
			poseStack.translate(itemOffset.x, itemOffset.y, 0.0D);

			// Resize the items
			poseStack.scale(SIZE, SIZE, SIZE);

			state.itemRenderStates[i].submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	@Override
	public void extractRenderState(T stove, AbstractStoveRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(stove, state, partialTicks, cameraPosition, breakProgress);
		state.direction = stove.getBlockState().getValue(StoveBlock.FACING).getOpposite();

		// base extractRenderState gets the light coords for the stove's position, but we use the position above the stove
		state.lightCoords = LevelRenderer.getLightCoords(stove.getLevel(), stove.getBlockPos().above());

		var items = stove.getItems();
		state.slotCount = items.size();
		state.offsets = new Vec2[state.slotCount];
		state.itemRenderStates = new ItemStackRenderState[state.slotCount];

		for (int i = 0; i < state.slotCount; i++) {
			ItemStack stack = items.getResource(i).toStack(items.getAmountAsInt(i));
			if (stack.isEmpty()) {
				state.itemRenderStates[i] = null;
				state.offsets[i] = null;
				continue;
			}

			state.itemRenderStates[i] = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(
				state.itemRenderStates[i],
				stack,
				ItemDisplayContext.FIXED,
				stove.getLevel(),
				null,
				(int) stove.getBlockPos().asLong()
			);
			state.offsets[i] = stove.getStoveItemOffset(i);
		}
	}

	public static class AbstractStoveRenderState extends BlockEntityRenderState {
		public Direction direction;
		public int slotCount;
		public Vec2[] offsets;
		public ItemStackRenderState[] itemRenderStates;
	}
}