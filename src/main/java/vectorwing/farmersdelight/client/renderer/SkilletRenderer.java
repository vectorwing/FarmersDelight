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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity, SkilletRenderer.SkilletRenderState>
{
	private final Random random = new Random();
	private final ItemModelResolver itemModelResolver;

	public SkilletRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public SkilletRenderState createRenderState() {
		return new SkilletRenderState();
	}

	@Override
	public void extractRenderState(SkilletBlockEntity skillet, SkilletRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(skillet, state, partialTicks, cameraPosition, breakProgress);

		state.direction = skillet.getBlockState().getValue(StoveBlock.FACING);
		state.items.clear();

		IItemHandler inventory = skillet.getInventory();
		int posLong = (int) skillet.getBlockPos().asLong();

		ItemStack stack = inventory.getStackInSlot(0);
		if (stack.isEmpty() || skillet.getLevel() == null) {
			return;
		}

		int seed = Item.getId(stack.getItem()) + stack.getDamageValue();
		this.random.setSeed(seed);

		int itemRenderCount = this.getModelCount(stack);
		for (int i = 0; i < itemRenderCount; i++) {
			ItemStackRenderState itemState = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.FIXED, skillet.getLevel(), null, posLong);

			float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
			float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

			state.items.add(new RenderedItem(itemState, xOffset, zOffset));
		}
	}

	@Override
	public void submit(SkilletRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		for (int i = 0; i < state.items.size(); i++) {
			RenderedItem rendered = state.items.get(i);
			if (rendered.itemState().isEmpty()) continue;

			poseStack.pushPose();

			// Stack up items in the skillet, with a slight offset per item
			poseStack.translate(0.5D + rendered.xOffset(), 0.1D + 0.03 * (i + 1), 0.5D + rendered.zOffset());

			// Rotate item to face the skillet's front side
			float degrees = -state.direction.toYRot();
			poseStack.mulPose(Axis.YP.rotationDegrees(degrees));

			// Rotate item flat on the skillet. Use X and Y from now on
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

			// Resize the items
			poseStack.scale(0.5F, 0.5F, 0.5F);

			rendered.itemState().submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	protected int getModelCount(ItemStack stack) {
		int modelCount = 1;

		if (stack.getCount() > 1) {
			modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4);
		}

		return modelCount;
	}

	public static class SkilletRenderState extends BlockEntityRenderState
	{
		public final List<RenderedItem> items = new ArrayList<>();
		public Direction direction = Direction.NORTH;
	}

	public record RenderedItem(ItemStackRenderState itemState, float xOffset, float zOffset) {}
}
