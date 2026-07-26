package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class ItemTags extends TagsProvider<Item>
{
	public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.ITEM, provider, FarmersDelight.MODID);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		copy(ModTags.Blocks.WILD_CROPS, ModTags.Items.WILD_CROPS);

		this.registerMinecraftTags();
		this.registerModTags();
		this.registerNeoForgeTags();
		this.registerCommonTags();
		this.registerCompatibilityTags();
	}

	private void registerMinecraftTags() {
		tag(net.minecraft.tags.ItemTags.BREAKS_DECORATED_POTS).addTag(ModTags.Items.KNIVES);
		tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(key(ModItems.GOLDEN_KNIFE.get()));
		tag(net.minecraft.tags.ItemTags.SIGNS).addTag(ModTags.Items.CANVAS_SIGNS);
		tag(net.minecraft.tags.ItemTags.HANGING_SIGNS).addTag(ModTags.Items.HANGING_CANVAS_SIGNS);
		tag(net.minecraft.tags.ItemTags.VILLAGER_PLANTABLE_SEEDS)
			.add(key(ModItems.CABBAGE_SEEDS.get()))
			.add(key(ModItems.TOMATO_SEEDS.get()))
			.add(key(ModItems.ONION.get()));

		tag(net.minecraft.tags.ItemTags.DURABILITY_ENCHANTABLE).addTag(ModTags.Items.KNIVES).add(key(ModItems.SKILLET.get()));
		tag(net.minecraft.tags.ItemTags.WEAPON_ENCHANTABLE).addTag(ModTags.Items.KNIVES).add(key(ModItems.SKILLET.get()));
		tag(net.minecraft.tags.ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ModTags.Items.KNIVES).add(key(ModItems.SKILLET.get()));
		tag(net.minecraft.tags.ItemTags.FIRE_ASPECT_ENCHANTABLE).addTag(ModTags.Items.KNIVES).add(key(ModItems.SKILLET.get()));
		tag(net.minecraft.tags.ItemTags.MINING_ENCHANTABLE).addTag(ModTags.Items.KNIVES);
		tag(net.minecraft.tags.ItemTags.MINING_LOOT_ENCHANTABLE).addTag(ModTags.Items.KNIVES);

		tag(net.minecraft.tags.ItemTags.MEAT)
			.add(key(ModItems.MINCED_BEEF.get()))
			.add(key(ModItems.BEEF_PATTY.get()))
			.add(key(ModItems.CHICKEN_CUTS.get()))
			.add(key(ModItems.COOKED_CHICKEN_CUTS.get()))
			.add(key(ModItems.BACON.get()))
			.add(key(ModItems.COOKED_BACON.get()))
			.add(key(ModItems.MUTTON_CHOPS.get()))
			.add(key(ModItems.COOKED_MUTTON_CHOPS.get()))
			.add(key(ModItems.HAM.get()))
			.add(key(ModItems.SMOKED_HAM.get()))
			.add(key(ModItems.DOG_FOOD.get()));
		tag(net.minecraft.tags.ItemTags.CAT_FOOD)
			.add((key(ModItems.SALMON_SLICE.get())))
			.add((key(ModItems.COD_SLICE.get())));
		tag(net.minecraft.tags.ItemTags.CHICKEN_FOOD)
			.add(key(ModItems.CABBAGE_SEEDS.get()))
			.add(key(ModItems.TOMATO_SEEDS.get()))
			.add(key(ModItems.RICE.get()));
		tag(net.minecraft.tags.ItemTags.PIG_FOOD)
			.add(key(ModItems.CABBAGE.get()))
			.add(key(ModItems.TOMATO.get()));
		tag(net.minecraft.tags.ItemTags.RABBIT_FOOD)
			.add(key(ModItems.CABBAGE.get()));
		tag(net.minecraft.tags.ItemTags.PARROT_FOOD)
			.add(key(ModItems.CABBAGE_SEEDS.get()))
			.add(key(ModItems.TOMATO_SEEDS.get()))
			.add(key(ModItems.RICE.get()));
		tag(net.minecraft.tags.ItemTags.HORSE_TEMPT_ITEMS)
			.add(key(ModItems.HORSE_FEED.get()));
	}

	private void registerModTags() {
		tag(ModTags.Items.SNACKS).add(
			key(ModItems.BARBECUE_STICK.get()),
			key(ModItems.EGG_SANDWICH.get()),
			key(ModItems.CHICKEN_SANDWICH.get()),
			key(ModItems.HAMBURGER.get()),
			key(ModItems.BACON_SANDWICH.get()),
			key(ModItems.MUTTON_WRAP.get()),
			key(ModItems.DUMPLINGS.get()),
			key(ModItems.STUFFED_POTATO.get()),
			key(ModItems.CABBAGE_ROLLS.get()),
			key(ModItems.SALMON_ROLL.get()),
			key(ModItems.COD_ROLL.get()),
			key(ModItems.KELP_ROLL.get()),
			key(ModItems.KELP_ROLL_SLICE.get())
		);
		tag(ModTags.Items.MEALS).add(
			key(Items.MUSHROOM_STEW),
			key(Items.BEETROOT_SOUP),
			key(Items.RABBIT_STEW),
			key(ModItems.MIXED_SALAD.get()),
			key(ModItems.COOKED_RICE.get()),
			key(ModItems.BONE_BROTH.get()),
			key(ModItems.BEEF_STEW.get()),
			key(ModItems.VEGETABLE_SOUP.get()),
			key(ModItems.FISH_STEW.get()),
			key(ModItems.CHICKEN_SOUP.get()),
			key(ModItems.FRIED_RICE.get()),
			key(ModItems.PUMPKIN_SOUP.get()),
			key(ModItems.BAKED_COD_STEW.get()),
			key(ModItems.NOODLE_SOUP.get()),
			key(ModItems.ONION_SOUP.get()),
			key(ModItems.BACON_AND_EGGS.get()),
			key(ModItems.RATATOUILLE.get()),
			key(ModItems.STEAK_AND_POTATOES.get()),
			key(ModItems.PASTA_WITH_MEATBALLS.get()),
			key(ModItems.PASTA_WITH_MUTTON_CHOP.get()),
			key(ModItems.MUSHROOM_RICE.get()),
			key(ModItems.ROASTED_MUTTON_CHOPS.get()),
			key(ModItems.VEGETABLE_NOODLES.get()),
			key(ModItems.SQUID_INK_PASTA.get()),
			key(ModItems.GRILLED_SALMON.get()),
			key(ModItems.ROAST_CHICKEN.get()),
			key(ModItems.STUFFED_PUMPKIN.get()),
			key(ModItems.HONEY_GLAZED_HAM.get()),
			key(ModItems.SHEPHERDS_PIE.get()),
			key(ModItems.GLEAMING_SALAD.get())
		);
		tag(ModTags.Items.DRINKS).add(
			key(ModItems.MILK_BOTTLE.get()),
			key(ModItems.APPLE_CIDER.get()),
			key(ModItems.MELON_JUICE.get()),
			key(ModItems.HOT_COCOA.get())
		);
		tag(ModTags.Items.SWEETS).add(
			key(Items.CAKE),
			key(Items.COOKIE),
			key(ModItems.CAKE_SLICE.get()),
			key(ModItems.APPLE_PIE_SLICE.get()),
			key(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()),
			key(ModItems.CHOCOLATE_PIE_SLICE.get()),
			key(ModItems.PUMPKIN_PIE_SLICE.get()),
			key(ModItems.SWEET_BERRY_COOKIE.get()),
			key(ModItems.HONEY_COOKIE.get()),
			key(ModItems.MELON_POPSICLE.get()),
			key(ModItems.GLOW_BERRY_CUSTARD.get()),
			key(ModItems.FRUIT_SALAD.get())
		);
		copy(ModTags.Blocks.FEASTS, ModTags.Items.FEASTS);
		tag(ModTags.Items.PIES).add(
			key(Items.PUMPKIN_PIE),
			key(ModItems.APPLE_PIE.get()),
			key(ModItems.SWEET_BERRY_CHEESECAKE.get()),
			key(ModItems.CHOCOLATE_PIE.get())
		);
		tag(ModTags.Items.KNIVES).add(key(ModItems.FLINT_KNIFE.get()), key(ModItems.IRON_KNIFE.get()), key(ModItems.DIAMOND_KNIFE.get()), key(ModItems.GOLDEN_KNIFE.get()), key(ModItems.NETHERITE_KNIFE.get()));
		tag(ModTags.Items.KNIFE_ENCHANTABLE).addTag(ModTags.Items.KNIVES);
		tag(ModTags.Items.STRAW_HARVESTERS).addTag(ModTags.Items.KNIVES);
		tag(ModTags.Items.CANVAS_SIGNS)
			.add(key(ModItems.CANVAS_SIGN.get()))
			.add(key(ModItems.WHITE_CANVAS_SIGN.get()))
			.add(key(ModItems.ORANGE_CANVAS_SIGN.get()))
			.add(key(ModItems.MAGENTA_CANVAS_SIGN.get()))
			.add(key(ModItems.LIGHT_BLUE_CANVAS_SIGN.get()))
			.add(key(ModItems.YELLOW_CANVAS_SIGN.get()))
			.add(key(ModItems.LIME_CANVAS_SIGN.get()))
			.add(key(ModItems.PINK_CANVAS_SIGN.get()))
			.add(key(ModItems.GRAY_CANVAS_SIGN.get()))
			.add(key(ModItems.LIGHT_GRAY_CANVAS_SIGN.get()))
			.add(key(ModItems.CYAN_CANVAS_SIGN.get()))
			.add(key(ModItems.PURPLE_CANVAS_SIGN.get()))
			.add(key(ModItems.BLUE_CANVAS_SIGN.get()))
			.add(key(ModItems.BROWN_CANVAS_SIGN.get()))
			.add(key(ModItems.GREEN_CANVAS_SIGN.get()))
			.add(key(ModItems.RED_CANVAS_SIGN.get()))
			.add(key(ModItems.BLACK_CANVAS_SIGN.get()));
		tag(ModTags.Items.HANGING_CANVAS_SIGNS)
			.add(key(ModItems.HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.WHITE_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.ORANGE_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.MAGENTA_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.YELLOW_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.LIME_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.PINK_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.GRAY_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.CYAN_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.PURPLE_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.BLUE_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.BROWN_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.GREEN_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.RED_HANGING_CANVAS_SIGN.get()))
			.add(key(ModItems.BLACK_HANGING_CANVAS_SIGN.get()));
		copy(ModTags.Blocks.CABINETS, ModTags.Items.CABINETS);
		copy(ModTags.Blocks.CABINETS_WOODEN, ModTags.Items.CABINETS_WOODEN);

		copy(ModTags.Blocks.MUSHROOM_COLONIES, ModTags.Items.MUSHROOM_COLONIES);

		tag(ModTags.Items.SERVING_CONTAINERS).add(key(Items.BOWL), key(Items.GLASS_BOTTLE), key(Items.BUCKET));
		tag(ModTags.Items.FLAT_ON_CUTTING_BOARD).add(key(Items.TRIDENT), key(Items.SPYGLASS))
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("supplementaries:quiver")))
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("autumnity:turkey")))
			.addOptional(ResourceKey.create(Registries.ITEM, Identifier.parse("autumnity:cooked_turkey")));
	}

	@SuppressWarnings("unchecked")
	private void registerNeoForgeTags() {
		// Add our custom tags to "common" tag groups
		tag(Tags.Items.CROPS)
			.addTag(CommonTags.Items.CROPS_GRAIN);
		tag(Tags.Items.DRINKS)
			.addTag(ModTags.Items.DRINKS);
		tag(Tags.Items.FOODS)
			.add(key(ModItems.TOMATO_SAUCE.get()))
			.add(key(ModItems.PIE_CRUST.get()))
			.add(key(ModItems.PUMPKIN_SLICE.get()))
			.add(key(ModItems.HAM.get()))
			.add(key(ModItems.SMOKED_HAM.get()))
			.add(key(ModItems.DOG_FOOD.get()))
			.addTag(ModTags.Items.SNACKS)
			.addTag(ModTags.Items.MEALS)
			.addTag(ModTags.Items.SWEETS)
			.addTag(CommonTags.Items.FOODS_LEAFY_GREEN)
			.addTag(CommonTags.Items.FOODS_DOUGH)
			.addTag(CommonTags.Items.FOODS_PASTA)
			.addTag(CommonTags.Items.FOODS_COOKED_EGG);

		tag(Tags.Items.FENCES).add(key(ModItems.ROPE_FENCE.get()));
		tag(Tags.Items.FENCE_GATES).add(key(ModItems.ROPE_FENCE_GATE.get()));

		tag(Tags.Items.DRINKS_MILK).add(key(ModItems.MILK_BOTTLE.get()));

		tag(Tags.Items.FOODS_VEGETABLE).add(key(ModItems.ONION.get()), key(ModItems.TOMATO.get()));
		tag(Tags.Items.FOODS_COOKIE).add(key(ModItems.HONEY_COOKIE.get()), key(ModItems.SWEET_BERRY_COOKIE.get()));
		tag(Tags.Items.FOODS_DOUGH).addTag(CommonTags.Items.FOODS_DOUGH_WHEAT);
		tag(Tags.Items.FOODS_RAW_MEAT).addTags(CommonTags.Items.FOODS_RAW_CHICKEN, CommonTags.Items.FOODS_RAW_PORK, CommonTags.Items.FOODS_RAW_BEEF, CommonTags.Items.FOODS_RAW_MUTTON);
		tag(Tags.Items.FOODS_RAW_FISH).addTags(CommonTags.Items.FOODS_RAW_COD, CommonTags.Items.FOODS_RAW_SALMON);
		tag(Tags.Items.FOODS_COOKED_MEAT).addTags(CommonTags.Items.FOODS_COOKED_CHICKEN, CommonTags.Items.FOODS_COOKED_PORK, CommonTags.Items.FOODS_COOKED_BEEF, CommonTags.Items.FOODS_COOKED_MUTTON);
		tag(Tags.Items.FOODS_COOKED_FISH).addTags(CommonTags.Items.FOODS_COOKED_COD, CommonTags.Items.FOODS_COOKED_SALMON);
		tag(Tags.Items.FOODS_FOOD_POISONING).add(
			key(ModItems.WHEAT_DOUGH.get()),
			key(ModItems.RAW_PASTA.get()),
			key(ModItems.CHICKEN_CUTS.get()),
			key(ModItems.NETHER_SALAD.get())
		);
		tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
			.add(key(ModItems.APPLE_PIE.get()))
			.add(key(ModItems.SWEET_BERRY_CHEESECAKE.get()))
			.add(key(ModItems.CHOCOLATE_PIE.get()))
			.addTag(ModTags.Items.FEASTS);
		tag(Tags.Items.FOODS_SOUP)
			.add(key(ModItems.BONE_BROTH.get()))
			.add(key(ModItems.BEEF_STEW.get()))
			.add(key(ModItems.VEGETABLE_SOUP.get()))
			.add(key(ModItems.CHICKEN_SOUP.get()))
			.add(key(ModItems.FISH_STEW.get()))
			.add(key(ModItems.PUMPKIN_SOUP.get()))
			.add(key(ModItems.BAKED_COD_STEW.get()))
			.add(key(ModItems.NOODLE_SOUP.get()));
		tag(Tags.Items.FOODS_PIE)
			.add(key(ModItems.APPLE_PIE_SLICE.get()))
			.add(key(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()))
			.add(key(ModItems.CHOCOLATE_PIE_SLICE.get()))
			.add(key(ModItems.PUMPKIN_PIE_SLICE.get()));

		tag(Tags.Items.TOOLS).addTag(CommonTags.Items.TOOLS_KNIFE);
		tag(Tags.Items.ROPES).add(key(ModItems.ROPE.get()));
		tag(Tags.Items.SEEDS).add(key(ModItems.CABBAGE_SEEDS.get()), key(ModItems.RICE.get()), key(ModItems.TOMATO_SEEDS.get()));
		tag(Tags.Items.CROPS).addTags(CommonTags.Items.CROPS_CABBAGE, CommonTags.Items.CROPS_ONION, CommonTags.Items.CROPS_RICE, CommonTags.Items.CROPS_TOMATO);
		tag(Tags.Items.STORAGE_BLOCKS).addTags(
			CommonTags.Items.STORAGE_BLOCKS_CARROT,
			CommonTags.Items.STORAGE_BLOCKS_POTATO,
			CommonTags.Items.STORAGE_BLOCKS_BEETROOT,
			CommonTags.Items.STORAGE_BLOCKS_CABBAGE,
			CommonTags.Items.STORAGE_BLOCKS_TOMATO,
			CommonTags.Items.STORAGE_BLOCKS_ONION,
			CommonTags.Items.STORAGE_BLOCKS_RICE,
			CommonTags.Items.STORAGE_BLOCKS_RICE_PANICLE,
			CommonTags.Items.STORAGE_BLOCKS_STRAW
		);
	}

	public void registerCommonTags() {
		tag(CommonTags.Items.CROPS_CABBAGE).add(key(ModItems.CABBAGE.get()), key(ModItems.CABBAGE_LEAF.get()));
		tag(CommonTags.Items.CROPS_ONION).add(key(ModItems.ONION.get()));
		tag(CommonTags.Items.CROPS_TOMATO).add(key(ModItems.TOMATO.get()));
		tag(CommonTags.Items.CROPS_RICE).add(key(ModItems.RICE.get()));

		tag(CommonTags.Items.FOODS_CABBAGE).add(key(ModItems.CABBAGE.get()), key(ModItems.CABBAGE_LEAF.get()));
		tag(CommonTags.Items.FOODS_TOMATO).add(key(ModItems.TOMATO.get()));
		tag(CommonTags.Items.FOODS_ONION).add(key(ModItems.ONION.get()));

		tag(CommonTags.Items.FOODS_DOUGH_WHEAT).add(key(ModItems.WHEAT_DOUGH.get()));
		tag(CommonTags.Items.CROPS_GRAIN).add(key(Items.WHEAT), key(ModItems.RICE.get()));
		tag(CommonTags.Items.FOODS_PASTA).add(key(ModItems.RAW_PASTA.get()));
		tag(CommonTags.Items.FOODS_LEAFY_GREEN).addTag(CommonTags.Items.FOODS_CABBAGE);

		tag(CommonTags.Items.FOODS_RAW_BACON).add(key(ModItems.BACON.get()));
		tag(CommonTags.Items.FOODS_RAW_BEEF).add(key(Items.BEEF), key(ModItems.MINCED_BEEF.get()));
		tag(CommonTags.Items.FOODS_RAW_CHICKEN).add(key(Items.CHICKEN), key(ModItems.CHICKEN_CUTS.get()));
		tag(CommonTags.Items.FOODS_RAW_PORK).add(key(Items.PORKCHOP)).addTag(CommonTags.Items.FOODS_RAW_BACON);
		tag(CommonTags.Items.FOODS_RAW_MUTTON).add(key(Items.MUTTON), key(ModItems.MUTTON_CHOPS.get()));
		tag(CommonTags.Items.FOODS_RAW_COD).add(key(Items.COD), key(ModItems.COD_SLICE.get()));
		tag(CommonTags.Items.FOODS_RAW_SALMON).add(key(Items.SALMON), key(ModItems.SALMON_SLICE.get()));
		tag(CommonTags.Items.FOODS_SAFE_RAW_FISH).addTag(Tags.Items.FOODS_RAW_FISH).remove(key(Items.PUFFERFISH));

		tag(CommonTags.Items.FOODS_COOKED_BACON).add(key(ModItems.COOKED_BACON.get()));
		tag(CommonTags.Items.FOODS_COOKED_BEEF).add(key(Items.COOKED_BEEF), key(ModItems.BEEF_PATTY.get()));
		tag(CommonTags.Items.FOODS_COOKED_CHICKEN).add(key(Items.COOKED_CHICKEN), key(ModItems.COOKED_CHICKEN_CUTS.get()));
		tag(CommonTags.Items.FOODS_COOKED_PORK).add(key(Items.COOKED_PORKCHOP)).addTag(CommonTags.Items.FOODS_COOKED_BACON);
		tag(CommonTags.Items.FOODS_COOKED_MUTTON).add(key(Items.COOKED_MUTTON), key(ModItems.COOKED_MUTTON_CHOPS.get()));
		tag(CommonTags.Items.FOODS_COOKED_COD).add(key(Items.COOKED_COD), key(ModItems.COOKED_COD_SLICE.get()));
		tag(CommonTags.Items.FOODS_COOKED_SALMON).add(key(Items.COOKED_SALMON), key(ModItems.COOKED_SALMON_SLICE.get()));
		tag(CommonTags.Items.FOODS_COOKED_EGG).add(key(ModItems.FRIED_EGG.get()));

		tag(CommonTags.Items.STORAGE_BLOCKS_CARROT).add(key(ModItems.CARROT_CRATE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_POTATO).add(key(ModItems.POTATO_CRATE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_BEETROOT).add(key(ModItems.BEETROOT_CRATE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_CABBAGE).add(key(ModItems.CABBAGE_CRATE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_TOMATO).add(key(ModItems.TOMATO_CRATE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_ONION).add(key(ModItems.ONION_CRATE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_RICE).add(key(ModItems.RICE_BAG.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_RICE_PANICLE).add(key(ModItems.RICE_BALE.get()));
		tag(CommonTags.Items.STORAGE_BLOCKS_STRAW).add(key(ModItems.STRAW_BALE.get()));

		tag(CommonTags.Items.TOOLS_KNIFE).add(key(ModItems.FLINT_KNIFE.get()), key(ModItems.IRON_KNIFE.get()), key(ModItems.DIAMOND_KNIFE.get()), key(ModItems.GOLDEN_KNIFE.get()), key(ModItems.NETHERITE_KNIFE.get()));
	}

	public void registerCompatibilityTags() {
		tag(CompatibilityTags.CREATE_UPRIGHT_ON_BELT)
			.addTag(ModTags.Items.MEALS)
			.addTag(ModTags.Items.DRINKS)
			.addTag(ModTags.Items.FEASTS)
			.add(key(ModItems.TOMATO_SAUCE.get()))
			.add(key(ModItems.DOG_FOOD.get()))
			.add(key(ModItems.FRUIT_SALAD.get()))
			.add(key(ModItems.NETHER_SALAD.get()))
			.add(key(ModItems.PIE_CRUST.get()))
			.add(key(ModItems.APPLE_PIE.get()))
			.add(key(ModItems.SWEET_BERRY_CHEESECAKE.get()))
			.add(key(ModItems.CHOCOLATE_PIE.get()));

		tag(CompatibilityTags.CREATE_CA_PLANT_FOODS)
			.add(key(ModItems.PUMPKIN_SLICE.get()))
			.add(key(ModItems.ROTTEN_TOMATO.get()))
			.add(key(ModItems.RICE_PANICLE.get()));
		tag(CompatibilityTags.CREATE_CA_PLANTS)
			.add(key(ModItems.SANDY_SHRUB.get()))
			.add(key(ModItems.BROWN_MUSHROOM_COLONY.get()))
			.add(key(ModItems.RED_MUSHROOM_COLONY.get()));

		tag(CompatibilityTags.ORIGINS_MEAT)
			.add(key(ModItems.FRIED_EGG.get()))
			.add(key(ModItems.COD_SLICE.get()))
			.add(key(ModItems.COOKED_COD_SLICE.get()))
			.add(key(ModItems.SALMON_SLICE.get()))
			.add(key(ModItems.COOKED_SALMON_SLICE.get()))
			.add(key(ModItems.BACON_AND_EGGS.get()));

		tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS)
			.add(key(ModItems.CABBAGE_SEEDS.get()))
			.add(key(ModItems.ONION.get()))
			.add(key(ModItems.RICE.get()));
		tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS)
			.add(key(ModItems.ONION.get()));
		tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS)
			.add(key(ModItems.TOMATO_SEEDS.get()))
			.add(key(ModItems.RICE.get()));
		tag(CompatibilityTags.SERENE_SEASONS_WINTER_CROPS)
			.add(key(ModItems.CABBAGE_SEEDS.get()));

		tag(CompatibilityTags.TINKERS_CONSTRUCT_SEEDS).add(key(ModItems.ONION.get()));
	}

	private static ResourceKey<Item> key(Item item) {
		return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
	}

	private void copy(TagKey<net.minecraft.world.level.block.Block> blockTag, TagKey<Item> itemTag) {
		if (blockTag == ModTags.Blocks.WILD_CROPS && itemTag == ModTags.Items.WILD_CROPS) {
			tag(itemTag).add(
				key(ModItems.WILD_CARROTS.get()),
				key(ModItems.WILD_POTATOES.get()),
				key(ModItems.WILD_BEETROOTS.get()),
				key(ModItems.WILD_CABBAGES.get()),
				key(ModItems.WILD_TOMATOES.get()),
				key(ModItems.WILD_ONIONS.get()),
				key(ModItems.WILD_RICE.get()));
		} else if (blockTag == ModTags.Blocks.FEASTS && itemTag == ModTags.Items.FEASTS) {
			tag(itemTag).add(
				key(ModItems.ROAST_CHICKEN_BLOCK.get()),
				key(ModItems.STUFFED_PUMPKIN_BLOCK.get()),
				key(ModItems.SHEPHERDS_PIE_BLOCK.get()),
				key(ModItems.HONEY_GLAZED_HAM_BLOCK.get()),
				key(ModItems.GLEAMING_SALAD_BLOCK.get()),
				key(ModItems.RICE_ROLL_MEDLEY_BLOCK.get()));
		} else if (blockTag == ModTags.Blocks.CABINETS && itemTag == ModTags.Items.CABINETS) {
			tag(itemTag).addTag(ModTags.Items.CABINETS_WOODEN);
		} else if (blockTag == ModTags.Blocks.CABINETS_WOODEN && itemTag == ModTags.Items.CABINETS_WOODEN) {
			tag(itemTag).add(
				key(ModItems.OAK_CABINET.get()),
				key(ModItems.SPRUCE_CABINET.get()),
				key(ModItems.BIRCH_CABINET.get()),
				key(ModItems.JUNGLE_CABINET.get()),
				key(ModItems.ACACIA_CABINET.get()),
				key(ModItems.DARK_OAK_CABINET.get()),
				key(ModItems.MANGROVE_CABINET.get()),
				key(ModItems.CHERRY_CABINET.get()),
				key(ModItems.BAMBOO_CABINET.get()),
				key(ModItems.CRIMSON_CABINET.get()),
				key(ModItems.WARPED_CABINET.get()));
		} else if (blockTag == ModTags.Blocks.MUSHROOM_COLONIES && itemTag == ModTags.Items.MUSHROOM_COLONIES) {
			tag(itemTag).add(key(ModItems.BROWN_MUSHROOM_COLONY.get()), key(ModItems.RED_MUSHROOM_COLONY.get()));
		}
	}
}
