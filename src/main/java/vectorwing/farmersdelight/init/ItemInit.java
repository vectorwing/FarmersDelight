package vectorwing.farmersdelight.init;

import vectorwing.farmersdelight.CreativeTab;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.items.Foods;
import vectorwing.farmersdelight.items.KnifeItem;
import vectorwing.farmersdelight.items.MealItem;
import vectorwing.farmersdelight.items.MilkBottleItem;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersDelight.MODID);

	public static final RegistryObject<Item> CABBAGE = ITEMS.register("cabbage",
			() -> new Item(new Item.Properties().food(Foods.CABBAGE).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
			() -> new Item(new Item.Properties().food(Foods.TOMATO).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> ONION = ITEMS.register("onion",
			() -> new BlockItem(BlockInit.ONION_CROP.get(), new Item.Properties().food(Foods.ONION).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> RICE = ITEMS.register("rice",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP)));

	public static final RegistryObject<Item> CABBAGE_SEEDS = ITEMS.register("cabbage_seeds", () -> new BlockItem(BlockInit.CABBAGE_CROP.get(), new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new BlockItem(BlockInit.TOMATO_CROP.get(), new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> RICE_PANICLE = ITEMS.register("rice_panicle", () -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP)));

	public static final RegistryObject<Item> FRIED_EGG = ITEMS.register("fried_egg",
			() -> new Item(new Item.Properties().food(Foods.FRIED_EGG).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle",
			() -> new MilkBottleItem(new Item.Properties().containerItem(Items.GLASS_BOTTLE).maxStackSize(16).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce",
			() -> new MealItem(new Item.Properties().food(Foods.TOMATO_SAUCE).containerItem(Items.BOWL).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> RAW_PASTA = ITEMS.register("raw_pasta",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> CAKE_SLICE = ITEMS.register("cake_slice",
			() -> new Item(new Item.Properties().food(Foods.CAKE_SLICE).group(FarmersDelight.ITEM_GROUP)));

	public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife",
			() -> new KnifeItem(ItemTier.STONE, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife",
			() -> new KnifeItem(ItemTier.IRON, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife",
			() -> new KnifeItem(ItemTier.DIAMOND, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> GOLDEN_KNIFE = ITEMS.register("golden_knife",
			() -> new KnifeItem(ItemTier.GOLD, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));

	public static final RegistryObject<Item> FRESH_SALAD = ITEMS.register("fresh_salad",
			() -> new MealItem(new Item.Properties().food(Foods.FRESH_SALAD).containerItem(Items.BOWL).maxStackSize(16).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> BARBECUE_STICK = ITEMS.register("barbecue_stick",
			() -> new Item(new Item.Properties().food(Foods.BARBECUE_STICK).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> CHICKEN_SANDWICH = ITEMS.register("chicken_sandwich",
			() -> new Item(new Item.Properties().food(Foods.CHICKEN_SANDWICH).group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> HAMBURGER = ITEMS.register("hamburger",
			() -> new Item(new Item.Properties().food(Foods.HAMBURGER).group(FarmersDelight.ITEM_GROUP)));
}
