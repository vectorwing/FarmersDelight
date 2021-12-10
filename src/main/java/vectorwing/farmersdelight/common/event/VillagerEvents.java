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
import net.minecraftforge.event.village.VillagerTradesEvent;
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
			trades.get(1).add(new EmeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
			trades.get(1).add(new EmeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
			trades.get(2).add(new EmeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
			trades.get(2).add(new EmeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
		}
	}

	static class EmeraldForItemsTrade implements VillagerTrades.ItemListing
	{
		private final Item tradeItem;
		private final int count;
		private final int maxUses;
		private final int xpValue;
		private final float priceMultiplier;

		public EmeraldForItemsTrade(ItemLike tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
			this.tradeItem = tradeItemIn.asItem();
			this.count = countIn;
			this.maxUses = maxUsesIn;
			this.xpValue = xpValueIn;
			this.priceMultiplier = 0.05F;
		}

		public MerchantOffer getOffer(Entity trader, Random rand) {
			ItemStack itemstack = new ItemStack(this.tradeItem, this.count);
			return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.xpValue, this.priceMultiplier);
		}
	}
}
