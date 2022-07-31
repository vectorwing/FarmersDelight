package vectorwing.farmersdelight.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.client.gui.ComfortHealthOverlay;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModContainerTypes;

public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event) {
		MenuScreens.register(ModContainerTypes.COOKING_POT.get(), CookingPotScreen::new);

		NourishmentHungerOverlay.init();
		ComfortHealthOverlay.init();
	}
}
