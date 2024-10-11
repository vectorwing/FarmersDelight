package vectorwing.farmersdelight.common.registry;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Sets;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.CookingPotItem;
import vectorwing.farmersdelight.common.item.DogFoodItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.item.FuelItem;
import vectorwing.farmersdelight.common.item.HorseFeedItem;
import vectorwing.farmersdelight.common.item.HotCocoaItem;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.item.MelonJuiceItem;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.item.MushroomColonyItem;
import vectorwing.farmersdelight.common.item.PopsicleItem;
import vectorwing.farmersdelight.common.item.RiceItem;
import vectorwing.farmersdelight.common.item.RopeItem;
import vectorwing.farmersdelight.common.item.RottenTomatoItem;
import vectorwing.farmersdelight.common.item.SkilletItem;

@SuppressWarnings("unused")
public class ModItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FarmersDelight.MODID);
	public static LinkedHashSet<DeferredItem<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

	public static DeferredItem<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
		DeferredItem<Item> block = ITEMS.register(name, supplier);
		CREATIVE_TAB_ITEMS.add(block);
		return block;
	}

	// Helper methods
	public static Item.Properties basicItem() {
		return new Item.Properties();
	}

	public static Item.Properties knifeItem(Tier tier) {
		return new Item.Properties().attributes(KnifeItem.createAttributes(tier, 0.5F, -2.0F));
	}

	public static Item.Properties foodItem(FoodProperties food) {
		return new Item.Properties().food(food);
	}

	public static Item.Properties bowlFoodItem(FoodProperties food) {
		return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16);
	}

	public static Item.Properties drinkItem() {
		return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
	}

	// Blocks
	public static final DeferredItem<Item> STOVE = registerWithTab("stove",
			() -> new BlockItem(ModBlocks.STOVE.get(), basicItem()));
	public static final DeferredItem<Item> COOKING_POT = registerWithTab("cooking_pot",
			() -> new CookingPotItem(ModBlocks.COOKING_POT.get(), basicItem().stacksTo(1)));
	public static final DeferredItem<Item> SKILLET = registerWithTab("skillet", () -> new SkilletItem(
			ModBlocks.SKILLET.get(),
			basicItem().stacksTo(1).attributes(SkilletItem.createAttributes(SkilletItem.SKILLET_TIER, 5.0F, -3.1F))));
	public static final DeferredItem<Item> CUTTING_BOARD = registerWithTab("cutting_board",
			() -> new FuelBlockItem(ModBlocks.CUTTING_BOARD.get(), basicItem(), 200));
	public static final DeferredItem<Item> BASKET = registerWithTab("basket",
			() -> new FuelBlockItem(ModBlocks.BASKET.get(), basicItem(), 300));

	public static final DeferredItem<Item> CARROT_CRATE = registerWithTab("carrot_crate",
			() -> new BlockItem(ModBlocks.CARROT_CRATE.get(), basicItem()));
	public static final DeferredItem<Item> POTATO_CRATE = registerWithTab("potato_crate",
			() -> new BlockItem(ModBlocks.POTATO_CRATE.get(), basicItem()));
	public static final DeferredItem<Item> BEETROOT_CRATE = registerWithTab("beetroot_crate",
			() -> new BlockItem(ModBlocks.BEETROOT_CRATE.get(), basicItem()));
	public static final DeferredItem<Item> CABBAGE_CRATE = registerWithTab("cabbage_crate",
			() -> new BlockItem(ModBlocks.CABBAGE_CRATE.get(), basicItem()));
	public static final DeferredItem<Item> TOMATO_CRATE = registerWithTab("tomato_crate",
			() -> new BlockItem(ModBlocks.TOMATO_CRATE.get(), basicItem()));
	public static final DeferredItem<Item> ONION_CRATE = registerWithTab("onion_crate",
			() -> new BlockItem(ModBlocks.ONION_CRATE.get(), basicItem()));
	public static final DeferredItem<Item> RICE_BALE = registerWithTab("rice_bale",
			() -> new BlockItem(ModBlocks.RICE_BALE.get(), basicItem()));
	public static final DeferredItem<Item> RICE_BAG = registerWithTab("rice_bag",
			() -> new BlockItem(ModBlocks.RICE_BAG.get(), basicItem()));
	public static final DeferredItem<Item> STRAW_BALE = registerWithTab("straw_bale",
			() -> new BlockItem(ModBlocks.STRAW_BALE.get(), basicItem()));

	public static final DeferredItem<Item> SAFETY_NET = registerWithTab("safety_net",
			() -> new FuelBlockItem(ModBlocks.SAFETY_NET.get(), basicItem(), 200));
	public static final DeferredItem<Item> OAK_CABINET = registerWithTab("oak_cabinet",
			() -> new FuelBlockItem(ModBlocks.OAK_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> SPRUCE_CABINET = registerWithTab("spruce_cabinet",
			() -> new FuelBlockItem(ModBlocks.SPRUCE_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> BIRCH_CABINET = registerWithTab("birch_cabinet",
			() -> new FuelBlockItem(ModBlocks.BIRCH_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> JUNGLE_CABINET = registerWithTab("jungle_cabinet",
			() -> new FuelBlockItem(ModBlocks.JUNGLE_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> ACACIA_CABINET = registerWithTab("acacia_cabinet",
			() -> new FuelBlockItem(ModBlocks.ACACIA_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> DARK_OAK_CABINET = registerWithTab("dark_oak_cabinet",
			() -> new FuelBlockItem(ModBlocks.DARK_OAK_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> MANGROVE_CABINET = registerWithTab("mangrove_cabinet",
			() -> new FuelBlockItem(ModBlocks.MANGROVE_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> CHERRY_CABINET = registerWithTab("cherry_cabinet",
			() -> new FuelBlockItem(ModBlocks.CHERRY_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> BAMBOO_CABINET = registerWithTab("bamboo_cabinet",
			() -> new FuelBlockItem(ModBlocks.BAMBOO_CABINET.get(), basicItem(), 300));
	public static final DeferredItem<Item> CRIMSON_CABINET = registerWithTab("crimson_cabinet",
			() -> new BlockItem(ModBlocks.CRIMSON_CABINET.get(), basicItem()));
	public static final DeferredItem<Item> WARPED_CABINET = registerWithTab("warped_cabinet",
			() -> new BlockItem(ModBlocks.WARPED_CABINET.get(), basicItem()));
	public static final DeferredItem<Item> TATAMI = registerWithTab("tatami",
			() -> new FuelBlockItem(ModBlocks.TATAMI.get(), basicItem(), 400));
	public static final DeferredItem<Item> FULL_TATAMI_MAT = registerWithTab("full_tatami_mat",
			() -> new FuelBlockItem(ModBlocks.FULL_TATAMI_MAT.get(), basicItem(), 200));
	public static final DeferredItem<Item> HALF_TATAMI_MAT = registerWithTab("half_tatami_mat",
			() -> new FuelBlockItem(ModBlocks.HALF_TATAMI_MAT.get(), basicItem()));
	public static final DeferredItem<Item> CANVAS_RUG = registerWithTab("canvas_rug",
			() -> new FuelBlockItem(ModBlocks.CANVAS_RUG.get(), basicItem(), 200));
	public static final DeferredItem<Item> ORGANIC_COMPOST = registerWithTab("organic_compost",
			() -> new BlockItem(ModBlocks.ORGANIC_COMPOST.get(), basicItem()));
	public static final DeferredItem<Item> RICH_SOIL = registerWithTab("rich_soil",
			() -> new BlockItem(ModBlocks.RICH_SOIL.get(), basicItem()));
	public static final DeferredItem<Item> RICH_SOIL_FARMLAND = registerWithTab("rich_soil_farmland",
			() -> new BlockItem(ModBlocks.RICH_SOIL_FARMLAND.get(), basicItem()));
	public static final DeferredItem<Item> ROPE = registerWithTab("rope",
			() -> new RopeItem(ModBlocks.ROPE.get(), basicItem()));

	// Canvas Signs...
	public static final DeferredItem<Item> CANVAS_SIGN = registerWithTab("canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.CANVAS_SIGN.get(), ModBlocks.CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> HANGING_CANVAS_SIGN = registerWithTab("hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.HANGING_CANVAS_SIGN.get(), ModBlocks.HANGING_CANVAS_WALL_SIGN.get(),
					basicItem()));

	public static final DeferredItem<Item> WHITE_CANVAS_SIGN = registerWithTab("white_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> WHITE_HANGING_CANVAS_SIGN = registerWithTab("white_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(),
					ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> LIGHT_GRAY_CANVAS_SIGN = registerWithTab("light_gray_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(),
					ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> LIGHT_GRAY_HANGING_CANVAS_SIGN = registerWithTab(
			"light_gray_hanging_canvas_sign", () -> new HangingSignItem(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
					ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> GRAY_CANVAS_SIGN = registerWithTab("gray_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> GRAY_HANGING_CANVAS_SIGN = registerWithTab("gray_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(),
					ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> BLACK_CANVAS_SIGN = registerWithTab("black_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> BLACK_HANGING_CANVAS_SIGN = registerWithTab("black_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(),
					ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> BROWN_CANVAS_SIGN = registerWithTab("brown_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> BROWN_HANGING_CANVAS_SIGN = registerWithTab("brown_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(),
					ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> RED_CANVAS_SIGN = registerWithTab("red_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> RED_HANGING_CANVAS_SIGN = registerWithTab("red_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.RED_HANGING_CANVAS_SIGN.get(),
					ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> ORANGE_CANVAS_SIGN = registerWithTab("orange_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.ORANGE_CANVAS_SIGN.get(),
					ModBlocks.ORANGE_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> ORANGE_HANGING_CANVAS_SIGN = registerWithTab("orange_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(),
					ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> YELLOW_CANVAS_SIGN = registerWithTab("yellow_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.YELLOW_CANVAS_SIGN.get(),
					ModBlocks.YELLOW_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> YELLOW_HANGING_CANVAS_SIGN = registerWithTab("yellow_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(),
					ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> LIME_CANVAS_SIGN = registerWithTab("lime_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> LIME_HANGING_CANVAS_SIGN = registerWithTab("lime_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.LIME_HANGING_CANVAS_SIGN.get(),
					ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> GREEN_CANVAS_SIGN = registerWithTab("green_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> GREEN_HANGING_CANVAS_SIGN = registerWithTab("green_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(),
					ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> CYAN_CANVAS_SIGN = registerWithTab("cyan_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> CYAN_HANGING_CANVAS_SIGN = registerWithTab("cyan_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(),
					ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> LIGHT_BLUE_CANVAS_SIGN = registerWithTab("light_blue_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(),
					ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> LIGHT_BLUE_HANGING_CANVAS_SIGN = registerWithTab(
			"light_blue_hanging_canvas_sign", () -> new HangingSignItem(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
					ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> BLUE_CANVAS_SIGN = registerWithTab("blue_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> BLUE_HANGING_CANVAS_SIGN = registerWithTab("blue_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(),
					ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> PURPLE_CANVAS_SIGN = registerWithTab("purple_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.PURPLE_CANVAS_SIGN.get(),
					ModBlocks.PURPLE_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> PURPLE_HANGING_CANVAS_SIGN = registerWithTab("purple_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(),
					ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> MAGENTA_CANVAS_SIGN = registerWithTab("magenta_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.MAGENTA_CANVAS_SIGN.get(),
					ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> MAGENTA_HANGING_CANVAS_SIGN = registerWithTab("magenta_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(),
					ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	public static final DeferredItem<Item> PINK_CANVAS_SIGN = registerWithTab("pink_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_CANVAS_WALL_SIGN.get()));
	public static final DeferredItem<Item> PINK_HANGING_CANVAS_SIGN = registerWithTab("pink_hanging_canvas_sign",
			() -> new HangingSignItem(ModBlocks.PINK_HANGING_CANVAS_SIGN.get(),
					ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(), basicItem()));

	// Tools
	public static final DeferredItem<Item> FLINT_KNIFE = registerWithTab("flint_knife",
			() -> new KnifeItem(ModMaterials.FLINT, knifeItem(ModMaterials.FLINT)));
	public static final DeferredItem<Item> IRON_KNIFE = registerWithTab("iron_knife",
			() -> new KnifeItem(Tiers.IRON, knifeItem(Tiers.IRON)));
	public static final DeferredItem<Item> DIAMOND_KNIFE = registerWithTab("diamond_knife",
			() -> new KnifeItem(Tiers.DIAMOND, knifeItem(Tiers.DIAMOND)));
	public static final DeferredItem<Item> NETHERITE_KNIFE = registerWithTab("netherite_knife",
			() -> new KnifeItem(Tiers.NETHERITE, knifeItem(Tiers.NETHERITE).fireResistant()));
	public static final DeferredItem<Item> GOLDEN_KNIFE = registerWithTab("golden_knife",
			() -> new KnifeItem(Tiers.GOLD, knifeItem(Tiers.GOLD)));

	public static final DeferredItem<Item> STRAW = registerWithTab("straw", () -> new FuelItem(basicItem()));
	public static final DeferredItem<Item> CANVAS = registerWithTab("canvas", () -> new FuelItem(basicItem(), 400));
	public static final DeferredItem<Item> TREE_BARK = registerWithTab("tree_bark",
			() -> new FuelItem(basicItem(), 200));

	// Wild Crops
	public static final DeferredItem<Item> SANDY_SHRUB = registerWithTab("sandy_shrub",
			() -> new BlockItem(ModBlocks.SANDY_SHRUB.get(), basicItem()));
	public static final DeferredItem<Item> WILD_CABBAGES = registerWithTab("wild_cabbages",
			() -> new BlockItem(ModBlocks.WILD_CABBAGES.get(), basicItem()));
	public static final DeferredItem<Item> WILD_ONIONS = registerWithTab("wild_onions",
			() -> new BlockItem(ModBlocks.WILD_ONIONS.get(), basicItem()));
	public static final DeferredItem<Item> WILD_TOMATOES = registerWithTab("wild_tomatoes",
			() -> new BlockItem(ModBlocks.WILD_TOMATOES.get(), basicItem()));
	public static final DeferredItem<Item> WILD_CARROTS = registerWithTab("wild_carrots",
			() -> new BlockItem(ModBlocks.WILD_CARROTS.get(), basicItem()));
	public static final DeferredItem<Item> WILD_POTATOES = registerWithTab("wild_potatoes",
			() -> new BlockItem(ModBlocks.WILD_POTATOES.get(), basicItem()));
	public static final DeferredItem<Item> WILD_BEETROOTS = registerWithTab("wild_beetroots",
			() -> new BlockItem(ModBlocks.WILD_BEETROOTS.get(), basicItem()));
	public static final DeferredItem<Item> WILD_RICE = registerWithTab("wild_rice",
			() -> new DoubleHighBlockItem(ModBlocks.WILD_RICE.get(), basicItem()));

	public static final DeferredItem<Item> BROWN_MUSHROOM_COLONY = registerWithTab("brown_mushroom_colony",
			() -> new MushroomColonyItem(ModBlocks.BROWN_MUSHROOM_COLONY.get(), basicItem()));
	public static final DeferredItem<Item> RED_MUSHROOM_COLONY = registerWithTab("red_mushroom_colony",
			() -> new MushroomColonyItem(ModBlocks.RED_MUSHROOM_COLONY.get(), basicItem()));

	// Basic Crops
	public static final DeferredItem<Item> CABBAGE = registerWithTab("cabbage",
			() -> new Item(foodItem(FoodValues.CABBAGE)));
	public static final DeferredItem<Item> TOMATO = registerWithTab("tomato",
			() -> new Item(foodItem(FoodValues.TOMATO)));
	public static final DeferredItem<Item> ONION = registerWithTab("onion",
			() -> new ItemNameBlockItem(ModBlocks.ONION_CROP.get(), foodItem(FoodValues.ONION)));
	public static final DeferredItem<Item> RICE_PANICLE = registerWithTab("rice_panicle", () -> new Item(basicItem()));
	public static final DeferredItem<Item> RICE = registerWithTab("rice",
			() -> new RiceItem(ModBlocks.RICE_CROP.get(), basicItem()));
	public static final DeferredItem<Item> CABBAGE_SEEDS = registerWithTab("cabbage_seeds",
			() -> new ItemNameBlockItem(ModBlocks.CABBAGE_CROP.get(), basicItem()));
	public static final DeferredItem<Item> TOMATO_SEEDS = registerWithTab("tomato_seeds",
			() -> new ItemNameBlockItem(ModBlocks.BUDDING_TOMATO_CROP.get(), basicItem()) {
				@Override
				public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
					super.registerBlocks(blockToItemMap, item);
					blockToItemMap.put(ModBlocks.TOMATO_CROP.get(), item);
				}

				@Override
				public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn) {
					super.removeFromBlockToItemMap(blockToItemMap, itemIn);
					blockToItemMap.remove(ModBlocks.TOMATO_CROP.get());
				}
			});
	public static final DeferredItem<Item> ROTTEN_TOMATO = registerWithTab("rotten_tomato",
			() -> new RottenTomatoItem(new Item.Properties().stacksTo(16)));

	// Foodstuffs
	public static final DeferredItem<Item> FRIED_EGG = registerWithTab("fried_egg",
			() -> new Item(foodItem(FoodValues.FRIED_EGG)));
	public static final DeferredItem<Item> MILK_BOTTLE = registerWithTab("milk_bottle",
			() -> new MilkBottleItem(drinkItem()));
	public static final DeferredItem<Item> HOT_COCOA = registerWithTab("hot_cocoa",
			() -> new HotCocoaItem(drinkItem()));
	public static final DeferredItem<Item> APPLE_CIDER = registerWithTab("apple_cider",
			() -> new DrinkableItem(drinkItem().food(FoodValues.APPLE_CIDER), true, false));
	public static final DeferredItem<Item> MELON_JUICE = registerWithTab("melon_juice",
			() -> new MelonJuiceItem(drinkItem()));
	public static final DeferredItem<Item> TOMATO_SAUCE = registerWithTab("tomato_sauce",
			() -> new ConsumableItem(foodItem(FoodValues.TOMATO_SAUCE).craftRemainder(Items.BOWL)));
	public static final DeferredItem<Item> WHEAT_DOUGH = registerWithTab("wheat_dough",
			() -> new Item(foodItem(FoodValues.WHEAT_DOUGH)));
	public static final DeferredItem<Item> RAW_PASTA = registerWithTab("raw_pasta",
			() -> new Item(foodItem(FoodValues.RAW_PASTA)));
	public static final DeferredItem<Item> PUMPKIN_SLICE = registerWithTab("pumpkin_slice",
			() -> new Item(foodItem(FoodValues.PUMPKIN_SLICE)));
	public static final DeferredItem<Item> CABBAGE_LEAF = registerWithTab("cabbage_leaf",
			() -> new Item(foodItem(FoodValues.CABBAGE_LEAF)));
	public static final DeferredItem<Item> MINCED_BEEF = registerWithTab("minced_beef",
			() -> new Item(foodItem(FoodValues.MINCED_BEEF)));
	public static final DeferredItem<Item> BEEF_PATTY = registerWithTab("beef_patty",
			() -> new Item(foodItem(FoodValues.BEEF_PATTY)));
	public static final DeferredItem<Item> CHICKEN_CUTS = registerWithTab("chicken_cuts",
			() -> new Item(foodItem(FoodValues.CHICKEN_CUTS)));
	public static final DeferredItem<Item> COOKED_CHICKEN_CUTS = registerWithTab("cooked_chicken_cuts",
			() -> new Item(foodItem(FoodValues.COOKED_CHICKEN_CUTS)));
	public static final DeferredItem<Item> BACON = registerWithTab("bacon", () -> new Item(foodItem(FoodValues.BACON)));
	public static final DeferredItem<Item> COOKED_BACON = registerWithTab("cooked_bacon",
			() -> new Item(foodItem(FoodValues.COOKED_BACON)));
	public static final DeferredItem<Item> COD_SLICE = registerWithTab("cod_slice",
			() -> new Item(foodItem(FoodValues.COD_SLICE)));
	public static final DeferredItem<Item> COOKED_COD_SLICE = registerWithTab("cooked_cod_slice",
			() -> new Item(foodItem(FoodValues.COOKED_COD_SLICE)));
	public static final DeferredItem<Item> SALMON_SLICE = registerWithTab("salmon_slice",
			() -> new Item(foodItem(FoodValues.SALMON_SLICE)));
	public static final DeferredItem<Item> COOKED_SALMON_SLICE = registerWithTab("cooked_salmon_slice",
			() -> new Item(foodItem(FoodValues.COOKED_SALMON_SLICE)));
	public static final DeferredItem<Item> MUTTON_CHOPS = registerWithTab("mutton_chops",
			() -> new Item(foodItem(FoodValues.MUTTON_CHOPS)));
	public static final DeferredItem<Item> COOKED_MUTTON_CHOPS = registerWithTab("cooked_mutton_chops",
			() -> new Item(foodItem(FoodValues.COOKED_MUTTON_CHOPS)));
	public static final DeferredItem<Item> HAM = registerWithTab("ham", () -> new Item(foodItem(FoodValues.HAM)));
	public static final DeferredItem<Item> SMOKED_HAM = registerWithTab("smoked_ham",
			() -> new Item(foodItem(FoodValues.SMOKED_HAM)));

	// Sweets
	public static final DeferredItem<Item> PIE_CRUST = registerWithTab("pie_crust",
			() -> new Item(foodItem(FoodValues.PIE_CRUST)));
	public static final DeferredItem<Item> APPLE_PIE = registerWithTab("apple_pie",
			() -> new BlockItem(ModBlocks.APPLE_PIE.get(), basicItem()));
	public static final DeferredItem<Item> SWEET_BERRY_CHEESECAKE = registerWithTab("sweet_berry_cheesecake",
			() -> new BlockItem(ModBlocks.SWEET_BERRY_CHEESECAKE.get(), basicItem()));
	public static final DeferredItem<Item> CHOCOLATE_PIE = registerWithTab("chocolate_pie",
			() -> new BlockItem(ModBlocks.CHOCOLATE_PIE.get(), basicItem()));
	public static final DeferredItem<Item> CAKE_SLICE = registerWithTab("cake_slice",
			() -> new Item(foodItem(FoodValues.CAKE_SLICE)));
	public static final DeferredItem<Item> APPLE_PIE_SLICE = registerWithTab("apple_pie_slice",
			() -> new Item(foodItem(FoodValues.PIE_SLICE)));
	public static final DeferredItem<Item> SWEET_BERRY_CHEESECAKE_SLICE = registerWithTab(
			"sweet_berry_cheesecake_slice", () -> new Item(foodItem(FoodValues.PIE_SLICE)));
	public static final DeferredItem<Item> CHOCOLATE_PIE_SLICE = registerWithTab("chocolate_pie_slice",
			() -> new Item(foodItem(FoodValues.PIE_SLICE)));
	public static final DeferredItem<Item> SWEET_BERRY_COOKIE = registerWithTab("sweet_berry_cookie",
			() -> new Item(foodItem(FoodValues.COOKIES)));
	public static final DeferredItem<Item> HONEY_COOKIE = registerWithTab("honey_cookie",
			() -> new Item(foodItem(FoodValues.COOKIES)));
	public static final DeferredItem<Item> MELON_POPSICLE = registerWithTab("melon_popsicle",
			() -> new PopsicleItem(foodItem(FoodValues.POPSICLE)));
	public static final DeferredItem<Item> GLOW_BERRY_CUSTARD = registerWithTab("glow_berry_custard",
			() -> new ConsumableItem(
					foodItem(FoodValues.GLOW_BERRY_CUSTARD).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
	public static final DeferredItem<Item> FRUIT_SALAD = registerWithTab("fruit_salad",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.FRUIT_SALAD), true));

	// Basic Meals
	public static final DeferredItem<Item> MIXED_SALAD = registerWithTab("mixed_salad",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.MIXED_SALAD), true));
	public static final DeferredItem<Item> NETHER_SALAD = registerWithTab("nether_salad",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.NETHER_SALAD)));
	public static final DeferredItem<Item> BARBECUE_STICK = registerWithTab("barbecue_stick",
			() -> new Item(foodItem(FoodValues.BARBECUE_STICK)));
	public static final DeferredItem<Item> EGG_SANDWICH = registerWithTab("egg_sandwich",
			() -> new Item(foodItem(FoodValues.EGG_SANDWICH)));
	public static final DeferredItem<Item> CHICKEN_SANDWICH = registerWithTab("chicken_sandwich",
			() -> new Item(foodItem(FoodValues.CHICKEN_SANDWICH)));
	public static final DeferredItem<Item> HAMBURGER = registerWithTab("hamburger",
			() -> new Item(foodItem(FoodValues.HAMBURGER)));
	public static final DeferredItem<Item> BACON_SANDWICH = registerWithTab("bacon_sandwich",
			() -> new Item(foodItem(FoodValues.BACON_SANDWICH)));
	public static final DeferredItem<Item> MUTTON_WRAP = registerWithTab("mutton_wrap",
			() -> new Item(foodItem(FoodValues.MUTTON_WRAP)));
	public static final DeferredItem<Item> DUMPLINGS = registerWithTab("dumplings",
			() -> new Item(foodItem(FoodValues.DUMPLINGS)));
	public static final DeferredItem<Item> STUFFED_POTATO = registerWithTab("stuffed_potato",
			() -> new Item(foodItem(FoodValues.STUFFED_POTATO)));
	public static final DeferredItem<Item> CABBAGE_ROLLS = registerWithTab("cabbage_rolls",
			() -> new Item(foodItem(FoodValues.CABBAGE_ROLLS)));
	public static final DeferredItem<Item> SALMON_ROLL = registerWithTab("salmon_roll",
			() -> new Item(foodItem(FoodValues.SALMON_ROLL)));
	public static final DeferredItem<Item> COD_ROLL = registerWithTab("cod_roll",
			() -> new Item(foodItem(FoodValues.COD_ROLL)));
	public static final DeferredItem<Item> KELP_ROLL = registerWithTab("kelp_roll",
			() -> new Item(foodItem(FoodValues.KELP_ROLL)));
	public static final DeferredItem<Item> KELP_ROLL_SLICE = registerWithTab("kelp_roll_slice",
			() -> new Item(foodItem(FoodValues.KELP_ROLL_SLICE)));

	// Soups and Stews
	public static final DeferredItem<Item> COOKED_RICE = registerWithTab("cooked_rice",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.COOKED_RICE), true));
	public static final DeferredItem<Item> BONE_BROTH = registerWithTab("bone_broth",
			() -> new DrinkableItem(bowlFoodItem(FoodValues.BONE_BROTH), true));
	public static final DeferredItem<Item> BEEF_STEW = registerWithTab("beef_stew",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.BEEF_STEW), true));
	public static final DeferredItem<Item> CHICKEN_SOUP = registerWithTab("chicken_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.CHICKEN_SOUP), true));
	public static final DeferredItem<Item> VEGETABLE_SOUP = registerWithTab("vegetable_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.VEGETABLE_SOUP), true));
	public static final DeferredItem<Item> FISH_STEW = registerWithTab("fish_stew",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.FISH_STEW), true));
	public static final DeferredItem<Item> FRIED_RICE = registerWithTab("fried_rice",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.FRIED_RICE), true));
	public static final DeferredItem<Item> PUMPKIN_SOUP = registerWithTab("pumpkin_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.PUMPKIN_SOUP), true));
	public static final DeferredItem<Item> BAKED_COD_STEW = registerWithTab("baked_cod_stew",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.BAKED_COD_STEW), true));
	public static final DeferredItem<Item> NOODLE_SOUP = registerWithTab("noodle_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.NOODLE_SOUP), true));

	// Plated Meals
	public static final DeferredItem<Item> BACON_AND_EGGS = registerWithTab("bacon_and_eggs",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.BACON_AND_EGGS), true));
	public static final DeferredItem<Item> PASTA_WITH_MEATBALLS = registerWithTab("pasta_with_meatballs",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.PASTA_WITH_MEATBALLS), true));
	public static final DeferredItem<Item> PASTA_WITH_MUTTON_CHOP = registerWithTab("pasta_with_mutton_chop",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.PASTA_WITH_MUTTON_CHOP), true));
	public static final DeferredItem<Item> MUSHROOM_RICE = registerWithTab("mushroom_rice",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.MUSHROOM_RICE), true));
	public static final DeferredItem<Item> ROASTED_MUTTON_CHOPS = registerWithTab("roasted_mutton_chops",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.ROASTED_MUTTON_CHOPS), true));
	public static final DeferredItem<Item> VEGETABLE_NOODLES = registerWithTab("vegetable_noodles",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.VEGETABLE_NOODLES), true));
	public static final DeferredItem<Item> STEAK_AND_POTATOES = registerWithTab("steak_and_potatoes",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.STEAK_AND_POTATOES), true));
	public static final DeferredItem<Item> RATATOUILLE = registerWithTab("ratatouille",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.RATATOUILLE), true));
	public static final DeferredItem<Item> SQUID_INK_PASTA = registerWithTab("squid_ink_pasta",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.SQUID_INK_PASTA), true));
	public static final DeferredItem<Item> GRILLED_SALMON = registerWithTab("grilled_salmon",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.GRILLED_SALMON), true));

	// Feasts
	public static final DeferredItem<Item> ROAST_CHICKEN_BLOCK = registerWithTab("roast_chicken_block",
			() -> new BlockItem(ModBlocks.ROAST_CHICKEN_BLOCK.get(), basicItem().stacksTo(1)));
	public static final DeferredItem<Item> ROAST_CHICKEN = registerWithTab("roast_chicken",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.ROAST_CHICKEN), true));

	public static final DeferredItem<Item> STUFFED_PUMPKIN_BLOCK = registerWithTab("stuffed_pumpkin_block",
			() -> new BlockItem(ModBlocks.STUFFED_PUMPKIN_BLOCK.get(), basicItem().stacksTo(1)));
	public static final DeferredItem<Item> STUFFED_PUMPKIN = registerWithTab("stuffed_pumpkin",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.STUFFED_PUMPKIN), true));

	public static final DeferredItem<Item> HONEY_GLAZED_HAM_BLOCK = registerWithTab("honey_glazed_ham_block",
			() -> new BlockItem(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(), basicItem().stacksTo(1)));
	public static final DeferredItem<Item> HONEY_GLAZED_HAM = registerWithTab("honey_glazed_ham",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.HONEY_GLAZED_HAM), true));

	public static final DeferredItem<Item> SHEPHERDS_PIE_BLOCK = registerWithTab("shepherds_pie_block",
			() -> new BlockItem(ModBlocks.SHEPHERDS_PIE_BLOCK.get(), basicItem().stacksTo(1)));
	public static final DeferredItem<Item> SHEPHERDS_PIE = registerWithTab("shepherds_pie",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.SHEPHERDS_PIE), true));

	public static final DeferredItem<Item> RICE_ROLL_MEDLEY_BLOCK = registerWithTab("rice_roll_medley_block",
			() -> new BlockItem(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(), basicItem().stacksTo(1)));

	// Pet Foods
	public static final DeferredItem<Item> DOG_FOOD = registerWithTab("dog_food",
			() -> new DogFoodItem(bowlFoodItem(FoodValues.DOG_FOOD)));
	public static final DeferredItem<Item> HORSE_FEED = registerWithTab("horse_feed",
			() -> new HorseFeedItem(basicItem().stacksTo(16)));
}
