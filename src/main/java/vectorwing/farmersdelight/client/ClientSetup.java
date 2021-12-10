package vectorwing.farmersdelight.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModContainerTypes;

public class ClientSetup
{
	public static void init(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_CABBAGES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_ONIONS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_TOMATOES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_CARROTS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_POTATOES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_BEETROOTS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_RICE.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ONION_CROP.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.CABBAGE_CROP.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATO_CROP.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.RICE_CROP.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.RICE_CROP_PANICLES.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.BROWN_MUSHROOM_COLONY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_MUSHROOM_COLONY.get(), RenderType.cutout());

		ItemBlockRenderTypes.setRenderLayer(ModBlocks.COOKING_POT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.CUTTING_BOARD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.BASKET.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ROPE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SAFETY_NET.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.CANVAS_RUG.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.FULL_TATAMI_MAT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.ROAST_CHICKEN_BLOCK.get(), RenderType.cutout());

		MenuScreens.register(ModContainerTypes.COOKING_POT.get(), CookingPotScreen::new);

		NourishmentHungerOverlay.init();
	}
}
