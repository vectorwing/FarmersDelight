package vectorwing.farmersdelight;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.client.event.ClientSetupEvents;
import vectorwing.farmersdelight.common.CommonSetup;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.*;
import vectorwing.farmersdelight.common.world.VillageStructures;
import vectorwing.farmersdelight.common.world.WildCropGeneration;

@Mod(FarmersDelight.MODID)
public class FarmersDelight
{
	public static final String MODID = "farmersdelight";
	public static final Logger LOGGER = LogManager.getLogger();

	public FarmersDelight(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(CommonSetup::init);
		if (FMLEnvironment.dist.isClient()) {
			modEventBus.addListener(ClientSetupEvents::init);
		}

		modContainer.registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_CONFIG);

		ModSounds.SOUNDS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModEffects.EFFECTS.register(modEventBus);
		ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModDataComponents.DATA_COMPONENTS.register(modEventBus);
		ModDataComponents.ENCHANTMENT_EFFECT_COMPONENTS.register(modEventBus);
		ModEntityTypes.ENTITIES.register(modEventBus);
		ModBlockEntityTypes.TILES.register(modEventBus);
		ModMenuTypes.MENU_TYPES.register(modEventBus);
		ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
		ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
		ModBiomeFeatures.FEATURES.register(modEventBus);
		ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
		ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
		ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
		ModLootFunctions.LOOT_FUNCTIONS.register(modEventBus);
		ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
		ModConditionCodecs.CONDITION_CODECS.register(modEventBus);
		ModIngredientTypes.INGREDIENT_TYPES.register(modEventBus);
		ModAdvancements.TRIGGERS.register(modEventBus);

		WildCropGeneration.load();
		NeoForge.EVENT_BUS.addListener(VillageStructures::addNewVillageBuilding);
	}
}
