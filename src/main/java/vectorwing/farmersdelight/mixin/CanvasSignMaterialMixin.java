package vectorwing.farmersdelight.mixin;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.blocks.signs.ICanvasSign;
import vectorwing.farmersdelight.client.tileentity.renderer.CanvasSignTileEntityRenderer;

@Mixin(SignTileEntityRenderer.class)
public class CanvasSignMaterialMixin
{
	@Inject(at = @At(value = "HEAD"), method = "getMaterial", cancellable = true)
	private static void useCanvasSignMaterials(Block blockIn, CallbackInfoReturnable<RenderMaterial> cir) {
		if (blockIn instanceof ICanvasSign) {
			cir.setReturnValue(CanvasSignTileEntityRenderer.getMaterial(blockIn));
			cir.cancel();
		}
	}
}
