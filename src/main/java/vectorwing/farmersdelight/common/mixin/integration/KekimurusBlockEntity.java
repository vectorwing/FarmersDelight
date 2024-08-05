package vectorwing.farmersdelight.common.mixin.integration;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import vectorwing.farmersdelight.common.block.PieBlock;

@SuppressWarnings("UnresolvedMixinReference")
@Pseudo
@Mixin(targets = "vazkii.botania.common.block.flower.generating.KekimurusBlockEntity", remap = false)
public class KekimurusBlockEntity {

    @WrapOperation(method = "tickFlower", at = @At(value = "CONSTANT", args = "classValue=net.minecraft.world.level.block.CakeBlock", remap = true))
    private boolean checkForPieBlock(Object block, Operation<Boolean> original, @Share("isPie") LocalBooleanRef isPie) {
        isPie.set(block instanceof PieBlock);
        return isPie.get() || original.call(block);
    }

    @ModifyExpressionValue(method = "tickFlower", at = @At(value = "CONSTANT", args = "intValue=6"))
    private int usePieMaxBites(int original, @Share("isPie") LocalBooleanRef isPie) {
        return isPie.get() ? 3 : original;
    }

    @ModifyExpressionValue(method = "tickFlower", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/CakeBlock;BITES:Lnet/minecraft/world/level/block/state/properties/IntegerProperty;", remap = true))
    private IntegerProperty usePieProperty(IntegerProperty original, @Share("isPie") LocalBooleanRef isPie) {
        if (isPie.get()) {
            return PieBlock.BITES;
        }
        return original;
    }

}
