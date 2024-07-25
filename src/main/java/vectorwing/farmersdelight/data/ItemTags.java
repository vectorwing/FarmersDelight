package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider
{
	public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, blockTagProvider, FarmersDelight.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		copy(ModTags.WILD_CROPS, ModTags.WILD_CROPS_ITEM);
		copy(BlockTags.SMALL_FLOWERS, net.minecraft.tags.ItemTags.SMALL_FLOWERS);

		this.registerMinecraftTags();
		this.registerModTags();
		this.registerNeoForgeTags();
		this.registerCommonTags();
		this.registerCompatibilityTags();
	}

	private void registerMinecraftTags() {
		tag(net.minecraft.tags.ItemTags.TALL_FLOWERS).add(ModItems.WILD_RICE.get());
		tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(ModItems.GOLDEN_KNIFE.get());

		tag(net.minecraft.tags.ItemTags.DURABILITY_ENCHANTABLE).addTag(ModTags.KNIVES);
		tag(net.minecraft.tags.ItemTags.WEAPON_ENCHANTABLE).addTag(ModTags.KNIVES);
		tag(net.minecraft.tags.ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ModTags.KNIVES);
		tag(net.minecraft.tags.ItemTags.FIRE_ASPECT_ENCHANTABLE).addTag(ModTags.KNIVES);
		tag(net.minecraft.tags.ItemTags.MINING_ENCHANTABLE).addTag(ModTags.KNIVES);
		tag(net.minecraft.tags.ItemTags.MINING_LOOT_ENCHANTABLE).addTag(ModTags.KNIVES);

		tag(net.minecraft.tags.ItemTags.MEAT)
				.add(ModItems.MINCED_BEEF.get())
				.add(ModItems.BEEF_PATTY.get())
				.add(ModItems.CHICKEN_CUTS.get())
				.add(ModItems.COOKED_CHICKEN_CUTS.get())
				.add(ModItems.BACON.get())
				.add(ModItems.COOKED_BACON.get())
				.add(ModItems.MUTTON_CHOPS.get())
				.add(ModItems.COOKED_MUTTON_CHOPS.get())
				.add(ModItems.HAM.get())
				.add(ModItems.SMOKED_HAM.get())
				.add(ModItems.DOG_FOOD.get());
		tag(net.minecraft.tags.ItemTags.CHICKEN_FOOD)
				.add(ModItems.CABBAGE_SEEDS.get())
				.add(ModItems.TOMATO_SEEDS.get())
				.add(ModItems.RICE.get());
		tag(net.minecraft.tags.ItemTags.PIG_FOOD)
				.add(ModItems.CABBAGE.get())
				.add(ModItems.TOMATO.get());
		tag(net.minecraft.tags.ItemTags.RABBIT_FOOD)
				.add(ModItems.CABBAGE.get());
		tag(net.minecraft.tags.ItemTags.PARROT_FOOD)
				.add(ModItems.CABBAGE_SEEDS.get())
				.add(ModItems.TOMATO_SEEDS.get())
				.add(ModItems.RICE.get());
		tag(net.minecraft.tags.ItemTags.HORSE_TEMPT_ITEMS)
				.add(ModItems.HORSE_FEED.get());
	}

	private void registerModTags() {
		tag(ModTags.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
		tag(ModTags.KNIFE_ENCHANTABLE).addTag(ModTags.KNIVES);
		tag(ModTags.STRAW_HARVESTERS).addTag(ModTags.KNIVES);
		tag(ModTags.CABBAGE_ROLL_INGREDIENTS).addTag(CommonTags.FOODS_RAW_PORK).addTag(CommonTags.FOODS_SAFE_RAW_FISH).addTag(CommonTags.FOODS_RAW_CHICKEN).addTag(CommonTags.FOODS_RAW_BEEF).addTag(CommonTags.FOODS_RAW_MUTTON).addTag(Tags.Items.EGGS).addTag(Tags.Items.MUSHROOMS).add(Items.CARROT, Items.POTATO, Items.BEETROOT);
		tag(ModTags.CANVAS_SIGNS)
				.add(ModItems.CANVAS_SIGN.get())
				.add(ModItems.WHITE_CANVAS_SIGN.get())
				.add(ModItems.ORANGE_CANVAS_SIGN.get())
				.add(ModItems.MAGENTA_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_BLUE_CANVAS_SIGN.get())
				.add(ModItems.YELLOW_CANVAS_SIGN.get())
				.add(ModItems.LIME_CANVAS_SIGN.get())
				.add(ModItems.PINK_CANVAS_SIGN.get())
				.add(ModItems.GRAY_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_GRAY_CANVAS_SIGN.get())
				.add(ModItems.CYAN_CANVAS_SIGN.get())
				.add(ModItems.PURPLE_CANVAS_SIGN.get())
				.add(ModItems.BLUE_CANVAS_SIGN.get())
				.add(ModItems.BROWN_CANVAS_SIGN.get())
				.add(ModItems.GREEN_CANVAS_SIGN.get())
				.add(ModItems.RED_CANVAS_SIGN.get())
				.add(ModItems.BLACK_CANVAS_SIGN.get());
		tag(ModTags.HANGING_CANVAS_SIGNS)
				.add(ModItems.HANGING_CANVAS_SIGN.get())
				.add(ModItems.WHITE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.ORANGE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.MAGENTA_HANGING_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.YELLOW_HANGING_CANVAS_SIGN.get())
				.add(ModItems.LIME_HANGING_CANVAS_SIGN.get())
				.add(ModItems.PINK_HANGING_CANVAS_SIGN.get())
				.add(ModItems.GRAY_HANGING_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get())
				.add(ModItems.CYAN_HANGING_CANVAS_SIGN.get())
				.add(ModItems.PURPLE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.BLUE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.BROWN_HANGING_CANVAS_SIGN.get())
				.add(ModItems.GREEN_HANGING_CANVAS_SIGN.get())
				.add(ModItems.RED_HANGING_CANVAS_SIGN.get())
				.add(ModItems.BLACK_HANGING_CANVAS_SIGN.get());
		tag(ModTags.WOODEN_CABINETS)
				.add(ModItems.OAK_CABINET.get())
				.add(ModItems.SPRUCE_CABINET.get())
				.add(ModItems.BIRCH_CABINET.get())
				.add(ModItems.JUNGLE_CABINET.get())
				.add(ModItems.ACACIA_CABINET.get())
				.add(ModItems.DARK_OAK_CABINET.get())
				.add(ModItems.MANGROVE_CABINET.get())
				.add(ModItems.CHERRY_CABINET.get())
				.add(ModItems.BAMBOO_CABINET.get())
				.add(ModItems.CRIMSON_CABINET.get())
				.add(ModItems.WARPED_CABINET.get());
		tag(ModTags.CABINETS).addTag(ModTags.WOODEN_CABINETS);
		tag(ModTags.OFFHAND_EQUIPMENT).add(Items.SHIELD)
				.addOptional(ResourceLocation.parse("create:extendo_grip"));
		tag(ModTags.SERVING_CONTAINERS).add(Items.BOWL, Items.GLASS_BOTTLE, Items.BUCKET);
		tag(ModTags.FLAT_ON_CUTTING_BOARD).add(Items.TRIDENT, Items.SPYGLASS)
				.addOptional(ResourceLocation.parse("supplementaries:quiver"))
				.addOptional(ResourceLocation.parse("autumnity:turkey"))
				.addOptional(ResourceLocation.parse("autumnity:cooked_turkey"));
	}

	@SuppressWarnings("unchecked")
	private void registerNeoForgeTags() {
		tag(Tags.Items.CROPS)
				.addTag(CommonTags.CROPS_GRAIN);
		tag(Tags.Items.FOODS)
				.addTag(CommonTags.FOODS_LEAFY_GREEN)
				.addTag(CommonTags.FOODS_DOUGH)
				.addTag(CommonTags.FOODS_PASTA)
				.addTag(CommonTags.FOODS_COOKED_EGG)
				.addTag(CommonTags.FOODS_MILK);
		tag(Tags.Items.FOODS_VEGETABLE).add(ModItems.ONION.get(), ModItems.TOMATO.get());
		tag(Tags.Items.FOODS_COOKIE).add(ModItems.HONEY_COOKIE.get(), ModItems.SWEET_BERRY_COOKIE.get());
		tag(Tags.Items.FOODS_RAW_MEAT).addTags(CommonTags.FOODS_RAW_CHICKEN, CommonTags.FOODS_RAW_PORK, CommonTags.FOODS_RAW_BEEF, CommonTags.FOODS_RAW_MUTTON);
		tag(Tags.Items.FOODS_RAW_FISH).addTags(CommonTags.FOODS_RAW_COD, CommonTags.FOODS_RAW_SALMON);
		tag(Tags.Items.FOODS_COOKED_MEAT).addTags(CommonTags.FOODS_COOKED_CHICKEN, CommonTags.FOODS_COOKED_PORK, CommonTags.FOODS_COOKED_BEEF, CommonTags.FOODS_COOKED_MUTTON);
		tag(Tags.Items.FOODS_COOKED_FISH).addTags(CommonTags.FOODS_COOKED_COD, CommonTags.FOODS_COOKED_SALMON);
		tag(Tags.Items.FOODS_FOOD_POISONING).add(ModItems.CHICKEN_CUTS.get());
		tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
				.add(ModItems.APPLE_PIE.get())
				.add(ModItems.SWEET_BERRY_CHEESECAKE.get())
				.add(ModItems.CHOCOLATE_PIE.get())
				.add(ModItems.ROAST_CHICKEN_BLOCK.get())
				.add(ModItems.HONEY_GLAZED_HAM_BLOCK.get())
				.add(ModItems.SHEPHERDS_PIE_BLOCK.get())
				.add(ModItems.STUFFED_PUMPKIN_BLOCK.get())
				.add(ModItems.RICE_ROLL_MEDLEY_BLOCK.get());

		tag(Tags.Items.TOOLS).addTag(CommonTags.TOOLS_KNIFE);
		tag(Tags.Items.SEEDS).add(ModItems.CABBAGE_SEEDS.get(), ModItems.RICE.get(), ModItems.TOMATO_SEEDS.get());
		tag(Tags.Items.CROPS).addTags(CommonTags.CROPS_CABBAGE, CommonTags.CROPS_ONION, CommonTags.CROPS_RICE, CommonTags.CROPS_TOMATO);
	}

	public void registerCommonTags() {
		tag(CommonTags.CROPS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
		tag(CommonTags.CROPS_ONION).add(ModItems.ONION.get());
		tag(CommonTags.CROPS_TOMATO).add(ModItems.TOMATO.get());
		tag(CommonTags.CROPS_RICE).add(ModItems.RICE.get());

		tag(CommonTags.FOODS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
		tag(CommonTags.FOODS_TOMATO).add(ModItems.TOMATO.get());
		tag(CommonTags.FOODS_ONION).add(ModItems.ONION.get());

		tag(CommonTags.FOODS_DOUGH).add(ModItems.WHEAT_DOUGH.get());
		tag(CommonTags.CROPS_GRAIN).add(Items.WHEAT, ModItems.RICE.get());
		tag(CommonTags.FOODS_MILK).add(Items.MILK_BUCKET, ModItems.MILK_BOTTLE.get());
		tag(CommonTags.FOODS_PASTA).add(ModItems.RAW_PASTA.get());
		tag(CommonTags.FOODS_LEAFY_GREEN).addTag(CommonTags.FOODS_CABBAGE);

		tag(CommonTags.FOODS_RAW_BACON).add(ModItems.BACON.get());
		tag(CommonTags.FOODS_RAW_BEEF).add(Items.BEEF, ModItems.MINCED_BEEF.get());
		tag(CommonTags.FOODS_RAW_CHICKEN).add(Items.CHICKEN, ModItems.CHICKEN_CUTS.get());
		tag(CommonTags.FOODS_RAW_PORK).add(Items.PORKCHOP).addTag(CommonTags.FOODS_RAW_BACON);
		tag(CommonTags.FOODS_RAW_MUTTON).add(Items.MUTTON, ModItems.MUTTON_CHOPS.get());
		tag(CommonTags.FOODS_RAW_COD).add(Items.COD, ModItems.COD_SLICE.get());
		tag(CommonTags.FOODS_RAW_SALMON).add(Items.SALMON, ModItems.SALMON_SLICE.get());
		tag(CommonTags.FOODS_SAFE_RAW_FISH).addTag(Tags.Items.FOODS_RAW_FISH).remove(Items.PUFFERFISH);

		tag(CommonTags.FOODS_COOKED_BACON).add(ModItems.COOKED_BACON.get());
		tag(CommonTags.FOODS_COOKED_BEEF).add(Items.COOKED_BEEF, ModItems.BEEF_PATTY.get());
		tag(CommonTags.FOODS_COOKED_CHICKEN).add(Items.COOKED_CHICKEN, ModItems.COOKED_CHICKEN_CUTS.get());
		tag(CommonTags.FOODS_COOKED_PORK).add(Items.COOKED_PORKCHOP).addTag(CommonTags.FOODS_COOKED_BACON);
		tag(CommonTags.FOODS_COOKED_MUTTON).add(Items.COOKED_MUTTON, ModItems.COOKED_MUTTON_CHOPS.get());
		tag(CommonTags.FOODS_COOKED_COD).add(Items.COOKED_COD, ModItems.COOKED_COD_SLICE.get());
		tag(CommonTags.FOODS_COOKED_SALMON).add(Items.COOKED_SALMON, ModItems.COOKED_SALMON_SLICE.get());
		tag(CommonTags.FOODS_COOKED_EGG).add(ModItems.FRIED_EGG.get());

		tag(CommonTags.TOOLS_KNIFE).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
	}

	public void registerCompatibilityTags() {
		tag(CompatibilityTags.CREATE_UPRIGHT_ON_BELT)
				.add(ModItems.MILK_BOTTLE.get())
				.add(ModItems.HOT_COCOA.get())
				.add(ModItems.APPLE_CIDER.get())
				.add(ModItems.MELON_JUICE.get())
				.add(ModItems.PIE_CRUST.get())
				.add(ModItems.APPLE_PIE.get())
				.add(ModItems.SWEET_BERRY_CHEESECAKE.get())
				.add(ModItems.CHOCOLATE_PIE.get());

		tag(CompatibilityTags.CREATE_CA_PLANT_FOODS)
				.add(ModItems.PUMPKIN_SLICE.get())
				.add(ModItems.ROTTEN_TOMATO.get())
				.add(ModItems.RICE_PANICLE.get());
		tag(CompatibilityTags.CREATE_CA_PLANTS)
				.add(ModItems.SANDY_SHRUB.get())
				.add(ModItems.BROWN_MUSHROOM_COLONY.get())
				.add(ModItems.RED_MUSHROOM_COLONY.get());

		tag(CompatibilityTags.ORIGINS_MEAT)
				.add(ModItems.FRIED_EGG.get())
				.add(ModItems.COD_SLICE.get())
				.add(ModItems.COOKED_COD_SLICE.get())
				.add(ModItems.SALMON_SLICE.get())
				.add(ModItems.COOKED_SALMON_SLICE.get())
				.add(ModItems.BACON_AND_EGGS.get());

		tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS)
				.add(ModItems.CABBAGE_SEEDS.get())
				.add(ModItems.ONION.get())
				.add(ModItems.RICE.get());
		tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS)
				.add(ModItems.ONION.get());
		tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS)
				.add(ModItems.TOMATO_SEEDS.get())
				.add(ModItems.RICE.get());
		tag(CompatibilityTags.SERENE_SEASONS_WINTER_CROPS)
				.add(ModItems.CABBAGE_SEEDS.get());

		tag(CompatibilityTags.TINKERS_CONSTRUCT_SEEDS).add(ModItems.ONION.get());
	}
}
