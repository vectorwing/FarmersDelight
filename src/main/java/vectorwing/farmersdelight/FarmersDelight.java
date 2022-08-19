package vectorwing.farmersdelight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.client.ClientSetup;
import vectorwing.farmersdelight.common.CommonSetup;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.*;
import vectorwing.farmersdelight.common.world.VillageStructures;

import javax.annotation.Nonnull;

@Mod(FarmersDelight.MODID)
public class FarmersDelight
{
	public static final String MODID = "farmersdelight";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public static final RecipeBookType RECIPE_TYPE_COOKING = RecipeBookType.create("COOKING");

	public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(FarmersDelight.MODID)
	{
		@Nonnull
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(ModBlocks.STOVE.get());
		}
	};

	public FarmersDelight() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(CommonSetup::init);
		modEventBus.addListener(ClientSetup::init);

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
		ModMenuTypes.MENU_TYPES.register(modEventBus);
		ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
		ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
		ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
		ModEntityTypes.ENTITIES.register(modEventBus);
		ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);

		MinecraftForge.EVENT_BUS.addListener(VillageStructures::addNewVillageBuilding);

		MinecraftForge.EVENT_BUS.register(this);
	}
}
