package vectorwing.farmersdelight.setup;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.conditions.VanillaCrateEnabledCondition;
import vectorwing.farmersdelight.loot.functions.CopyMealFunction;
import vectorwing.farmersdelight.loot.functions.SmokerCookFunction;
import vectorwing.farmersdelight.registry.ModAdvancements;
import vectorwing.farmersdelight.registry.ModEffects;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.tile.dispenser.CuttingBoardDispenseBehavior;
import vectorwing.farmersdelight.utils.tags.ModTags;
import vectorwing.farmersdelight.world.CropPatchGeneration;
import vectorwing.farmersdelight.world.VillageStructures;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
@ParametersAreNonnullByDefault
public class CommonEventHandler
{
	private static final ResourceLocation SHIPWRECK_SUPPLY_CHEST = LootTables.CHESTS_SHIPWRECK_SUPPLY;
	private static final Set<ResourceLocation> VILLAGE_HOUSE_CHESTS = Sets.newHashSet(
			LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE,
			LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
	private static final String[] SCAVENGING_ENTITIES = new String[]{"cow", "pig", "chicken", "rabbit", "horse", "donkey", "mule", "llama", "spider", "hoglin", "shulker"};

	public static void init(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			registerCompostables();
			registerDispenserBehaviors();
			CropPatchGeneration.registerConfiguredFeatures();
		});

		ModAdvancements.register();

		LootFunctionManager.func_237451_a_(CopyMealFunction.ID.toString(), new CopyMealFunction.Serializer());
		LootFunctionManager.func_237451_a_(SmokerCookFunction.ID.toString(), new SmokerCookFunction.Serializer());

		CraftingHelper.register(new VanillaCrateEnabledCondition.Serializer());

		if (Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
			VillageStructures.init();
		}
	}

	public static void registerDispenserBehaviors() {
		if (Configuration.DISPENSER_TOOLS_CUTTING_BOARD.get()) {
			CuttingBoardDispenseBehavior.registerBehaviour(Items.WOODEN_PICKAXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.WOODEN_AXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.WOODEN_SHOVEL, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.STONE_PICKAXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.STONE_AXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.STONE_SHOVEL, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.IRON_PICKAXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.IRON_AXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.IRON_SHOVEL, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.DIAMOND_PICKAXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.DIAMOND_AXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.DIAMOND_SHOVEL, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.GOLDEN_PICKAXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.GOLDEN_AXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.GOLDEN_SHOVEL, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.NETHERITE_PICKAXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.NETHERITE_AXE, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.NETHERITE_SHOVEL, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(Items.SHEARS, new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(ModItems.FLINT_KNIFE.get(), new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(ModItems.IRON_KNIFE.get(), new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(ModItems.DIAMOND_KNIFE.get(), new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(ModItems.GOLDEN_KNIFE.get(), new CuttingBoardDispenseBehavior());
			CuttingBoardDispenseBehavior.registerBehaviour(ModItems.NETHERITE_KNIFE.get(), new CuttingBoardDispenseBehavior());
		}
	}

	public static void registerCompostables() {
		// 30% chance
		ComposterBlock.CHANCES.put(ModItems.TREE_BARK.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.STRAW.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.CHANCES.put(ModItems.RICE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.RICE_PANICLE.get(), 0.65F);

		// 50% chance
		ComposterBlock.CHANCES.put(ModItems.PUMPKIN_SLICE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.CABBAGE_LEAF.get(), 0.65F);

		// 65% chance
		ComposterBlock.CHANCES.put(ModItems.CABBAGE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.ONION.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.TOMATO.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_CABBAGES.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_ONIONS.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_TOMATOES.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_CARROTS.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_POTATOES.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_BEETROOTS.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.WILD_RICE.get(), 0.65F);
		ComposterBlock.CHANCES.put(ModItems.PIE_CRUST.get(), 0.65F);

		// 85% chance
		ComposterBlock.CHANCES.put(ModItems.RICE_BALE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.SWEET_BERRY_COOKIE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.HONEY_COOKIE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.CAKE_SLICE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.APPLE_PIE_SLICE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.CHOCOLATE_PIE_SLICE.get(), 0.85F);
		ComposterBlock.CHANCES.put(ModItems.RAW_PASTA.get(), 0.85F);

		// 100% chance
		ComposterBlock.CHANCES.put(ModItems.APPLE_PIE.get(), 1.0F);
		ComposterBlock.CHANCES.put(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1.0F);
		ComposterBlock.CHANCES.put(ModItems.CHOCOLATE_PIE.get(), 1.0F);
		ComposterBlock.CHANCES.put(ModItems.DUMPLINGS.get(), 1.0F);
		ComposterBlock.CHANCES.put(ModItems.STUFFED_PUMPKIN.get(), 1.0F);
		ComposterBlock.CHANCES.put(ModItems.BROWN_MUSHROOM_COLONY.get(), 1.0F);
		ComposterBlock.CHANCES.put(ModItems.RED_MUSHROOM_COLONY.get(), 1.0F);
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.Climate climate = event.getClimate();

		if (event.getName().getPath().equals("beach")) {
			if (Configuration.GENERATE_WILD_BEETROOTS.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_BEETROOTS);
			}
			if (Configuration.GENERATE_WILD_CABBAGES.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_CABBAGES);
			}
		}

		if (event.getCategory().equals(Biome.Category.SWAMP) || event.getCategory().equals(Biome.Category.JUNGLE)) {
			if (Configuration.GENERATE_WILD_RICE.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_RICE);
			}
		}

		if (climate.temperature >= 1.0F) {
			if (Configuration.GENERATE_WILD_TOMATOES.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_TOMATOES);
			}
		}

		if (climate.temperature > 0.3F && climate.temperature < 1.0F) {
			if (Configuration.GENERATE_WILD_CARROTS.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_CARROTS);
			}
			if (Configuration.GENERATE_WILD_ONIONS.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_ONIONS);
			}
		}

		if (climate.temperature > 0.0F && climate.temperature <= 0.3F) {
			if (Configuration.GENERATE_WILD_POTATOES.get()) {
				builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_POTATOES);
			}
		}
	}

	@SubscribeEvent
	public static void onVillagerTrades(VillagerTradesEvent event) {
		if (!Configuration.FARMERS_BUY_FD_CROPS.get()) return;

		Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
		VillagerProfession profession = event.getType();
		if (profession.getRegistryName() == null) return;
		if (profession.getRegistryName().getPath().equals("farmer")) {
			trades.get(1).add(new EmeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
			trades.get(1).add(new EmeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
			trades.get(2).add(new EmeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
			trades.get(2).add(new EmeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
		}
	}

	@SubscribeEvent
	public static void handleAdditionalFoodEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntityLiving();

		// Adds 3:00 of Jump Boost II when eating Rabbit Stew
		if (Configuration.RABBIT_STEW_JUMP_BOOST.get() && food.equals(Items.RABBIT_STEW)) {
			entity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 3600, 1));
		}

		// Adds 5:00 of Comfort when eating foods inside the tag farmersdelight:comfort_foods
		if (Configuration.COMFORT_FOOD_TAG_EFFECT.get() && food.isIn(ModTags.COMFORT_FOODS)) {
			entity.addPotionEffect(new EffectInstance(ModEffects.COMFORT.get(), 6000, 0));
		}
	}

	static class EmeraldForItemsTrade implements VillagerTrades.ITrade
	{
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
	public static void onLootLoad(LootTableLoadEvent event) {
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
