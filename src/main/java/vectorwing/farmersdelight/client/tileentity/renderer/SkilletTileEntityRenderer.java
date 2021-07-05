package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.IItemHandler;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.tile.SkilletTileEntity;

public class SkilletTileEntityRenderer extends TileEntityRenderer<SkilletTileEntity>
{
	public SkilletTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(SkilletTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(StoveBlock.HORIZONTAL_FACING);
		IItemHandler inventory = tileEntityIn.getInventory();

		ItemStack stack = inventory.getStackInSlot(0);

		if (!stack.isEmpty()) {
			matrixStackIn.push();

			// Center item on top of the skillet
			matrixStackIn.translate(0.5D, 0.1D, 0.5D);

			// Rotate item to face the stove's front side
			float degrees = -direction.getHorizontalAngle();
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(degrees));

			// Rotate item flat on the stove. Use X and Y from now on
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

			// Resize the items
			matrixStackIn.scale(0.6F, 0.6F, 0.6F);

			if (tileEntityIn.getWorld() != null)
				Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			matrixStackIn.pop();
		}
	}
}
