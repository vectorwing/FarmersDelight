package vectorwing.farmersdelight.client.tileentity.renderer;

import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.blocks.StoveBlockTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;

public class StoveBlockTileRenderer extends TileEntityRenderer<StoveBlockTile>
{
	public StoveBlockTileRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	public void render(StoveBlockTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(StoveBlock.FACING).getOpposite();
		NonNullList<ItemStack> nonnulllist = tileEntityIn.getInventory();

		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = nonnulllist.get(i);
			if (itemstack != ItemStack.EMPTY) {
				matrixStackIn.push();

				// Center item above the stove
				matrixStackIn.translate(0.5D, 1.01D, 0.5D);

				// Rotate item to face the stove's front side
				float f = -direction.getHorizontalAngle();
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

				// Rotate item flat on the stove. Use X and Y from now on
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

				// Neatly align items according to their index
				Vec2d itemOffset = tileEntityIn.getStoveItemOffset(i);
				matrixStackIn.translate(itemOffset.x, itemOffset.y, 0.0D);

				// Resize the items? I dunno lmao
				matrixStackIn.scale(0.375F, 0.375F, 0.375F);

				Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
				matrixStackIn.pop();
			}
		}
	}
}