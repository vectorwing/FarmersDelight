package vectorwing.farmersdelight.items;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import vectorwing.farmersdelight.registry.ModEffects;

import java.util.Map;

public class Foods
{
	public static final int BRIEF_DURATION = 600;	// 30 seconds
	public static final int SHORT_DURATION = 1200;	// 1 minute
	public static final int MEDIUM_DURATION = 3600;	// 3 minutes
	public static final int LONG_DURATION = 6000;	// 5 minutes

	// Raw Crops
	public static final Food CABBAGE = (new Food.Builder())
			.nutrition(2).saturationMod(0.4f).build();
	public static final Food TOMATO = (new Food.Builder())
			.nutrition(1).saturationMod(0.3f).build();
	public static final Food ONION = (new Food.Builder())
			.nutrition(2).saturationMod(0.4f).build();

	// Drinks (mostly for effects)
	public static final Food APPLE_CIDER = (new Food.Builder())
			.nutrition(0).saturationMod(0.0f).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 0), 1.0F).build();

	// Basic Foods
	public static final Food FRIED_EGG = (new Food.Builder())
			.nutrition(4).saturationMod(0.4f).build();
	public static final Food TOMATO_SAUCE = (new Food.Builder())
			.nutrition(2).saturationMod(0.4f).build();
	public static final Food WHEAT_DOUGH = (new Food.Builder())
			.nutrition(1).saturationMod(0.2f).build();
	public static final Food RAW_PASTA = (new Food.Builder())
			.nutrition(3).saturationMod(0.4F).effect(() -> new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).build();
	public static final Food PIE_CRUST = (new Food.Builder())
			.nutrition(2).saturationMod(0.2f).build();
	public static final Food PUMPKIN_SLICE = (new Food.Builder())
			.nutrition(3).saturationMod(0.3f).build();
	public static final Food CABBAGE_LEAF = (new Food.Builder())
			.nutrition(1).saturationMod(0.4f).fast().build();
	public static final Food MINCED_BEEF = (new Food.Builder())
			.nutrition(2).saturationMod(0.3f).meat().fast().build();
	public static final Food BEEF_PATTY = (new Food.Builder())
			.nutrition(4).saturationMod(0.8f).meat().fast().build();
	public static final Food CHICKEN_CUTS = (new Food.Builder())
			.nutrition(1).saturationMod(0.3f).effect(() -> new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).meat().fast().build();
	public static final Food COOKED_CHICKEN_CUTS = (new Food.Builder())
			.nutrition(3).saturationMod(0.6f).meat().fast().build();
	public static final Food BACON = (new Food.Builder())
			.nutrition(2).saturationMod(0.3f).meat().fast().build();
	public static final Food COOKED_BACON = (new Food.Builder())
			.nutrition(4).saturationMod(0.8f).meat().fast().build();
	public static final Food COD_SLICE = (new Food.Builder())
			.nutrition(1).saturationMod(0.1f).fast().build();
	public static final Food COOKED_COD_SLICE = (new Food.Builder())
			.nutrition(3).saturationMod(0.5f).fast().build();
	public static final Food SALMON_SLICE = (new Food.Builder())
			.nutrition(1).saturationMod(0.1f).fast().build();
	public static final Food COOKED_SALMON_SLICE = (new Food.Builder())
			.nutrition(3).saturationMod(0.8f).fast().build();
	public static final Food MUTTON_CHOP = (new Food.Builder())
			.nutrition(1).saturationMod(0.3f).meat().fast().build();
	public static final Food COOKED_MUTTON_CHOP = (new Food.Builder())
			.nutrition(3).saturationMod(0.8f).meat().fast().build();
	public static final Food HAM = (new Food.Builder())
			.nutrition(5).saturationMod(0.3f).build();
	public static final Food SMOKED_HAM = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f).build();

	// Sweets
	public static final Food POPSICLE = (new Food.Builder())
			.nutrition(3).saturationMod(0.2f).fast().alwaysEat().build();
	public static final Food COOKIES = (new Food.Builder())
			.nutrition(2).saturationMod(0.1f).fast().build();
	public static final Food CAKE_SLICE = (new Food.Builder())
			.nutrition(2).saturationMod(0.1f).fast()
			.effect(() -> new EffectInstance(Effects.MOVEMENT_SPEED, 400, 0), 1.0F).build();
	public static final Food PIE_SLICE = (new Food.Builder())
			.nutrition(3).saturationMod(0.3f).fast()
			.effect(() -> new EffectInstance(Effects.MOVEMENT_SPEED, 600, 0), 1.0F).build();
	public static final Food FRUIT_SALAD = (new Food.Builder())
			.nutrition(6).saturationMod(0.6f)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F).build();

	// Handheld Foods
	public static final Food MIXED_SALAD = (new Food.Builder())
			.nutrition(6).saturationMod(0.6f)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F).build();
	public static final Food NETHER_SALAD = (new Food.Builder())
			.nutrition(5).saturationMod(0.4f)
			.effect(() -> new EffectInstance(Effects.CONFUSION, 240, 0), 0.3F).build();
	public static final Food BARBECUE_STICK = (new Food.Builder())
			.nutrition(8).saturationMod(0.9f).build();
	public static final Food EGG_SANDWICH = (new Food.Builder())
			.nutrition(8).saturationMod(0.8f).build();
	public static final Food CHICKEN_SANDWICH = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f).build();
	public static final Food HAMBURGER = (new Food.Builder())
			.nutrition(11).saturationMod(0.8f).build();
	public static final Food BACON_SANDWICH = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f).build();
	public static final Food MUTTON_WRAP = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f).build();
	public static final Food DUMPLINGS = (new Food.Builder())
			.nutrition(8).saturationMod(0.8f).build();
	public static final Food STUFFED_POTATO = (new Food.Builder())
			.nutrition(10).saturationMod(0.7f).build();
	public static final Food CABBAGE_ROLLS = (new Food.Builder())
			.nutrition(5).saturationMod(0.5f).build();

	// Bowl Foods
	public static final Food COOKED_RICE = (new Food.Builder())
			.nutrition(6).saturationMod(0.4f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), BRIEF_DURATION, 0), 1.0F).build();
	public static final Food BEEF_STEW = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final Food VEGETABLE_SOUP = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final Food FISH_STEW = (new Food.Builder())
			.nutrition(10).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final Food CHICKEN_SOUP = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food FRIED_RICE = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food PUMPKIN_SOUP = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food BAKED_COD_STEW = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food NOODLE_SOUP = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();

	// Plated Foods
	public static final Food BACON_AND_EGGS = (new Food.Builder())
			.nutrition(9).saturationMod(0.6f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), SHORT_DURATION, 0), 1.0F).build();
	public static final Food RATATOUILLE = (new Food.Builder())
			.nutrition(9).saturationMod(0.6f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), SHORT_DURATION, 0), 1.0F).build();
	public static final Food STEAK_AND_POTATOES = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final Food PASTA_WITH_MEATBALLS = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final Food PASTA_WITH_MUTTON_CHOP = (new Food.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final Food ROASTED_MUTTON_CHOPS = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food VEGETABLE_NOODLES = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food SQUID_INK_PASTA = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food GRILLED_SALMON = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), MEDIUM_DURATION, 0), 1.0F).build();

	// Feast Portions
	public static final Food ROAST_CHICKEN = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food STUFFED_PUMPKIN = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food HONEY_GLAZED_HAM = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();
	public static final Food SHEPHERDS_PIE = (new Food.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), LONG_DURATION, 0), 1.0F).build();

	public static final Food DOG_FOOD = (new Food.Builder())
			.nutrition(4).saturationMod(0.2f).meat().build();

	// Vanilla SoupItems
	public static final Map<Item, Food> VANILLA_SOUP_EFFECTS = (new ImmutableMap.Builder<Item, Food>())
			.put(Items.MUSHROOM_STEW, (new Food.Builder())
					.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build())
			.put(Items.BEETROOT_SOUP, (new Food.Builder())
					.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build())
			.put(Items.RABBIT_STEW, (new Food.Builder())
					.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build())
			.build();
}
