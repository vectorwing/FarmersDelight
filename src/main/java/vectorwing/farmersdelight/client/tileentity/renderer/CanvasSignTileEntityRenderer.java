package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.utils.ModAtlases;

import java.util.List;

/**
 * TODO: There might be cleaner ways to execute what you want with this TER.
 *
 * Canvas Sign rendering involves a few tweaks for it to look proper in-game.
 * Make sure to decide later whether these changes are better mixed-in or copy-pasted like this.
 *
 * Render tweaks:
 * - Hijacking getMaterial to return FD's dye-based RenderMaterials;
 * - Brighter text tones to fit the backgrounds more nicely;
 */

@OnlyIn(Dist.CLIENT)
public class CanvasSignTileEntityRenderer extends SignTileEntityRenderer
{
	public static final float TEXT_LINE_HEIGHT = 10;
	public static final float TEXT_VERTICAL_OFFSET = 19;

	private final SignTileEntityRenderer.SignModel model = new SignTileEntityRenderer.SignModel();

	public CanvasSignTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(SignTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		BlockState state = tileEntityIn.getBlockState();

		matrixStackIn.push();

		// Determine which kind of sign to render, and its rotation angle
		if (state.getBlock() instanceof StandingSignBlock) {
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			float angle = -((float)(state.get(StandingSignBlock.ROTATION) * 360) / 16.0F);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(angle));
			this.model.signStick.showModel = true;
		} else {
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			float angle = -state.get(WallSignBlock.FACING).getHorizontalAngle();
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(angle));
			matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);
			this.model.signStick.showModel = false;
		}

		// Render the sign
		matrixStackIn.push();
		float signScale = 0.6666667F;
		matrixStackIn.scale(signScale, -signScale, -signScale);
		RenderMaterial material = getMaterial(state.getBlock());
		IVertexBuilder vertexBuilder = material.getBuffer(bufferIn, this.model::getRenderType);
		this.model.signBoard.render(matrixStackIn, vertexBuilder, combinedLightIn, combinedOverlayIn);
		this.model.signStick.render(matrixStackIn, vertexBuilder, combinedLightIn, combinedOverlayIn);
		matrixStackIn.pop();

		// Decorate the sign's text
		FontRenderer fontRenderer = this.renderDispatcher.getFontRenderer();
		float textScale = 0.010416667F;
		matrixStackIn.translate(0.0D, 0.33333334F, 0.046666667F);
		matrixStackIn.scale(textScale, -textScale, textScale);
		int colorCode = tileEntityIn.getTextColor().getTextColor();
		double textBrightness = 0.5D;
		int red = (int)((double)NativeImage.getRed(colorCode) * textBrightness);
		int green = (int)((double)NativeImage.getGreen(colorCode) * textBrightness);
		int blue = (int)((double)NativeImage.getBlue(colorCode) * textBrightness);
		int textColor = NativeImage.getCombined(0, blue, green, red);

		// Render the sign's text
		for(int i = 0; i < 4; ++i) {
			IReorderingProcessor reorderingProcessor = tileEntityIn.reorderText(i, (textProps) -> {
				List<IReorderingProcessor> textLines = fontRenderer.trimStringToWidth(textProps, 90);
				return textLines.isEmpty() ? IReorderingProcessor.field_242232_a : textLines.get(0);
			});
			if (reorderingProcessor != null) {
				float x = (float)(-fontRenderer.func_243245_a(reorderingProcessor) / 2);
				float y = i * TEXT_LINE_HEIGHT - TEXT_VERTICAL_OFFSET;
				fontRenderer.drawEntityText(reorderingProcessor, x, y, textColor, false, matrixStackIn.getLast().getMatrix(), bufferIn, false, 0, combinedLightIn);
			}
		}

		matrixStackIn.pop();
	}

	public static RenderMaterial getMaterial(Block blockIn) {
		return ModAtlases.CANVAS_SIGN_BLANK_MATERIAL;
	}
}
