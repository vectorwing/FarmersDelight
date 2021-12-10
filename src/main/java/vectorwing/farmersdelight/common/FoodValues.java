package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Map;

public class FoodValues
{
	public static final int BURST_EFFECT = 200;
	public static final int BRIEF_DURATION = 600;
	public static final int SHORT_DURATION = 1200;
	public static final int MEDIUM_DURATION = 3600;
	public static final int LONG_DURATION = 6000;

	// Raw Vegetables
	public static final FoodProperties CABBAGE = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.4f).build();
	public static final FoodProperties TOMATO = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.3f).build();
	public static final FoodProperties ONION = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.4f).build();

	// Drinks (mostly for effects)
	public static final FoodProperties APPLE_CIDER = (new FoodProperties.Builder())
			.effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 1.0F).build();

	// Basic Foods
	public static final FoodProperties FRIED_EGG = (new FoodProperties.Builder())
			.nutrition(4).saturationMod(0.4f).build();
	public static final FoodProperties TOMATO_SAUCE = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.4f).build();
	public static final FoodProperties WHEAT_DOUGH = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.2f).build();
	public static final FoodProperties RAW_PASTA = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.4F).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).build();
	public static final FoodProperties PIE_CRUST = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.2f).build();
	public static final FoodProperties PUMPKIN_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.3f).build();
	public static final FoodProperties CABBAGE_LEAF = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.4f).fast().build();
	public static final FoodProperties MINCED_BEEF = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.3f).meat().fast().build();
	public static final FoodProperties BEEF_PATTY = (new FoodProperties.Builder())
			.nutrition(4).saturationMod(0.8f).meat().fast().build();
	public static final FoodProperties CHICKEN_CUTS = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.3f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).meat().fast().build();
	public static final FoodProperties COOKED_CHICKEN_CUTS = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.6f).meat().fast().build();
	public static final FoodProperties BACON = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.3f).meat().fast().build();
	public static final FoodProperties COOKED_BACON = (new FoodProperties.Builder())
			.nutrition(4).saturationMod(0.8f).meat().fast().build();
	public static final FoodProperties COD_SLICE = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.1f).fast().build();
	public static final FoodProperties COOKED_COD_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.5f).fast().build();
	public static final FoodProperties SALMON_SLICE = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.1f).fast().build();
	public static final FoodProperties COOKED_SALMON_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.8f).fast().build();
	public static final FoodProperties MUTTON_CHOP = (new FoodProperties.Builder())
			.nutrition(1).saturationMod(0.3f).meat().fast().build();
	public static final FoodProperties COOKED_MUTTON_CHOP = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.8f).meat().fast().build();
	public static final FoodProperties HAM = (new FoodProperties.Builder())
			.nutrition(5).saturationMod(0.3f).build();
	public static final FoodProperties SMOKED_HAM = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f).build();

	// Sweets
	public static final FoodProperties POPSICLE = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.2f).fast().alwaysEat().build();
	public static final FoodProperties COOKIES = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.1f).fast().build();
	public static final FoodProperties CAKE_SLICE = (new FoodProperties.Builder())
			.nutrition(2).saturationMod(0.1f).fast()
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0), 1.0F).build();
	public static final FoodProperties PIE_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationMod(0.3f).fast()
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0), 1.0F).build();
	public static final FoodProperties FRUIT_SALAD = (new FoodProperties.Builder())
			.nutrition(6).saturationMod(0.6f)
			.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F).build();

	// Handheld Foods
	public static final FoodProperties MIXED_SALAD = (new FoodProperties.Builder())
			.nutrition(6).saturationMod(0.6f)
			.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F).build();
	public static final FoodProperties NETHER_SALAD = (new FoodProperties.Builder())
			.nutrition(5).saturationMod(0.4f)
			.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 240, 0), 0.3F).build();
	public static final FoodProperties BARBECUE_STICK = (new FoodProperties.Builder())
			.nutrition(8).saturationMod(0.9f).build();
	public static final FoodProperties EGG_SANDWICH = (new FoodProperties.Builder())
			.nutrition(8).saturationMod(0.8f).build();
	public static final FoodProperties CHICKEN_SANDWICH = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, BURST_EFFECT, 0), 1.0F).build();
	public static final FoodProperties HAMBURGER = (new FoodProperties.Builder())
			.nutrition(11).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, BURST_EFFECT, 0), 1.0F).build();
	public static final FoodProperties BACON_SANDWICH = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BURST_EFFECT, 0), 1.0F).build();
	public static final FoodProperties MUTTON_WRAP = (new FoodProperties.Builder())
			.nutrition(11).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, BURST_EFFECT, 0), 1.0F).build();
	public static final FoodProperties DUMPLINGS = (new FoodProperties.Builder())
			.nutrition(8).saturationMod(0.8f).build();
	public static final FoodProperties STUFFED_POTATO = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.7f).build();
	public static final FoodProperties CABBAGE_ROLLS = (new FoodProperties.Builder())
			.nutrition(5).saturationMod(0.5f).build();

	// Bowl Foods
	public static final FoodProperties COOKED_RICE = (new FoodProperties.Builder())
			.nutrition(6).saturationMod(0.4f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), BRIEF_DURATION, 0), 1.0F).build();
	public static final FoodProperties BEEF_STEW = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final FoodProperties CHICKEN_SOUP = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final FoodProperties VEGETABLE_SOUP = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties FISH_STEW = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final FoodProperties FRIED_RICE = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final FoodProperties PUMPKIN_SOUP = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final FoodProperties BAKED_COD_STEW = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties NOODLE_SOUP = (new FoodProperties.Builder())
			.nutrition(10).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build();

	// Plated Foods
	public static final FoodProperties BACON_AND_EGGS = (new FoodProperties.Builder())
			.nutrition(9).saturationMod(0.6f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), SHORT_DURATION, 0), 1.0F).build();
	public static final FoodProperties STEAK_AND_POTATOES = (new FoodProperties.Builder())
			.nutrition(12).saturationMod(0.8f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), MEDIUM_DURATION, 0), 1.0F).build();
	public static final FoodProperties PASTA_WITH_MEATBALLS = (new FoodProperties.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties PASTA_WITH_MUTTON_CHOP = (new FoodProperties.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties ROASTED_MUTTON_CHOPS = (new FoodProperties.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties VEGETABLE_NOODLES = (new FoodProperties.Builder())
			.nutrition(14).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties RATATOUILLE = (new FoodProperties.Builder())
			.nutrition(9).saturationMod(0.6f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), SHORT_DURATION, 0), 1.0F).build();
	public static final FoodProperties SQUID_INK_PASTA = (new FoodProperties.Builder())
			.nutrition(14).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties GRILLED_SALMON = (new FoodProperties.Builder())
			.nutrition(12).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();

	// Feast Portions
	public static final FoodProperties ROAST_CHICKEN = (new FoodProperties.Builder())
			.nutrition(14).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties STUFFED_PUMPKIN = (new FoodProperties.Builder())
			.nutrition(14).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties HONEY_GLAZED_HAM = (new FoodProperties.Builder())
			.nutrition(14).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();
	public static final FoodProperties SHEPHERDS_PIE = (new FoodProperties.Builder())
			.nutrition(14).saturationMod(0.9f)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), LONG_DURATION, 0), 1.0F).build();

	// Pet Foods
	public static final FoodProperties DOG_FOOD = (new FoodProperties.Builder())
			.nutrition(4).saturationMod(0.2f).meat().build();

	// Vanilla SoupItems
	public static final Map<Item, FoodProperties> VANILLA_SOUP_EFFECTS = (new ImmutableMap.Builder<Item, FoodProperties>())
			.put(Items.MUSHROOM_STEW, (new FoodProperties.Builder())
					.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build())
			.put(Items.BEETROOT_SOUP, (new FoodProperties.Builder())
					.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), MEDIUM_DURATION, 0), 1.0F).build())
			.put(Items.RABBIT_STEW, (new FoodProperties.Builder())
					.effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), LONG_DURATION, 0), 1.0F).build())
			.build();
}
