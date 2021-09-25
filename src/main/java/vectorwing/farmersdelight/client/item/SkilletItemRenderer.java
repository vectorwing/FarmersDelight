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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3f;

public class SkilletItemRenderer extends ItemStackTileEntityRenderer
{
	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(buffer, RenderType.getSolid(), true, stack.hasEffect());
		IBakedModel mainModel = Minecraft.getInstance()
				.getItemRenderer()
				.getItemModelWithOverrides(stack, null, null);

		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			matrixStack.push();

			// Render skillet
			Minecraft.getInstance().getItemRenderer().renderModel(mainModel, stack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);

			// Render food item
			matrixStack.translate(0.5D, 0.1D, 0.5D);
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
			matrixStack.rotate(Vector3f.ZP.rotationDegrees(180));
			matrixStack.scale(0.6F, 0.6F, 0.6F);

			ItemStack cookingStack = ItemStack.read(tag.getCompound("Cooking"));
			Minecraft.getInstance().getItemRenderer().renderItem(cookingStack, ItemCameraTransforms.TransformType.FIXED, combinedLight, combinedOverlay, matrixStack, buffer);

			matrixStack.pop();
		} else {
			Minecraft.getInstance().getItemRenderer().renderModel(mainModel, stack, combinedLight, combinedOverlay, matrixStack, ivertexbuilder);
		}
	}
}
