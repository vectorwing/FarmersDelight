package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModItems;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Credits to Vazkii and team for some references on mass-reading blocks to datagen!
 */
public class ItemModels extends ItemModelProvider
{
	public static final String GENERATED = "item/generated";
	public static final String HANDHELD = "item/handheld";

	public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, FarmersDelight.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		Set<Item> items = ForgeRegistries.ITEMS.getValues().stream().filter(i -> FarmersDelight.MODID.equals(ForgeRegistries.ITEMS.getKey(i).getNamespace()))
				.collect(Collectors.toSet());

		// Specific cases
		itemGeneratedModel(ModItems.WILD_RICE.get(), resourceBlock(itemName(ModItems.WILD_RICE.get()) + "_top"));
		items.remove(ModItems.WILD_RICE.get());

		blockBasedModel(ModItems.TATAMI.get(), "_half");
		items.remove(ModItems.TATAMI.get());

		blockBasedModel(ModItems.ORGANIC_COMPOST.get(), "_0");
		items.remove(ModItems.ORGANIC_COMPOST.get());

		// Blocks with special item sprites
		Set<Item> spriteBlockItems = Sets.newHashSet(
				ModItems.FULL_TATAMI_MAT.get(),
				ModItems.HALF_TATAMI_MAT.get(),
				ModItems.ROPE.get(),
				ModItems.APPLE_PIE.get(),
				ModItems.SWEET_BERRY_CHEESECAKE.get(),
				ModItems.CHOCOLATE_PIE.get(),
				ModItems.CABBAGE_SEEDS.get(),
				ModItems.TOMATO_SEEDS.get(),
				ModItems.ONION.get(),
				ModItems.RICE.get(),
				ModItems.STUFFED_PUMPKIN_BLOCK.get()
		);
		takeAll(items, spriteBlockItems.toArray(new Item[0])).forEach(item -> withExistingParent(itemName(item), GENERATED).texture("layer0", resourceItem(itemName(item))));

		// Blocks with flat block textures for their items
		Set<Item> flatBlockItems = Sets.newHashSet(
				ModItems.SAFETY_NET.get(),
				ModItems.WILD_BEETROOTS.get(),
				ModItems.WILD_CABBAGES.get(),
				ModItems.WILD_CARROTS.get(),
				ModItems.WILD_ONIONS.get(),
				ModItems.WILD_POTATOES.get(),
				ModItems.WILD_TOMATOES.get()
		);
		takeAll(items, flatBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(item, resourceBlock(itemName(item))));

		// Blocks whose items look alike
		takeAll(items, i -> i instanceof BlockItem).forEach(item -> blockBasedModel(item, ""));

		// Handheld items
		Set<Item> handheldItems = Sets.newHashSet(
				ModItems.BARBECUE_STICK.get(),
				ModItems.FLINT_KNIFE.get(),
				ModItems.IRON_KNIFE.get(),
				ModItems.DIAMOND_KNIFE.get(),
				ModItems.GOLDEN_KNIFE.get(),
				ModItems.NETHERITE_KNIFE.get()
		);
		takeAll(items, handheldItems.toArray(new Item[0])).forEach(item -> itemHandheldModel(item, resourceItem(itemName(item))));

		// Generated items
		items.forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));
	}

	public void blockBasedModel(Item item, String suffix) {
		withExistingParent(itemName(item), resourceBlock(itemName(item) + suffix));
	}

	public void itemHandheldModel(Item item, ResourceLocation texture) {
		withExistingParent(itemName(item), HANDHELD).texture("layer0", texture);
	}

	public void itemGeneratedModel(Item item, ResourceLocation texture) {
		withExistingParent(itemName(item), GENERATED).texture("layer0", texture);
	}

	private String itemName(Item item) {
		return item.getRegistryName().getPath();
	}

	public ResourceLocation resourceBlock(String path) {
		return new ResourceLocation(FarmersDelight.MODID, "block/" + path);
	}

	public ResourceLocation resourceItem(String path) {
		return new ResourceLocation(FarmersDelight.MODID, "item/" + path);
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				FarmersDelight.LOGGER.warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			FarmersDelight.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
		List<T> ret = new ArrayList<>();

		Iterator<T> iter = src.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (pred.test(item)) {
				iter.remove();
				ret.add(item);
			}
		}

		if (ret.isEmpty()) {
			FarmersDelight.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}
}