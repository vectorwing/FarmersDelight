package vectorwing.farmersdelight.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.blocks.signs.ICanvasSign;
import vectorwing.farmersdelight.client.tileentity.renderer.CanvasSignTileEntityRenderer;

@Mixin(SignRenderer.class)
public class CanvasSignMaterialMixin
{
	@Inject(at = @At(value = "HEAD"), method = "getMaterial", cancellable = true)
	private static void useCanvasSignMaterials(Block blockIn, CallbackInfoReturnable<Material> cir) {
		if (blockIn instanceof ICanvasSign) {
			cir.setReturnValue(CanvasSignTileEntityRenderer.getMaterial(blockIn));
			cir.cancel();
		}
	}
}
