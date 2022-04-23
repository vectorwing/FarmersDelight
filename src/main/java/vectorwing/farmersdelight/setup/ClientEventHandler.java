package vectorwing.farmersdelight.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.ComfortHealthOverlay;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishedHungerOverlay;
import vectorwing.farmersdelight.client.particles.StarParticle;
import vectorwing.farmersdelight.client.particles.SteamParticle;
import vectorwing.farmersdelight.client.tileentity.renderer.CanvasSignTileEntityRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.CuttingBoardTileEntityRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.SkilletTileEntityRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.StoveTileEntityRenderer;
import vectorwing.farmersdelight.registry.*;
import vectorwing.farmersdelight.utils.ModAtlases;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler
{
	public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = new ResourceLocation(FarmersDelight.MODID, "item/empty_container_slot_bowl");

	@SubscribeEvent
	public static void onStitchEvent(TextureStitchEvent.Pre event) {
		ResourceLocation stitching = event.getMap().location();
		if (stitching.equals(new ResourceLocation("textures/atlas/signs.png"))) {
			event.addSprite(ModAtlases.BLANK_CANVAS_SIGN_MATERIAL.texture());
			for (RenderMaterial material : ModAtlases.DYED_CANVAS_SIGN_MATERIALS.values()) {
				event.addSprite(material.texture());
			}
		}
		if (!stitching.equals(AtlasTexture.LOCATION_BLOCKS)) {
			return;
		}
		event.addSprite(EMPTY_CONTAINER_SLOT_BOWL);
	}

	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_CABBAGES.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_ONIONS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_TOMATOES.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_CARROTS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_POTATOES.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_BEETROOTS.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.WILD_RICE.get(), RenderType.cutout());

		RenderTypeLookup.setRenderLayer(ModBlocks.ONION_CROP.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CABBAGE_CROP.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.TOMATO_CROP.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RICE_CROP.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RICE_UPPER_CROP.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.BROWN_MUSHROOM_COLONY.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.RED_MUSHROOM_COLONY.get(), RenderType.cutout());

		RenderTypeLookup.setRenderLayer(ModBlocks.COOKING_POT.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CUTTING_BOARD.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.BASKET.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ROPE.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.SAFETY_NET.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.CANVAS_RUG.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.FULL_TATAMI_MAT.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ROAST_CHICKEN_BLOCK.get(), RenderType.cutout());

		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.STOVE_TILE.get(),
				StoveTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.CUTTING_BOARD_TILE.get(),
				CuttingBoardTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.CANVAS_SIGN_TILE.get(),
				CanvasSignTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.SKILLET_TILE.get(),
				SkilletTileEntityRenderer::new);

		ItemRenderer itemRenderer = event.getMinecraftSupplier().get().getItemRenderer();

		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ROTTEN_TOMATO.get(),
				(renderManager) -> new SpriteRenderer<>(renderManager, itemRenderer));

		ScreenManager.register(ModContainerTypes.COOKING_POT.get(), CookingPotScreen::new);

		NourishedHungerOverlay.init();
		ComfortHealthOverlay.init();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticles(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
	}
}
