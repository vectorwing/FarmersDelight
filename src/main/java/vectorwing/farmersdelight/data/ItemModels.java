package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Credits to Vazkii and team for some references on mass-reading blocks to datagen!
 */
public class ItemModels extends ModelProvider
{
	public static final Identifier MUG = resourceItem("mug");
	private static final ModelTemplate MUG_TEMPLATE = new ModelTemplate(java.util.Optional.of(MUG), java.util.Optional.empty(), TextureSlot.LAYER0);

	public ItemModels(PackOutput output) {
		super(output, FarmersDelight.MODID);
	}

	@Override
	protected Stream<? extends Holder<Block>> getKnownBlocks() {
		return Stream.empty();
	}

	@Override
	protected Stream<? extends Holder<Item>> getKnownItems() {
		return BuiltInRegistries.ITEM.listElements()
				.filter(holder -> holder.getKey().identifier().getNamespace().equals(FarmersDelight.MODID))
				.filter(holder -> holder.value() != ModItems.SKILLET.get());
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		Set<Item> items = BuiltInRegistries.ITEM.stream()
				.filter(item -> FarmersDelight.MODID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()))
				.filter(item -> item != ModItems.SKILLET.get())
				.collect(Collectors.toCollection(LinkedHashSet::new));

		itemGeneratedModel(itemModels, ModItems.WILD_RICE.get(), resourceBlock(itemName(ModItems.WILD_RICE.get()) + "_top"));
		items.remove(ModItems.WILD_RICE.get());

		itemGeneratedModel(itemModels, ModItems.BROWN_MUSHROOM_COLONY.get(), resourceBlock(itemName(ModItems.BROWN_MUSHROOM_COLONY.get()) + "_stage3"));
		items.remove(ModItems.BROWN_MUSHROOM_COLONY.get());

		itemGeneratedModel(itemModels, ModItems.DEBUG_PUMPKIN_PIE.get(), resourceItem("debug_pumpkin_pie"));
		items.remove(ModItems.DEBUG_PUMPKIN_PIE.get());

		itemGeneratedModel(itemModels, ModItems.RED_MUSHROOM_COLONY.get(), resourceBlock(itemName(ModItems.RED_MUSHROOM_COLONY.get()) + "_stage3"));
		items.remove(ModItems.RED_MUSHROOM_COLONY.get());

		blockBasedModel(itemModels, ModItems.TATAMI.get(), "_half");
		items.remove(ModItems.TATAMI.get());

		blockBasedModel(itemModels, ModItems.ORGANIC_COMPOST.get(), "_stage0");
		items.remove(ModItems.ORGANIC_COMPOST.get());

		blockBasedModel(itemModels, ModItems.ROPE_FENCE.get(), "_inventory");
		items.remove(ModItems.ROPE_FENCE.get());

		Set<Item> mugItems = Sets.newHashSet(
				ModItems.HOT_COCOA.get(),
				ModItems.APPLE_CIDER.get(),
				ModItems.MELON_JUICE.get());
		takeAll(items, mugItems.toArray(new Item[0])).forEach(item -> itemMugModel(itemModels, item, resourceItem(itemName(item))));

