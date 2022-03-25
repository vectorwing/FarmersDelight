package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.*;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.HotCocoaItem;
import vectorwing.farmersdelight.common.item.MelonJuiceItem;
import vectorwing.farmersdelight.common.item.MilkBottleItem;
import vectorwing.farmersdelight.common.FoodValues;

@SuppressWarnings("unused")
public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersDelight.MODID);

	// Blocks
	public static final RegistryObject<Item> STOVE = ITEMS.register("stove",
			() -> new BlockItem(ModBlocks.STOVE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COOKING_POT = ITEMS.register("cooking_pot",
			() -> new BlockItem(ModBlocks.COOKING_POT.get(), new Item.Properties().stacksTo(1).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SKILLET = ITEMS.register("skillet",
			() -> new SkilletItem(ModBlocks.SKILLET.get(), new Item.Properties().stacksTo(1).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CUTTING_BOARD = ITEMS.register("cutting_board",
			() -> new FuelBlockItem(ModBlocks.CUTTING_BOARD.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 200));
	public static final RegistryObject<Item> BASKET = ITEMS.register("basket",
			() -> new FuelBlockItem(ModBlocks.BASKET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));

	public static final RegistryObject<Item> CARROT_CRATE = ITEMS.register("carrot_crate",
			() -> new BlockItem(ModBlocks.CARROT_CRATE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> POTATO_CRATE = ITEMS.register("potato_crate",
			() -> new BlockItem(ModBlocks.POTATO_CRATE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> BEETROOT_CRATE = ITEMS.register("beetroot_crate",
			() -> new BlockItem(ModBlocks.BEETROOT_CRATE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CABBAGE_CRATE = ITEMS.register("cabbage_crate",
			() -> new BlockItem(ModBlocks.CABBAGE_CRATE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> TOMATO_CRATE = ITEMS.register("tomato_crate",
			() -> new BlockItem(ModBlocks.TOMATO_CRATE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> ONION_CRATE = ITEMS.register("onion_crate",
			() -> new BlockItem(ModBlocks.ONION_CRATE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICE_BALE = ITEMS.register("rice_bale",
			() -> new BlockItem(ModBlocks.RICE_BALE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICE_BAG = ITEMS.register("rice_bag",
			() -> new BlockItem(ModBlocks.RICE_BAG.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> STRAW_BALE = ITEMS.register("straw_bale",
			() -> new BlockItem(ModBlocks.STRAW_BALE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

	public static final RegistryObject<Item> SAFETY_NET = ITEMS.register("safety_net",
			() -> new FuelBlockItem(ModBlocks.SAFETY_NET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 200));
	public static final RegistryObject<Item> OAK_CABINET = ITEMS.register("oak_cabinet",
			() -> new FuelBlockItem(ModBlocks.OAK_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));
	public static final RegistryObject<Item> SPRUCE_CABINET = ITEMS.register("spruce_cabinet",
			() -> new FuelBlockItem(ModBlocks.SPRUCE_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));
	public static final RegistryObject<Item> BIRCH_CABINET = ITEMS.register("birch_cabinet",
			() -> new FuelBlockItem(ModBlocks.BIRCH_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));
	public static final RegistryObject<Item> JUNGLE_CABINET = ITEMS.register("jungle_cabinet",
			() -> new FuelBlockItem(ModBlocks.JUNGLE_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));
	public static final RegistryObject<Item> ACACIA_CABINET = ITEMS.register("acacia_cabinet",
			() -> new FuelBlockItem(ModBlocks.ACACIA_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));
	public static final RegistryObject<Item> DARK_OAK_CABINET = ITEMS.register("dark_oak_cabinet",
			() -> new FuelBlockItem(ModBlocks.DARK_OAK_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 300));
	public static final RegistryObject<Item> CRIMSON_CABINET = ITEMS.register("crimson_cabinet",
			() -> new BlockItem(ModBlocks.CRIMSON_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WARPED_CABINET = ITEMS.register("warped_cabinet",
			() -> new BlockItem(ModBlocks.WARPED_CABINET.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> TATAMI = ITEMS.register("tatami",
			() -> new FuelBlockItem(ModBlocks.TATAMI.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 400));
	public static final RegistryObject<Item> FULL_TATAMI_MAT = ITEMS.register("full_tatami_mat",
			() -> new FuelBlockItem(ModBlocks.FULL_TATAMI_MAT.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 200));
	public static final RegistryObject<Item> HALF_TATAMI_MAT = ITEMS.register("half_tatami_mat",
			() -> new FuelBlockItem(ModBlocks.HALF_TATAMI_MAT.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CANVAS_RUG = ITEMS.register("canvas_rug",
			() -> new FuelBlockItem(ModBlocks.CANVAS_RUG.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 200));
	public static final RegistryObject<Item> ORGANIC_COMPOST = ITEMS.register("organic_compost",
			() -> new BlockItem(ModBlocks.ORGANIC_COMPOST.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICH_SOIL = ITEMS.register("rich_soil",
			() -> new BlockItem(ModBlocks.RICH_SOIL.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICH_SOIL_FARMLAND = ITEMS.register("rich_soil_farmland",
			() -> new BlockItem(ModBlocks.RICH_SOIL_FARMLAND.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> ROPE = ITEMS.register("rope",
			() -> new RopeItem(ModBlocks.ROPE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

	public static final RegistryObject<Item> CANVAS_SIGN = ITEMS.register("canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.CANVAS_SIGN.get(), ModBlocks.CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> WHITE_CANVAS_SIGN = ITEMS.register("white_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> ORANGE_CANVAS_SIGN = ITEMS.register("orange_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.ORANGE_CANVAS_SIGN.get(), ModBlocks.ORANGE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> MAGENTA_CANVAS_SIGN = ITEMS.register("magenta_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.MAGENTA_CANVAS_SIGN.get(), ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIGHT_BLUE_CANVAS_SIGN = ITEMS.register("light_blue_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> YELLOW_CANVAS_SIGN = ITEMS.register("yellow_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.YELLOW_CANVAS_SIGN.get(), ModBlocks.YELLOW_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIME_CANVAS_SIGN = ITEMS.register("lime_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> PINK_CANVAS_SIGN = ITEMS.register("pink_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> GRAY_CANVAS_SIGN = ITEMS.register("gray_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> LIGHT_GRAY_CANVAS_SIGN = ITEMS.register("light_gray_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> CYAN_CANVAS_SIGN = ITEMS.register("cyan_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> PURPLE_CANVAS_SIGN = ITEMS.register("purple_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.PURPLE_CANVAS_SIGN.get(), ModBlocks.PURPLE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> BLUE_CANVAS_SIGN = ITEMS.register("blue_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> BROWN_CANVAS_SIGN = ITEMS.register("brown_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> GREEN_CANVAS_SIGN = ITEMS.register("green_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> RED_CANVAS_SIGN = ITEMS.register("red_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_CANVAS_WALL_SIGN.get()));
	public static final RegistryObject<Item> BLACK_CANVAS_SIGN = ITEMS.register("black_canvas_sign",
			() -> new SignItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_CANVAS_WALL_SIGN.get()));

	// Tools
	public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife",
			() -> new KnifeItem(ModMaterials.FLINT, 0.5F, -2.0F, new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife",
			() -> new KnifeItem(Tiers.IRON, 0.5F, -2.0F, new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife",
			() -> new KnifeItem(Tiers.DIAMOND, 0.5F, -2.0F, new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> NETHERITE_KNIFE = ITEMS.register("netherite_knife",
			() -> new KnifeItem(Tiers.NETHERITE, 0.5F, -2.0F, new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).fireResistant()));
	public static final RegistryObject<Item> GOLDEN_KNIFE = ITEMS.register("golden_knife",
			() -> new KnifeItem(Tiers.GOLD, 0.5F, -2.0F, new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

	public static final RegistryObject<Item> STRAW = ITEMS.register("straw", () -> new FuelItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CANVAS = ITEMS.register("canvas", () -> new FuelItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 400));
	public static final RegistryObject<Item> TREE_BARK = ITEMS.register("tree_bark", () -> new FuelItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB), 200));

	// Wild Crops
	public static final RegistryObject<Item> WILD_CABBAGES = ITEMS.register("wild_cabbages",
			() -> new BlockItem(ModBlocks.WILD_CABBAGES.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WILD_ONIONS = ITEMS.register("wild_onions",
			() -> new BlockItem(ModBlocks.WILD_ONIONS.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WILD_TOMATOES = ITEMS.register("wild_tomatoes",
			() -> new BlockItem(ModBlocks.WILD_TOMATOES.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WILD_CARROTS = ITEMS.register("wild_carrots",
			() -> new BlockItem(ModBlocks.WILD_CARROTS.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WILD_POTATOES = ITEMS.register("wild_potatoes",
			() -> new BlockItem(ModBlocks.WILD_POTATOES.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WILD_BEETROOTS = ITEMS.register("wild_beetroots",
			() -> new BlockItem(ModBlocks.WILD_BEETROOTS.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WILD_RICE = ITEMS.register("wild_rice",
			() -> new DoubleHighBlockItem(ModBlocks.WILD_RICE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

	public static final RegistryObject<Item> BROWN_MUSHROOM_COLONY = ITEMS.register("brown_mushroom_colony",
			() -> new MushroomColonyItem(ModBlocks.BROWN_MUSHROOM_COLONY.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RED_MUSHROOM_COLONY = ITEMS.register("red_mushroom_colony",
			() -> new MushroomColonyItem(ModBlocks.RED_MUSHROOM_COLONY.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

	// Basic Crops
	public static final RegistryObject<Item> CABBAGE = ITEMS.register("cabbage",
			() -> new Item(new Item.Properties().food(FoodValues.CABBAGE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
			() -> new Item(new Item.Properties().food(FoodValues.TOMATO).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> ONION = ITEMS.register("onion",
			() -> new ItemNameBlockItem(ModBlocks.ONION_CROP.get(), new Item.Properties().food(FoodValues.ONION).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICE_PANICLE = ITEMS.register("rice_panicle", () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RICE = ITEMS.register("rice",
			() -> new RiceItem(ModBlocks.RICE_CROP.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CABBAGE_SEEDS = ITEMS.register("cabbage_seeds", () -> new ItemNameBlockItem(ModBlocks.CABBAGE_CROP.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new ItemNameBlockItem(ModBlocks.TOMATO_CROP.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

	// Foodstuffs
	public static final RegistryObject<Item> FRIED_EGG = ITEMS.register("fried_egg",
			() -> new Item(new Item.Properties().food(FoodValues.FRIED_EGG).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle",
			() -> new MilkBottleItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> HOT_COCOA = ITEMS.register("hot_cocoa",
			() -> new HotCocoaItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> APPLE_CIDER = ITEMS.register("apple_cider",
			() -> new DrinkableItem(new Item.Properties().food(FoodValues.APPLE_CIDER).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true, false));
	public static final RegistryObject<Item> MELON_JUICE = ITEMS.register("melon_juice",
			() -> new MelonJuiceItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.TOMATO_SAUCE).craftRemainder(Items.BOWL).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> WHEAT_DOUGH = ITEMS.register("wheat_dough",
			() -> new Item(new Item.Properties().food(FoodValues.WHEAT_DOUGH).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> RAW_PASTA = ITEMS.register("raw_pasta",
			() -> new Item(new Item.Properties().food(FoodValues.RAW_PASTA).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> PUMPKIN_SLICE = ITEMS.register("pumpkin_slice",
			() -> new Item(new Item.Properties().food(FoodValues.PUMPKIN_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CABBAGE_LEAF = ITEMS.register("cabbage_leaf",
			() -> new Item(new Item.Properties().food(FoodValues.CABBAGE_LEAF).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> MINCED_BEEF = ITEMS.register("minced_beef",
			() -> new Item(new Item.Properties().food(FoodValues.MINCED_BEEF).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> BEEF_PATTY = ITEMS.register("beef_patty",
			() -> new Item(new Item.Properties().food(FoodValues.BEEF_PATTY).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CHICKEN_CUTS = ITEMS.register("chicken_cuts",
			() -> new Item(new Item.Properties().food(FoodValues.CHICKEN_CUTS).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COOKED_CHICKEN_CUTS = ITEMS.register("cooked_chicken_cuts",
			() -> new Item(new Item.Properties().food(FoodValues.COOKED_CHICKEN_CUTS).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> BACON = ITEMS.register("bacon",
			() -> new Item(new Item.Properties().food(FoodValues.BACON).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COOKED_BACON = ITEMS.register("cooked_bacon",
			() -> new Item(new Item.Properties().food(FoodValues.COOKED_BACON).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COD_SLICE = ITEMS.register("cod_slice",
			() -> new Item(new Item.Properties().food(FoodValues.COD_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COOKED_COD_SLICE = ITEMS.register("cooked_cod_slice",
			() -> new Item(new Item.Properties().food(FoodValues.COOKED_COD_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SALMON_SLICE = ITEMS.register("salmon_slice",
			() -> new Item(new Item.Properties().food(FoodValues.SALMON_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COOKED_SALMON_SLICE = ITEMS.register("cooked_salmon_slice",
			() -> new Item(new Item.Properties().food(FoodValues.COOKED_SALMON_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> MUTTON_CHOPS = ITEMS.register("mutton_chops",
			() -> new Item(new Item.Properties().food(FoodValues.MUTTON_CHOP).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> COOKED_MUTTON_CHOPS = ITEMS.register("cooked_mutton_chops",
			() -> new Item(new Item.Properties().food(FoodValues.COOKED_MUTTON_CHOP).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> HAM = ITEMS.register("ham",
			() -> new Item(new Item.Properties().food(FoodValues.HAM).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SMOKED_HAM = ITEMS.register("smoked_ham",
			() -> new Item(new Item.Properties().food(FoodValues.SMOKED_HAM).tab(FarmersDelight.CREATIVE_TAB)));

	// Sweets
	public static final RegistryObject<Item> PIE_CRUST = ITEMS.register("pie_crust",
			() -> new Item(new Item.Properties().food(FoodValues.PIE_CRUST).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> APPLE_PIE = ITEMS.register("apple_pie",
			() -> new BlockItem(ModBlocks.APPLE_PIE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SWEET_BERRY_CHEESECAKE = ITEMS.register("sweet_berry_cheesecake",
			() -> new BlockItem(ModBlocks.SWEET_BERRY_CHEESECAKE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CHOCOLATE_PIE = ITEMS.register("chocolate_pie",
			() -> new BlockItem(ModBlocks.CHOCOLATE_PIE.get(), new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CAKE_SLICE = ITEMS.register("cake_slice",
			() -> new Item(new Item.Properties().food(FoodValues.CAKE_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> APPLE_PIE_SLICE = ITEMS.register("apple_pie_slice",
			() -> new Item(new Item.Properties().food(FoodValues.PIE_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SWEET_BERRY_CHEESECAKE_SLICE = ITEMS.register("sweet_berry_cheesecake_slice",
			() -> new Item(new Item.Properties().food(FoodValues.PIE_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CHOCOLATE_PIE_SLICE = ITEMS.register("chocolate_pie_slice",
			() -> new Item(new Item.Properties().food(FoodValues.PIE_SLICE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SWEET_BERRY_COOKIE = ITEMS.register("sweet_berry_cookie",
			() -> new Item(new Item.Properties().food(FoodValues.COOKIES).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> HONEY_COOKIE = ITEMS.register("honey_cookie",
			() -> new Item(new Item.Properties().food(FoodValues.COOKIES).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> MELON_POPSICLE = ITEMS.register("melon_popsicle",
			() -> new PopsicleItem(new Item.Properties().food(FoodValues.POPSICLE).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> FRUIT_SALAD = ITEMS.register("fruit_salad",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.FRUIT_SALAD).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));

	// Basic Meals
	public static final RegistryObject<Item> MIXED_SALAD = ITEMS.register("mixed_salad",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.MIXED_SALAD).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> NETHER_SALAD = ITEMS.register("nether_salad",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.NETHER_SALAD).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> BARBECUE_STICK = ITEMS.register("barbecue_stick",
			() -> new Item(new Item.Properties().food(FoodValues.BARBECUE_STICK).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> EGG_SANDWICH = ITEMS.register("egg_sandwich",
			() -> new Item(new Item.Properties().food(FoodValues.EGG_SANDWICH).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CHICKEN_SANDWICH = ITEMS.register("chicken_sandwich",
			() -> new Item(new Item.Properties().food(FoodValues.CHICKEN_SANDWICH).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> HAMBURGER = ITEMS.register("hamburger",
			() -> new Item(new Item.Properties().food(FoodValues.HAMBURGER).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> BACON_SANDWICH = ITEMS.register("bacon_sandwich",
			() -> new Item(new Item.Properties().food(FoodValues.BACON_SANDWICH).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> MUTTON_WRAP = ITEMS.register("mutton_wrap",
			() -> new Item(new Item.Properties().food(FoodValues.MUTTON_WRAP).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> DUMPLINGS = ITEMS.register("dumplings",
			() -> new Item(new Item.Properties().food(FoodValues.DUMPLINGS).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> STUFFED_POTATO = ITEMS.register("stuffed_potato",
			() -> new Item(new Item.Properties().food(FoodValues.STUFFED_POTATO).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> CABBAGE_ROLLS = ITEMS.register("cabbage_rolls",
			() -> new Item(new Item.Properties().food(FoodValues.CABBAGE_ROLLS).tab(FarmersDelight.CREATIVE_TAB)));

	// Soups and Stews
	public static final RegistryObject<Item> COOKED_RICE = ITEMS.register("cooked_rice",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.COOKED_RICE).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> BEEF_STEW = ITEMS.register("beef_stew",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.BEEF_STEW).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> CHICKEN_SOUP = ITEMS.register("chicken_soup",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.CHICKEN_SOUP).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> VEGETABLE_SOUP = ITEMS.register("vegetable_soup",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.VEGETABLE_SOUP).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> FISH_STEW = ITEMS.register("fish_stew",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.FISH_STEW).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> FRIED_RICE = ITEMS.register("fried_rice",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.FRIED_RICE).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> PUMPKIN_SOUP = ITEMS.register("pumpkin_soup",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.PUMPKIN_SOUP).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> BAKED_COD_STEW = ITEMS.register("baked_cod_stew",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.BAKED_COD_STEW).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> NOODLE_SOUP = ITEMS.register("noodle_soup",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.NOODLE_SOUP).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));

	// Plated Meals
	public static final RegistryObject<Item> BACON_AND_EGGS = ITEMS.register("bacon_and_eggs",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.BACON_AND_EGGS).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> PASTA_WITH_MEATBALLS = ITEMS.register("pasta_with_meatballs",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.PASTA_WITH_MEATBALLS).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> PASTA_WITH_MUTTON_CHOP = ITEMS.register("pasta_with_mutton_chop",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.PASTA_WITH_MUTTON_CHOP).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> ROASTED_MUTTON_CHOPS = ITEMS.register("roasted_mutton_chops",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.ROASTED_MUTTON_CHOPS).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> VEGETABLE_NOODLES = ITEMS.register("vegetable_noodles",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.VEGETABLE_NOODLES).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> STEAK_AND_POTATOES = ITEMS.register("steak_and_potatoes",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.STEAK_AND_POTATOES).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> RATATOUILLE = ITEMS.register("ratatouille",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.RATATOUILLE).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> SQUID_INK_PASTA = ITEMS.register("squid_ink_pasta",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.SQUID_INK_PASTA).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> GRILLED_SALMON = ITEMS.register("grilled_salmon",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.GRILLED_SALMON).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));

	// Feasts
	public static final RegistryObject<Item> ROAST_CHICKEN_BLOCK = ITEMS.register("roast_chicken_block",
			() -> new BlockItem(ModBlocks.ROAST_CHICKEN_BLOCK.get(), new Item.Properties().stacksTo(1).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> ROAST_CHICKEN = ITEMS.register("roast_chicken",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.ROAST_CHICKEN).stacksTo(16).craftRemainder(Items.BOWL).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> STUFFED_PUMPKIN_BLOCK = ITEMS.register("stuffed_pumpkin_block",
			() -> new BlockItem(ModBlocks.STUFFED_PUMPKIN_BLOCK.get(), new Item.Properties().stacksTo(1).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> STUFFED_PUMPKIN = ITEMS.register("stuffed_pumpkin",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.STUFFED_PUMPKIN).stacksTo(16).craftRemainder(Items.BOWL).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> HONEY_GLAZED_HAM_BLOCK = ITEMS.register("honey_glazed_ham_block",
			() -> new BlockItem(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(), new Item.Properties().stacksTo(1).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> HONEY_GLAZED_HAM = ITEMS.register("honey_glazed_ham",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.HONEY_GLAZED_HAM).stacksTo(16).craftRemainder(Items.BOWL).tab(FarmersDelight.CREATIVE_TAB), true));
	public static final RegistryObject<Item> SHEPHERDS_PIE_BLOCK = ITEMS.register("shepherds_pie_block",
			() -> new BlockItem(ModBlocks.SHEPHERDS_PIE_BLOCK.get(), new Item.Properties().stacksTo(1).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> SHEPHERDS_PIE = ITEMS.register("shepherds_pie",
			() -> new ConsumableItem(new Item.Properties().food(FoodValues.SHEPHERDS_PIE).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB), true));

	// Pet Foods
	public static final RegistryObject<Item> DOG_FOOD = ITEMS.register("dog_food",
			() -> new DogFoodItem(new Item.Properties().food(FoodValues.DOG_FOOD).craftRemainder(Items.BOWL).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));
	public static final RegistryObject<Item> HORSE_FEED = ITEMS.register("horse_feed",
			() -> new HorseFeedItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB)));
}
