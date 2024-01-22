package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.*;

import java.util.Map;

@SuppressWarnings("unused")
public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersDelight.MODID);

	// Helper methods
	public static Item.Properties basicItem() {
		return new Item.Properties().tab(FarmersDelight.CREATIVE_TAB);
	}

	public static Item.Properties foodItem(FoodProperties food) {
		return new Item.Properties().food(food).tab(FarmersDelight.CREATIVE_TAB);
	}

	public static Item.Properties bowlFoodItem(FoodProperties food) {
		return new Item.Properties().food(food).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB);
	}

	public static Item.Properties drinkItem() {
		return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB);
	}

	// Blocks
	public static final RegistryObject<Item> STOVE = ITEMS.register("stove",
			() -> new BlockItem(ModBlocks.STOVE.get(), basicItem()));
	public static final RegistryObject<Item> COOKING_POT = ITEMS.register("cooking_pot",
			() -> new CookingPotItem(ModBlocks.COOKING_POT.get(), basicItem().stacksTo(1)));
	public static final RegistryObject<Item> SKILLET = ITEMS.register("skillet",
			() -> new SkilletItem(ModBlocks.SKILLET.get(), basicItem().stacksTo(1)));
	public static final RegistryObject<Item> CUTTING_BOARD = ITEMS.register("cutting_board",
			() -> new FuelBlockItem(ModBlocks.CUTTING_BOARD.get(), basicItem(), 200));
	public static final RegistryObject<Item> BASKET = ITEMS.register("basket",
			() -> new FuelBlockItem(ModBlocks.BASKET.get(), basicItem(), 300));

	public static final RegistryObject<Item> CARROT_CRATE = ITEMS.register("carrot_crate",
			() -> new BlockItem(ModBlocks.CARROT_CRATE.get(), basicItem()));
	public static final RegistryObject<Item> POTATO_CRATE = ITEMS.register("potato_crate",
			() -> new BlockItem(ModBlocks.POTATO_CRATE.get(), basicItem()));
	public static final RegistryObject<Item> BEETROOT_CRATE = ITEMS.register("beetroot_crate",
			() -> new BlockItem(ModBlocks.BEETROOT_CRATE.get(), basicItem()));
	public static final RegistryObject<Item> CABBAGE_CRATE = ITEMS.register("cabbage_crate",
			() -> new BlockItem(ModBlocks.CABBAGE_CRATE.get(), basicItem()));
	public static final RegistryObject<Item> TOMATO_CRATE = ITEMS.register("tomato_crate",
			() -> new BlockItem(ModBlocks.TOMATO_CRATE.get(), basicItem()));
	public static final RegistryObject<Item> ONION_CRATE = ITEMS.register("onion_crate",
			() -> new BlockItem(ModBlocks.ONION_CRATE.get(), basicItem()));
	public static final RegistryObject<Item> RICE_BALE = ITEMS.register("rice_bale",
			() -> new BlockItem(ModBlocks.RICE_BALE.get(), basicItem()));
	public static final RegistryObject<Item> RICE_BAG = ITEMS.register("rice_bag",
			() -> new BlockItem(ModBlocks.RICE_BAG.get(), basicItem()));
	public static final RegistryObject<Item> STRAW_BALE = ITEMS.register("straw_bale",
			() -> new BlockItem(ModBlocks.STRAW_BALE.get(), basicItem()));

	public static final RegistryObject<Item> SAFETY_NET = ITEMS.register("safety_net",
			() -> new FuelBlockItem(ModBlocks.SAFETY_NET.get(), basicItem(), 200));
	public static final RegistryObject<Item> OAK_CABINET = ITEMS.register("oak_cabinet",
			() -> new FuelBlockItem(ModBlocks.OAK_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> SPRUCE_CABINET = ITEMS.register("spruce_cabinet",
			() -> new FuelBlockItem(ModBlocks.SPRUCE_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> BIRCH_CABINET = ITEMS.register("birch_cabinet",
			() -> new FuelBlockItem(ModBlocks.BIRCH_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> JUNGLE_CABINET = ITEMS.register("jungle_cabinet",
			() -> new FuelBlockItem(ModBlocks.JUNGLE_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> ACACIA_CABINET = ITEMS.register("acacia_cabinet",
			() -> new FuelBlockItem(ModBlocks.ACACIA_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> DARK_OAK_CABINET = ITEMS.register("dark_oak_cabinet",
			() -> new FuelBlockItem(ModBlocks.DARK_OAK_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> MANGROVE_CABINET = ITEMS.register("mangrove_cabinet",
			() -> new FuelBlockItem(ModBlocks.MANGROVE_CABINET.get(), basicItem(), 300));
	public static final RegistryObject<Item> CRIMSON_CABINET = ITEMS.register("crimson_cabinet",
			() -> new BlockItem(ModBlocks.CRIMSON_CABINET.get(), basicItem()));
	public static final RegistryObject<Item> WARPED_CABINET = ITEMS.register("warped_cabinet",
			() -> new BlockItem(ModBlocks.WARPED_CABINET.get(), basicItem()));
	public static final RegistryObject<Item> TATAMI = ITEMS.register("tatami",
			() -> new FuelBlockItem(ModBlocks.TATAMI.get(), basicItem(), 400));
	public static final RegistryObject<Item> FULL_TATAMI_MAT = ITEMS.register("full_tatami_mat",
			() -> new FuelBlockItem(ModBlocks.FULL_TATAMI_MAT.get(), basicItem(), 200));
	public static final RegistryObject<Item> HALF_TATAMI_MAT = ITEMS.register("half_tatami_mat",
			() -> new FuelBlockItem(ModBlocks.HALF_TATAMI_MAT.get(), basicItem()));
	public static final RegistryObject<Item> CANVAS_RUG = ITEMS.register("canvas_rug",
			() -> new FuelBlockItem(ModBlocks.CANVAS_RUG.get(), basicItem(), 200));
	public static final RegistryObject<Item> ORGANIC_COMPOST = ITEMS.register("organic_compost",
			() -> new BlockItem(ModBlocks.ORGANIC_COMPOST.get(), basicItem()));
	public static final RegistryObject<Item> RICH_SOIL = ITEMS.register("rich_soil",
			() -> new BlockItem(ModBlocks.RICH_SOIL.get(), basicItem()));
	public static final RegistryObject<Item> RICH_SOIL_FARMLAND = ITEMS.register("rich_soil_farmland",
			() -> new BlockItem(ModBlocks.RICH_SOIL_FARMLAND.get(), basicItem()));
	public static final RegistryObject<Item> ROPE = ITEMS.register("rope",
			() -> new RopeItem(ModBlocks.ROPE.get(), basicItem()));

	public static final RegistryObject<Item> CANVAS_SIGN = ITEMS.register("canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.CANVAS_SIGN.get(), ModBlocks.CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> WHITE_CANVAS_SIGN = ITEMS.register("white_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> ORANGE_CANVAS_SIGN = ITEMS.register("orange_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.ORANGE_CANVAS_SIGN.get(), ModBlocks.ORANGE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> MAGENTA_CANVAS_SIGN = ITEMS.register("magenta_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.MAGENTA_CANVAS_SIGN.get(), ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIGHT_BLUE_CANVAS_SIGN = ITEMS.register("light_blue_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> YELLOW_CANVAS_SIGN = ITEMS.register("yellow_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.YELLOW_CANVAS_SIGN.get(), ModBlocks.YELLOW_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIME_CANVAS_SIGN = ITEMS.register("lime_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> PINK_CANVAS_SIGN = ITEMS.register("pink_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> GRAY_CANVAS_SIGN = ITEMS.register("gray_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIGHT_GRAY_CANVAS_SIGN = ITEMS.register("light_gray_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> CYAN_CANVAS_SIGN = ITEMS.register("cyan_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> PURPLE_CANVAS_SIGN = ITEMS.register("purple_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.PURPLE_CANVAS_SIGN.get(), ModBlocks.PURPLE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> BLUE_CANVAS_SIGN = ITEMS.register("blue_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> BROWN_CANVAS_SIGN = ITEMS.register("brown_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> GREEN_CANVAS_SIGN = ITEMS.register("green_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> RED_CANVAS_SIGN = ITEMS.register("red_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> BLACK_CANVAS_SIGN = ITEMS.register("black_canvas_sign",
			() -> new SignItem(basicItem(), ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_CANVAS_WALL_SIGN.get()));

	// Tools
	public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife",
			() -> new KnifeItem(ModMaterials.FLINT, 0.5F, -2.0F, basicItem()));
	public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife",
			() -> new KnifeItem(Tiers.IRON, 0.5F, -2.0F, basicItem()));
	public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife",
			() -> new KnifeItem(Tiers.DIAMOND, 0.5F, -2.0F, basicItem()));
	public static final RegistryObject<Item> NETHERITE_KNIFE = ITEMS.register("netherite_knife",
			() -> new KnifeItem(Tiers.NETHERITE, 0.5F, -2.0F, basicItem().fireResistant()));
	public static final RegistryObject<Item> GOLDEN_KNIFE = ITEMS.register("golden_knife",
			() -> new KnifeItem(Tiers.GOLD, 0.5F, -2.0F, basicItem()));

	public static final RegistryObject<Item> STRAW = ITEMS.register("straw", () -> new FuelItem(basicItem()));
	public static final RegistryObject<Item> CANVAS = ITEMS.register("canvas", () -> new FuelItem(basicItem(), 400));
	public static final RegistryObject<Item> TREE_BARK = ITEMS.register("tree_bark", () -> new FuelItem(basicItem(), 200));

	// Wild Crops
	public static final RegistryObject<Item> SANDY_SHRUB = ITEMS.register("sandy_shrub",
			() -> new BlockItem(ModBlocks.SANDY_SHRUB.get(), basicItem()));
	public static final RegistryObject<Item> WILD_CABBAGES = ITEMS.register("wild_cabbages",
			() -> new BlockItem(ModBlocks.WILD_CABBAGES.get(), basicItem()));
	public static final RegistryObject<Item> WILD_ONIONS = ITEMS.register("wild_onions",
			() -> new BlockItem(ModBlocks.WILD_ONIONS.get(), basicItem()));
	public static final RegistryObject<Item> WILD_TOMATOES = ITEMS.register("wild_tomatoes",
			() -> new BlockItem(ModBlocks.WILD_TOMATOES.get(), basicItem()));
	public static final RegistryObject<Item> WILD_CARROTS = ITEMS.register("wild_carrots",
			() -> new BlockItem(ModBlocks.WILD_CARROTS.get(), basicItem()));
	public static final RegistryObject<Item> WILD_POTATOES = ITEMS.register("wild_potatoes",
			() -> new BlockItem(ModBlocks.WILD_POTATOES.get(), basicItem()));
	public static final RegistryObject<Item> WILD_BEETROOTS = ITEMS.register("wild_beetroots",
			() -> new BlockItem(ModBlocks.WILD_BEETROOTS.get(), basicItem()));
	public static final RegistryObject<Item> WILD_RICE = ITEMS.register("wild_rice",
			() -> new DoubleHighBlockItem(ModBlocks.WILD_RICE.get(), basicItem()));

	public static final RegistryObject<Item> BROWN_MUSHROOM_COLONY = ITEMS.register("brown_mushroom_colony",
			() -> new MushroomColonyItem(ModBlocks.BROWN_MUSHROOM_COLONY.get(), basicItem()));
	public static final RegistryObject<Item> RED_MUSHROOM_COLONY = ITEMS.register("red_mushroom_colony",
			() -> new MushroomColonyItem(ModBlocks.RED_MUSHROOM_COLONY.get(), basicItem()));

	// Basic Crops
	public static final RegistryObject<Item> CABBAGE = ITEMS.register("cabbage",
			() -> new Item(foodItem(FoodValues.CABBAGE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
			() -> new Item(foodItem(FoodValues.TOMATO).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> ONION = ITEMS.register("onion",
			() -> new ItemNameBlockItem(ModBlocks.ONION_CROP.get(), foodItem(FoodValues.ONION).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICE_PANICLE = ITEMS.register("rice_panicle", () -> new Item(basicItem()));
	public static final RegistryObject<Item> RICE = ITEMS.register("rice",
			() -> new RiceItem(ModBlocks.RICE_CROP.get(), basicItem()));
	public static final RegistryObject<Item> CABBAGE_SEEDS = ITEMS.register("cabbage_seeds", () -> new ItemNameBlockItem(ModBlocks.CABBAGE_CROP.get(), basicItem()));
	public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new ItemNameBlockItem(ModBlocks.BUDDING_TOMATO_CROP.get(), basicItem())
	{
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
	public static final RegistryObject<Item> ROTTEN_TOMATO = ITEMS.register("rotten_tomato",
			() -> new RottenTomatoItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));

	// Foodstuffs
	public static final RegistryObject<Item> FRIED_EGG = ITEMS.register("fried_egg",
			() -> new Item(foodItem(FoodValues.FRIED_EGG).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle",
			() -> new MilkBottleItem(drinkItem()));
	public static final RegistryObject<Item> HOT_COCOA = ITEMS.register("hot_cocoa",
			() -> new HotCocoaItem(drinkItem()));
	public static final RegistryObject<Item> APPLE_CIDER = ITEMS.register("apple_cider",
			() -> new DrinkableItem(drinkItem().food(FoodValues.APPLE_CIDER), true, false));
	public static final RegistryObject<Item> MELON_JUICE = ITEMS.register("melon_juice",
			() -> new MelonJuiceItem(drinkItem()));
	public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce",
			() -> new ConsumableItem(foodItem(FoodValues.TOMATO_SAUCE).craftRemainder(Items.BOWL)));
	public static final RegistryObject<Item> WHEAT_DOUGH = ITEMS.register("wheat_dough",
			() -> new Item(foodItem(FoodValues.WHEAT_DOUGH)));
	public static final RegistryObject<Item> RAW_PASTA = ITEMS.register("raw_pasta",
			() -> new Item(foodItem(FoodValues.RAW_PASTA)));
	public static final RegistryObject<Item> PUMPKIN_SLICE = ITEMS.register("pumpkin_slice",
			() -> new Item(foodItem(FoodValues.PUMPKIN_SLICE)));
	public static final RegistryObject<Item> CABBAGE_LEAF = ITEMS.register("cabbage_leaf",
			() -> new Item(foodItem(FoodValues.CABBAGE_LEAF)));
	public static final RegistryObject<Item> MINCED_BEEF = ITEMS.register("minced_beef",
			() -> new Item(foodItem(FoodValues.MINCED_BEEF)));
	public static final RegistryObject<Item> BEEF_PATTY = ITEMS.register("beef_patty",
			() -> new Item(foodItem(FoodValues.BEEF_PATTY)));
	public static final RegistryObject<Item> CHICKEN_CUTS = ITEMS.register("chicken_cuts",
			() -> new Item(foodItem(FoodValues.CHICKEN_CUTS)));
	public static final RegistryObject<Item> COOKED_CHICKEN_CUTS = ITEMS.register("cooked_chicken_cuts",
			() -> new Item(foodItem(FoodValues.COOKED_CHICKEN_CUTS)));
	public static final RegistryObject<Item> BACON = ITEMS.register("bacon",
			() -> new Item(foodItem(FoodValues.BACON)));
	public static final RegistryObject<Item> COOKED_BACON = ITEMS.register("cooked_bacon",
			() -> new Item(foodItem(FoodValues.COOKED_BACON)));
	public static final RegistryObject<Item> COD_SLICE = ITEMS.register("cod_slice",
			() -> new Item(foodItem(FoodValues.COD_SLICE)));
	public static final RegistryObject<Item> COOKED_COD_SLICE = ITEMS.register("cooked_cod_slice",
			() -> new Item(foodItem(FoodValues.COOKED_COD_SLICE)));
	public static final RegistryObject<Item> SALMON_SLICE = ITEMS.register("salmon_slice",
			() -> new Item(foodItem(FoodValues.SALMON_SLICE)));
	public static final RegistryObject<Item> COOKED_SALMON_SLICE = ITEMS.register("cooked_salmon_slice",
			() -> new Item(foodItem(FoodValues.COOKED_SALMON_SLICE)));
	public static final RegistryObject<Item> MUTTON_CHOPS = ITEMS.register("mutton_chops",
			() -> new Item(foodItem(FoodValues.MUTTON_CHOP)));
	public static final RegistryObject<Item> COOKED_MUTTON_CHOPS = ITEMS.register("cooked_mutton_chops",
			() -> new Item(foodItem(FoodValues.COOKED_MUTTON_CHOP)));
	public static final RegistryObject<Item> HAM = ITEMS.register("ham",
			() -> new Item(foodItem(FoodValues.HAM)));
	public static final RegistryObject<Item> SMOKED_HAM = ITEMS.register("smoked_ham",
			() -> new Item(foodItem(FoodValues.SMOKED_HAM)));

	// Sweets
	public static final RegistryObject<Item> PIE_CRUST = ITEMS.register("pie_crust",
			() -> new Item(foodItem(FoodValues.PIE_CRUST)));
	public static final RegistryObject<Item> APPLE_PIE = ITEMS.register("apple_pie",
			() -> new BlockItem(ModBlocks.APPLE_PIE.get(), basicItem()));
	public static final RegistryObject<Item> SWEET_BERRY_CHEESECAKE = ITEMS.register("sweet_berry_cheesecake",
			() -> new BlockItem(ModBlocks.SWEET_BERRY_CHEESECAKE.get(), basicItem()));
	public static final RegistryObject<Item> CHOCOLATE_PIE = ITEMS.register("chocolate_pie",
			() -> new BlockItem(ModBlocks.CHOCOLATE_PIE.get(), basicItem()));
	public static final RegistryObject<Item> CAKE_SLICE = ITEMS.register("cake_slice",
			() -> new Item(foodItem(FoodValues.CAKE_SLICE)));
	public static final RegistryObject<Item> APPLE_PIE_SLICE = ITEMS.register("apple_pie_slice",
			() -> new Item(foodItem(FoodValues.PIE_SLICE)));
	public static final RegistryObject<Item> SWEET_BERRY_CHEESECAKE_SLICE = ITEMS.register("sweet_berry_cheesecake_slice",
			() -> new Item(foodItem(FoodValues.PIE_SLICE)));
	public static final RegistryObject<Item> CHOCOLATE_PIE_SLICE = ITEMS.register("chocolate_pie_slice",
			() -> new Item(foodItem(FoodValues.PIE_SLICE)));
	public static final RegistryObject<Item> SWEET_BERRY_COOKIE = ITEMS.register("sweet_berry_cookie",
			() -> new Item(foodItem(FoodValues.COOKIES)));
	public static final RegistryObject<Item> HONEY_COOKIE = ITEMS.register("honey_cookie",
			() -> new Item(foodItem(FoodValues.COOKIES)));
	public static final RegistryObject<Item> MELON_POPSICLE = ITEMS.register("melon_popsicle",
			() -> new PopsicleItem(foodItem(FoodValues.POPSICLE)));
	public static final RegistryObject<Item> GLOW_BERRY_CUSTARD = ITEMS.register("glow_berry_custard",
			() -> new ConsumableItem(foodItem(FoodValues.GLOW_BERRY_CUSTARD).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
	public static final RegistryObject<Item> FRUIT_SALAD = ITEMS.register("fruit_salad",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.FRUIT_SALAD), true));

	// Basic Meals
	public static final RegistryObject<Item> MIXED_SALAD = ITEMS.register("mixed_salad",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.MIXED_SALAD), true));
	public static final RegistryObject<Item> NETHER_SALAD = ITEMS.register("nether_salad",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.NETHER_SALAD)));
	public static final RegistryObject<Item> BARBECUE_STICK = ITEMS.register("barbecue_stick",
			() -> new Item(foodItem(FoodValues.BARBECUE_STICK)));
	public static final RegistryObject<Item> EGG_SANDWICH = ITEMS.register("egg_sandwich",
			() -> new Item(foodItem(FoodValues.EGG_SANDWICH)));
	public static final RegistryObject<Item> CHICKEN_SANDWICH = ITEMS.register("chicken_sandwich",
			() -> new Item(foodItem(FoodValues.CHICKEN_SANDWICH)));
	public static final RegistryObject<Item> HAMBURGER = ITEMS.register("hamburger",
			() -> new Item(foodItem(FoodValues.HAMBURGER)));
	public static final RegistryObject<Item> BACON_SANDWICH = ITEMS.register("bacon_sandwich",
			() -> new Item(foodItem(FoodValues.BACON_SANDWICH)));
	public static final RegistryObject<Item> MUTTON_WRAP = ITEMS.register("mutton_wrap",
			() -> new Item(foodItem(FoodValues.MUTTON_WRAP)));
	public static final RegistryObject<Item> DUMPLINGS = ITEMS.register("dumplings",
			() -> new Item(foodItem(FoodValues.DUMPLINGS)));
	public static final RegistryObject<Item> STUFFED_POTATO = ITEMS.register("stuffed_potato",
			() -> new Item(foodItem(FoodValues.STUFFED_POTATO)));
	public static final RegistryObject<Item> CABBAGE_ROLLS = ITEMS.register("cabbage_rolls",
			() -> new Item(foodItem(FoodValues.CABBAGE_ROLLS)));
	public static final RegistryObject<Item> SALMON_ROLL = ITEMS.register("salmon_roll",
			() -> new Item(foodItem(FoodValues.SALMON_ROLL)));
	public static final RegistryObject<Item> COD_ROLL = ITEMS.register("cod_roll",
			() -> new Item(foodItem(FoodValues.COD_ROLL)));
	public static final RegistryObject<Item> KELP_ROLL = ITEMS.register("kelp_roll",
			() -> new KelpRollItem(foodItem(FoodValues.KELP_ROLL)));
	public static final RegistryObject<Item> KELP_ROLL_SLICE = ITEMS.register("kelp_roll_slice",
			() -> new Item(foodItem(FoodValues.KELP_ROLL_SLICE)));

	// Soups and Stews
	public static final RegistryObject<Item> COOKED_RICE = ITEMS.register("cooked_rice",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.COOKED_RICE), true));
	public static final RegistryObject<Item> BONE_BROTH = ITEMS.register("bone_broth",
			() -> new DrinkableItem(bowlFoodItem(FoodValues.BONE_BROTH), true));
	public static final RegistryObject<Item> BEEF_STEW = ITEMS.register("beef_stew",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.BEEF_STEW), true));
	public static final RegistryObject<Item> CHICKEN_SOUP = ITEMS.register("chicken_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.CHICKEN_SOUP), true));
	public static final RegistryObject<Item> VEGETABLE_SOUP = ITEMS.register("vegetable_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.VEGETABLE_SOUP), true));
	public static final RegistryObject<Item> FISH_STEW = ITEMS.register("fish_stew",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.FISH_STEW), true));
	public static final RegistryObject<Item> FRIED_RICE = ITEMS.register("fried_rice",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.FRIED_RICE), true));
	public static final RegistryObject<Item> PUMPKIN_SOUP = ITEMS.register("pumpkin_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.PUMPKIN_SOUP), true));
	public static final RegistryObject<Item> BAKED_COD_STEW = ITEMS.register("baked_cod_stew",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.BAKED_COD_STEW), true));
	public static final RegistryObject<Item> NOODLE_SOUP = ITEMS.register("noodle_soup",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.NOODLE_SOUP), true));

	// Plated Meals
	public static final RegistryObject<Item> BACON_AND_EGGS = ITEMS.register("bacon_and_eggs",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.BACON_AND_EGGS), true));
	public static final RegistryObject<Item> PASTA_WITH_MEATBALLS = ITEMS.register("pasta_with_meatballs",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.PASTA_WITH_MEATBALLS), true));
	public static final RegistryObject<Item> PASTA_WITH_MUTTON_CHOP = ITEMS.register("pasta_with_mutton_chop",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.PASTA_WITH_MUTTON_CHOP), true));
	public static final RegistryObject<Item> MUSHROOM_RICE = ITEMS.register("mushroom_rice",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.MUSHROOM_RICE), true));
	public static final RegistryObject<Item> ROASTED_MUTTON_CHOPS = ITEMS.register("roasted_mutton_chops",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.ROASTED_MUTTON_CHOPS), true));
	public static final RegistryObject<Item> VEGETABLE_NOODLES = ITEMS.register("vegetable_noodles",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.VEGETABLE_NOODLES), true));
	public static final RegistryObject<Item> STEAK_AND_POTATOES = ITEMS.register("steak_and_potatoes",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.STEAK_AND_POTATOES), true));
	public static final RegistryObject<Item> RATATOUILLE = ITEMS.register("ratatouille",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.RATATOUILLE), true));
	public static final RegistryObject<Item> SQUID_INK_PASTA = ITEMS.register("squid_ink_pasta",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.SQUID_INK_PASTA), true));
	public static final RegistryObject<Item> GRILLED_SALMON = ITEMS.register("grilled_salmon",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.GRILLED_SALMON), true));

	// Feasts
	public static final RegistryObject<Item> ROAST_CHICKEN_BLOCK = ITEMS.register("roast_chicken_block",
			() -> new BlockItem(ModBlocks.ROAST_CHICKEN_BLOCK.get(), basicItem().stacksTo(1)));
	public static final RegistryObject<Item> ROAST_CHICKEN = ITEMS.register("roast_chicken",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.ROAST_CHICKEN), true));

	public static final RegistryObject<Item> STUFFED_PUMPKIN_BLOCK = ITEMS.register("stuffed_pumpkin_block",
			() -> new BlockItem(ModBlocks.STUFFED_PUMPKIN_BLOCK.get(), basicItem().stacksTo(1)));
	public static final RegistryObject<Item> STUFFED_PUMPKIN = ITEMS.register("stuffed_pumpkin",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.STUFFED_PUMPKIN), true));

	public static final RegistryObject<Item> HONEY_GLAZED_HAM_BLOCK = ITEMS.register("honey_glazed_ham_block",
			() -> new BlockItem(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(), basicItem().stacksTo(1)));
	public static final RegistryObject<Item> HONEY_GLAZED_HAM = ITEMS.register("honey_glazed_ham",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.HONEY_GLAZED_HAM), true));

	public static final RegistryObject<Item> SHEPHERDS_PIE_BLOCK = ITEMS.register("shepherds_pie_block",
			() -> new BlockItem(ModBlocks.SHEPHERDS_PIE_BLOCK.get(), basicItem().stacksTo(1)));
	public static final RegistryObject<Item> SHEPHERDS_PIE = ITEMS.register("shepherds_pie",
			() -> new ConsumableItem(bowlFoodItem(FoodValues.SHEPHERDS_PIE), true));

	public static final RegistryObject<Item> RICE_ROLL_MEDLEY_BLOCK = ITEMS.register("rice_roll_medley_block",
			() -> new BlockItem(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(), basicItem().stacksTo(1)));

	// Pet Foods
	public static final RegistryObject<Item> DOG_FOOD = ITEMS.register("dog_food",
			() -> new DogFoodItem(bowlFoodItem(FoodValues.DOG_FOOD)));
	public static final RegistryObject<Item> HORSE_FEED = ITEMS.register("horse_feed",
			() -> new HorseFeedItem(basicItem().stacksTo(16)));
}
