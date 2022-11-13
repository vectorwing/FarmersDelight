package vectorwing.farmersdelight.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Random;
import java.util.function.BiConsumer;

@Mixin(TrunkPlacer.class)
public class KeepRichSoilTreeMixin
{
	/**
	 * Due to how Trees generate, this mixin is needed to prevent Rich Soil from becoming Podzol under a Giant Spruce Tree growth.
	 */
	@Inject(at = @At(value = "HEAD"), method = "setDirtAt", cancellable = true)
	private static void cancelSetDirtIfRichSoil(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, Random random, BlockPos pos, TreeConfiguration config, CallbackInfo ci) {
		if (level.isStateAtPosition(pos, state -> state.is(ModBlocks.RICH_SOIL.get()))) {
			ci.cancel();
		}
	}
}
