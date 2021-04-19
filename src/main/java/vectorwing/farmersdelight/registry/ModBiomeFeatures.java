package vectorwing.farmersdelight.registry;

import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.world.features.RiceCropFeature;

public class ModBiomeFeatures
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, FarmersDelight.MODID);

	public static final RegistryObject<Feature<BlockClusterFeatureConfig>> RICE = FEATURES.register("rice", () -> new RiceCropFeature(BlockClusterFeatureConfig.CODEC));
}
