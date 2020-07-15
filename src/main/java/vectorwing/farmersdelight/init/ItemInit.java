package vectorwing.farmersdelight.init;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.items.KnifeItem;
import vectorwing.farmersdelight.items.MilkBottleItem;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmersDelight.MODID);

	public static final RegistryObject<Item> CABBAGE_SEEDS = ITEMS.register("cabbage_seeds", () -> new BlockItem(BlockInit.CABBAGE_CROP.get(), new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new BlockItem(BlockInit.TOMATO_CROP.get(), new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> RICE_PANICLE = ITEMS.register("rice_panicle", () -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> ONION = ITEMS.register("onion",
			() -> new BlockItem(BlockInit.ONION_CROP.get(), new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(1).saturation(0.1F).build())));

	public static final RegistryObject<Item> CABBAGE = ITEMS.register("cabbage",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(2).saturation(0.1F).build())));
	public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(1).saturation(0.1F).build())));
	public static final RegistryObject<Item> RICE = ITEMS.register("rice",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP)));

	public static final RegistryObject<Item> FRIED_EGG = ITEMS.register("fried_egg",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(4).saturation(0.3F).build())));
	public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle",
			() -> new MilkBottleItem(new Item.Properties().group(FarmersDelight.ITEM_GROUP).containerItem(Items.GLASS_BOTTLE).maxStackSize(16)));
	public static final RegistryObject<Item> TOMATO_SAUCE = ITEMS.register("tomato_sauce",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> CAKE_SLICE = ITEMS.register("cake_slice",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(2).saturation(0.1F).build())));

	public static final RegistryObject<Item> FLINT_KNIFE = ITEMS.register("flint_knife",
			() -> new KnifeItem(ItemTier.STONE, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> IRON_KNIFE = ITEMS.register("iron_knife",
			() -> new KnifeItem(ItemTier.IRON, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> DIAMOND_KNIFE = ITEMS.register("diamond_knife",
			() -> new KnifeItem(ItemTier.DIAMOND, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));
	public static final RegistryObject<Item> GOLDEN_KNIFE = ITEMS.register("golden_knife",
			() -> new KnifeItem(ItemTier.GOLD, 1, -1.8F, new Item.Properties().group(FarmersDelight.ITEM_GROUP)));

	public static final RegistryObject<Item> CHICKEN_SANDWICH = ITEMS.register("chicken_sandwich",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(8).saturation(0.7F).build())));
	public static final RegistryObject<Item> HAMBURGER = ITEMS.register("hamburger",
			() -> new Item(new Item.Properties().group(FarmersDelight.ITEM_GROUP).food(new Food.Builder().hunger(10).saturation(0.8F).build())));
}
