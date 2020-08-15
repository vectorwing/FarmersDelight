package vectorwing.farmersdelight.client.tileentity.renderer;

import net.minecraft.client.renderer.WorldRenderer;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.tile.StoveTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec2f;

public class StoveTileEntityRenderer extends TileEntityRenderer<StoveTileEntity>
{
	public StoveTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	public void render(StoveTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(StoveBlock.FACING).getOpposite();
		NonNullList<ItemStack> nonnulllist = tileEntityIn.getInventory();

		for(int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = nonnulllist.get(i);
			if (!itemstack.isEmpty()) {
				matrixStackIn.push();

				// Center item above the stove
				matrixStackIn.translate(0.5D, 1.02D, 0.5D);

				// Rotate item to face the stove's front side
				float f = -direction.getHorizontalAngle();
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

				// Rotate item flat on the stove. Use X and Y from now on
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

				// Neatly align items according to their index
				Vec2f itemOffset = tileEntityIn.getStoveItemOffset(i);
				matrixStackIn.translate(itemOffset.x, itemOffset.y, 0.0D);

				// Resize the items
				matrixStackIn.scale(0.375F, 0.375F, 0.375F);

				Minecraft.getInstance().getItemRenderer().renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, WorldRenderer.getCombinedLight(tileEntityIn.getWorld(), tileEntityIn.getPos().up()), combinedOverlayIn, matrixStackIn, bufferIn);
				matrixStackIn.pop();
			}
		}
	}
}