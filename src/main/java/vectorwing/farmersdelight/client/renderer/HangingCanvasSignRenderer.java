package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.HangingCanvasSignRenderState;
import vectorwing.farmersdelight.common.registry.ModAtlases;

public class HangingCanvasSignRenderer extends AbstractCanvasSignRenderer<HangingCanvasSignRenderState> {
    private final Models models;

	public HangingCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
        this.models = Models.create(context);
    }

    @Override
    protected Model.Simple getSignModel(HangingCanvasSignRenderState state) {
        return models.get(state.attachmentType);
    }

	@Override
	protected SpriteId getSignSprite(@Nullable DyeColor dyeColor) {
		return ModAtlases.getHangingCanvasSignMaterial(dyeColor);
	}

    @Override
    public void extractRenderState(
            final @NonNull SignBlockEntity blockEntity,
            final HangingCanvasSignRenderState state,
            final float partialTicks,
            final @NonNull Vec3 cameraPosition,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.attachmentType = HangingSignBlock.getAttachmentPoint(blockState);
        if (blockState.getBlock() instanceof WallHangingSignBlock) {
            state.transformations = HangingSignRenderer.TRANSFORMATIONS.wallTransformation(blockState.getValue(WallHangingSignBlock.FACING));
        } else {
            state.transformations = HangingSignRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(CeilingHangingSignBlock.ROTATION));
        }
    }

    @Override
    public HangingCanvasSignRenderState createRenderState() {
        return new HangingCanvasSignRenderState();
    }

    private record Models(Model.Simple ceiling, Model.Simple ceilingMiddle, Model.Simple wall) {
        public static Models create(final BlockEntityRendererProvider.Context context) {
            return new Models(
                    HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.CEILING),
                    HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.CEILING_MIDDLE),
                    HangingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, HangingSignBlock.Attachment.WALL)
            );
        }

        public Model.Simple get(final HangingSignBlock.Attachment attachmentType) {
            return switch (attachmentType) {
                case CEILING -> this.ceiling;
                case CEILING_MIDDLE -> this.ceilingMiddle;
                case WALL -> this.wall;
            };
        }
    }
}

