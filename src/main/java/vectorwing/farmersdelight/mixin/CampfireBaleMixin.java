package vectorwing.farmersdelight.mixin;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.Set;

@Mixin(CampfireBlock.class)
public abstract class CampfireBaleMixin
{
	@Inject(at = @At("HEAD"), method = "isHayBlock", cancellable = true)
	public void isHayBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		Set<Block> hayBales = Sets.newHashSet(ModBlocks.STRAW_BALE.get(), ModBlocks.RICE_BALE.get());
		if (hayBales.contains(state.getBlock())) {
			cir.setReturnValue(true);
		}
	}
}