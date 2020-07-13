package com.github.vectorwing.farmersdelight;

import com.github.vectorwing.farmersdelight.init.BlockInit;
import com.github.vectorwing.farmersdelight.init.ItemInit;
import com.github.vectorwing.farmersdelight.init.TileEntityInit;
import com.github.vectorwing.farmersdelight.setup.ClientEventHandler;
import com.github.vectorwing.farmersdelight.setup.CommonEventHandler;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FarmersDelight.MODID)
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmersDelight
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "farmersdelight";

    public static final ItemGroup ITEM_GROUP = new ItemGroup("farmersdelight") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemInit.CABBAGE.get());
        }
    };

    public FarmersDelight() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CommonEventHandler::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientEventHandler::init);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        TileEntityInit.TILES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        BlockInit.BLOCKS.getEntries().stream()
                .filter(block -> !(block.get() instanceof CropsBlock))
                .map(RegistryObject::get).forEach(block -> {
            final Item.Properties properties = new Item.Properties().group(FarmersDelight.ITEM_GROUP);
            final BlockItem blockItem = new BlockItem(block, properties);
            blockItem.setRegistryName(block.getRegistryName());
            registry.register(blockItem);
        });

        LOGGER.debug("Registered BlockItems!");
    }
}
