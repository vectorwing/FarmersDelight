package vectorwing.farmersdelight.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.RichSoilFarmlandBlock;

@Mixin(FarmBlock.class)
public class KeepRichSoilUntrampledMixin {
    @Inject(at = @At(value = "HEAD"), method = "turnToDirt", cancellable = true)
    private static void turnToDirt(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (state.getBlock() instanceof RichSoilFarmlandBlock) ci.cancel();
    }
}
