package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.effect.ComfortEffect;
import vectorwing.farmersdelight.common.effect.NourishmentEffect;

import java.util.function.Supplier;

public class ModEffects
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FarmersDelight.MODID);

	public static final Supplier<MobEffect> NOURISHMENT = EFFECTS.register("nourishment", NourishmentEffect::new);
	public static final Supplier<MobEffect> COMFORT = EFFECTS.register("comfort", ComfortEffect::new);
}
