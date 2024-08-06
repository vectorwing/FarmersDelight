package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.FarmersDelight;

public class ModDamageTypes
{
	public static final ResourceKey<DamageType> STOVE_BURN = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(FarmersDelight.MODID, "stove_burn"));

	public static DamageSource getSimpleDamageSource(Level level, ResourceKey<DamageType> type) {
		return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type));
	}

	public static void bootstrap(BootstapContext<DamageType> context) {
		context.register(STOVE_BURN, new DamageType("farmersdelight.stove", 0.1F, DamageEffects.BURNING));
	}
}
