package vectorwing.farmersdelight.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(Feature.class)
public class KeepRichSoilGiantTreeMixin
{
	/**
	 * Due to how Trees generate, this mixin is needed to prevent Rich Soil from becoming Podzol under a Giant Spruce Tree growth.
	 */
	@Inject(at = @At(value = "HEAD"), method = "isGrassOrDirt", cancellable = true)
	private static void keepRichSoil(LevelSimulatedReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (world.isStateAtPosition(pos, state -> state.is(ModBlocks.RICH_SOIL.get()))) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
}
