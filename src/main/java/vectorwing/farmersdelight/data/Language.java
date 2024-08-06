package vectorwing.farmersdelight.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.*;

import java.util.function.Supplier;

public class Language extends LanguageProvider {
	public Language(PackOutput output) {
		super(output, FarmersDelight.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		this.addBlock(ModBlocks.STOVE, "Stove");
		this.addBlock(ModBlocks.COOKING_POT, "Cooking Pot");
		this.addBlock(ModBlocks.BASKET, "Basket");
		this.addBlock(ModBlocks.CUTTING_BOARD, "Cutting Board");
		this.addBlock(ModBlocks.SKILLET, "Skillet");
		this.addBlock(ModBlocks.OAK_CABINET, "Oak Cabinet");
		this.addBlock(ModBlocks.BIRCH_CABINET, "Birch Cabinet");
		this.addBlock(ModBlocks.SPRUCE_CABINET, "Spruce Cabinet");
		this.addBlock(ModBlocks.JUNGLE_CABINET, "Jungle Cabinet");
		this.addBlock(ModBlocks.ACACIA_CABINET, "Acacia Cabinet");
		this.addBlock(ModBlocks.DARK_OAK_CABINET, "Dark Oak Cabinet");
		this.addBlock(ModBlocks.MANGROVE_CABINET, "Mangrove Cabinet");
		this.addBlock(ModBlocks.CHERRY_CABINET, "Cherry Cabinet");
		this.addBlock(ModBlocks.BAMBOO_CABINET, "Bamboo Cabinet");
		this.addBlock(ModBlocks.CRIMSON_CABINET, "Crimson Cabinet");
		this.addBlock(ModBlocks.WARPED_CABINET, "Warped Cabinet");
		this.addBlock(ModBlocks.ROPE, "Rope");
		this.addBlock(ModBlocks.SAFETY_NET, "Safety Net");
		this.addBlock(ModBlocks.CANVAS_RUG, "Canvas Rug");
		this.addBlock(ModBlocks.TATAMI, "Tatami Block");
		this.addBlock(ModBlocks.FULL_TATAMI_MAT, "Full Tatami Mat");
		this.addBlock(ModBlocks.HALF_TATAMI_MAT, "Half Tatami Mat");
		this.addBlock(ModBlocks.ORGANIC_COMPOST, "Organic Compost");
		this.addBlock(ModBlocks.RICH_SOIL, "Rich Soil");
		this.addBlock(ModBlocks.RICH_SOIL_FARMLAND, "Rich Soil Farmland");
		this.addBlock(ModBlocks.CARROT_CRATE, "Carrot Crate");
		this.addBlock(ModBlocks.POTATO_CRATE, "Potato Crate");
		this.addBlock(ModBlocks.BEETROOT_CRATE, "Beetroot Crate");
		this.addBlock(ModBlocks.CABBAGE_CRATE, "Cabbage Crate");
		this.addBlock(ModBlocks.TOMATO_CRATE, "Tomato Crate");
		this.addBlock(ModBlocks.ONION_CRATE, "Onion Crate");
		this.addBlock(ModBlocks.RICE_BALE, "Rice Bale");
		this.addBlock(ModBlocks.RICE_BAG, "Bag of Rice");
		this.addBlock(ModBlocks.STRAW_BALE, "Straw Bale");

		this.addBlock(ModBlocks.CANVAS_SIGN, "Canvas Sign");
		this.addBlock(ModBlocks.HANGING_CANVAS_SIGN, "Hanging Canvas Sign");

		for (DyeColor color : DyeColor.values()) {
			this.add(BuiltInRegistries.BLOCK.get(new ResourceLocation(FarmersDelight.MODID, color.getName() + "_canvas_sign")),
					WordUtils.capitalize(color.getName().replace('_', ' ')) + " Canvas Sign");
			this.add(BuiltInRegistries.BLOCK.get(new ResourceLocation(FarmersDelight.MODID, color.getName() + "_hanging_canvas_sign")),
					WordUtils.capitalize(color.getName().replace('_', ' ')) + " Hanging Canvas Sign");
		}

		this.addBlock(ModBlocks.BUDDING_TOMATO_CROP, "Budding Tomato Vine");
		this.addBlock(ModBlocks.TOMATO_CROP, "Tomato Vine");
		this.addBlock(ModBlocks.CABBAGE_CROP, "Cabbage");
		this.addBlock(ModBlocks.ONION_CROP, "Onions");
		this.addBlock(ModBlocks.RICE_CROP, "Rice Crops");
		this.addBlock(ModBlocks.RICE_CROP_PANICLES, "Rice Crops");
		this.addBlock(ModBlocks.BROWN_MUSHROOM_COLONY, "Brown Mushroom Colony");
		this.addBlock(ModBlocks.RED_MUSHROOM_COLONY, "Red Mushroom Colony");

		this.addBlock(ModBlocks.SANDY_SHRUB, "Sandy Shrub");
		this.addBlockWithJeiInfo(ModBlocks.WILD_CABBAGES, "Wild Cabbage", "Cabbages can be commonly found as a wild plant in beach coasts.");
		this.addBlockWithJeiInfo(ModBlocks.WILD_TOMATOES, "Tomato Shrub", "Tomatoes can be commonly found as a wild plant in arid lands, such as savannas and deserts.");
		this.addBlockWithJeiInfo(ModBlocks.WILD_ONIONS, "Wild Onion", "Onions can be commonly found as a wild plant in temperate lands, such as plains and forests.");
		this.addBlockWithJeiInfo(ModBlocks.WILD_CARROTS, "Wild Carrot", "Carrots can be commonly found as a wild plant in temperate lands, such as plains and forests.");
		this.addBlockWithJeiInfo(ModBlocks.WILD_POTATOES, "Wild Potato", "Potatoes can be commonly found as a wild plant in cold lands, such as mountains and taigas.");
		this.addBlockWithJeiInfo(ModBlocks.WILD_BEETROOTS, "Sea Beet", "Beetroot can be commonly found as a wild plant in beach coasts.");
		this.addBlockWithJeiInfo(ModBlocks.WILD_RICE, "Wild Rice", "Rice can be commonly found as a wild plant in the ponds of wet lands, such as swamps and jungles.");

		this.addBlock(ModBlocks.APPLE_PIE, "Apple Pie");
		this.addBlock(ModBlocks.SWEET_BERRY_CHEESECAKE, "Sweet Berry Cheesecake");
		this.addBlock(ModBlocks.CHOCOLATE_PIE, "Chocolate Pie");

		this.addItem(ModItems.TOMATO, "Tomato");
		this.addItem(ModItems.CABBAGE, "Cabbage");
		this.addItem(ModItems.ONION, "Onion");
		this.addItem(ModItems.RICE, "Rice");
		this.addItem(ModItems.CABBAGE_SEEDS, "Cabbage Seeds");
		this.addItem(ModItems.TOMATO_SEEDS, "Tomato Seeds");
		this.addItem(ModItems.RICE_PANICLE, "Rice Panicle");
		this.addItem(ModItems.ROTTEN_TOMATO, "Rotten Tomato");

		this.addItemWithJeiInfo(ModItems.STRAW, "Straw", "A dry plant fiber used in crafting. Obtained by cutting grassy crops and plants with a Knife.");
		this.addItem(ModItems.CANVAS, "Canvas");
		this.addItem(ModItems.TREE_BARK, "Tree Bark");

		this.addItem(ModItems.TOMATO_SAUCE, "Tomato");
		this.addItem(ModItems.FRIED_EGG, "Fried Egg");
		this.addItem(ModItems.MILK_BOTTLE, "Milk Bottle");
		this.addItem(ModItems.HOT_COCOA, "Hot Cocoa");
		this.addItem(ModItems.APPLE_CIDER, "Apple Cider");
		this.addItem(ModItems.MELON_JUICE, "Melon Juice");
		this.addItem(ModItems.WHEAT_DOUGH, "Wheat Dough");
		this.addItem(ModItems.RAW_PASTA, "Raw Pasta");
		this.addItem(ModItems.PUMPKIN_SLICE, "Pumpkin Slice");
		this.addItem(ModItems.CABBAGE_LEAF, "Cabbage Leaf");
		this.addItem(ModItems.MINCED_BEEF, "Minced Beef");
		this.addItem(ModItems.BEEF_PATTY, "Beef Patty");
		this.addItem(ModItems.CHICKEN_CUTS, "Raw Chicken Cuts");
		this.addItem(ModItems.COOKED_CHICKEN_CUTS, "Cooked Chicken Cuts");
		this.addItem(ModItems.BACON, "Raw Bacon");
		this.addItem(ModItems.COOKED_BACON, "Cooked Bacon");
		this.addItem(ModItems.COD_SLICE, "Raw Cod Slice");
		this.addItem(ModItems.COOKED_COD_SLICE, "Cooked Cod Slice");
		this.addItem(ModItems.SALMON_SLICE, "Raw Salmon Slice");
		this.addItem(ModItems.COOKED_SALMON_SLICE, "Cooked Salmon Slice");
		this.addItem(ModItems.MUTTON_CHOPS, "Raw Mutton Chops");
		this.addItem(ModItems.COOKED_MUTTON_CHOPS, "Cooked Mutton Chops");
		this.addItemWithJeiInfo(ModItems.HAM, "Ham", "A large cut of meat from a Pig or Hoglin's leg.\n\nYou will need something lighter than a Sword to cut one...");
		this.addItem(ModItems.SMOKED_HAM, "Smoked Ham");

		this.addItem(ModItems.PIE_CRUST, "Pie Crust");
		this.addItem(ModItems.CAKE_SLICE, "Slice of Cake");
		this.addItem(ModItems.APPLE_PIE_SLICE, "Slice of Apple Pie");
		this.addItem(ModItems.SWEET_BERRY_CHEESECAKE_SLICE, "Slice of Sweet Berry Cheesecake");
		this.addItem(ModItems.CHOCOLATE_PIE_SLICE, "Slice of Chocolate Pie");
		this.addItem(ModItems.FRUIT_SALAD, "Fruit Salad");
		this.addItem(ModItems.SWEET_BERRY_COOKIE, "Sweet Berry Cookie");
		this.addItem(ModItems.HONEY_COOKIE, "Honey Cookie");
		this.addItem(ModItems.MELON_POPSICLE, "Melon Popsicle");
		this.addItem(ModItems.GLOW_BERRY_CUSTARD, "Glow Berry Custard");

		this.addItem(ModItems.FLINT_KNIFE, "Flint Knife");
		this.addItem(ModItems.IRON_KNIFE, "Iron Knife");
		this.addItem(ModItems.GOLDEN_KNIFE, "Golden Knife");
		this.addItem(ModItems.DIAMOND_KNIFE, "Diamond Knife");
		this.addItem(ModItems.NETHERITE_KNIFE, "Netherite Knife");
		this.add("farmersdelight.jei.info.knife", "A lightweight melee weapon, with fast swings and gentle damage.\n\nThey can harvest Straw from grasses, and guarantee secondary drops from animals.");

		this.addItem(ModItems.BARBECUE_STICK, "Barbecue on a Stick");
		this.addItem(ModItems.EGG_SANDWICH, "Egg Sandwich");
		this.addItem(ModItems.CHICKEN_SANDWICH, "Chicken Sandwich");
		this.addItem(ModItems.HAMBURGER, "Hamburger");
		this.addItem(ModItems.BACON_SANDWICH, "Bacon Sandwich");
		this.addItem(ModItems.MUTTON_WRAP, "Mutton Wrap");
		this.addItem(ModItems.DUMPLINGS, "Dumplings");
		this.addItem(ModItems.STUFFED_POTATO, "Stuffed Potato");
		this.addItem(ModItems.CABBAGE_ROLLS, "Cabbage Rolls");
		this.addItem(ModItems.SALMON_ROLL, "Salmon Roll");
		this.addItem(ModItems.COD_ROLL, "Cod Roll");
		this.addItem(ModItems.KELP_ROLL, "Kelp Roll");
		this.addItem(ModItems.KELP_ROLL_SLICE, "Kelp Roll Slice");

		this.addItem(ModItems.COOKED_RICE, "Cooked Rice");
		this.addItem(ModItems.MIXED_SALAD, "Mixed Salad");
		this.addItem(ModItems.NETHER_SALAD, "Nether Salad");
		this.addItem(ModItems.BEEF_STEW, "Beef Stew");
		this.addItem(ModItems.CHICKEN_SOUP, "Chicken Soup");
		this.addItem(ModItems.VEGETABLE_SOUP, "Vegetable Soup");
		this.addItem(ModItems.FISH_STEW, "Fish Stew");
		this.addItem(ModItems.FRIED_RICE, "Fried Rice");
		this.addItem(ModItems.PUMPKIN_SOUP, "Pumpkin Soup");
		this.addItem(ModItems.BAKED_COD_STEW, "Baked Cod Stew");
		this.addItem(ModItems.NOODLE_SOUP, "Noodle Soup");
		this.addItem(ModItems.BONE_BROTH, "Bone Broth");

		this.addItem(ModItems.BACON_AND_EGGS, "Bacon and Eggs");
		this.addItem(ModItems.PASTA_WITH_MEATBALLS, "Pasta with Meatballs");
		this.addItem(ModItems.PASTA_WITH_MUTTON_CHOP, "Pasta with Mutton Chop");
		this.addItem(ModItems.ROASTED_MUTTON_CHOPS, "Roasted Mutton Chops");
		this.addItem(ModItems.STEAK_AND_POTATOES, "Steak and Potatoes");
		this.addItem(ModItems.VEGETABLE_NOODLES, "Vegetable Noodles");
		this.addItem(ModItems.RATATOUILLE, "Ratatouille");
		this.addItem(ModItems.SQUID_INK_PASTA, "Squid Ink Pasta");
		this.addItem(ModItems.GRILLED_SALMON, "Grilled Salmon");
		this.addItem(ModItems.MUSHROOM_RICE, "Mushroom Rice");

		this.addBlock(ModBlocks.ROAST_CHICKEN_BLOCK, "Roast Chicken");
		this.addItem(ModItems.ROAST_CHICKEN, "Plate of Roast Chicken");
		this.addBlock(ModBlocks.STUFFED_PUMPKIN_BLOCK, "Stuffed Pumpkin");
		this.addItem(ModItems.STUFFED_PUMPKIN, "Bowl of Stuffed Pumpkin");
		this.addBlock(ModBlocks.HONEY_GLAZED_HAM_BLOCK, "Honey Glazed Ham");
		this.addItem(ModItems.HONEY_GLAZED_HAM, "Plate of Honey Glazed Ham");
		this.addBlock(ModBlocks.SHEPHERDS_PIE_BLOCK, "Shepherd's Pie");
		this.addItem(ModItems.SHEPHERDS_PIE, "Plate of Shepherd's Pie");
		this.addBlock(ModBlocks.RICE_ROLL_MEDLEY_BLOCK, "Rice Roll Medley");

		this.addItem(ModItems.DOG_FOOD, "Dog Food");
		this.addItem(ModItems.HORSE_FEED, "Horse Feed");

		this.addEntityType(ModEntityTypes.ROTTEN_TOMATO, "Rotten Tomato");

		this.addEffect(ModEffects.COMFORT, "Comfort", "Provides natural regeneration, regardless of how hungry you are.");
		this.addEffect(ModEffects.NOURISHMENT, "Nourishment", "Prevents hunger loss, except when using saturation to heal damage.");

		this.addEnchantment(ModEnchantments.BACKSTABBING, "Backstabbing", "Amplifies damage when striking a target from behind.");

		this.add("itemGroup.farmersdelight", "Farmer's Delight");

		this.add("farmersdelight.container.recipe_book.cookable", "Show Cookable");
		this.add("farmersdelight.container.cooking_pot", "Cooking Pot");
		this.add("farmersdelight.container.cooking_pot.served_on", "Served on: %s");
		this.add("farmersdelight.container.cooking_pot.heated", "Heated");
		this.add("farmersdelight.container.cooking_pot.not_heated", "Needs heat from below");
		this.add("farmersdelight.container.basket", "Basket");
		this.add("farmersdelight.container.cabinet", "Cabinet");

		this.addSpecialTooltip(ModBlocks.COOKING_POT, "empty", "Empty");
		this.addSpecialTooltip(ModBlocks.COOKING_POT, "single_serving", "Holds 1 serving of:");
		this.addSpecialTooltip(ModBlocks.COOKING_POT, "many_servings", "Holds %s servings of:");
		this.addSpecialTooltip(ModItems.DOG_FOOD, "when_feeding", "When fed to a tamed Wolf:");
		this.addSpecialTooltip(ModItems.HORSE_FEED, "when_feeding", "When fed to a tamed mount:");
		this.addBasicTooltip(ModItems.MILK_BOTTLE, "Clears 1 Effect");
		this.addBasicTooltip(ModItems.HOT_COCOA, "Clears 1 Harmful Effect");
		this.addBasicTooltip(ModItems.MELON_JUICE, "Minor Instant Health");

		this.addAdvancement("root", "Farmer's Delight", "A world of flavor awaits you!");

		this.addAdvancement("craft_knife", "Hunt and Gather", "Craft a Knife to scavenge extra goods from plants and animals");
		this.addAdvancement("harvest_straw", "Grasping at Straws", "Harvest grass, wheat or rice with a Knife to collect Straw");
		this.addAdvancement("place_organic_compost", "Advanced Composting", "Place down some Organic Compost. It composts better with sun, water and mushrooms!");
		this.addAdvancement("get_rich_soil", "Plant Food", "Organic Compost slowly decays into Rich Soil, an upgrade for your farms!");
		this.addAdvancement("get_ham", "Wild Butcher", "Use a Knife to extract Ham from Pigs or Hoglins");
		this.addAdvancement("use_cutting_board", "Watch Your Fingers", "With a tool in hand, use a Cutting Board to break down an item");
		this.addAdvancement("obtain_netherite_knife", "If You Can't Take the Heat...", "Spend a whole Netherite Ingot to upgrade your knife! Or get out of the kitchen.");

		this.addAdvancement("get_fd_seed", "Crops of the Wild", "Four new crops may be found in the wild, across many climates... or maybe in a chest somewhere.");
		this.addAdvancement("plant_rice", "Dipping Your Roots", "Plant grains of Rice in a shallow water puddle");
		this.addAdvancement("harvest_ropelogged_tomato", "Tall-mato", "Hang some rope above a tomato crop to make it grow taller");
		this.addAdvancement("hit_raider_with_rotten_tomato", "Boo! Hiss!", "Throw a Rotten Tomato at one of these pesky raiders!");
		this.addAdvancement("get_mushroom_colony", "Fungus Among Us", "Shear a fully mature Mushroom Colony. To grow them like this, you'll need a very rich soil...");
		this.addAdvancement("plant_all_crops", "Crop Rotation", "Cultivate every food-related plant you can find, such as vegetables, fruits, fungi or roots!");

		this.addAdvancement("place_campfire", "Bonfire Lit", "Place down a Campfire to cook some food");
		this.addAdvancement("use_skillet", "Portable Cooking", "Skillets let you cook on the go. Stand near heat, then hold food in your other hand!");
		this.addAdvancement("place_skillet", "Sizzling Hot!", "Sneak to place your Skillet down as a block");
		this.addAdvancement("place_cooking_pot", "Dinner's Served!", "Put down a Cooking Pot and start preparing meals!");
		this.addAdvancement("eat_comfort_food", "Comforting!", "Warm bowl meals will keep you healing steadily, no matter how hungry you are!");
		this.addAdvancement("eat_nourishing_food", "Nourishing!", "Plated meals will prevent you from getting exhausted or hungry for a while!");
		this.addAdvancement("place_feast", "A Glorious Feast", "Some meals are big enough to be placed down and shared with friends!");
		this.addAdvancement("master_chef", "Master Chef", "Eat a course of every meal available!");

		this.addSubtitle(ModSounds.BLOCK_COOKING_POT_BOIL, "Cooking Pot Boils");
		this.addSubtitle(ModSounds.BLOCK_CUTTING_BOARD_KNIFE, "Knife cuts");
		this.addSubtitle(ModSounds.BLOCK_STOVE_CRACKLE, "Stove crackles");
		this.addSubtitle(ModSounds.BLOCK_SKILLET_SIZZLE, "Skillet sizzles");
		this.addSubtitle(ModSounds.BLOCK_SKILLET_ADD_FOOD, "Skillet hisses");
		this.addSubtitle(ModSounds.BLOCK_CABINET_OPEN, "Cabinet opens");
		this.addSubtitle(ModSounds.BLOCK_CABINET_CLOSE, "Cabinet closes");
		this.addSubtitle(ModSounds.ITEM_TOMATO_PICK_FROM_BUSH, "Tomatoes pop");
		this.addSubtitle(ModSounds.ENTITY_ROTTEN_TOMATO_THROW, "Rotten Tomato flies");
		this.addSubtitle(ModSounds.ENTITY_ROTTEN_TOMATO_HIT, "Rotten Tomato splatters");

		this.addDeathMessage("stove", "%1$s was grilled to perfection");
		this.addDeathMessage("stove.player", "%1$s was thrown on the grill by Chef %2$s");

		this.add("farmersdelight.block.feast.use_container", "You need a %s to eat this.");
		this.add("farmersdelight.block.rice.invalid_placement", "This crop won't survive well outside shallow water.");
		this.add("farmersdelight.block.cutting_board.invalid_item", "This doesn't seem cuttable...");
		this.add("farmersdelight.block.cutting_board.invalid_tool", "Maybe with a different tool...");
		this.add("farmersdelight.block.skillet.invalid_item", "This can't be cooked...");
		this.add("farmersdelight.block.skillet.underwater", "Can't cook while flooded...");
		this.add("farmersdelight.item.skillet.how_to_cook", "Use your other hand to hold ingredients!");
		this.add("farmersdelight.item.skillet.underwater", "Can't cook underwater...");

		this.add("farmersdelight.jei.cooking", "Cooking");
		this.add("farmersdelight.jei.cutting", "Cooking");
		this.add("farmersdelight.jei.chance", "%1$s%% chance");
		this.add("farmersdelight.jei.decomposition", "Decomposition");
		this.add("farmersdelight.jei.decomposition.light", "Sped up by sunlight");
		this.add("farmersdelight.jei.decomposition.fluid", "Sped up by adjacent water");
		this.add("farmersdelight.jei.decomposition.accelerators", "Sped up by adjacent activators (see below)");
	}

	public void addEffect(Supplier<? extends MobEffect> key, String name, String description) {
		super.addEffect(key, name);
		this.add(key.get().getDescriptionId() + ".desc", description);
	}

	public void addEnchantment(Supplier<? extends Enchantment> key, String name, String description) {
		super.addEnchantment(key, name);
		this.add(key.get().getDescriptionId() + ".desc", description);
	}

	public void addBasicTooltip(RegistryObject<?> object, String name) {
		this.add("farmersdelight.tooltip." + object.getId().getPath(), name);
	}

	public void addSpecialTooltip(RegistryObject<?> object, String descriptor, String name) {
		this.add("farmersdelight.tooltip." + object.getId().getPath() + "." + descriptor, name);
	}

	public void addBlockWithJeiInfo(RegistryObject<? extends Block> block, String name, String info) {
		this.addBlock(block, name);
		this.add("farmersdelight.jei.info." + block.getId().getPath(), info);
	}

	public void addItemWithJeiInfo(RegistryObject<? extends Item> item, String name, String info) {
		this.addItem(item, name);
		this.add("farmersdelight.jei.info." + item.getId().getPath(), info);
	}

	public void addAdvancement(String key, String title, String desc) {
		this.add("farmersdelight.advancement." + key, title);
		this.add("farmersdelight.advancement." + key + ".desc", desc);
	}

	public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
		String[] splitSoundName = sound.getId().getPath().split("\\.", 3);
		this.add("farmersdelight.subtitles." + splitSoundName[1] + "." + splitSoundName[2], name);
	}

	public void addDeathMessage(String key, String name) {
		this.add("death.attack.farmersdelight." + key, name);
	}
}
