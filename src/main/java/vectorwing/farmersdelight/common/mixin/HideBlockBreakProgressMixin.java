package vectorwing.farmersdelight.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
import net.minecraft.world.level.block.RenderShape;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(LevelRenderer.class)
public abstract class HideBlockBreakProgressMixin
{
	@ModifyExpressionValue(method = "submitBlockDestroyAnimation", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/RenderShape;MODEL:Lnet/minecraft/world/level/block/RenderShape;", opcode = Opcodes.GETSTATIC))
	private RenderShape hideBlockDamage(RenderShape original, @Local(name = "state") BlockBreakingRenderState state) {
		if (state.blockState().getBlock() == ModBlocks.CANVAS_RUG.get()) {
			return null;
		}
		return original;
	}
}