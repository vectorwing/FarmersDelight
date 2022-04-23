package vectorwing.farmersdelight.common.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
@ParametersAreNonnullByDefault
public class VillagerEvents
{
	@SubscribeEvent
	public static void onVillagerTrades(VillagerTradesEvent event) {
		if (!Configuration.FARMERS_BUY_FD_CROPS.get()) return;

		Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
		VillagerProfession profession = event.getType();
		if (profession.getRegistryName() == null) return;
		if (profession.getRegistryName().getPath().equals("farmer")) {
			trades.get(1).add(emeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
			trades.get(1).add(emeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
			trades.get(2).add(emeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
			trades.get(2).add(emeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
		}
	}

	@SubscribeEvent
	public static void onWandererTrades(WandererTradesEvent event) {
		if (Configuration.WANDERING_TRADER_SELLS_FD_ITEMS.get()) {
			List<VillagerTrades.ItemListing> trades = event.getGenericTrades();
			trades.add(new BasicItemListing(1, new ItemStack(ModItems.CABBAGE_SEEDS.get()), 1, 12, 1));
			trades.add(new BasicItemListing(1, new ItemStack(ModItems.TOMATO_SEEDS.get()), 1, 12, 1));
			trades.add(new BasicItemListing(1, new ItemStack(ModItems.RICE.get()), 1, 12, 1));
			trades.add(new BasicItemListing(1, new ItemStack(ModItems.ONION.get()), 1, 12, 1));
		}
	}

	public static BasicItemListing emeraldForItemsTrade(ItemLike item, int count, int maxTrades, int xp) {
		return new BasicItemListing(new ItemStack(item, count), new ItemStack(Items.EMERALD), maxTrades, xp, 1);
	}
}
