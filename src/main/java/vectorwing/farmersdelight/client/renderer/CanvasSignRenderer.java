package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

import java.util.List;

public class CanvasSignRenderer extends AbstractSignRenderer<CanvasSignRenderer.CanvasSignRenderState>
{
	private final Font font;
	private final SpriteGetter sprites;
	private final Model.Simple standingModel;
	private final Model.Simple wallModel;

	public CanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		this.font = context.font();
		this.sprites = context.sprites();
		this.standingModel = StandingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, PlainSignBlock.Attachment.GROUND);
		this.wallModel = StandingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, PlainSignBlock.Attachment.WALL);
	}

	@Override
	public CanvasSignRenderState createRenderState() {
		return new CanvasSignRenderState();
	}

	@Override
	public void extractRenderState(SignBlockEntity sign, CanvasSignRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
		// Sets the base sign render data (text, light, wood type, break progress).
		super.extractRenderState(sign, state, partialTicks, cameraPosition, breakProgress);

		BlockState blockState = sign.getBlockState();
		state.dyeColor = blockState.getBlock() instanceof CanvasSign canvasSign ? canvasSign.getBackgroundColor() : null;
		this.extractSignTransformations(blockState, state);
	}

	/**
	 * Computes the body/text transformations for this sign type. Standing canvas signs reuse the vanilla
	 * standing-sign transformations; the hanging subclass overrides this with the hanging-sign equivalents.
	 */
	protected void extractSignTransformations(BlockState blockState, CanvasSignRenderState state) {
		state.attachmentType = PlainSignBlock.getAttachmentPoint(blockState);
		if (blockState.getBlock() instanceof WallSignBlock) {
			state.transformations = StandingSignRenderer.TRANSFORMATIONS.wallTransformation(blockState.getValue(WallSignBlock.FACING));
		} else {
			state.transformations = StandingSignRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(StandingSignBlock.ROTATION));
		}
	}

	@Override
	protected Model.Simple getSignModel(CanvasSignRenderState state) {
		return state.attachmentType == PlainSignBlock.Attachment.WALL ? this.wallModel : this.standingModel;
	}

	@Override
	protected SpriteId getSignSprite(WoodType type) {
		// Unused: canvas signs choose their sprite from the dye color, see getCanvasSprite.
		return Sheets.getSignSprite(type);
	}

	/**
	 * Resolves the canvas sign sprite from the dyed background color (or the blank canvas material when undyed).
	 */
	protected SpriteId getCanvasSprite(CanvasSignRenderState state) {
		Material material = ModAtlases.getCanvasSignMaterial(state.dyeColor);
		return new SpriteId(Sheets.SIGN_SHEET, material.sprite());
	}

	@Override
	public void submit(CanvasSignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		Model.Simple bodyModel = this.getSignModel(state);
		SpriteId sprite = this.getCanvasSprite(state);

		poseStack.pushPose();
		poseStack.mulPose(state.transformations.body());
		submitNodeCollector.submitModel(bodyModel, Unit.INSTANCE, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, this.sprites, 0, state.breakProgress);
		poseStack.popPose();

		if (state.frontText != null) {
			poseStack.pushPose();
			poseStack.mulPose(state.transformations.frontText());
			this.submitSignText(state, poseStack, submitNodeCollector, state.frontText);
			poseStack.popPose();
		}

		if (state.backText != null) {
			poseStack.pushPose();
			poseStack.mulPose(state.transformations.backText());
			this.submitSignText(state, poseStack, submitNodeCollector, state.backText);
			poseStack.popPose();
		}
	}

	private void submitSignText(CanvasSignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, SignText signText) {
		int darkColor = AbstractSignRenderer.getDarkColor(signText);
		int signMidpoint = 4 * state.textLineHeight / 2;
		FormattedCharSequence[] formattedLines = signText.getRenderMessages(state.isTextFilteringEnabled, input -> {
			List<FormattedCharSequence> components = this.font.split(input, state.maxTextLineWidth);
			return components.isEmpty() ? FormattedCharSequence.EMPTY : components.get(0);
		});

		int textColor;
		boolean drawOutline;
		int lightVal;
		if (signText.hasGlowingText()) {
			textColor = signText.getColor().getTextColor();
			drawOutline = textColor == DyeColor.BLACK.getTextColor() || state.drawOutline;
			lightVal = 15728880;
		} else {
			textColor = darkColor;
			drawOutline = false;
			lightVal = state.lightCoords;
		}

		for (int i = 0; i < 4; i++) {
			FormattedCharSequence actualLine = formattedLines[i];
			float x1 = -this.font.width(actualLine) / 2;
			submitNodeCollector.submitText(
				poseStack,
				x1,
				i * state.textLineHeight - signMidpoint,
				actualLine,
				false,
				Font.DisplayMode.POLYGON_OFFSET,
				lightVal,
				textColor,
				0,
				drawOutline ? darkColor : 0
			);
		}
	}

	public static class CanvasSignRenderState extends net.minecraft.client.renderer.blockentity.state.StandingSignRenderState
	{
		public @Nullable DyeColor dyeColor;
		// Only populated/used by hanging canvas signs (whose attachment enum differs from the standing one).
		public net.minecraft.world.level.block.HangingSignBlock.Attachment hangingAttachment = net.minecraft.world.level.block.HangingSignBlock.Attachment.CEILING;
	}
}
