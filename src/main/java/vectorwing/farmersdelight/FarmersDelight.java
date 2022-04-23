package vectorwing.farmersdelight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.crafting.ingredients.ToolIngredient;
import vectorwing.farmersdelight.registry.*;
import vectorwing.farmersdelight.setup.CommonEventHandler;
import vectorwing.farmersdelight.setup.Configuration;

@Mod(FarmersDelight.MODID)
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmersDelight
{
	public static final String MODID = "farmersdelight";
	public static final FDItemGroup ITEM_GROUP = new FDItemGroup(FarmersDelight.MODID);

	public static final Logger LOGGER = LogManager.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.disableHtmlEscaping()
			.create();

	public FarmersDelight() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(CommonEventHandler::init);
		modEventBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);

		ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
		ModEnchantments.ENCHANTMENTS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModEffects.EFFECTS.register(modEventBus);
		ModBiomeFeatures.FEATURES.register(modEventBus);
		ModSounds.SOUNDS.register(modEventBus);
		ModTileEntityTypes.TILES.register(modEventBus);
		ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
		ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
		ModEntityTypes.ENTITIES.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		CraftingHelper.register(new ResourceLocation(MODID, "tool"), ToolIngredient.SERIALIZER);

		event.getRegistry().register(CookingPotRecipe.SERIALIZER);
		event.getRegistry().register(CuttingBoardRecipe.SERIALIZER);
	}
}
