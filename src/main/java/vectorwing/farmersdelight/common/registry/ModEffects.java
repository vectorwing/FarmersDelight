package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.effect.ComfortEffect;
import vectorwing.farmersdelight.common.effect.NourishmentEffect;

public class ModEffects
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FarmersDelight.MODID);

	public static final RegistryObject<MobEffect> NOURISHMENT = EFFECTS.register("nourishment", NourishmentEffect::new);
	public static final RegistryObject<MobEffect> COMFORT = EFFECTS.register("comfort", ComfortEffect::new);
}
