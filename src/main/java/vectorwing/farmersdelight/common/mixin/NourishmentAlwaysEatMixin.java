package vectorwing.farmersdelight.common.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModEffects;

@Mixin(Player.class)
public class NourishmentAlwaysEatMixin
{
	@Inject(
			method = "canEat",
			at = @At("HEAD"),
			cancellable = true)
	private void alwaysEatUnderNourishmentEffect(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
		if (((Player) (Object) this).hasEffect(ModEffects.NOURISHMENT.get())) {
			cir.setReturnValue(true);
		}
	}
}
