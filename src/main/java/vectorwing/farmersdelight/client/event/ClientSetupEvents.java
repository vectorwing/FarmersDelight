package vectorwing.farmersdelight.client.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.CookingPotTooltip;
import vectorwing.farmersdelight.client.particle.SparkleParticle;
import vectorwing.farmersdelight.client.particle.StarParticle;
import vectorwing.farmersdelight.client.particle.SteamParticle;
import vectorwing.farmersdelight.client.renderer.CuttingBoardRenderer;
import vectorwing.farmersdelight.client.renderer.DefaultStoveRenderer;
import vectorwing.farmersdelight.client.renderer.CanvasSignRenderer;
import vectorwing.farmersdelight.client.renderer.HangingCanvasSignRenderer;
import vectorwing.farmersdelight.client.renderer.SkilletRenderer;
import vectorwing.farmersdelight.common.registry.*;

@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class ClientSetupEvents
{
	public static void init(final FMLClientSetupEvent event) {
		// TODO 26.2: Re-port skillet item predicates and custom item renderer.
	}

	@SubscribeEvent
	public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(CookingPotTooltip.CookingPotTooltipComponent.class, CookingPotTooltip::new);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.ROTTEN_TOMATO.get(), ThrownItemRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SKILLET.get(), SkilletRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.STOVE.get(), DefaultStoveRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CANVAS_SIGN.get(), CanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.HANGING_CANVAS_SIGN.get(), HangingCanvasSignRenderer::new);
	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenuTypes.COOKING_POT.get(), CookingPotScreen::new);
	}

	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		event.registerSpriteSet(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
		event.registerSpriteSet(ModParticleTypes.SPARKLE.get(), SparkleParticle.Factory::new);
	}
}
