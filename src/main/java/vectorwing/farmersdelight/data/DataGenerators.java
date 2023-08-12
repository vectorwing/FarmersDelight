package vectorwing.farmersdelight.data;

import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
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
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.data.loot.FDBlockLoot;
import vectorwing.farmersdelight.data.loot.FDChestLoot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		BlockTags blockTags = new BlockTags(output, lookupProvider, helper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new EntityTags(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new Recipes(output));
		generator.addProvider(event.includeServer(), new Advancements(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new LootModifiers(output));
		generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(
				new LootTableProvider.SubProviderEntry(FDBlockLoot::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(FDChestLoot::new, LootContextParamSets.CHEST)
		)));
		DatapackBuiltinEntriesProvider datapackProvider = new DatapackRegistries(output, lookupProvider);
		CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(event.includeServer(), datapackProvider);

		BlockStates blockStates = new BlockStates(output, helper);
		generator.addProvider(event.includeClient(), blockStates);
		generator.addProvider(event.includeClient(), new ItemModels(output, blockStates.models().existingFileHelper));

		generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
				Component.literal("Farmer's Delight resources"),
				DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
				Arrays.stream(PackType.values()).collect(Collectors.toMap(Function.identity(), DetectedVersion.BUILT_IN::getPackVersion)))));
	}
}
