package vectorwing.farmersdelight.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.world.features.RiceCropFeature;

public class ModBiomeFeatures
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, FarmersDelight.MODID);

	public static final RegistryObject<Feature<RandomPatchConfiguration>> RICE = FEATURES.register("rice", () -> new RiceCropFeature(RandomPatchConfiguration.CODEC));
}
