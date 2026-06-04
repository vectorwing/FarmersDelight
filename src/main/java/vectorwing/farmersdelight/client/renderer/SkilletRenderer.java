package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
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
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import java.util.Random;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity, SkilletRenderer.SkilletRenderState>
{
	private final Random random = new Random();
	private final ItemModelResolver itemModelResolver;

	public SkilletRenderer(BlockEntityRendererProvider.Context context) {
		itemModelResolver = context.itemModelResolver();
	}

	@Override
	public SkilletRenderState createRenderState() {
		return new SkilletRenderState();
	}

	@Override
	public void submit(SkilletRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		this.random.setSeed(state.seed);

		if (!state.contents.isEmpty()) {
			for (int i = 0; i < state.modelCount; i++) {
				poseStack.pushPose();

				// Stack up items in the skillet, with a slight offset per item
				float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				poseStack.translate(0.5D + xOffset, 0.1D + 0.03 * (i + 1), 0.5D + zOffset);

				// Rotate item to face the skillet's front side
				float degrees = -state.direction.toYRot();
				poseStack.mulPose(Axis.YP.rotationDegrees(degrees));

				// Rotate item flat on the skillet. Use X and Y from now on
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

				// Resize the items
				poseStack.scale(0.5F, 0.5F, 0.5F);

				state.contents.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

				poseStack.popPose();
			}
		}
	}

	protected int getModelCount(ItemStack stack) {
		int modelCount = 1;

		if (stack.getCount() > 1) {
			modelCount += Mth.ceil(((float) stack.getCount() / stack.getMaxStackSize()) * 4);
		}

		return modelCount;
	}

	@Override
	public void extractRenderState(SkilletBlockEntity skillet, SkilletRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(skillet, state, partialTicks, cameraPosition, breakProgress);
		state.direction = skillet.getBlockState().getValue(StoveBlock.FACING);

		ItemStack inventory = skillet.getInventory().getStackInSlot(0);
		state.contents = new ItemStackRenderState();
		state.seed = state.contents.isEmpty() ? 187 : Item.getId(inventory.getItem()) + inventory.getDamageValue();;
		this.itemModelResolver.updateForTopItem(
			state.contents,
			inventory,
			ItemDisplayContext.FIXED,
			skillet.getLevel(),
			null,
			state.seed
		);

		state.modelCount = getModelCount(inventory);
	}

	public static class SkilletRenderState extends BlockEntityRenderState {
		public Direction direction;
		public ItemStackRenderState contents;
		public int seed;
		public int modelCount;
	}
}
