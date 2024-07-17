package vectorwing.farmersdelight.common.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;

@EventBusSubscriber(modid = FarmersDelight.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModBusEvents
{
	@SubscribeEvent
	public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
		if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
			Configuration.SOUP_ITEM_LIST.get().forEach((key) -> {
				Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(key));
				event.modify(item, (builder) -> builder.set(DataComponents.MAX_STACK_SIZE, 16));
			});
		}
	}
}
