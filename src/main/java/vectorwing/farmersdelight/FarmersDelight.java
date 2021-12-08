package vectorwing.farmersdelight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
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
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.world.VillageStructures;
import vectorwing.farmersdelight.core.Configuration;
import vectorwing.farmersdelight.core.FDCreativeModeTab;
import vectorwing.farmersdelight.core.event.ClientSetupEvents;
import vectorwing.farmersdelight.core.event.CommonEvents;
import vectorwing.farmersdelight.core.registry.*;

@Mod(FarmersDelight.MODID)
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmersDelight
{
	public static final String MODID = "farmersdelight";
	public static final FDCreativeModeTab CREATIVE_TAB = new FDCreativeModeTab(FarmersDelight.MODID);

	public static final Logger LOGGER = LogManager.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public FarmersDelight() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(CommonEvents::init);
		modEventBus.addListener(ClientSetupEvents::init);
		modEventBus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);

		ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
		ModEnchantments.ENCHANTMENTS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModEffects.EFFECTS.register(modEventBus);
		ModBiomeFeatures.FEATURES.register(modEventBus);
		ModSounds.SOUNDS.register(modEventBus);
		ModBlockEntityTypes.TILES.register(modEventBus);
		ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
		ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

		MinecraftForge.EVENT_BUS.addListener(VillageStructures::addNewVillageBuilding);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
		CraftingHelper.register(new ResourceLocation(MODID, "tool_action"), ToolActionIngredient.SERIALIZER);

		event.getRegistry().register(CookingPotRecipe.SERIALIZER);
		event.getRegistry().register(CuttingBoardRecipe.SERIALIZER);
	}
}
