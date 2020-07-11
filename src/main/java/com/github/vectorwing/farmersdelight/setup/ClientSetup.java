package com.github.vectorwing.farmersdelight.setup;

import com.github.vectorwing.farmersdelight.FarmersDelight;
import com.github.vectorwing.farmersdelight.init.BlockInit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static void init(final FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(BlockInit.ONION_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(BlockInit.CABBAGE_CROP.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(BlockInit.TOMATO_CROP.get(), RenderType.getCutout());
	}
}
