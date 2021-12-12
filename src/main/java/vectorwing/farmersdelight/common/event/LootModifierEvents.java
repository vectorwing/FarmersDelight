package vectorwing.farmersdelight.common.event;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootModifierEvents
{
	@SubscribeEvent
	public static void registerLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> ev) {
		ev.getRegistry().register(
				new PastrySlicingModifier.Serializer().setRegistryName(FarmersDelight.MODID, "pastry_slicing")
		);
		ev.getRegistry().register(
				new AddItemModifier.Serializer().setRegistryName(FarmersDelight.MODID, "add_item")
		);
	}
}
