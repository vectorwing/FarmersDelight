package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.HangingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import vectorwing.farmersdelight.common.registry.ModAtlases;

public class HangingCanvasSignRenderer extends CanvasSignRenderer
{
	private final Model.Simple ceilingModel;
	private final Model.Simple ceilingMiddleModel;
	private final Model.Simple wallModel;

	public HangingCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		this.ceilingModel = HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.CEILING);
		this.ceilingMiddleModel = HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.CEILING_MIDDLE);
		this.wallModel = HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.WALL);
	}

	@Override
	protected void extractSignTransformations(BlockState blockState, CanvasSignRenderState state) {
		state.hangingAttachment = HangingSignBlock.getAttachmentPoint(blockState);
		if (blockState.getBlock() instanceof WallHangingSignBlock) {
			state.transformations = HangingSignRenderer.TRANSFORMATIONS.wallTransformation(blockState.getValue(WallHangingSignBlock.FACING));
		} else {
			state.transformations = HangingSignRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(CeilingHangingSignBlock.ROTATION));
		}
	}

	@Override
	protected Model.Simple getSignModel(CanvasSignRenderState state) {
		return switch (state.hangingAttachment) {
			case CEILING -> this.ceilingModel;
			case CEILING_MIDDLE -> this.ceilingMiddleModel;
			case WALL -> this.wallModel;
		};
	}

	@Override
	protected SpriteId getCanvasSprite(CanvasSignRenderState state) {
		Material material = ModAtlases.getHangingCanvasSignMaterial(state.dyeColor);
		return new SpriteId(Sheets.SIGN_SHEET, material.sprite());
	}
}
