package vectorwing.farmersdelight.registry;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.effects.ComfortEffect;
import vectorwing.farmersdelight.effects.NourishedEffect;

public class ModEffects {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, FarmersDelight.MODID);

	public static final RegistryObject<Effect> NOURISHED = EFFECTS.register("nourished", NourishedEffect::new);
	public static final RegistryObject<Effect> COMFORT = EFFECTS.register("comfort", ComfortEffect::new);
}
