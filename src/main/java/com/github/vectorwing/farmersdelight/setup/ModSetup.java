package com.github.vectorwing.farmersdelight.setup;

import com.github.vectorwing.farmersdelight.FarmersDelight;
import com.github.vectorwing.farmersdelight.init.ItemInit;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static void init(final FMLCommonSetupEvent event)
	{
		ComposterBlock.CHANCES.put(ItemInit.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ItemInit.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ItemInit.CABBAGE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ItemInit.ONION.get(), 0.65F);
		ComposterBlock.CHANCES.put(ItemInit.TOMATO.get(), 0.65F);
	}
}
