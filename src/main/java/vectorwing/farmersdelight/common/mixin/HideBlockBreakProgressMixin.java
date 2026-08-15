package vectorwing.farmersdelight.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(LevelRenderer.class)
public abstract class HideBlockBreakProgressMixin
{
	@Inject(method = "submitBlockDestroyAnimation", at = @At("HEAD"))
	private void farmersdelight$hideCanvasRugBlockDamage(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState,
			CallbackInfo ci) {
		levelRenderState.blockBreakingRenderStates.removeIf(state -> state.blockState().getBlock() == ModBlocks.CANVAS_RUG.get());
	}
}
