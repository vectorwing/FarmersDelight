package vectorwing.farmersdelight.setup;

import com.google.common.collect.Sets;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.fml.DeferredWorkQueue;
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
import vectorwing.farmersdelight.world.CropPatchGeneration;

import java.util.Set;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEventHandler
{
	//private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation SHIPWRECK_SUPPLY_CHEST = LootTables.CHESTS_SHIPWRECK_SUPPLY;
	private static final Set<ResourceLocation> VILLAGE_HOUSE_CHESTS = Sets.newHashSet(
			LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
	private static final String[] SCAVENGING_ENTITIES = new String[] { "cow", "chicken", "rabbit", "horse", "donkey", "mule", "llama", "shulker" };

	public static void init(final FMLCommonSetupEvent event)
	{
		ComposterBlock.CHANCES.put(ModItems.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.CABBAGE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.ONION.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO.get(), 0.65F);

		LootFunctionManager.registerFunction(new CopyMealFunction.Serializer());

		DeferredWorkQueue.runLater(CropPatchGeneration::generateCrop);
	}

	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event)
	{
		//String prefix = "minecraft:chests/village/";
		//String name = event.getName().toString();

		for (String entity : SCAVENGING_ENTITIES) {
			if (event.getName().equals(new ResourceLocation("minecraft", "entities/" + entity))) {
				event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/" + entity))).build());
			}
		}

		if (Configuration.CROPS_ON_SHIPWRECKS.get() && event.getName().equals(SHIPWRECK_SUPPLY_CHEST)) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/shipwreck_supply")).weight(1).quality(0)).name("supply_fd_crops").build());
		}

		if (Configuration.CROPS_ON_VILLAGE_HOUSES.get() && VILLAGE_HOUSE_CHESTS.contains(event.getName())) {
			event.getTable().addPool(LootPool.builder().addEntry(
							TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/crops_villager_houses")).weight(1).quality(0)).name("villager_houses_fd_crops").build());
		}
//		if (Configuration.CROPS_ON_VILLAGE_HOUSES.get() && name.startsWith(prefix)) {
//			String file = name.substring(name.indexOf(prefix) + prefix.length());
//			switch (file) {
//				case "village_plains_house":
//				case "village_savanna_house":
//				case "village_desert_house":
//				case "village_snowy_house":
//				case "village_taiga_house":
//					event.getTable().addPool(LootPool.builder().addEntry(
//							TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/" + file)).weight(1).quality(0)).name(file + "_fd_crops").build());
//					break;
//				default:
//					break;
//			}
//		}
	}
}
