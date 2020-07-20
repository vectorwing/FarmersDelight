package vectorwing.farmersdelight.setup;

import net.minecraft.client.gui.ScreenManager;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.tileentity.renderer.StoveTileEntityRenderer;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.init.ModContainerTypes;
import vectorwing.farmersdelight.init.TileEntityInit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static void init(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ModBlocks.ONION_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CABBAGE_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TOMATO_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RICE_CROP.get(), RenderType.getCutout());
		ClientRegistry.bindTileEntityRenderer(TileEntityInit.STOVE_TILE.get(),
				StoveTileEntityRenderer::new);
		ScreenManager.registerFactory(ModContainerTypes.COOKING_POT.get(), CookingPotScreen::new);
	}
}
