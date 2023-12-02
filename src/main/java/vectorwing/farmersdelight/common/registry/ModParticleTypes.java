package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

public class ModParticleTypes
{
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, FarmersDelight.MODID);

	public static final Supplier<SimpleParticleType> STAR = PARTICLE_TYPES.register("star",
			() -> new SimpleParticleType(true));
	public static final Supplier<SimpleParticleType> STEAM = PARTICLE_TYPES.register("steam",
			() -> new SimpleParticleType(true));
}
