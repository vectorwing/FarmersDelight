package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.tile.StoveTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoveTileEntityRenderer extends TileEntityRenderer<StoveTileEntity>
{
	private static final Minecraft MC = Minecraft.getInstance();

	public StoveTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(StoveTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Direction direction = tileEntityIn.getBlockState().get(StoveBlock.HORIZONTAL_FACING).getOpposite();

		ItemStackHandler inventory = tileEntityIn.getInventory();

		for (int i = 0; i < inventory.getSlots(); ++i) {
			ItemStack stoveStack = inventory.getStackInSlot(i);
			if (!stoveStack.isEmpty()) {
				matrixStackIn.push();

				// Center item above the stove
				matrixStackIn.translate(0.5D, 1.02D, 0.5D);

				// Rotate item to face the stove's front side
				float f = -direction.getHorizontalAngle();
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

				// Rotate item flat on the stove. Use X and Y from now on
				matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));

				// Neatly align items according to their index
				Vector2f itemOffset = tileEntityIn.getStoveItemOffset(i);
				matrixStackIn.translate(itemOffset.x, itemOffset.y, 0.0D);

				// Resize the items
				matrixStackIn.scale(0.375F, 0.375F, 0.375F);

				if (tileEntityIn.getWorld() != null)
					MC.getItemRenderer().renderItem(stoveStack, ItemCameraTransforms.TransformType.FIXED, WorldRenderer.getCombinedLight(tileEntityIn.getWorld(), tileEntityIn.getPos().up()), combinedOverlayIn, matrixStackIn, bufferIn);
				matrixStackIn.pop();
			}
		}
	}
}