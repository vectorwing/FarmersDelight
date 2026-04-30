package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.HashMap;

public class CommonSetup
{
	public static void init(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			registerDispenserBehaviors();
			registerItemSetAdditions();
		});
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerProjectileBehavior(ModItems.ROTTEN_TOMATO.get());
	}

	public static void registerItemSetAdditions() {
		// TODO: Replaced by ItemTags.VILLAGER_PICKS_UP. Add them to this tag once datagen isn't broken.
//		Set<Item> newWantedItems = Sets.newHashSet(
//				ModItems.CABBAGE.get(),
//				ModItems.TOMATO.get(),
//				ModItems.ONION.get(),
//				ModItems.RICE.get(),
//				ModItems.CABBAGE_SEEDS.get(),
//				ModItems.TOMATO_SEEDS.get(),
//				ModItems.RICE_PANICLE.get());
//		newWantedItems.addAll(Villager.WANTED_ITEMS);
//		Villager.WANTED_ITEMS = ImmutableSet.copyOf(newWantedItems);

		HashMap<Item, Integer> newFoodPoints = new HashMap<>();
		newFoodPoints.put(ModItems.CABBAGE.get(), 1);
		newFoodPoints.put(ModItems.TOMATO.get(), 1);
		newFoodPoints.put(ModItems.ONION.get(), 1);
		newFoodPoints.put(ModItems.RICE.get(), 2);
		newFoodPoints.putAll(Villager.FOOD_POINTS);

		Villager.FOOD_POINTS = ImmutableMap.copyOf(newFoodPoints);
	}
}
