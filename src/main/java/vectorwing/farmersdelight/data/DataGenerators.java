package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.data.event.GatherDataEvent;
import vectorwing.farmersdelight.FarmersDelight;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		BlockTags blockTags = new BlockTags(generator, FarmersDelight.MODID, helper);
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ItemTags(generator, blockTags, FarmersDelight.MODID, helper));
		generator.addProvider(event.includeServer(), new EntityTags(generator, FarmersDelight.MODID, helper));
		generator.addProvider(event.includeServer(), new Recipes(generator));
		generator.addProvider(event.includeServer(), new Advancements(generator));

		BlockStates blockStates = new BlockStates(generator, helper);
		generator.addProvider(event.includeClient(), blockStates);
		generator.addProvider(event.includeClient(), new ItemModels(generator, blockStates.models().existingFileHelper));
	}
}
