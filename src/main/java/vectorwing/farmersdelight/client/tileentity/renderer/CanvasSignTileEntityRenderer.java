package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.blocks.signs.ICanvasSign;
import vectorwing.farmersdelight.utils.ModAtlases;

import java.util.List;

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

		matrixStackIn.pushPose();

		// Determine which kind of sign to render, and its rotation angle
		if (state.getBlock() instanceof StandingSignBlock) {
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			float angle = -((float) (state.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(angle));
			this.model.stick.visible = true;
		} else {
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			float angle = -state.getValue(WallSignBlock.FACING).toYRot();
			matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(angle));
			matrixStackIn.translate(0.0D, -0.3125D, -0.4375D);
			this.model.stick.visible = false;
		}

		// Render the sign
		matrixStackIn.pushPose();
		float signScale = 0.6666667F;
		matrixStackIn.scale(signScale, -signScale, -signScale);
		RenderMaterial material = getMaterial(state.getBlock());
		IVertexBuilder vertexBuilder = material.buffer(bufferIn, this.model::renderType);
		this.model.sign.render(matrixStackIn, vertexBuilder, combinedLightIn, combinedOverlayIn);
		this.model.stick.render(matrixStackIn, vertexBuilder, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();

		// Decorate the sign's text
		FontRenderer fontRenderer = this.renderer.getFont();
		float textScale = 0.010416667F;
		matrixStackIn.translate(0.0D, 0.33333334F, 0.046666667F);
		matrixStackIn.scale(textScale, -textScale, textScale);
		int colorCode = tileEntityIn.getColor().getTextColor();
		double textBrightness = 0.6D;
		int red = (int) ((double) NativeImage.getR(colorCode) * textBrightness);
		int green = (int) ((double) NativeImage.getG(colorCode) * textBrightness);
		int blue = (int) ((double) NativeImage.getB(colorCode) * textBrightness);
		int textColor = NativeImage.combine(0, blue, green, red);

		// Render the sign's text
		for (int i = 0; i < 4; ++i) {
			IReorderingProcessor reorderingProcessor = tileEntityIn.getRenderMessage(i, (textProps) -> {
				List<IReorderingProcessor> textLines = fontRenderer.split(textProps, 90);
				return textLines.isEmpty() ? IReorderingProcessor.EMPTY : textLines.get(0);
			});
			if (reorderingProcessor != null) {
				float x = (float) (-fontRenderer.width(reorderingProcessor) / 2);
				float y = i * TEXT_LINE_HEIGHT - TEXT_VERTICAL_OFFSET;
				fontRenderer.drawInBatch(reorderingProcessor, x, y, textColor, false, matrixStackIn.last().pose(), bufferIn, false, 0, combinedLightIn);
			}
		}

		matrixStackIn.popPose();
	}

	public static RenderMaterial getMaterial(Block blockIn) {
		DyeColor dye = null;
		if (blockIn instanceof ICanvasSign) {
			dye = ((ICanvasSign) blockIn).getBackgroundColor();
		}

		return dye != null ? ModAtlases.DYED_CANVAS_SIGN_MATERIALS.get(dye) : ModAtlases.BLANK_CANVAS_SIGN_MATERIAL;
	}
}
