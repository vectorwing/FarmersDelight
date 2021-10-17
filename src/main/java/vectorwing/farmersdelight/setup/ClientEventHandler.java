package vectorwing.farmersdelight.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.NourishedHungerOverlay;
import vectorwing.farmersdelight.client.particles.StarParticle;
import vectorwing.farmersdelight.client.particles.SteamParticle;
import vectorwing.farmersdelight.client.tileentity.renderer.CanvasSignRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.CuttingBoardRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.SkilletRenderer;
import vectorwing.farmersdelight.client.tileentity.renderer.StoveRenderer;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModContainerTypes;
import vectorwing.farmersdelight.registry.ModParticleTypes;
import vectorwing.farmersdelight.registry.ModBlockEntityTypes;
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
			for (Material material : ModAtlases.DYED_CANVAS_SIGN_MATERIALS.values()) {
				event.addSprite(material.texture());
			}
		}
		if (!stitching.equals(TextureAtlas.LOCATION_BLOCKS)) {
			return;
		}
		event.addSprite(EMPTY_CONTAINER_SLOT_BOWL);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntityTypes.STOVE_TILE.get(), StoveRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CUTTING_BOARD_TILE.get(), CuttingBoardRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CANVAS_SIGN_TILE.get(), CanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SKILLET_TILE.get(), SkilletRenderer::new);
	}

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
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.RICE_UPPER_CROP.get(), RenderType.cutout());
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

		NourishedHungerOverlay.init();
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticles(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
	}
}
