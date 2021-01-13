package vectorwing.farmersdelight.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.AlterGroundTreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.Random;

@Mixin(Feature.class)
public class KeepRichSoilMixin {

    @Inject(cancellable = true, at = @At(value = "HEAD"), method = "isDirtAt")
    private static void keepRichSoil(IWorldGenerationBaseReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (world.hasBlockState(pos, state -> state.isIn(ModBlocks.RICH_SOIL.get()))) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
