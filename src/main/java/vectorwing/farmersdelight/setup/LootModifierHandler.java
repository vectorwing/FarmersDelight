package vectorwing.farmersdelight.setup;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.loot.modifiers.AddItemModifier;
import vectorwing.farmersdelight.loot.modifiers.AddLootTableModifier;
import vectorwing.farmersdelight.loot.modifiers.PastrySlicingModifier;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootModifierHandler
{
	@SubscribeEvent
	public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> ev) {
		ev.getRegistry().register(
				new PastrySlicingModifier.Serializer().setRegistryName(FarmersDelight.MODID, "pastry_slicing")
		);
		ev.getRegistry().register(
				new AddItemModifier.Serializer().setRegistryName(FarmersDelight.MODID, "add_item")
		);
		ev.getRegistry().register(
				new AddLootTableModifier.Serializer().setRegistryName(FarmersDelight.MODID, "add_loot_table")
		);
	}
}
