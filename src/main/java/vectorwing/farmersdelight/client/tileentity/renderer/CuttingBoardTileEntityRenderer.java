package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import vectorwing.farmersdelight.blocks.CuttingBoardBlock;
import vectorwing.farmersdelight.tile.CuttingBoardTileEntity;

public class CuttingBoardTileEntityRenderer extends TileEntityRenderer<CuttingBoardTileEntity>
{
	public CuttingBoardTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(CuttingBoardTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(CuttingBoardBlock.HORIZONTAL_FACING).getOpposite();
		ItemStack itemStack = tileEntityIn.getStoredItem();

		if (!itemStack.isEmpty()) {
			matrixStackIn.push();

			ItemRenderer itemRenderer = Minecraft.getInstance()
					.getItemRenderer();
			boolean blockItem = itemRenderer.getItemModelWithOverrides(itemStack, tileEntityIn.getWorld(), null)
					.isGui3d();

			if (tileEntityIn.getIsItemCarvingBoard()) {
				renderItemCarved(matrixStackIn, direction, itemStack);
			} else if (blockItem) {
				renderBlock(matrixStackIn, direction);
			} else {
				renderItemLayingDown(matrixStackIn, direction);
			}

			Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
			matrixStackIn.pop();
		}
	}

	public void renderItemLayingDown(MatrixStack matrixStackIn, Direction direction) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.08D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.getHorizontalAngle();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

		// Rotate item flat on the cutting board. Use X and Y from now on
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	public void renderBlock(MatrixStack matrixStackIn, Direction direction) {
		// Center block above the cutting board
		matrixStackIn.translate(0.5D, 0.27D, 0.5D);

		// Rotate block to face the cutting board's front side
		float f = -direction.getHorizontalAngle();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

		// Resize the block
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}

	public void renderItemCarved(MatrixStack matrixStackIn, Direction direction, ItemStack itemStack) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.25D, 0.5D);

		// Rotate item to face the cutting board's front side
		float f = -direction.getHorizontalAngle();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

		// Rotate item to be carved on the surface, A little less so for hoes and pickaxes.
		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(itemStack.getItem() instanceof PickaxeItem || itemStack.getItem() instanceof HoeItem ? 225.0F : 180.0F));

		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}
}
