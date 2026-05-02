package vectorwing.farmersdelight.common.registry;

import com.google.common.collect.Sets;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.*;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModItems
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FarmersDelight.MODID);

	public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

	public static ResourceKey<Item> key(String path) {
		return ResourceKey.create(Registries.ITEM, FarmersDelight.id(path));
	}

	private static Supplier<Item> registerWithTab(final String name, final Function<Item.Properties, Item> function, final Item.Properties properties) {
		properties.setId(key(name));
		Supplier<Item> item = ITEMS.register(name, () -> function.apply(properties));
		CREATIVE_TAB_ITEMS.add(item);
		return item;
	}

	public static Supplier<Item> registerHidden(final String name, final Function<Item.Properties, Item> function, final Item.Properties properties) {
		properties.setId(key(name));
		return ITEMS.register(name, () -> function.apply(properties));
	}

	private static Supplier<Item> registerFuelWithTab(final String name, final Item.Properties properties, final int burnTime) {
		properties.setId(key(name));
		Supplier<Item> item = ITEMS.register(name, () -> new FuelItem(properties, burnTime));
		CREATIVE_TAB_ITEMS.add(item);
		return item;
	}

	private static Supplier<Item> registerBlockWithTab(final String name, final BiFunction<Block, Item.Properties, Item> function, final Block block, final Item.Properties properties) {
		properties.setId(key(name));
		properties.useBlockDescriptionPrefix();
		Supplier<Item> item = ITEMS.register(name, () -> function.apply(block, properties));
		CREATIVE_TAB_ITEMS.add(item);
		return item;
	}

	private static Supplier<Item> registerItemNameBlockWithTab(final String name, final BiFunction<Block, Item.Properties, Item> function, final Block block, final Item.Properties properties) {
		properties.setId(key(name));
		Supplier<Item> item = ITEMS.register(name, () -> function.apply(block, properties));
		CREATIVE_TAB_ITEMS.add(item);
		return item;
	}

	private static Supplier<Item> registerFuelBlockWithTab(final String name, final Block block, final Item.Properties properties, final int burnTime) {
		properties.setId(key(name));
		properties.useBlockDescriptionPrefix();
		Supplier<Item> item = ITEMS.register(name, () -> new FuelBlockItem(block, properties, burnTime));
		CREATIVE_TAB_ITEMS.add(item);
		return item;
	}

	// Helper methods
	public static Item.Properties basicItem() {
		return new Item.Properties();
	}

	public static Item.Properties knifeItem(ToolMaterial material) {
		HolderGetter<Block> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
		return new Item.Properties()
			.durability(material.durability())
			.repairable(material.repairItems())
			.enchantable(material.enchantmentValue())
			.attributes(KnifeItem.createAttributes(material, 0.5F, -2.0F))
			.component(DataComponents.TOOL, new Tool(
				List.of(
					Tool.Rule.deniesDrops(holderGetter.getOrThrow(material.incorrectBlocksForDrops())),
					Tool.Rule.minesAndDrops(holderGetter.getOrThrow(ModTags.Blocks.MINEABLE_WITH_KNIFE), material.speed())
				), 1.0F, 1, false))
			.component(DataComponents.WEAPON, new Weapon(2));
	}

	public static Item.Properties foodItem(FoodProperties food) {
		return foodItem(food, null);
	}

	public static Item.Properties foodItem(FoodProperties food, @Nullable Consumable consumable) {
		return new Item.Properties().food(food)
			.component(DataComponents.CONSUMABLE, consumable != null ? consumable : Consumables.DEFAULT_FOOD);
	}

	public static Item.Properties bowlFoodItem(FoodProperties food) {
		return bowlFoodItem(food, null);
	}

	public static Item.Properties bowlFoodItem(FoodProperties food, @Nullable Consumable consumable) {
		return new Item.Properties().food(food)
			.component(DataComponents.CONSUMABLE, consumable != null ? consumable : Consumables.DEFAULT_FOOD)
			.craftRemainder(Items.BOWL)
			.stacksTo(16);
	}

	public static Item.Properties drinkItem() {
		return drinkItem(null);
	}

	public static Item.Properties drinkItem(@Nullable Consumable consumable) {
		return new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)
			.component(DataComponents.CONSUMABLE, consumable != null ? consumable : Consumables.DEFAULT_DRINK)
			.stacksTo(16);
	}

	// Blocks
	public static final Supplier<Item> STOVE = registerBlockWithTab("stove",
		BlockItem::new, ModBlocks.STOVE.get(), basicItem());
	public static final Supplier<Item> COOKING_POT = registerBlockWithTab("cooking_pot",
		CookingPotItem::new, ModBlocks.COOKING_POT.get(), basicItem().stacksTo(1));
	public static final Supplier<Item> SKILLET = registerBlockWithTab("skillet",
		SkilletItem::new, ModBlocks.SKILLET.get(), basicItem().stacksTo(1)
			.durability(SkilletItem.SKILLET_MATERIAL.durability())
			.repairable(SkilletItem.SKILLET_MATERIAL.repairItems())
			.enchantable(SkilletItem.SKILLET_MATERIAL.enchantmentValue())
			.attributes(SkilletItem.createAttributes(SkilletItem.SKILLET_MATERIAL, 5.0F, -3.1F))
			.component(DataComponents.TOOL, new Tool(Collections.emptyList(), 1.0F, 1, false))
			.component(DataComponents.WEAPON, new Weapon(1)));
	public static final Supplier<Item> CUTTING_BOARD = registerFuelBlockWithTab("cutting_board",
		ModBlocks.CUTTING_BOARD.get(), basicItem(), 200);
	public static final Supplier<Item> WOODEN_BASKET = registerFuelBlockWithTab("wooden_basket",
		ModBlocks.WOODEN_BASKET.get(), basicItem(), 300);
	public static final Supplier<Item> BAMBOO_BASKET = registerFuelBlockWithTab("bamboo_basket",
		ModBlocks.BAMBOO_BASKET.get(), basicItem(), 300);

	public static final Supplier<Item> CARROT_CRATE = registerBlockWithTab("carrot_crate",
		BlockItem::new, ModBlocks.CARROT_CRATE.get(), basicItem());
	public static final Supplier<Item> POTATO_CRATE = registerBlockWithTab("potato_crate",
		BlockItem::new, ModBlocks.POTATO_CRATE.get(), basicItem());
	public static final Supplier<Item> BEETROOT_CRATE = registerBlockWithTab("beetroot_crate",
		BlockItem::new, ModBlocks.BEETROOT_CRATE.get(), basicItem());
	public static final Supplier<Item> CABBAGE_CRATE = registerBlockWithTab("cabbage_crate",
		BlockItem::new, ModBlocks.CABBAGE_CRATE.get(), basicItem());
	public static final Supplier<Item> TOMATO_CRATE = registerBlockWithTab("tomato_crate",
		BlockItem::new, ModBlocks.TOMATO_CRATE.get(), basicItem());
	public static final Supplier<Item> ONION_CRATE = registerBlockWithTab("onion_crate",
		BlockItem::new, ModBlocks.ONION_CRATE.get(), basicItem());
	public static final Supplier<Item> RICE_BALE = registerBlockWithTab("rice_bale",
		BlockItem::new, ModBlocks.RICE_BALE.get(), basicItem());
	public static final Supplier<Item> RICE_BAG = registerBlockWithTab("rice_bag",
		BlockItem::new, ModBlocks.RICE_BAG.get(), basicItem());
	public static final Supplier<Item> STRAW_BALE = registerBlockWithTab("straw_bale",
		BlockItem::new, ModBlocks.STRAW_BALE.get(), basicItem());

	public static final Supplier<Item> SAFETY_NET = registerFuelBlockWithTab("safety_net",
		ModBlocks.SAFETY_NET.get(), basicItem(), 200);
	public static final Supplier<Item> OAK_CABINET = registerFuelBlockWithTab("oak_cabinet",
		ModBlocks.OAK_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> SPRUCE_CABINET = registerFuelBlockWithTab("spruce_cabinet",
		ModBlocks.SPRUCE_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> BIRCH_CABINET = registerFuelBlockWithTab("birch_cabinet",
		ModBlocks.BIRCH_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> JUNGLE_CABINET = registerFuelBlockWithTab("jungle_cabinet",
		ModBlocks.JUNGLE_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> ACACIA_CABINET = registerFuelBlockWithTab("acacia_cabinet",
		ModBlocks.ACACIA_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> DARK_OAK_CABINET = registerFuelBlockWithTab("dark_oak_cabinet",
		ModBlocks.DARK_OAK_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> MANGROVE_CABINET = registerFuelBlockWithTab("mangrove_cabinet",
		ModBlocks.MANGROVE_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> CHERRY_CABINET = registerFuelBlockWithTab("cherry_cabinet",
		ModBlocks.CHERRY_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> BAMBOO_CABINET = registerFuelBlockWithTab("bamboo_cabinet",
		ModBlocks.BAMBOO_CABINET.get(), basicItem(), 300);
	// TODO: Not yet! Make it boot first!
//	public static final Supplier<Item> PALE_OAK_CABINET = registerFuelBlockWithTab("pale_oak_cabinet",
//		ModBlocks.PALE_OAK_CABINET.get(), basicItem(), 300);
	public static final Supplier<Item> CRIMSON_CABINET = registerBlockWithTab("crimson_cabinet",
		BlockItem::new, ModBlocks.CRIMSON_CABINET.get(), basicItem());
	public static final Supplier<Item> WARPED_CABINET = registerBlockWithTab("warped_cabinet",
		BlockItem::new, ModBlocks.WARPED_CABINET.get(), basicItem());
	public static final Supplier<Item> TATAMI = registerFuelBlockWithTab("tatami",
		ModBlocks.TATAMI.get(), basicItem(), 400);
	public static final Supplier<Item> FULL_TATAMI_MAT = registerFuelBlockWithTab("full_tatami_mat",
		ModBlocks.FULL_TATAMI_MAT.get(), basicItem(), 200);
	public static final Supplier<Item> HALF_TATAMI_MAT = registerFuelBlockWithTab("half_tatami_mat",
		ModBlocks.HALF_TATAMI_MAT.get(), basicItem(), 100);
	public static final Supplier<Item> CANVAS_RUG = registerFuelBlockWithTab("canvas_rug",
		ModBlocks.CANVAS_RUG.get(), basicItem(), 200);
	public static final Supplier<Item> ROPE_FENCE = registerBlockWithTab("rope_fence",
		BlockItem::new, ModBlocks.ROPE_FENCE.get(), basicItem());
	public static final Supplier<Item> ROPE_FENCE_GATE = registerBlockWithTab("rope_fence_gate",
		BlockItem::new, ModBlocks.ROPE_FENCE_GATE.get(), basicItem());
	public static final Supplier<Item> ORGANIC_COMPOST = registerBlockWithTab("organic_compost",
		BlockItem::new, ModBlocks.ORGANIC_COMPOST.get(), basicItem());
	public static final Supplier<Item> RICH_SOIL = registerBlockWithTab("rich_soil",
		BlockItem::new, ModBlocks.RICH_SOIL.get(), basicItem());
	public static final Supplier<Item> RICH_SOIL_FARMLAND = registerBlockWithTab("rich_soil_farmland",
		BlockItem::new, ModBlocks.RICH_SOIL_FARMLAND.get(), basicItem());
	public static final Supplier<Item> ROPE = registerBlockWithTab("rope",
		RopeItem::new, ModBlocks.ROPE.get(), basicItem());

	// Canvas Signs...
	public static final Supplier<Item> CANVAS_SIGN = registerWithTab("canvas_sign",
		(properties) -> new SignItem(ModBlocks.CANVAS_SIGN.get(), ModBlocks.CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> HANGING_CANVAS_SIGN = registerWithTab("hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.HANGING_CANVAS_SIGN.get(), ModBlocks.HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> WHITE_CANVAS_SIGN = registerWithTab("white_canvas_sign",
		(properties) -> new SignItem(ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> WHITE_HANGING_CANVAS_SIGN = registerWithTab("white_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(), ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> LIGHT_GRAY_CANVAS_SIGN = registerWithTab("light_gray_canvas_sign",
		(properties) -> new SignItem(ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> LIGHT_GRAY_HANGING_CANVAS_SIGN = registerWithTab("light_gray_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> GRAY_CANVAS_SIGN = registerWithTab("gray_canvas_sign",
		(properties) -> new SignItem(ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> GRAY_HANGING_CANVAS_SIGN = registerWithTab("gray_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(), ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> BLACK_CANVAS_SIGN = registerWithTab("black_canvas_sign",
		(properties) -> new SignItem(ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> BLACK_HANGING_CANVAS_SIGN = registerWithTab("black_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(), ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> BROWN_CANVAS_SIGN = registerWithTab("brown_canvas_sign",
		(properties) -> new SignItem(ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> BROWN_HANGING_CANVAS_SIGN = registerWithTab("brown_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(), ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> RED_CANVAS_SIGN = registerWithTab("red_canvas_sign",
		(properties) -> new SignItem(ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> RED_HANGING_CANVAS_SIGN = registerWithTab("red_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.RED_HANGING_CANVAS_SIGN.get(), ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> ORANGE_CANVAS_SIGN = registerWithTab("orange_canvas_sign",
		(properties) -> new SignItem(ModBlocks.ORANGE_CANVAS_SIGN.get(), ModBlocks.ORANGE_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> ORANGE_HANGING_CANVAS_SIGN = registerWithTab("orange_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(), ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> YELLOW_CANVAS_SIGN = registerWithTab("yellow_canvas_sign",
		(properties) -> new SignItem(ModBlocks.YELLOW_CANVAS_SIGN.get(), ModBlocks.YELLOW_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> YELLOW_HANGING_CANVAS_SIGN = registerWithTab("yellow_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(), ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> LIME_CANVAS_SIGN = registerWithTab("lime_canvas_sign",
		(properties) -> new SignItem(ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> LIME_HANGING_CANVAS_SIGN = registerWithTab("lime_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.LIME_HANGING_CANVAS_SIGN.get(), ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> GREEN_CANVAS_SIGN = registerWithTab("green_canvas_sign",
		(properties) -> new SignItem(ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> GREEN_HANGING_CANVAS_SIGN = registerWithTab("green_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(), ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> CYAN_CANVAS_SIGN = registerWithTab("cyan_canvas_sign",
		(properties) -> new SignItem(ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> CYAN_HANGING_CANVAS_SIGN = registerWithTab("cyan_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(), ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> LIGHT_BLUE_CANVAS_SIGN = registerWithTab("light_blue_canvas_sign",
		(properties) -> new SignItem(ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> LIGHT_BLUE_HANGING_CANVAS_SIGN = registerWithTab("light_blue_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> BLUE_CANVAS_SIGN = registerWithTab("blue_canvas_sign",
		(properties) -> new SignItem(ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> BLUE_HANGING_CANVAS_SIGN = registerWithTab("blue_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(), ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> PURPLE_CANVAS_SIGN = registerWithTab("purple_canvas_sign",
		(properties) -> new SignItem(ModBlocks.PURPLE_CANVAS_SIGN.get(), ModBlocks.PURPLE_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> PURPLE_HANGING_CANVAS_SIGN = registerWithTab("purple_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(), ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> MAGENTA_CANVAS_SIGN = registerWithTab("magenta_canvas_sign",
		(properties) -> new SignItem(ModBlocks.MAGENTA_CANVAS_SIGN.get(), ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> MAGENTA_HANGING_CANVAS_SIGN = registerWithTab("magenta_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(), ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	public static final Supplier<Item> PINK_CANVAS_SIGN = registerWithTab("pink_canvas_sign",
		(properties) -> new SignItem(ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());
	public static final Supplier<Item> PINK_HANGING_CANVAS_SIGN = registerWithTab("pink_hanging_canvas_sign",
		(properties) -> new HangingSignItem(ModBlocks.PINK_HANGING_CANVAS_SIGN.get(), ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(), properties), basicItem().useBlockDescriptionPrefix());

	// Tools
	public static final Supplier<Item> FLINT_KNIFE = registerWithTab("flint_knife",
		KnifeItem::new, knifeItem(ModMaterials.FLINT));
	public static final Supplier<Item> COPPER_KNIFE = registerWithTab("copper_knife",
		KnifeItem::new, knifeItem(ToolMaterial.COPPER));
	public static final Supplier<Item> IRON_KNIFE = registerWithTab("iron_knife",
		KnifeItem::new, knifeItem(ToolMaterial.IRON));
	public static final Supplier<Item> DIAMOND_KNIFE = registerWithTab("diamond_knife",
		KnifeItem::new, knifeItem(ToolMaterial.DIAMOND));
	public static final Supplier<Item> NETHERITE_KNIFE = registerWithTab("netherite_knife",
		KnifeItem::new, knifeItem(ToolMaterial.NETHERITE).fireResistant());
	public static final Supplier<Item> GOLDEN_KNIFE = registerWithTab("golden_knife",
		KnifeItem::new, knifeItem(ToolMaterial.GOLD));

	public static final Supplier<Item> STRAW = registerFuelWithTab("straw", basicItem(), 100);
	public static final Supplier<Item> CANVAS = registerFuelWithTab("canvas", basicItem(), 400);
	public static final Supplier<Item> TREE_BARK = registerFuelWithTab("tree_bark", basicItem(), 200);

	// Wild Crops
	public static final Supplier<Item> SANDY_SHRUB = registerBlockWithTab("sandy_shrub",
		BlockItem::new, ModBlocks.SANDY_SHRUB.get(), basicItem());
	public static final Supplier<Item> WILD_CABBAGES = registerBlockWithTab("wild_cabbages",
		BlockItem::new, ModBlocks.WILD_CABBAGES.get(), basicItem());
	public static final Supplier<Item> WILD_ONIONS = registerBlockWithTab("wild_onions",
		BlockItem::new, ModBlocks.WILD_ONIONS.get(), basicItem());
	public static final Supplier<Item> WILD_TOMATOES = registerBlockWithTab("wild_tomatoes",
		BlockItem::new, ModBlocks.WILD_TOMATOES.get(), basicItem());
	public static final Supplier<Item> WILD_CARROTS = registerBlockWithTab("wild_carrots",
		BlockItem::new, ModBlocks.WILD_CARROTS.get(), basicItem());
	public static final Supplier<Item> WILD_POTATOES = registerBlockWithTab("wild_potatoes",
		BlockItem::new, ModBlocks.WILD_POTATOES.get(), basicItem());
	public static final Supplier<Item> WILD_BEETROOTS = registerBlockWithTab("wild_beetroots",
		BlockItem::new, ModBlocks.WILD_BEETROOTS.get(), basicItem());
	public static final Supplier<Item> WILD_RICE = registerBlockWithTab("wild_rice",
		DoubleHighBlockItem::new, ModBlocks.WILD_RICE.get(), basicItem());

	public static final Supplier<Item> BROWN_MUSHROOM_COLONY = registerBlockWithTab("brown_mushroom_colony",
		MushroomColonyItem::new, ModBlocks.BROWN_MUSHROOM_COLONY.get(), basicItem());
	public static final Supplier<Item> RED_MUSHROOM_COLONY = registerBlockWithTab("red_mushroom_colony",
		MushroomColonyItem::new, ModBlocks.RED_MUSHROOM_COLONY.get(), basicItem());

	// Basic Crops
	public static final Supplier<Item> CABBAGE = registerWithTab("cabbage",
		Item::new, foodItem(FoodValues.CABBAGE));
	public static final Supplier<Item> TOMATO = registerWithTab("tomato",
		Item::new, foodItem(FoodValues.TOMATO));
	public static final Supplier<Item> ONION = registerItemNameBlockWithTab("onion",
		BlockItem::new, ModBlocks.ONION_CROP.get(), foodItem(FoodValues.ONION));
	public static final Supplier<Item> RICE_PANICLE = registerWithTab("rice_panicle",
		Item::new, basicItem());
	public static final Supplier<Item> RICE = registerItemNameBlockWithTab("rice",
		RiceItem::new, ModBlocks.RICE_CROP.get(), basicItem());
	public static final Supplier<Item> CABBAGE_SEEDS = registerItemNameBlockWithTab("cabbage_seeds",
		BlockItem::new, ModBlocks.CABBAGE_CROP.get(), basicItem());
	public static final Supplier<Item> TOMATO_SEEDS = registerItemNameBlockWithTab("tomato_seeds",
		(block, properties) -> new BlockItem(block, properties)
		{
			@Override
			public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
				super.registerBlocks(blockToItemMap, item);
				blockToItemMap.put(ModBlocks.TOMATO_CROP.get(), item);
			}
		}, ModBlocks.BUDDING_TOMATO_CROP.get(), basicItem());
	public static final Supplier<Item> ROTTEN_TOMATO = registerWithTab("rotten_tomato",
		RottenTomatoItem::new, basicItem().stacksTo(16));

	// Foodstuffs
	public static final Supplier<Item> FRIED_EGG = registerWithTab("fried_egg",
		Item::new, foodItem(FoodValues.FRIED_EGG));
	public static final Supplier<Item> MILK_BOTTLE = registerWithTab("milk_bottle",
		properties -> new ConsumableItem(properties, false, true), drinkItem(FoodValues.ConsumableValues.MILK_BOTTLE));
	public static final Supplier<Item> HOT_COCOA = registerWithTab("hot_cocoa",
		properties -> new ConsumableItem(properties, false, true), drinkItem(FoodValues.ConsumableValues.HOT_COCOA));
	public static final Supplier<Item> APPLE_CIDER = registerWithTab("apple_cider",
		properties -> new ConsumableItem(properties, true, false),
		drinkItem().food(FoodValues.APPLE_CIDER, FoodValues.ConsumableValues.APPLE_CIDER));
	public static final Supplier<Item> MELON_JUICE = registerWithTab("melon_juice",
		properties -> new ConsumableItem(properties, false, true), drinkItem(FoodValues.ConsumableValues.MELON_JUICE));
	public static final Supplier<Item> TOMATO_SAUCE = registerWithTab("tomato_sauce",
		ConsumableItem::new, foodItem(FoodValues.TOMATO_SAUCE).craftRemainder(Items.BOWL));
	public static final Supplier<Item> WHEAT_DOUGH = registerWithTab("wheat_dough",
		Item::new, foodItem(FoodValues.WHEAT_DOUGH, FoodValues.ConsumableValues.HUNGER_INDUCING_FOOD));
	public static final Supplier<Item> RAW_PASTA = registerWithTab("raw_pasta",
		Item::new, foodItem(FoodValues.RAW_PASTA, FoodValues.ConsumableValues.HUNGER_INDUCING_FOOD));
	public static final Supplier<Item> PUMPKIN_SLICE = registerWithTab("pumpkin_slice",
		Item::new, foodItem(FoodValues.PUMPKIN_SLICE));
	public static final Supplier<Item> CABBAGE_LEAF = registerWithTab("cabbage_leaf",
		Item::new, foodItem(FoodValues.CABBAGE_LEAF, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> MINCED_BEEF = registerWithTab("minced_beef",
		Item::new, foodItem(FoodValues.MINCED_BEEF, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> BEEF_PATTY = registerWithTab("beef_patty",
		Item::new, foodItem(FoodValues.BEEF_PATTY, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> CHICKEN_CUTS = registerWithTab("chicken_cuts",
		Item::new, foodItem(FoodValues.CHICKEN_CUTS, FoodValues.ConsumableValues.CHICKEN_CUTS));
	public static final Supplier<Item> COOKED_CHICKEN_CUTS = registerWithTab("cooked_chicken_cuts",
		Item::new, foodItem(FoodValues.COOKED_CHICKEN_CUTS, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> BACON = registerWithTab("bacon",
		Item::new, foodItem(FoodValues.BACON, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> COOKED_BACON = registerWithTab("cooked_bacon",
		Item::new, foodItem(FoodValues.COOKED_BACON, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> COD_SLICE = registerWithTab("cod_slice",
		Item::new, foodItem(FoodValues.COD_SLICE, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> COOKED_COD_SLICE = registerWithTab("cooked_cod_slice",
		Item::new, foodItem(FoodValues.COOKED_COD_SLICE, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> SALMON_SLICE = registerWithTab("salmon_slice",
		Item::new, foodItem(FoodValues.SALMON_SLICE, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> COOKED_SALMON_SLICE = registerWithTab("cooked_salmon_slice",
		Item::new, foodItem(FoodValues.COOKED_SALMON_SLICE, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> MUTTON_CHOPS = registerWithTab("mutton_chops",
		Item::new, foodItem(FoodValues.MUTTON_CHOPS, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> COOKED_MUTTON_CHOPS = registerWithTab("cooked_mutton_chops",
		Item::new, foodItem(FoodValues.COOKED_MUTTON_CHOPS, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> HAM = registerWithTab("ham",
		Item::new, foodItem(FoodValues.HAM));
	public static final Supplier<Item> SMOKED_HAM = registerWithTab("smoked_ham",
		Item::new, foodItem(FoodValues.SMOKED_HAM));
	public static final Supplier<Item> PIE_CRUST = registerWithTab("pie_crust",
		Item::new, foodItem(FoodValues.PIE_CRUST));

	// Sweets
	public static final Supplier<Item> APPLE_PIE = registerBlockWithTab("apple_pie",
		PlaceableItem::new, ModBlocks.APPLE_PIE.get(), basicItem());
	public static final Supplier<Item> SWEET_BERRY_CHEESECAKE = registerBlockWithTab("sweet_berry_cheesecake",
		PlaceableItem::new, ModBlocks.SWEET_BERRY_CHEESECAKE.get(), basicItem());
	public static final Supplier<Item> CHOCOLATE_PIE = registerBlockWithTab("chocolate_pie",
		PlaceableItem::new, ModBlocks.CHOCOLATE_PIE.get(), basicItem());
	public static final Supplier<Item> CAKE_SLICE = registerWithTab("cake_slice",
		Item::new, foodItem(FoodValues.CAKE_SLICE, FoodValues.ConsumableValues.CAKE_SLICE));
	public static final Supplier<Item> APPLE_PIE_SLICE = registerWithTab("apple_pie_slice",
		Item::new, foodItem(FoodValues.PIE_SLICE, FoodValues.ConsumableValues.PIE_SLICE));
	public static final Supplier<Item> SWEET_BERRY_CHEESECAKE_SLICE = registerWithTab("sweet_berry_cheesecake_slice",
		Item::new, foodItem(FoodValues.PIE_SLICE, FoodValues.ConsumableValues.PIE_SLICE));
	public static final Supplier<Item> CHOCOLATE_PIE_SLICE = registerWithTab("chocolate_pie_slice",
		Item::new, foodItem(FoodValues.PIE_SLICE, FoodValues.ConsumableValues.PIE_SLICE));
	public static final Supplier<Item> PUMPKIN_PIE_SLICE = registerWithTab("pumpkin_pie_slice",
		Item::new, foodItem(FoodValues.PIE_SLICE, FoodValues.ConsumableValues.PIE_SLICE));
	public static final Supplier<Item> SWEET_BERRY_COOKIE = registerWithTab("sweet_berry_cookie",
		Item::new, foodItem(FoodValues.COOKIES, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> HONEY_COOKIE = registerWithTab("honey_cookie",
		Item::new, foodItem(FoodValues.COOKIES, FoodValues.ConsumableValues.FAST_FOOD));
	public static final Supplier<Item> MELON_POPSICLE = registerWithTab("melon_popsicle",
		ConsumableItem::new, foodItem(FoodValues.POPSICLE, FoodValues.ConsumableValues.MELON_POPSICLE));
	public static final Supplier<Item> GLOW_BERRY_CUSTARD = registerWithTab("glow_berry_custard",
		properties -> new ConsumableItem(properties, true),
		foodItem(FoodValues.GLOW_BERRY_CUSTARD, FoodValues.ConsumableValues.GLOW_BERRY_CUSTARD).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16));
	public static final Supplier<Item> FRUIT_SALAD = registerWithTab("fruit_salad",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.FRUIT_SALAD, FoodValues.ConsumableValues.FRUIT_SALAD));

	// Basic Meals
	public static final Supplier<Item> MIXED_SALAD = registerWithTab("mixed_salad",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.MIXED_SALAD, FoodValues.ConsumableValues.MIXED_SALAD));
	public static final Supplier<Item> NETHER_SALAD = registerWithTab("nether_salad",
		ConsumableItem::new,
		bowlFoodItem(FoodValues.NETHER_SALAD, FoodValues.ConsumableValues.NETHER_SALAD));
	public static final Supplier<Item> BARBECUE_STICK = registerWithTab("barbecue_stick",
		ConsumableItem::new, foodItem(FoodValues.BARBECUE_STICK));
	public static final Supplier<Item> EGG_SANDWICH = registerWithTab("egg_sandwich",
		ConsumableItem::new, foodItem(FoodValues.EGG_SANDWICH));
	public static final Supplier<Item> CHICKEN_SANDWICH = registerWithTab("chicken_sandwich",
		ConsumableItem::new, foodItem(FoodValues.CHICKEN_SANDWICH));
	public static final Supplier<Item> HAMBURGER = registerWithTab("hamburger",
		ConsumableItem::new, foodItem(FoodValues.HAMBURGER));
	public static final Supplier<Item> BACON_SANDWICH = registerWithTab("bacon_sandwich",
		ConsumableItem::new, foodItem(FoodValues.BACON_SANDWICH));
	public static final Supplier<Item> MUTTON_WRAP = registerWithTab("mutton_wrap",
		ConsumableItem::new, foodItem(FoodValues.MUTTON_WRAP));
	public static final Supplier<Item> DUMPLINGS = registerWithTab("dumplings",
		ConsumableItem::new, foodItem(FoodValues.DUMPLINGS));
	public static final Supplier<Item> STUFFED_POTATO = registerWithTab("stuffed_potato",
		ConsumableItem::new, foodItem(FoodValues.STUFFED_POTATO));
	public static final Supplier<Item> CABBAGE_ROLLS = registerWithTab("cabbage_rolls",
		ConsumableItem::new, foodItem(FoodValues.CABBAGE_ROLLS));
	public static final Supplier<Item> SALMON_ROLL = registerWithTab("salmon_roll",
		ConsumableItem::new, foodItem(FoodValues.SALMON_ROLL));
	public static final Supplier<Item> COD_ROLL = registerWithTab("cod_roll",
		ConsumableItem::new, foodItem(FoodValues.COD_ROLL));
	public static final Supplier<Item> KELP_ROLL = registerWithTab("kelp_roll",
		ConsumableItem::new, foodItem(FoodValues.KELP_ROLL, FoodValues.ConsumableValues.KELP_ROLL));
	public static final Supplier<Item> KELP_ROLL_SLICE = registerWithTab("kelp_roll_slice",
		ConsumableItem::new, foodItem(FoodValues.KELP_ROLL_SLICE, FoodValues.ConsumableValues.FAST_FOOD));

	// Soups and Stews
	public static final Supplier<Item> COOKED_RICE = registerWithTab("cooked_rice",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.COOKED_RICE, FoodValues.ConsumableValues.NOURISHMENT_BRIEF_DURATION));
	public static final Supplier<Item> BONE_BROTH = registerWithTab("bone_broth",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.BONE_BROTH, FoodValues.ConsumableValues.BONE_BROTH));
	public static final Supplier<Item> BEEF_STEW = registerWithTab("beef_stew",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.BEEF_STEW, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> CHICKEN_SOUP = registerWithTab("chicken_soup",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.CHICKEN_SOUP, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> VEGETABLE_SOUP = registerWithTab("vegetable_soup",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.VEGETABLE_SOUP, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> FISH_STEW = registerWithTab("fish_stew",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.FISH_STEW, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> FRIED_RICE = registerWithTab("fried_rice",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.FRIED_RICE, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> PUMPKIN_SOUP = registerWithTab("pumpkin_soup",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.PUMPKIN_SOUP, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> BAKED_COD_STEW = registerWithTab("baked_cod_stew",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.BAKED_COD_STEW, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> NOODLE_SOUP = registerWithTab("noodle_soup",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.NOODLE_SOUP, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> ONION_SOUP = registerWithTab("onion_soup",
		properties -> new ConsumableItem(properties, true),
		// TODO: Double check consumable...
		bowlFoodItem(FoodValues.SHEPHERDS_PIE, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	// Plated Meals
	public static final Supplier<Item> BACON_AND_EGGS = registerWithTab("bacon_and_eggs",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.BACON_AND_EGGS, FoodValues.ConsumableValues.NOURISHMENT_SHORT_DURATION));
	public static final Supplier<Item> PASTA_WITH_MEATBALLS = registerWithTab("pasta_with_meatballs",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.PASTA_WITH_MEATBALLS, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> PASTA_WITH_MUTTON_CHOP = registerWithTab("pasta_with_mutton_chop",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.PASTA_WITH_MUTTON_CHOP, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> MUSHROOM_RICE = registerWithTab("mushroom_rice",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.MUSHROOM_RICE, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> ROASTED_MUTTON_CHOPS = registerWithTab("roasted_mutton_chops",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.ROASTED_MUTTON_CHOPS, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> VEGETABLE_NOODLES = registerWithTab("vegetable_noodles",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.VEGETABLE_NOODLES, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> STEAK_AND_POTATOES = registerWithTab("steak_and_potatoes",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.STEAK_AND_POTATOES, FoodValues.ConsumableValues.NOURISHMENT_MEDIUM_DURATION));
	public static final Supplier<Item> RATATOUILLE = registerWithTab("ratatouille",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.RATATOUILLE, FoodValues.ConsumableValues.NOURISHMENT_SHORT_DURATION));
	public static final Supplier<Item> SQUID_INK_PASTA = registerWithTab("squid_ink_pasta",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.SQUID_INK_PASTA, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));
	public static final Supplier<Item> GRILLED_SALMON = registerWithTab("grilled_salmon",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.GRILLED_SALMON, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	// Feasts
	public static final Supplier<Item> ROAST_CHICKEN_BLOCK = registerBlockWithTab("roast_chicken_block",
		PlaceableItem::new, ModBlocks.ROAST_CHICKEN_BLOCK.get(), basicItem().stacksTo(1));
	public static final Supplier<Item> ROAST_CHICKEN = registerWithTab("roast_chicken",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.ROAST_CHICKEN, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	public static final Supplier<Item> STUFFED_PUMPKIN_BLOCK = registerBlockWithTab("stuffed_pumpkin_block",
		PlaceableItem::new, ModBlocks.STUFFED_PUMPKIN_BLOCK.get(), basicItem().stacksTo(1));
	public static final Supplier<Item> STUFFED_PUMPKIN = registerWithTab("stuffed_pumpkin",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.STUFFED_PUMPKIN, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	public static final Supplier<Item> HONEY_GLAZED_HAM_BLOCK = registerBlockWithTab("honey_glazed_ham_block",
		PlaceableItem::new, ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(), basicItem().stacksTo(1));
	public static final Supplier<Item> HONEY_GLAZED_HAM = registerWithTab("honey_glazed_ham",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.HONEY_GLAZED_HAM, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	public static final Supplier<Item> SHEPHERDS_PIE_BLOCK = registerBlockWithTab("shepherds_pie_block",
		PlaceableItem::new, ModBlocks.SHEPHERDS_PIE_BLOCK.get(), basicItem().stacksTo(1));
	public static final Supplier<Item> SHEPHERDS_PIE = registerWithTab("shepherds_pie",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.SHEPHERDS_PIE, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	public static final Supplier<Item> GLEAMING_SALAD_BLOCK = registerBlockWithTab("gleaming_salad_block",
		PlaceableItem::new, ModBlocks.GLEAMING_SALAD_BLOCK.get(), basicItem().stacksTo(1));
	public static final Supplier<Item> GLEAMING_SALAD = registerWithTab("gleaming_salad",
		properties -> new ConsumableItem(properties, true),
		bowlFoodItem(FoodValues.GLEAMING_SALAD, FoodValues.ConsumableValues.NOURISHMENT_LONG_DURATION));

	public static final Supplier<Item> RICE_ROLL_MEDLEY_BLOCK = registerBlockWithTab("rice_roll_medley_block",
		PlaceableItem::new, ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(), basicItem().stacksTo(1));

	// Pet Foods
	public static final Supplier<Item> DOG_FOOD = registerWithTab("dog_food",
		DogFoodItem::new, bowlFoodItem(FoodValues.DOG_FOOD));
	public static final Supplier<Item> HORSE_FEED = registerWithTab("horse_feed",
		HorseFeedItem::new, basicItem().stacksTo(16));

	// Hidden (Debug) Items
	public static final Supplier<Item> DEBUG_PUMPKIN_PIE = registerHidden("debug_pumpkin_pie",
		properties -> new BlockItem(ModBlocks.PUMPKIN_PIE.get(), properties)
		{
			@Override
			public void appendHoverText(final ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
				super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
				builder.accept(TextUtils.DEBUG_ITEM);
			}
		}, basicItem());
}
