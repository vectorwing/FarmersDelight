package vectorwing.farmersdelight.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.client.gui.CanvasSignEditScreen;
import vectorwing.farmersdelight.common.block.entity.CanvasSignBlockEntity;

@Mixin(LocalPlayer.class)
public class CanvasSignEditScreenMixin
{
	@Shadow
	@Final
	protected Minecraft minecraft;

	@Inject(at = @At(value = "HEAD"), method = "openTextEdit", cancellable = true)
	private void openCanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, CallbackInfo ci) {
		if (signBlockEntity instanceof CanvasSignBlockEntity) {
			minecraft.setScreen(new CanvasSignEditScreen(signBlockEntity, isFront, minecraft.isTextFilteringEnabled()));
			ci.cancel();
		}
	}
}
