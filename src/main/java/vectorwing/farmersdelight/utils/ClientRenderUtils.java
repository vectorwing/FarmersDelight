package vectorwing.farmersdelight.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

/**
 * Util for helping with rendering elements across the mod, when vanilla methods don't expose enough to use.
 */
public class ClientRenderUtils
{
	/**
	 * Renders an Item into the GUI, allowing the size to be defined instead of hardcoded.
	 * This function is ripped right from the game's rendering code. I am probably doing something stupid.
	 */
	public static void renderItemIntoGUIScalable(ItemStack stack, int width, int height, IBakedModel bakedmodel, ItemRenderer renderer, TextureManager textureManager) {
		RenderSystem.pushMatrix();
		textureManager.bind(AtlasTexture.LOCATION_BLOCKS);
		Texture texture = textureManager.getTexture(AtlasTexture.LOCATION_BLOCKS);
		if (texture != null) {
			texture.setFilter(false, false);
		}
		RenderSystem.enableRescaleNormal();
		RenderSystem.enableAlphaTest();
		RenderSystem.defaultAlphaFunc();
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.translatef((float) 0, (float) 0, 100.0F + renderer.blitOffset);
		RenderSystem.translatef(8.0F, 8.0F, 0.0F);
		RenderSystem.scalef(1.0F, -1.0F, 1.0F);
		RenderSystem.scalef(48.0F, 48.0F, 48.0F);
		MatrixStack matrixStack = new MatrixStack();
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
		boolean flag = !bakedmodel.usesBlockLight();
		if (flag) {
			RenderHelper.setupForFlatItems();
		}

		renderer.render(stack, ItemCameraTransforms.TransformType.GUI, false, matrixStack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
		irendertypebuffer$impl.endBatch();
		RenderSystem.enableDepthTest();
		if (flag) {
			RenderHelper.setupFor3DItems();
		}

		RenderSystem.disableAlphaTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.popMatrix();
	}
}
