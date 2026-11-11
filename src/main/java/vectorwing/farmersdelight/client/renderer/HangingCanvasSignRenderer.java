package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.Model;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.state.HangingSignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.block.entity.HangingCanvasSignBlockEntity;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

import javax.annotation.Nullable;
import java.util.List;

// TODO this is gross and duplicates a bunch of code, but it works for now
public class HangingCanvasSignRenderer implements BlockEntityRenderer<HangingCanvasSignBlockEntity, HangingCanvasSignRenderer.HangingCanvasSignRenderState>
{
	private static final int OUTLINE_RENDER_DISTANCE = Mth.square(16);

	private final Model.Simple wallModel;
	private final Model.Simple ceilingModel;
	private final Model.Simple middleModel;
	private final SpriteGetter sprites;
	private final Font font;

	public HangingCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		this.wallModel = HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.WALL);
		this.ceilingModel = HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.CEILING);
		this.middleModel = HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.CEILING_MIDDLE);

		this.font = context.font();
		this.sprites = context.sprites();
	}

	@Override
	public HangingCanvasSignRenderState createRenderState() {
		return new HangingCanvasSignRenderState();
	}

	@Override
	public void submit(HangingCanvasSignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		this.submitSignWithText(state, poseStack, state.breakProgress, submitNodeCollector);
	}

	private void submitSignWithText(HangingCanvasSignRenderState state, PoseStack poseStack, ModelFeatureRenderer.CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector) {
		Model.Simple bodyModel = getSignModel(state.attachmentType);
		poseStack.pushPose();
		poseStack.mulPose(state.transformations.body());
		submitNodeCollector.submitModel(bodyModel, Unit.INSTANCE, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, ModAtlases.getHangingCanvasSignSprite(state.dye), this.sprites, 0, breakProgress);
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

	private void submitSignText(HangingCanvasSignRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, SignText signText) {
		int signMidpoint = 4 * state.textLineHeight / 2;
		FormattedCharSequence[] formattedLines = signText.getRenderMessages(state.isTextFilteringEnabled, (input) -> {
			List<FormattedCharSequence> components = this.font.split(input, state.maxTextLineWidth);
			return components.isEmpty() ? FormattedCharSequence.EMPTY : components.get(0);
		});
		int darkColor;
		int textColor;
		boolean drawOutline;
		int lightVal;
		if (signText.hasGlowingText()) {
			darkColor = getDarkColor(signText, true);
			textColor = signText.getColor().getTextColor();
			drawOutline = textColor == DyeColor.BLACK.getTextColor() || state.drawOutline;
			lightVal = 15728880;
		} else {
			darkColor = getDarkColor(signText, false);
			textColor = darkColor;
			drawOutline = false;
			lightVal = state.lightCoords;
		}

		for(int i = 0; i < 4; ++i) {
			FormattedCharSequence actualLine = formattedLines[i];
			float x1 = (float)(-this.font.width(actualLine) / 2);
			submitNodeCollector.submitText(poseStack, x1, (float)(i * state.textLineHeight - signMidpoint), actualLine, false, Font.DisplayMode.POLYGON_OFFSET, lightVal, textColor, 0, drawOutline ? darkColor : 0);
		}
	}

	private Model.Simple getSignModel(HangingSignBlock.Attachment attachment) {
		return switch(attachment) {
			case WALL -> wallModel;
			case CEILING -> ceilingModel;
			case CEILING_MIDDLE -> middleModel;
		};
	}

	private static boolean isOutlineVisible(BlockPos pos) {
		Minecraft minecraft = Minecraft.getInstance();
		LocalPlayer player = minecraft.player;
		if (player != null && minecraft.options.getCameraType().isFirstPerson() && player.isScoping()) {
			return true;
		} else {
			Entity camera = minecraft.getCameraEntity();
			return camera != null && camera.distanceToSqr(Vec3.atCenterOf(pos)) < (double)OUTLINE_RENDER_DISTANCE;
		}
	}

	protected static int getDarkColor(SignText text, boolean isOutlineVisible) {
		int textColor = text.getColor().getTextColor();
		float brightness = isOutlineVisible ? 0.4f : 0.6f;
		return textColor == DyeColor.BLACK.getTextColor() && text.hasGlowingText() ? -988212 : ARGB.scaleRGB(textColor, brightness);
	}

	@Override
	public void extractRenderState(HangingCanvasSignBlockEntity blockEntity, HangingCanvasSignRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.maxTextLineWidth = blockEntity.getMaxTextLineWidth();
		state.textLineHeight = blockEntity.getTextLineHeight();
		state.frontText = blockEntity.getFrontText();
		state.backText = blockEntity.getBackText();
		state.isTextFilteringEnabled = Minecraft.getInstance().isTextFilteringEnabled();
		state.drawOutline = isOutlineVisible(blockEntity.getBlockPos());
		state.woodType = SignBlock.getWoodType(blockEntity.getBlockState().getBlock());

		BlockState blockState = blockEntity.getBlockState();
		state.attachmentType = HangingSignBlock.getAttachmentPoint(blockState);
		if (blockState.getBlock() instanceof WallSignBlock) {
			state.transformations = HangingSignRenderer.TRANSFORMATIONS.wallTransformation(blockState.getValue(WallSignBlock.FACING));
		} else {
			state.transformations = HangingSignRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(StandingSignBlock.ROTATION));
		}

		state.dye = ((CanvasSign)blockState.getBlock()).getBackgroundColor();
	}

	@Override
	public AABB getRenderBoundingBox(HangingCanvasSignBlockEntity blockEntity) {
		if (blockEntity.getBlockState().getBlock() instanceof StandingSignBlock) {
			BlockPos pos = blockEntity.getBlockPos();
			return new AABB(pos.getX(), pos.getY(), pos.getZ(), (double)pos.getX() + (double)1.0F, (double)pos.getY() + (double)1.125F, (double)pos.getZ() + (double)1.0F);
		} else {
			return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity);
		}
	}

	public static class HangingCanvasSignRenderState extends HangingSignRenderState
	{
		@Nullable public DyeColor dye;
	}
}
