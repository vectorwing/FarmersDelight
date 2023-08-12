package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.world.WildCropGeneration;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatapackRegistries extends DatapackBuiltinEntriesProvider
{
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, WildCropGeneration::bootstrapConfiguredFeatures)
			.add(Registries.PLACED_FEATURE, WildCropGeneration::bootstrapPlacedFeatures)
			.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);

	public DatapackRegistries(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
		super(output, future, BUILDER, Set.of("minecraft", FarmersDelight.MODID));
	}
}
