package vectorwing.farmersdelight.setup;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.init.ModItems;
import vectorwing.farmersdelight.loot.functions.CopyMealFunction;
import net.minecraft.block.ComposterBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEventHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation SHIPWRECK_SUPPLY_CHEST = new ResourceLocation("minecraft", "chests/shipwreck_supply");
	private static final String[] SCAVENGING_ENTITIES = new String[] { "cow", "chicken", "rabbit", "horse", "donkey", "mule", "llama", "shulker" };

	public static void init(final FMLCommonSetupEvent event)
	{
		ComposterBlock.CHANCES.put(ModItems.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.CABBAGE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.ONION.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO.get(), 0.65F);

		LootFunctionManager.registerFunction(new CopyMealFunction.Serializer());
	}

	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event) {
		String prefix = "minecraft:chests/village/";
		String name = event.getName().toString();

		for (String entity : SCAVENGING_ENTITIES) {
			if (event.getName().equals(new ResourceLocation("minecraft", "entities/" + entity))) {
				event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/" + entity))).build());
			}
		}

		if (Configuration.CROPS_ON_SHIPWRECKS.get() && event.getName().equals(SHIPWRECK_SUPPLY_CHEST)) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/shipwreck_supply"))).build());
		}

		if (Configuration.CROPS_ON_VILLAGE_HOUSES.get() && name.startsWith(prefix)) {
			String file = name.substring(name.indexOf(prefix) + prefix.length());
			switch (file) {
				case "village_plains_house":
				case "village_savanna_house":
				case "village_desert_house":
				case "village_snowy_house":
				case "village_taiga_house":
					event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/" + file))).build());
					break;
				default:
					break;
			}
		}
	}
}
