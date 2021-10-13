package vectorwing.farmersdelight.setup;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.item.SkilletModel;

import java.util.Map;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelEventHandler
{
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		ModelLoader.addSpecialModel(new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet_cooking"), "inventory"));
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

		ModelResourceLocation skilletLocation = new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet"), "inventory");
		IBakedModel skilletModel = modelRegistry.get(skilletLocation);
		ModelResourceLocation skilletCookingLocation = new ModelResourceLocation(new ResourceLocation(FarmersDelight.MODID, "skillet_cooking"), "inventory");
		IBakedModel skilletCookingModel = modelRegistry.get(skilletCookingLocation);
		modelRegistry.put(skilletLocation, new SkilletModel(event.getModelLoader(), skilletModel, skilletCookingModel));
	}
}
