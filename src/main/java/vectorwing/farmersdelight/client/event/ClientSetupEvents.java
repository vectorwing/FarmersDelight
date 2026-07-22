package vectorwing.farmersdelight.client.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.CookingPotTooltip;
import vectorwing.farmersdelight.client.gui.HUDOverlays;
import vectorwing.farmersdelight.client.particle.SparkleParticle;
import vectorwing.farmersdelight.client.particle.StarParticle;
import vectorwing.farmersdelight.client.particle.SteamParticle;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.client.renderer.*;
import vectorwing.farmersdelight.common.registry.*;

@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class ClientSetupEvents
{
	public static void init(final FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void registerRecipeBookCategories(RegisterRecipeBookSearchCategoriesEvent event) {
		RecipeCategories.init(event);
	}

	@SubscribeEvent
	public static void registerCustomTooltipRenderers(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(CookingPotTooltip.CookingPotTooltipComponent.class, CookingPotTooltip::new);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		HUDOverlays.register(event);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.ROTTEN_TOMATO.get(), ThrownItemRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.STOVE.get(), DefaultStoveRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CANVAS_SIGN.get(), StandingCanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.HANGING_CANVAS_SIGN.get(), HangingCanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SKILLET.get(), SkilletRenderer::new);
	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenuTypes.COOKING_POT.get(), CookingPotScreen::new);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		event.registerSpriteSet(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
		event.registerSpriteSet(ModParticleTypes.SPARKLE.get(), SparkleParticle.Factory::new);
	}
}

