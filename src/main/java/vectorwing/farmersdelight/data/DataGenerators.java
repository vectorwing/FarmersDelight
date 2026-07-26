package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBiomeModifiers;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.world.WildCropGeneration;
import vectorwing.farmersdelight.data.loot.FDBlockLoot;
import vectorwing.farmersdelight.data.loot.FDChestLoot;
import vectorwing.farmersdelight.data.tools.StructureUpdater;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = FarmersDelight.MODID)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherServerData(GatherDataEvent.Server event) {
		PackOutput output = event.getGenerator().getPackOutput();
		RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
				.add(Registries.CONFIGURED_FEATURE, WildCropGeneration::bootstrapConfiguredFeatures)
				.add(Registries.PLACED_FEATURE, WildCropGeneration::bootstrapPlacedFeatures)
				.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrapBiomeModifiers)
				.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrapDamageTypes)
				.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
		DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder, Set.of(FarmersDelight.MODID));
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
		event.addProvider(new ExistingGeneratedResources(output));
		event.addProvider(datapackProvider);

		BlockTags blockTags = new BlockTags(output, lookupProvider);
		event.addProvider(blockTags);
		event.addProvider(new ItemTags(output, lookupProvider));
		event.addProvider(new EntityTags(output, lookupProvider));
		event.addProvider(new DamageTypeTags(output, lookupProvider, FarmersDelight.MODID));
		event.addProvider(new EnchantmentTags(output, lookupProvider));
		event.addProvider(new Recipes(output, lookupProvider));
		event.addProvider(new LootModifiers(output, lookupProvider));
		event.addProvider(new DataMaps(output, lookupProvider));
		event.addProvider(new Advancements(output, lookupProvider));
		event.addProvider(new LootTableProvider(output, Collections.emptySet(), List.of(
				new LootTableProvider.SubProviderEntry(FDBlockLoot::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(FDChestLoot::new, LootContextParamSets.CHEST)
		), lookupProvider));
		event.addProvider(new StructureUpdater("structures/village/houses", FarmersDelight.MODID, event.getResourceManager(PackType.SERVER_DATA), output));

	}

	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event) {
		PackOutput output = event.getGenerator().getPackOutput();
		event.addProvider(new ExistingGeneratedResources(output));
		event.addProvider(new BlockStates(output));
		event.addProvider(new ItemModels(output));
		event.addProvider(new SoundDefinitions(output));
	}
}
