package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.StandingCanvasSignRenderState;
import vectorwing.farmersdelight.common.registry.ModAtlases;

public class StandingCanvasSignRenderer extends AbstractCanvasSignRenderer<StandingCanvasSignRenderState> {
	private final Models signModels;

	public StandingCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		this.signModels = Models.create(context);
	}

    @Override
    protected Model.Simple getSignModel(StandingCanvasSignRenderState state) {
        return signModels.get(state.attachmentType);
    }

    @Override
    protected SpriteId getSignSprite(@Nullable DyeColor color) {
        return ModAtlases.getCanvasSignMaterial(color);
    }

    @Override
    public void extractRenderState(
            final @NonNull SignBlockEntity blockEntity,
            final StandingCanvasSignRenderState state,
            final float partialTicks,
            final @NonNull Vec3 cameraPosition,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.attachmentType = PlainSignBlock.getAttachmentPoint(blockState);
        if (blockState.getBlock() instanceof WallSignBlock) {
            state.transformations = StandingSignRenderer.TRANSFORMATIONS.wallTransformation(blockState.getValue(WallSignBlock.FACING));
        } else {
            state.transformations = StandingSignRenderer.TRANSFORMATIONS.freeTransformations(blockState.getValue(StandingSignBlock.ROTATION));
        }
    }

    @Override
    public StandingCanvasSignRenderState createRenderState() {
        return new StandingCanvasSignRenderState();
    }

    private record Models(Model.Simple standing, Model.Simple wall) {
        public static Models create(final BlockEntityRendererProvider.Context context) {
            return new Models(
                    StandingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, PlainSignBlock.Attachment.GROUND),
                    StandingSignRenderer.createSignModel(context.entityModelSet(), WoodType.SPRUCE, PlainSignBlock.Attachment.WALL)
            );
        }

        public Model.Simple get(final PlainSignBlock.Attachment attachmentType) {
            return switch (attachmentType) {
                case GROUND -> this.standing;
                case WALL -> this.wall;
            };
        }
    }
}

