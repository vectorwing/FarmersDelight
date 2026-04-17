package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import java.util.Random;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity>
{
	private final Random random = new Random();

	public SkilletRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(SkilletBlockEntity skillet, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		Direction direction = skillet.getBlockState().getValue(StoveBlock.FACING);
		IItemHandler inventory = skillet.getInventory();
		int posLong = (int) skillet.getBlockPos().asLong();

		ItemStack stack = inventory.getStackInSlot(0);
		int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
		this.random.setSeed(seed);

		if (!stack.isEmpty()) {
			int itemRenderCount = this.getModelCount(stack);
			for (int i = 0; i < itemRenderCount; i++) {
				poseStack.pushPose();

				// Stack up items in the skillet, with a slight offset per item
				float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				poseStack.translate(0.5D + xOffset, 0.1D + 0.03 * (i + 1), 0.5D + zOffset);

				// Rotate item to face the skillet's front side
				float degrees = -direction.toYRot();
				poseStack.mulPose(Axis.YP.rotationDegrees(degrees));

				// Rotate item flat on the skillet. Use X and Y from now on
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

				// Resize the items
				poseStack.scale(0.5F, 0.5F, 0.5F);

				if (skillet.getLevel() != null)
					Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, skillet.getLevel(), posLong);
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
}
