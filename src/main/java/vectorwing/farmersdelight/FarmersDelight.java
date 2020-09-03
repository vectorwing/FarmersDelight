package vectorwing.farmersdelight;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.registry.*;
import vectorwing.farmersdelight.setup.ClientEventHandler;
import vectorwing.farmersdelight.setup.CommonEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.setup.Configuration;

@Mod(FarmersDelight.MODID)
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmersDelight
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "farmersdelight";
    public static final FDItemGroup ITEM_GROUP = new FDItemGroup(FarmersDelight.MODID);

    public FarmersDelight() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonEventHandler::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandler::init);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEnchantments.ENCHANTMENTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModTileEntityTypes.TILES.register(modEventBus);
        ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(FarmersDelight.MODID, "cooking"), CookingPotRecipe.TYPE);
        event.getRegistry().register(CookingPotRecipe.SERIALIZER);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(FarmersDelight.MODID, "cutting"), CuttingBoardRecipe.TYPE);
        event.getRegistry().register(CuttingBoardRecipe.SERIALIZER);
    }
}
