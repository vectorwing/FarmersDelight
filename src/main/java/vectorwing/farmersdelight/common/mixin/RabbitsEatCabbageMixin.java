package vectorwing.farmersdelight.common.mixin;

import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.tag.ForgeTags;

@Mixin(Rabbit.class)
public class RabbitsEatCabbageMixin
{
	@Inject(at = @At(value = "HEAD"), method = "isFood", cancellable = true)
	public void isCabbage(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.is(ForgeTags.CROPS_CABBAGE)) {
			cir.setReturnValue(true);
		}
	}
}
