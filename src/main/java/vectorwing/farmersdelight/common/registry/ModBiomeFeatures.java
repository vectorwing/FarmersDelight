package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.world.feature.WildRiceFeature;

public class ModBiomeFeatures
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, FarmersDelight.MODID);

	public static final RegistryObject<Feature<RandomPatchConfiguration>> WILD_RICE = FEATURES.register("wild_rice", () -> new WildRiceFeature(RandomPatchConfiguration.CODEC));
}
