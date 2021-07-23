package vectorwing.farmersdelight.registry;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;

public class ModParticleTypes
{
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FarmersDelight.MODID);

	public static final RegistryObject<BasicParticleType> STAR = PARTICLE_TYPES.register("star",
			() -> new BasicParticleType(true));
	public static final RegistryObject<BasicParticleType> STEAM = PARTICLE_TYPES.register("steam",
			() -> new BasicParticleType(true));
}
