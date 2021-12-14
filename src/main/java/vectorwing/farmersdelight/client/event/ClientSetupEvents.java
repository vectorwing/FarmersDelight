package vectorwing.farmersdelight.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.model.SkilletModel;
import vectorwing.farmersdelight.client.particle.StarParticle;
import vectorwing.farmersdelight.client.particle.SteamParticle;
import vectorwing.farmersdelight.client.renderer.CanvasSignRenderer;
import vectorwing.farmersdelight.client.renderer.CuttingBoardRenderer;
import vectorwing.farmersdelight.client.renderer.SkilletRenderer;
import vectorwing.farmersdelight.client.renderer.StoveRenderer;
import vectorwing.farmersdelight.common.registry.ModAtlases;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;

import java.util.Map;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents
{
	public static final ResourceLocation EMPTY_CONTAINER_SLOT_BOWL = new ResourceLocation(FarmersDelight.MODID, "item/empty_container_slot_bowl");

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		ForgeModelBakery.addSpecialModel(new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet_cooking"), "inventory"));
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		Map<ResourceLocation, BakedModel> modelRegistry = event.getModelRegistry();

		ModelResourceLocation skilletLocation = new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet"), "inventory");
		BakedModel skilletModel = modelRegistry.get(skilletLocation);
		ModelResourceLocation skilletCookingLocation = new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet_cooking"), "inventory");
		BakedModel skilletCookingModel = modelRegistry.get(skilletCookingLocation);
		modelRegistry.put(skilletLocation, new SkilletModel(event.getModelLoader(), skilletModel, skilletCookingModel));
	}

	@SubscribeEvent
	public static void onStitchEvent(TextureStitchEvent.Pre event) {
		ResourceLocation stitching = event.getAtlas().location();
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
		event.registerBlockEntityRenderer(ModBlockEntityTypes.STOVE.get(), StoveRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CUTTING_BOARD.get(), CuttingBoardRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CANVAS_SIGN.get(), CanvasSignRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SKILLET.get(), SkilletRenderer::new);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerParticles(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STAR.get(), StarParticle.Factory::new);
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.STEAM.get(), SteamParticle.Factory::new);
	}
}
