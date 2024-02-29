package vectorwing.farmersdelight.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.client.gui.ComfortHealthOverlay;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> MenuScreens.register(ModMenuTypes.COOKING_POT.get(), CookingPotScreen::new));

		NourishmentHungerOverlay.init();
		ComfortHealthOverlay.init();
	}
}
