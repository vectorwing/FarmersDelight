package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.IItemHandler;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.tile.SkilletTileEntity;

import java.util.Random;

public class SkilletTileEntityRenderer extends TileEntityRenderer<SkilletTileEntity>
{
	private final Random random = new Random();

	public SkilletTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(SkilletTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(StoveBlock.HORIZONTAL_FACING);
		IItemHandler inventory = tileEntityIn.getInventory();

		ItemStack stack = inventory.getStackInSlot(0);
		int seed = stack.isEmpty() ? 187 : Item.getIdFromItem(stack.getItem()) + stack.getDamage();
		this.random.setSeed(seed);

		if (!stack.isEmpty()) {
			int itemRenderCount = this.getModelCount(stack);
			for (int i = 0; i < itemRenderCount; i++) {
				matrixStackIn.push();

				// Stack up items in the skillet, with a slight offset per item
				float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				matrixStackIn.translate(0.5D + xOffset, 0.1D + 0.03 * (i + 1), 0.5D + zOffset);

				// Rotate item to face the skillet's front side
				float degrees = -direction.getHorizontalAngle();
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(degrees));

				// Rotate item flat on the skillet. Use X and Y from now on
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

				// Resize the items
				matrixStackIn.scale(0.5F, 0.5F, 0.5F);

				if (tileEntityIn.getWorld() != null)
					Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
				matrixStackIn.pop();
			}
		}
	}

	protected int getModelCount(ItemStack stack) {
		if (stack.getCount() > 48) {
			return 5;
		} else if (stack.getCount() > 32) {
			return 4;
		} else if (stack.getCount() > 16) {
			return 3;
		} else if (stack.getCount() > 1) {
			return 2;
		}
		return 1;
	}
}
