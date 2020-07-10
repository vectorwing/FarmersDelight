package com.github.vectorwing.farmersdelight.setup;

import com.github.vectorwing.farmersdelight.FarmersDelight;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static void init(final FMLCommonSetupEvent event)
	{
	}
}
