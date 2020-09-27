package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import vectorwing.farmersdelight.FarmersDelight;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		if (event.includeServer()) {
			BlockTags blockTags = new BlockTags(generator);
			generator.addProvider(blockTags);
			generator.addProvider(new ItemTags(generator, blockTags));
			generator.addProvider(new Recipes(generator));
			generator.addProvider(new Advancements(generator));
		}
		//if (event.includeClient()) {
			//generator.addProvider(new Items(generator, event.getExistingFileHelper()));
		//}
	}
}
