package vectorwing.farmersdelight.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.ModelEvent.BakingCompleted;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.model.SkilletModel;
import vectorwing.farmersdelight.client.particle.StarParticle;
import vectorwing.farmersdelight.client.particle.SteamParticle;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.client.renderer.CanvasSignRenderer;
import vectorwing.farmersdelight.client.renderer.CuttingBoardRenderer;
import vectorwing.farmersdelight.client.renderer.SkilletRenderer;
import vectorwing.farmersdelight.client.renderer.StoveRenderer;
import vectorwing.farmersdelight.common.registry.*;

import java.util.Map;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents
{
	@SubscribeEvent
	public static void onRegisterRecipeBookCategories(RegisterRecipeBookCategoriesEvent event)
	{
		RecipeCategories.init(event);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterAdditional event) {
		event.register(new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet_cooking"), "inventory"));
	}

	@SubscribeEvent
	public static void onEntityRendererRegister(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityTypes.ROTTEN_TOMATO.get(), ThrownItemRenderer::new);
	}


	@SubscribeEvent
	public static void onCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == ModCreativeTabs.TAB_FARMERS_DELIGHT.getKey()) {
			ModItems.CREATIVE_TAB_ITEMS.forEach(event::accept);
		}
	}

	@SubscribeEvent
	public static void onModelBake(BakingCompleted event) {
		Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();

		ModelResourceLocation skilletLocation = new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet"), "inventory");
		BakedModel skilletModel = modelRegistry.get(skilletLocation);
		ModelResourceLocation skilletCookingLocation = new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet_cooking"), "inventory");
		BakedModel skilletCookingModel = modelRegistry.get(skilletCookingLocation);
		// TODO: Sigh... figure out what's wrong with the skillet. Again.
//		modelRegistry.put(skilletLocation, new SkilletModel(event.getModelBakery(), skilletModel, skilletCookingModel));
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntityTypes.STOVE.get(), StoveRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CANVAS_SIGN.get(), CanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SKILLET.get(), SkilletRenderer::new);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
	}
}
