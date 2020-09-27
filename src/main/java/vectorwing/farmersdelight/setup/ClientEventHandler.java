package vectorwing.farmersdelight.setup;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.tileentity.renderer.CuttingBoardTileEntityRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.StoveTileEntityRenderer;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModContainerTypes;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {
	public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = new ResourceLocation(FarmersDelight.MODID, "items/empty_container_slot_bowl");

	@SubscribeEvent
	public static void onStitchEvent(TextureStitchEvent.Pre event) {
		ResourceLocation stitching = event.getMap().getTextureLocation();
		if (!stitching.equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
			return;
		}
		boolean added = event.addSprite(EMPTY_CONTAINER_SLOT_BOWL);
	}

	public static void init(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_CABBAGES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_ONIONS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_TOMATOES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_CARROTS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_POTATOES.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_BEETROOTS.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ModBlocks.ONION_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CABBAGE_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TOMATO_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RICE_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TALL_RICE_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.BROWN_MUSHROOM_COLONY.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RED_MUSHROOM_COLONY.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ModBlocks.COOKING_POT.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CUTTING_BOARD.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.BASKET.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ROPE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SAFETY_NET.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.FULL_TATAMI_MAT.get(), RenderType.getCutout());

		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.STOVE_TILE.get(),
				StoveTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.CUTTING_BOARD_TILE.get(),
				CuttingBoardTileEntityRenderer::new);

		ScreenManager.registerFactory(ModContainerTypes.COOKING_POT.get(), CookingPotScreen::new);
	}
}
