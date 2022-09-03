package vectorwing.farmersdelight.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.client.gui.ComfortHealthOverlay;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;

public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SANDY_SHRUB.get(), RenderType.cutout());
		event.enqueueWork(() ->
		{
			MenuScreens.register(ModMenuTypes.COOKING_POT.get(), CookingPotScreen::new);
			RecipeCategories.init();
		});

		NourishmentHungerOverlay.init();
		ComfortHealthOverlay.init();
	}
}
