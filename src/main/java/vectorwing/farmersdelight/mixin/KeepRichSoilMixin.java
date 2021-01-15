package vectorwing.farmersdelight.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.registry.ModBlocks;

@Mixin(Feature.class)
public class KeepRichSoilMixin
{
	/**
	 * Due to how Trees generate, this mixin is needed to prevent Rich Soil from becoming Podzol under a Giant Spruce Tree growth.
	 */
	@Shadow
	@Inject(cancellable = true, at = @At(value = "HEAD"), method = "isDirtAt(Lnet/minecraft/world/gen/IWorldGenerationBaseReader;Lnet/minecraft/util/math/BlockPos;)Z")
	private static void keepRichSoil(IWorldGenerationBaseReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (world.hasBlockState(pos, state -> state.isIn(ModBlocks.RICH_SOIL.get()))) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
