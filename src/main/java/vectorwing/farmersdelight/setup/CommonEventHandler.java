package vectorwing.farmersdelight.setup;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModAdvancements;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModEffects;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.loot.functions.CopyMealFunction;
import net.minecraft.block.ComposterBlock;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import vectorwing.farmersdelight.tile.dispenser.CuttingBoardDispenseBehavior;
import vectorwing.farmersdelight.world.CropPatchGeneration;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEventHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
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
		ComposterBlock.CHANCES.put(ModItems.TREE_BARK.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.STRAW.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.CABBAGE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.ONION.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO.get(), 0.65F);

		ModAdvancements.register();

		LootFunctionManager.registerFunction(new CopyMealFunction.Serializer());

		DispenserBlock.registerDispenseBehavior(Items.WOODEN_PICKAXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.WOODEN_AXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.WOODEN_SHOVEL, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.STONE_PICKAXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.STONE_AXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.STONE_SHOVEL, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.IRON_PICKAXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.IRON_AXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.IRON_SHOVEL, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.DIAMOND_PICKAXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.DIAMOND_AXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.DIAMOND_SHOVEL, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.GOLDEN_PICKAXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.GOLDEN_AXE, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.GOLDEN_SHOVEL, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(Items.SHEARS, new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(ModItems.FLINT_KNIFE.get(), new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(ModItems.IRON_KNIFE.get(), new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(ModItems.DIAMOND_KNIFE.get(), new CuttingBoardDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(ModItems.GOLDEN_KNIFE.get(), new CuttingBoardDispenseBehavior());

		DeferredWorkQueue.runLater(CropPatchGeneration::generateCrop);
	}

	@SubscribeEvent
	public static void onRemapBlocks(RegistryEvent.MissingMappings<Block> event) {
		ModContainer mod = ModList.get().getModContainerById(FarmersDelight.MODID).get();
		event.setModContainer(mod);

		// Mulch -> Rich Soil
		event.getMappings().stream()
				.filter(mapping -> mapping.key.getPath().equals("mulch"))
				.forEach(mapping -> mapping.remap(ModBlocks.RICH_SOIL.get()));
		LOGGER.info("Remapping block 'farmersdelight:mulch' to 'farmersdelight:rich_soil'...");

		// Mulch Farmland -> Rich Soil Farmland
		event.getMappings().stream()
				.filter(mapping -> mapping.key.getPath().equals("mulch_farmland"))
				.forEach(mapping -> mapping.remap(ModBlocks.RICH_SOIL_FARMLAND.get()));
		LOGGER.info("Remapping block 'farmersdelight:mulch_farmland' to 'farmersdelight:rich_soil_farmland'...");
	}

	@SubscribeEvent
	public static void onRemapItems(RegistryEvent.MissingMappings<Item> event) {
		ModContainer mod = ModList.get().getModContainerById(FarmersDelight.MODID).get();
		event.setModContainer(mod);

		// Mulch -> Rich Soil
		event.getMappings().stream()
				.filter(mapping -> mapping.key.getPath().equals("mulch"))
				.forEach(mapping -> mapping.remap(ModItems.RICH_SOIL.get()));
		LOGGER.info("Remapping item 'farmersdelight:mulch' to 'farmersdelight:rich_soil'...");

		// Mulch Farmland -> Rich Soil Farmland
		event.getMappings().stream()
				.filter(mapping -> mapping.key.getPath().equals("mulch_farmland"))
				.forEach(mapping -> mapping.remap(ModItems.RICH_SOIL_FARMLAND.get()));
		LOGGER.info("Remapping item 'farmersdelight:mulch_farmland' to 'farmersdelight:rich_soil_farmland'...");
	}

	@SubscribeEvent
	public static void onVillagerTrades(VillagerTradesEvent event) {
		if (!Configuration.FARMERS_BUY_FD_CROPS.get()) return;

		Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
		VillagerProfession profession = event.getType();
		if (profession.getRegistryName() == null) return;
		if (profession.getRegistryName().getPath().equals("farmer"))
		{
			trades.get(1).add(new EmeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
			trades.get(1).add(new EmeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
			trades.get(2).add(new EmeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
			trades.get(2).add(new EmeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
		}
	}

	@SubscribeEvent
	public static void onSoupItemConsumed(LivingEntityUseItemEvent.Finish event) {
		if (!Configuration.VANILLA_SOUP_EFFECTS.get()) return;

		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntityLiving();
		if (food instanceof SoupItem && !food.equals(Items.SUSPICIOUS_STEW)) {
			if (food.equals(Items.RABBIT_STEW)) {
				entity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 1));
			}
			entity.addPotionEffect(new EffectInstance(ModEffects.COMFORT.get(), 9600, 0));
		}
	}

	static class EmeraldForItemsTrade implements VillagerTrades.ITrade {
		private final Item tradeItem;
		private final int count;
		private final int maxUses;
		private final int xpValue;
		private final float priceMultiplier;

		public EmeraldForItemsTrade(IItemProvider tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
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

	@SubscribeEvent
	public static void onHoeUse(UseHoeEvent event) {
		ItemUseContext context = event.getContext();
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		BlockState state = world.getBlockState(pos);

		if (context.getFace() != Direction.DOWN && world.isAirBlock(pos.up()) && state.getBlock() == ModBlocks.RICH_SOIL.get()) {
			world.playSound(event.getPlayer(), pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.setBlockState(pos, ModBlocks.RICH_SOIL_FARMLAND.get().getDefaultState(), 11);
			event.setResult(Event.Result.ALLOW);
		}
	}

	@SubscribeEvent
	public static void onLootLoad(LootTableLoadEvent event)
	{
		for (String entity : SCAVENGING_ENTITIES) {
			if (event.getName().equals(new ResourceLocation("minecraft", "entities/" + entity))) {
				event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/" + entity))).name(entity + "_fd_drops").build());
			}
		}

		if (Configuration.CROPS_ON_SHIPWRECKS.get() && event.getName().equals(SHIPWRECK_SUPPLY_CHEST)) {
			event.getTable().addPool(LootPool.builder().addEntry(TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/shipwreck_supply")).weight(1).quality(0)).name("supply_fd_crops").build());
		}

		if (Configuration.CROPS_ON_VILLAGE_HOUSES.get() && VILLAGE_HOUSE_CHESTS.contains(event.getName())) {
			event.getTable().addPool(LootPool.builder().addEntry(
							TableLootEntry.builder(new ResourceLocation(FarmersDelight.MODID, "inject/crops_villager_houses")).weight(1).quality(0)).name("villager_houses_fd_crops").build());
		}
	}
}