		Set<Item> spriteBlockItems = Sets.newHashSet(
				ModItems.FULL_TATAMI_MAT.get(),
				ModItems.HALF_TATAMI_MAT.get(),
				ModItems.ROPE.get(),
				ModItems.CANVAS_SIGN.get(),
				ModItems.HANGING_CANVAS_SIGN.get(),
				ModItems.WHITE_CANVAS_SIGN.get(),
				ModItems.WHITE_HANGING_CANVAS_SIGN.get(),
				ModItems.ORANGE_CANVAS_SIGN.get(),
				ModItems.ORANGE_HANGING_CANVAS_SIGN.get(),
				ModItems.MAGENTA_CANVAS_SIGN.get(),
				ModItems.MAGENTA_HANGING_CANVAS_SIGN.get(),
				ModItems.LIGHT_BLUE_CANVAS_SIGN.get(),
				ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
				ModItems.YELLOW_CANVAS_SIGN.get(),
				ModItems.YELLOW_HANGING_CANVAS_SIGN.get(),
				ModItems.LIME_CANVAS_SIGN.get(),
				ModItems.LIME_HANGING_CANVAS_SIGN.get(),
				ModItems.PINK_CANVAS_SIGN.get(),
				ModItems.PINK_HANGING_CANVAS_SIGN.get(),
				ModItems.GRAY_CANVAS_SIGN.get(),
				ModItems.GRAY_HANGING_CANVAS_SIGN.get(),
				ModItems.LIGHT_GRAY_CANVAS_SIGN.get(),
				ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
				ModItems.CYAN_CANVAS_SIGN.get(),
				ModItems.CYAN_HANGING_CANVAS_SIGN.get(),
				ModItems.PURPLE_CANVAS_SIGN.get(),
				ModItems.PURPLE_HANGING_CANVAS_SIGN.get(),
				ModItems.BLUE_CANVAS_SIGN.get(),
				ModItems.BLUE_HANGING_CANVAS_SIGN.get(),
				ModItems.BROWN_CANVAS_SIGN.get(),
				ModItems.BROWN_HANGING_CANVAS_SIGN.get(),
				ModItems.GREEN_CANVAS_SIGN.get(),
				ModItems.GREEN_HANGING_CANVAS_SIGN.get(),
				ModItems.RED_CANVAS_SIGN.get(),
				ModItems.RED_HANGING_CANVAS_SIGN.get(),
				ModItems.BLACK_CANVAS_SIGN.get(),
				ModItems.BLACK_HANGING_CANVAS_SIGN.get(),
				ModItems.APPLE_PIE.get(),
				ModItems.SWEET_BERRY_CHEESECAKE.get(),
				ModItems.CHOCOLATE_PIE.get(),
				ModItems.CABBAGE_SEEDS.get(),
				ModItems.TOMATO_SEEDS.get(),
				ModItems.ONION.get(),
				ModItems.RICE.get(),
				ModItems.ROAST_CHICKEN_BLOCK.get(),
				ModItems.STUFFED_PUMPKIN_BLOCK.get(),
				ModItems.HONEY_GLAZED_HAM_BLOCK.get(),
				ModItems.SHEPHERDS_PIE_BLOCK.get(),
				ModItems.GLEAMING_SALAD_BLOCK.get(),
				ModItems.RICE_ROLL_MEDLEY_BLOCK.get()
		);
		takeAll(items, spriteBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(itemModels, item, resourceItem(itemName(item))));

		Set<Item> flatBlockItems = Sets.newHashSet(
				ModItems.SAFETY_NET.get(),
				ModItems.SANDY_SHRUB.get(),
				ModItems.WILD_BEETROOTS.get(),
				ModItems.WILD_CABBAGES.get(),
				ModItems.WILD_CARROTS.get(),
				ModItems.WILD_ONIONS.get(),
				ModItems.WILD_POTATOES.get(),
				ModItems.WILD_TOMATOES.get()
		);
		takeAll(items, flatBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(itemModels, item, resourceBlock(itemName(item))));

		takeAll(items, item -> item instanceof BlockItem).forEach(item -> blockBasedModel(itemModels, item, ""));

		Set<Item> handheldItems = Sets.newHashSet(
				ModItems.BARBECUE_STICK.get(),
				ModItems.HAM.get(),
				ModItems.SMOKED_HAM.get(),
				ModItems.FLINT_KNIFE.get(),
				ModItems.IRON_KNIFE.get(),
				ModItems.DIAMOND_KNIFE.get(),
				ModItems.GOLDEN_KNIFE.get(),
				ModItems.NETHERITE_KNIFE.get()
		);
		takeAll(items, handheldItems.toArray(new Item[0])).forEach(item -> itemHandheldModel(itemModels, item));

		items.forEach(item -> itemGeneratedModel(itemModels, item, resourceItem(itemName(item))));
	}

	@Override
	public String getName() {
		return "Item Model Definitions - " + FarmersDelight.MODID;
	}

	public static void blockBasedModel(ItemModelGenerators itemModels, Item item, String suffix) {
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(resourceBlock(itemName(item) + suffix)));
	}

	public static void itemHandheldModel(ItemModelGenerators itemModels, Item item) {
		itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
	}

	public static void itemGeneratedModel(ItemModelGenerators itemModels, Item item, Identifier texture) {
		Identifier model = ModelTemplates.FLAT_ITEM.create(resourceItem(itemName(item)), TextureMapping.layer0(new Material(texture)), itemModels.modelOutput);
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	public static void itemMugModel(ItemModelGenerators itemModels, Item item, Identifier texture) {
		Identifier model = MUG_TEMPLATE.create(resourceItem(itemName(item)), TextureMapping.layer0(new Material(texture)), itemModels.modelOutput);
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	private static String itemName(Item item) {
		return BuiltInRegistries.ITEM.getKey(item).getPath();
	}

	public static Identifier resourceBlock(String path) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block/" + path);
	}

	public static Identifier resourceItem(String path) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "item/" + path);
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		java.util.List<T> ret = Arrays.asList(items);
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
		java.util.List<T> ret = new ArrayList<>();

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
