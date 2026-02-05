package vectorwing.farmersdelight.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.client.gui.CookingPotTooltip;
import vectorwing.farmersdelight.client.gui.HUDOverlays;
import vectorwing.farmersdelight.client.particle.StarParticle;
import vectorwing.farmersdelight.client.particle.SteamParticle;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.client.renderer.*;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.*;

@EventBusSubscriber(modid = FarmersDelight.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents
{
	public static void init(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> ItemProperties.register(ModItems.SKILLET.get(), ResourceLocation.withDefaultNamespace("cooking"),
				(stack, world, entity, s) -> stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT, ItemStackWrapper.EMPTY).getStack().isEmpty() ? 0 : 1)
		);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions()
		{
			BlockEntityWithoutLevelRenderer renderer = new SkilletItemRenderer();

			@Override
			public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return renderer;
			}
		}, ModItems.SKILLET.get());
	}

	@SubscribeEvent
	public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
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
		event.registerBlockEntityRenderer(ModBlockEntityTypes.STOVE.get(), StoveRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CANVAS_SIGN.get(), CanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.HANGING_CANVAS_SIGN.get(), HangingCanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SKILLET.get(), SkilletRenderer::new);
	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenuTypes.COOKING_POT.get(), CookingPotScreen::new);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
	}

//	@SubscribeEvent
//	public static void onModelRegister(ModelEvent.RegisterAdditional event) {
//		event.register(new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "skillet_cooking"), "inventory"));
//	}

	// TODO: I hate the skillet model so much...
//	@SubscribeEvent
//	public static void onModelBake(ModelEvent.ModifyBakingResult event) {
//		Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();
//
//		ModelResourceLocation skilletLocation = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "skillet"), "inventory");
//		BakedModel skilletModel = modelRegistry.get(skilletLocation);
//		ModelResourceLocation skilletCookingLocation = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "skillet_cooking"), "inventory");
//		BakedModel skilletCookingModel = modelRegistry.get(skilletCookingLocation);
//		modelRegistry.put(skilletLocation, new SkilletModel(event.getModelBakery(), skilletModel, skilletCookingModel));
//	}
}
