package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import vectorwing.farmersdelight.FarmersDelight;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();
		if (event.includeServer()) {
			BlockTags blockTags = new BlockTags(generator, FarmersDelight.MODID, helper);
			generator.addProvider(blockTags);
			generator.addProvider(new ItemTags(generator, blockTags, FarmersDelight.MODID, helper));
			generator.addProvider(new EntityTags(generator, FarmersDelight.MODID, helper));
			generator.addProvider(new Recipes(generator));
			generator.addProvider(new Advancements(generator));
		}
		if (event.includeClient()) {
			BlockStates blockStates = new BlockStates(generator, helper);
			generator.addProvider(blockStates);
			generator.addProvider(new ItemModels(generator, blockStates.models().existingFileHelper));
		}
	}
}
