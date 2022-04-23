package vectorwing.farmersdelight.setup;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.conditions.VanillaCrateEnabledCondition;
import vectorwing.farmersdelight.entity.RottenTomatoEntity;
import vectorwing.farmersdelight.items.Foods;
import vectorwing.farmersdelight.loot.functions.CopyMealFunction;
import vectorwing.farmersdelight.loot.functions.CopySkilletFunction;
import vectorwing.farmersdelight.loot.functions.SmokerCookFunction;
import vectorwing.farmersdelight.mixin.accessors.ChickenEntityAccessor;
import vectorwing.farmersdelight.mixin.accessors.PigEntityAccessor;
import vectorwing.farmersdelight.registry.ModAdvancements;
import vectorwing.farmersdelight.registry.ModEffects;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.tile.dispenser.CuttingBoardDispenseBehavior;
import vectorwing.farmersdelight.utils.tags.ModTags;
import vectorwing.farmersdelight.world.CropPatchGeneration;
import vectorwing.farmersdelight.world.VillageStructures;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
@ParametersAreNonnullByDefault
public class CommonEventHandler
{
	public static void init(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			registerCompostables();
			registerDispenserBehaviors();
			CropPatchGeneration.registerConfiguredFeatures();

			List<ItemStack> chickenFood = new ArrayList<>();
			Collections.addAll(chickenFood, ChickenEntityAccessor.getFoodItems().getItems());
			chickenFood.add(new ItemStack(ModItems.CABBAGE_SEEDS.get()));
			chickenFood.add(new ItemStack(ModItems.TOMATO_SEEDS.get()));
			chickenFood.add(new ItemStack(ModItems.RICE.get()));
			ChickenEntityAccessor.setFoodItems(Ingredient.of(chickenFood.stream()));

			List<ItemStack> pigFood = new ArrayList<>();
			Collections.addAll(pigFood, PigEntityAccessor.getFoodItems().getItems());
			pigFood.add(new ItemStack(ModItems.CABBAGE.get()));
			pigFood.add(new ItemStack(ModItems.TOMATO.get()));
			PigEntityAccessor.setFoodItems(Ingredient.of(pigFood.stream()));
		});

		ModAdvancements.register();

		LootFunctionManager.register(CopyMealFunction.ID.toString(), new CopyMealFunction.Serializer());
		LootFunctionManager.register(CopySkilletFunction.ID.toString(), new CopySkilletFunction.Serializer());
		LootFunctionManager.register(SmokerCookFunction.ID.toString(), new SmokerCookFunction.Serializer());

		CraftingHelper.register(new VanillaCrateEnabledCondition.Serializer());

		if (Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
			VillageStructures.init();
		}
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(ModItems.ROTTEN_TOMATO.get(), new ProjectileDispenseBehavior()
		{
			@Nonnull
			protected ProjectileEntity getProjectile(World pLevel, IPosition pPosition, ItemStack pStack) {
				return new RottenTomatoEntity(pLevel, pPosition.x(), pPosition.y(), pPosition.z());
			}
		});

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
		ComposterBlock.COMPOSTABLES.put(ModItems.TREE_BARK.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.STRAW.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RICE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RICE_PANICLE.get(), 0.65F);

		// 50% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.PUMPKIN_SLICE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CABBAGE_LEAF.get(), 0.65F);

		// 65% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.CABBAGE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.ONION.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.TOMATO.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_CABBAGES.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_ONIONS.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_TOMATOES.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_CARROTS.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_POTATOES.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_BEETROOTS.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_RICE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.PIE_CRUST.get(), 0.65F);

		// 85% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.RICE_BALE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SWEET_BERRY_COOKIE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.HONEY_COOKIE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CAKE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.APPLE_PIE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CHOCOLATE_PIE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RAW_PASTA.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.ROTTEN_TOMATO.get(), 0.85F);

		// 100% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.APPLE_PIE.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CHOCOLATE_PIE.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.DUMPLINGS.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.STUFFED_PUMPKIN.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.BROWN_MUSHROOM_COLONY.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RED_MUSHROOM_COLONY.get(), 1.0F);
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.Climate climate = event.getClimate();

		if (event.getName().getPath().equals("beach")) {
			if (Configuration.GENERATE_WILD_BEETROOTS.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_BEETROOTS);
			}
			if (Configuration.GENERATE_WILD_CABBAGES.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_CABBAGES);
			}
		}

		if (event.getCategory().equals(Biome.Category.SWAMP) || event.getCategory().equals(Biome.Category.JUNGLE)) {
			if (Configuration.GENERATE_WILD_RICE.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_RICE);
			}
		}

		if (climate.temperature >= 1.0F) {
			if (Configuration.GENERATE_WILD_TOMATOES.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_TOMATOES);
			}
		}

		if (climate.temperature > 0.3F && climate.temperature < 1.0F) {
			if (Configuration.GENERATE_WILD_CARROTS.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_CARROTS);
			}
			if (Configuration.GENERATE_WILD_ONIONS.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_ONIONS);
			}
		}

		if (climate.temperature > 0.0F && climate.temperature <= 0.3F) {
			if (Configuration.GENERATE_WILD_POTATOES.get()) {
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CropPatchGeneration.PATCH_WILD_POTATOES);
			}
		}
	}

	@SubscribeEvent
	public static void onVillagerTrades(VillagerTradesEvent event) {
		if (Configuration.FARMERS_BUY_FD_CROPS.get()) {
			Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
			VillagerProfession profession = event.getType();
			if (profession.getRegistryName() == null) return;
			if (profession.getRegistryName().getPath().equals("farmer")) {
				trades.get(1).add(emeraldForItemsTrade(ModItems.ONION.get(), 26, 16, 2));
				trades.get(1).add(emeraldForItemsTrade(ModItems.TOMATO.get(), 26, 16, 2));
				trades.get(2).add(emeraldForItemsTrade(ModItems.CABBAGE.get(), 16, 16, 5));
				trades.get(2).add(emeraldForItemsTrade(ModItems.RICE.get(), 20, 16, 5));
			}
		}
	}

	@SubscribeEvent
	public static void onWandererTrades(WandererTradesEvent event) {
		if (Configuration.WANDERING_TRADER_SELLS_FD_ITEMS.get()) {
			List<VillagerTrades.ITrade> trades = event.getGenericTrades();
			trades.add(new BasicTrade(1, new ItemStack(ModItems.CABBAGE_SEEDS.get()), 1, 12, 1));
			trades.add(new BasicTrade(1, new ItemStack(ModItems.TOMATO_SEEDS.get()), 1, 12, 1));
			trades.add(new BasicTrade(1, new ItemStack(ModItems.RICE.get()), 1, 12, 1));
			trades.add(new BasicTrade(1, new ItemStack(ModItems.ONION.get()), 1, 12, 1));
		}
	}

	public static BasicTrade emeraldForItemsTrade(Item item, int count, int maxTrades, int xp) {
		return new BasicTrade(new ItemStack(item, count), new ItemStack(Items.EMERALD), maxTrades, xp, 1);
	}

	@SubscribeEvent
	public static void handleAdditionalFoodEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntityLiving();

		if (Configuration.RABBIT_STEW_JUMP_BOOST.get() && food.equals(Items.RABBIT_STEW)) {
			entity.addEffect(new EffectInstance(Effects.JUMP, 200, 1));
		}

		if (Configuration.COMFORT_FOOD_TAG_EFFECT.get()) {
			Food soupEffects = Foods.VANILLA_SOUP_EFFECTS.get(food);

			if (soupEffects != null) {
				for (Pair<EffectInstance, Float> pair : soupEffects.getEffects()) {
					entity.addEffect(pair.getFirst());
				}
				return;
			}

			if (ModTags.COMFORT_FOODS.contains(food)) {
				entity.addEffect(new EffectInstance(ModEffects.COMFORT.get(), Foods.MEDIUM_DURATION, 0));
			}
		}
	}
}
