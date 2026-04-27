package vectorwing.farmersdelight.common.mixin;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.sensing.SecondaryPoiSensor;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Thanks to MehVahdJukaar for the mixin implementation!
 */
@Mixin(SecondaryPoiSensor.class)
public class VillagersTargetRichSoilMixin
{
	@WrapOperation(method = "doTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/npc/Villager;)V",
			at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableSet;contains(Ljava/lang/Object;)Z"))
	public boolean detectModdedFarmland(ImmutableSet<Block> instance, Object o, Operation<Boolean> original, @Local(argsOnly = true) Villager villager) {
		if (villager.getVillagerData().getProfession() == VillagerProfession.FARMER) {
			return original.call(instance, o) || o instanceof FarmBlock;
		} else {
			return original.call(instance, o);
		}
	}
}
