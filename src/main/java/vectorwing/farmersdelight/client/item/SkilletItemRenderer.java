package vectorwing.farmersdelight.client.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;
import vectorwing.farmersdelight.registry.ModItems;

public class SkilletItemRenderer extends ItemStackTileEntityRenderer
{
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(buffer, RenderType.getSolid(), true, stack.hasEffect());
		IBakedModel mainModel = Minecraft.getInstance()
				.getItemRenderer()
				.getItemModelWithOverrides(stack, null, null);

		Minecraft.getInstance().getItemRenderer().renderModel(mainModel, stack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);

		matrixStack.push();
		matrixStack.translate(0.5D, 0.1D, 0.5D);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
		matrixStack.scale(0.5F, 0.5F, 0.5F);
		Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(Items.CHICKEN), ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);
		matrixStack.pop();
	}
}
