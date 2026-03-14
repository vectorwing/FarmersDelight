package vectorwing.farmersdelight.data;

import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBiomeModifiers;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.world.WildCropGeneration;
import vectorwing.farmersdelight.data.loot.FDBlockLoot;
import vectorwing.farmersdelight.data.loot.FDChestLoot;
import vectorwing.farmersdelight.data.tools.StructureUpdater;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
				.add(Registries.CONFIGURED_FEATURE, WildCropGeneration::bootstrapConfiguredFeatures)
				.add(Registries.PLACED_FEATURE, WildCropGeneration::bootstrapPlacedFeatures)
				.add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrapBiomeModifiers)
				.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrapDamageTypes);
		DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(output, lookupProvider, registrySetBuilder, Set.of(FarmersDelight.MODID));
		generator.addProvider(event.includeServer(), datapackProvider);

		BlockTags blockTags = new BlockTags(output, lookupProvider, helper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new EntityTags(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new LootModifiers(output));
		generator.addProvider(event.includeServer(), new Recipes(output));
		generator.addProvider(event.includeServer(), new Advancements(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(
				new LootTableProvider.SubProviderEntry(FDBlockLoot::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(FDChestLoot::new, LootContextParamSets.CHEST)
		)));
		generator.addProvider(event.includeServer(), new StructureUpdater("structures/village/houses", FarmersDelight.MODID, helper, output));

		BlockStates blockStates = new BlockStates(output, helper);
		generator.addProvider(event.includeClient(), blockStates);
		generator.addProvider(event.includeClient(), new ItemModels(output, blockStates.models().existingFileHelper));
		generator.addProvider(event.includeClient(), new SoundDefinitions(output, helper));

		generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
				Component.literal("Farmer's Delight resources"),
				DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
				Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));
	}
}
