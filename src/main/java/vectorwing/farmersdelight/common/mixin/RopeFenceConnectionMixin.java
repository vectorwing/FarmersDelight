package vectorwing.farmersdelight.common.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin(FenceBlock.class)
public abstract class RopeFenceConnectionMixin
{
	@Inject(at = @At("HEAD"), method = "connectsTo", cancellable = true)
	public void denyConnectionToRopeFenceGate(BlockState state, boolean isSideSolid, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (state.is(ModBlocks.ROPE_FENCE_GATE.get())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(at = @At("HEAD"), method = "isSameFence", cancellable = true)
	public void denyConnectionToRopeFence(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.is(ModBlocks.ROPE_FENCE.get())) {
			cir.setReturnValue(false);
		}
	}
}
