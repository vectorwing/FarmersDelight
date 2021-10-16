package vectorwing.farmersdelight.client.tileentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.util.FormattedCharSequence;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.blocks.signs.ICanvasSign;
import vectorwing.farmersdelight.utils.ModAtlases;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CanvasSignTileEntityRenderer extends SignRenderer
{
	public static final float TEXT_LINE_HEIGHT = 10;
	public static final float TEXT_VERTICAL_OFFSET = 19;

	private final SignRenderer.SignModel model;
	private final Font font;

	public CanvasSignTileEntityRenderer(BlockEntityRendererProvider.Context context) {
		// TODO: Study whether you gotta map each dye color here, like Signs do to WoodType
		super(context);
		this.model = new SignModel(context.bakeLayer(ModelLayers.createSignModelName(WoodType.SPRUCE)));
		this.font = context.getFont();
	}

	@Override
	public void render(SignBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		BlockState state = tileEntityIn.getBlockState();

		poseStack.pushPose();

		// Determine which kind of sign to render, and its rotation angle
		if (state.getBlock() instanceof StandingSignBlock) {
			poseStack.translate(0.5D, 0.5D, 0.5D);
			float angle = -((float) (state.getValue(StandingSignBlock.ROTATION) * 360) / 16.0F);
			poseStack.mulPose(Vector3f.YP.rotationDegrees(angle));
			this.model.stick.visible = true;
		} else {
			poseStack.translate(0.5D, 0.5D, 0.5D);
			float angle = -state.getValue(WallSignBlock.FACING).toYRot();
			poseStack.mulPose(Vector3f.YP.rotationDegrees(angle));
			poseStack.translate(0.0D, -0.3125D, -0.4375D);
			this.model.stick.visible = false;
		}

		// Render the sign
		poseStack.pushPose();
		float signScale = 0.6666667F;
		poseStack.scale(signScale, -signScale, -signScale);
		Material material = getMaterial(state.getBlock());
		VertexConsumer vertexBuilder = material.buffer(bufferIn, this.model::renderType);
		this.model.root.render(poseStack, vertexBuilder, combinedLightIn, combinedOverlayIn);
//		this.model.stick.render(poseStack, vertexBuilder, combinedLightIn, combinedOverlayIn);
		poseStack.popPose();

		// Decorate the sign's text
		//Font fontRenderer = this.renderer.getFont();
		float textScale = 0.010416667F;
		poseStack.translate(0.0D, 0.33333334F, 0.046666667F);
		poseStack.scale(textScale, -textScale, textScale);
		int colorCode = tileEntityIn.getColor().getTextColor();
		double textBrightness = 0.6D;
		int red = (int) ((double) NativeImage.getR(colorCode) * textBrightness);
		int green = (int) ((double) NativeImage.getG(colorCode) * textBrightness);
		int blue = (int) ((double) NativeImage.getB(colorCode) * textBrightness);
		int textColor = NativeImage.combine(0, blue, green, red);

		// Render the sign's text
//		for (int i = 0; i < 4; ++i) {
//			FormattedCharSequence reorderingProcessor = tileEntityIn.getRenderMessage(i, (textProps) -> {
//				List<FormattedCharSequence> textLines = fontRenderer.split(textProps, 90);
//				return textLines.isEmpty() ? FormattedCharSequence.EMPTY : textLines.get(0);
//			});
//			if (reorderingProcessor != null) {
//				float x = (float) (-fontRenderer.width(reorderingProcessor) / 2);
//				float y = i * TEXT_LINE_HEIGHT - TEXT_VERTICAL_OFFSET;
//				this.font.drawInBatch(reorderingProcessor, x, y, textColor, false, poseStack.last().pose(), bufferIn, false, 0, combinedLightIn);
//			}
//		}

		poseStack.popPose();
	}

	public static Material getMaterial(Block blockIn) {
		DyeColor dye = null;
		if (blockIn instanceof ICanvasSign) {
			dye = ((ICanvasSign) blockIn).getBackgroundColor();
		}

		return dye != null ? ModAtlases.DYED_CANVAS_SIGN_MATERIALS.get(dye) : ModAtlases.BLANK_CANVAS_SIGN_MATERIAL;
	}
}
