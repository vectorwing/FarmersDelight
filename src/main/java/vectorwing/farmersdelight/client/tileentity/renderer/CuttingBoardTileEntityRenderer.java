package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import vectorwing.farmersdelight.blocks.CuttingBoardBlock;
import vectorwing.farmersdelight.tile.CuttingBoardTileEntity;

public class CuttingBoardTileEntityRenderer extends TileEntityRenderer<CuttingBoardTileEntity>
{
	public CuttingBoardTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) { super(rendererDispatcherIn); }

	@Override
	public void render(CuttingBoardTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		Direction direction = tileEntityIn.getBlockState().get(CuttingBoardBlock.FACING).getOpposite();
		ItemStack itemStack = tileEntityIn.getItem();

		if (!itemStack.isEmpty()) {
			matrixStackIn.push();

			// Center item above the cutting board
			matrixStackIn.translate(0.5D, 0.08D, 0.5D);

			// Rotate item to face the cutting board's front side
			float f = -direction.getHorizontalAngle();
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

			// Rotate item flat on the cutting board. Use X and Y from now on
			matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

			// Resize the item
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);

			Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			matrixStackIn.pop();
		}
	}
}
