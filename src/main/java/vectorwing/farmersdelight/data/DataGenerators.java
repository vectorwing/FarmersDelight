package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.data.event.GatherDataEvent;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.concurrent.CompletableFuture;

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

		BlockTags blockTags = new BlockTags(output, lookupProvider, FarmersDelight.MODID, helper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ItemTags(output, lookupProvider, blockTags.contentsGetter(), FarmersDelight.MODID, helper));
		generator.addProvider(event.includeServer(), new EntityTags(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new Recipes(output));
		// TODO: Fix this monstrosity...
//		generator.addProvider(event.includeServer(), new Advancements(generator));

		BlockStates blockStates = new BlockStates(output, helper);
		generator.addProvider(event.includeClient(), blockStates);
		generator.addProvider(event.includeClient(), new ItemModels(output, blockStates.models().existingFileHelper));
	}
}
