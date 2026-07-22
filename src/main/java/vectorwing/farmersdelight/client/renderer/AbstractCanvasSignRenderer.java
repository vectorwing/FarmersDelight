package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.CanvasSignRenderState;
import vectorwing.farmersdelight.common.block.state.CanvasSign;

// Vanilla copy of AbstractSignRenderer with WoodType replaced with DyeColor
public abstract class AbstractCanvasSignRenderer<S extends CanvasSignRenderState> extends AbstractSignRenderer<S> {
    @Nullable
    private static DyeColor capturedColor;
    private final SpriteGetter sprites;

    public AbstractCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        sprites = context.sprites();
    }

    protected abstract SpriteId getSignSprite(@Nullable DyeColor color);

    @Override
    protected SpriteId getSignSprite(@NonNull WoodType type) {
        throw new UnsupportedOperationException("Obtaining sign sprite from WoodType is unsupported by Canvas Signs");
    }

    @Override
    public void extractRenderState(
            final @NonNull SignBlockEntity blockEntity,
            final S state,
            final float partialTicks,
            final @NonNull Vec3 cameraPosition,
            final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = blockEntity.getBlockState();
        state.color = blockState.getBlock() instanceof CanvasSign canvasSign ? canvasSign.getBackgroundColor() : null;
    }

    public void submit(final S state, final @NonNull PoseStack poseStack, final @NonNull SubmitNodeCollector submitNodeCollector, final @NonNull CameraRenderState camera) {
        capturedColor = state.color;
        super.submit(state, poseStack, submitNodeCollector, camera);
        capturedColor = null;
    }

    @Override
    protected void submitSign(
            @NonNull PoseStack poseStack,
            int lightCoords,
            @NonNull WoodType type,
            Model.@NonNull Simple signModel,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
            SubmitNodeCollector submitNodeCollector
    ) {
        SpriteId sprite = getSignSprite(capturedColor);
        submitNodeCollector.submitModel(signModel, Unit.INSTANCE, poseStack, lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, this.sprites, 0, breakProgress);
    }
}

