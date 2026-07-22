package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.HashMap;
import java.util.Set;

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
		// Villager food collections are immutable in 26.1; this behavior needs a data-driven replacement.
	}
}
