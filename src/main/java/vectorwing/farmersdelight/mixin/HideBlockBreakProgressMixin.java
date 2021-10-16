package vectorwing.farmersdelight.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.registry.ModBlocks;

@Mixin(BlockRendererDispatcher.class)
public abstract class HideBlockBreakProgressMixin
{
	@Inject(method = "renderBreakingTexture", at = @At("HEAD"), cancellable = true)
	private void hideBlockDamage(BlockState blockStateIn, BlockPos posIn, IBlockDisplayReader lightReaderIn, MatrixStack matrixStackIn, IVertexBuilder vertexBuilderIn, CallbackInfo ci) {
		if (blockStateIn.getBlock() == ModBlocks.CANVAS_RUG.get()) {
			ci.cancel();
		}
	}
}
