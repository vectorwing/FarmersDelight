package vectorwing.farmersdelight.setup;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.loot.modifiers.PastrySlicingModifier;
import vectorwing.farmersdelight.loot.modifiers.StrawHarvestingModifier;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootModifierHandler
{
	@SubscribeEvent
	public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> ev) {
		ev.getRegistry().register(
				new PastrySlicingModifier.CakeSliceSerializer().setRegistryName(FarmersDelight.MODID, "pastry_slicing")
		);
		ev.getRegistry().register(
				new StrawHarvestingModifier.Serializer().setRegistryName(FarmersDelight.MODID, "straw_harvesting")
		);
	}
}
