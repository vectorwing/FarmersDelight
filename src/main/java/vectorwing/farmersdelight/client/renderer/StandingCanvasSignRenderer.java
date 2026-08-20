package vectorwing.farmersdelight.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.StandingCanvasSignRenderState;

public class StandingCanvasSignRenderer extends AbstractCanvasSignRenderer<StandingCanvasSignRenderState>
{
    public StandingCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(
            SignBlockEntity blockEntity,
            StandingCanvasSignRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        BlockState blockState = blockEntity.getBlockState();
        state.attachmentType = PlainSignBlock.getAttachmentPoint(blockState);

        if (blockState.getBlock() instanceof WallSignBlock) {
            state.transformations = StandingSignRenderer.TRANSFORMATIONS.wallTransformation(
                    blockState.getValue(WallSignBlock.FACING)
            );
        } else {
            state.transformations = StandingSignRenderer.TRANSFORMATIONS.freeTransformations(
                    blockState.getValue(StandingSignBlock.ROTATION)
            );
        }
    }

    @Override
    public StandingCanvasSignRenderState createRenderState() {
        return new StandingCanvasSignRenderState();
    }
}