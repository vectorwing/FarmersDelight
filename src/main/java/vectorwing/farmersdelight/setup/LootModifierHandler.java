package vectorwing.farmersdelight.setup;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.loot.modifiers.CakeSliceLoot;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootModifierHandler
{
	@SubscribeEvent
	public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> ev)
	{
		ev.getRegistry().register(
				new CakeSliceLoot.CakeSliceSerializer().setRegistryName(FarmersDelight.MODID, "cake")
		);
	}
}
