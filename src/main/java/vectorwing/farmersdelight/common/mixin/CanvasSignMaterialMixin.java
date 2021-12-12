package vectorwing.farmersdelight.common.mixin;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vectorwing.farmersdelight.client.renderer.CanvasSignRenderer;
import vectorwing.farmersdelight.common.block.state.CanvasSign;

@Mixin(SignEditScreen.class)
public class CanvasSignMaterialMixin
{
	@Shadow
	@Final
	private SignBlockEntity sign;

	@ModifyVariable(at = @At(value = "STORE", ordinal = 0), method = "render")
	public Material useCanvasSignMaterials(Material material) {
		Block block = sign.getBlockState().getBlock();
		if (block instanceof CanvasSign canvasSign) {
			return CanvasSignRenderer.getMaterial(canvasSign.getBackgroundColor());
		}
		return material;
	}
}
