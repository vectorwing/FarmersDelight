package vectorwing.farmersdelight.items;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import vectorwing.farmersdelight.registry.ModEffects;

public class Foods
{
	// Raw Crops
	public static final Food CABBAGE = (new Food.Builder())
			.hunger(2).saturation(0.4f).build();
	public static final Food TOMATO = (new Food.Builder())
			.hunger(1).saturation(0.3f).build();
	public static final Food ONION = (new Food.Builder())
			.hunger(2).saturation(0.4f).build();

	// Drinks (mostly for effects)
	public static final Food APPLE_CIDER = (new Food.Builder())
			.hunger(0).saturation(0.0f).effect(() -> new EffectInstance(Effects.ABSORPTION, 1200, 0), 1.0F).build();

	// Basic Foods
	public static final Food FRIED_EGG = (new Food.Builder())
			.hunger(4).saturation(0.4f).build();
	public static final Food TOMATO_SAUCE = (new Food.Builder())
			.hunger(2).saturation(0.4f).build();
	public static final Food WHEAT_DOUGH = (new Food.Builder())
			.hunger(1).saturation(0.2f).build();
	public static final Food RAW_PASTA = (new Food.Builder())
			.hunger(3).saturation(0.4F).effect(() -> new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).build();
	public static final Food PIE_CRUST = (new Food.Builder())
			.hunger(2).saturation(0.2f).build();
	public static final Food PUMPKIN_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.3f).build();
	public static final Food CABBAGE_LEAF = (new Food.Builder())
			.hunger(1).saturation(0.4f).build();
	public static final Food MINCED_BEEF = (new Food.Builder())
			.hunger(2).saturation(0.3f).meat().build();
	public static final Food BEEF_PATTY = (new Food.Builder())
			.hunger(4).saturation(0.8f).meat().build();
	public static final Food CHICKEN_CUTS = (new Food.Builder())
			.hunger(1).saturation(0.3f).effect(() -> new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).meat().build();
	public static final Food COOKED_CHICKEN_CUTS = (new Food.Builder())
			.hunger(3).saturation(0.6f).meat().build();
	public static final Food BACON = (new Food.Builder())
			.hunger(2).saturation(0.3f).meat().build();
	public static final Food COOKED_BACON = (new Food.Builder())
			.hunger(4).saturation(0.8f).meat().build();
	public static final Food COD_SLICE = (new Food.Builder())
			.hunger(1).saturation(0.1f).build();
	public static final Food COOKED_COD_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.5f).build();
	public static final Food SALMON_SLICE = (new Food.Builder())
			.hunger(1).saturation(0.1f).build();
	public static final Food COOKED_SALMON_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.8f).build();
	public static final Food MUTTON_CHOP = (new Food.Builder())
			.hunger(1).saturation(0.3f).meat().build();
	public static final Food COOKED_MUTTON_CHOP = (new Food.Builder())
			.hunger(3).saturation(0.8f).meat().build();
	public static final Food HAM = (new Food.Builder())
			.hunger(5).saturation(0.3f).build();
	public static final Food SMOKED_HAM = (new Food.Builder())
			.hunger(10).saturation(0.8f).build();

	// Sweets
	public static final Food POPSICLE = (new Food.Builder())
			.hunger(3).saturation(0.2f).fastToEat().setAlwaysEdible()
			.effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 160, 0), 1.0F).build();
	public static final Food COOKIES = (new Food.Builder())
			.hunger(2).saturation(0.1f).fastToEat().build();
	public static final Food CAKE_SLICE = (new Food.Builder())
			.hunger(2).saturation(0.1f).fastToEat()
			.effect(() -> new EffectInstance(Effects.SPEED, 400, 0), 1.0F).build();
	public static final Food PIE_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.3f).fastToEat()
			.effect(() -> new EffectInstance(Effects.SPEED, 600, 0), 1.0F).build();
	public static final Food FRUIT_SALAD = (new Food.Builder())
			.hunger(6).saturation(0.6f)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F).build();

	// Handheld Foods
	public static final Food MIXED_SALAD = (new Food.Builder())
			.hunger(6).saturation(0.6f)
			.effect(() -> new EffectInstance(Effects.REGENERATION, 100, 0), 1.0F).build();
	public static final Food NETHER_SALAD = (new Food.Builder())
			.hunger(5).saturation(0.4f)
			.effect(() -> new EffectInstance(Effects.NAUSEA, 240, 0), 0.3F).build();
	public static final Food BARBECUE_STICK = (new Food.Builder())
			.hunger(8).saturation(0.9f).build();
	public static final Food EGG_SANDWICH = (new Food.Builder())
			.hunger(8).saturation(0.8f).build();
	public static final Food CHICKEN_SANDWICH = (new Food.Builder())
			.hunger(10).saturation(0.8f)
			.effect(() -> new EffectInstance(Effects.STRENGTH, 200, 0), 1.0F).build();
	public static final Food HAMBURGER = (new Food.Builder())
			.hunger(11).saturation(0.8f)
			.effect(() -> new EffectInstance(Effects.STRENGTH, 300, 0), 1.0F).build();
	public static final Food BACON_SANDWICH = (new Food.Builder())
			.hunger(10).saturation(0.8f)
			.effect(() -> new EffectInstance(Effects.RESISTANCE, 200, 0), 1.0F).build();
	public static final Food MUTTON_WRAP = (new Food.Builder())
			.hunger(11).saturation(0.8f)
			.effect(() -> new EffectInstance(Effects.RESISTANCE, 300, 0), 1.0F).build();
	public static final Food DUMPLINGS = (new Food.Builder())
			.hunger(8).saturation(0.8f).build();
	public static final Food STUFFED_POTATO = (new Food.Builder())
			.hunger(10).saturation(0.7f).build();
	public static final Food CABBAGE_ROLLS = (new Food.Builder())
			.hunger(6).saturation(0.5f).build();

	// Bowl Foods
	public static final Food COOKED_RICE = (new Food.Builder())
			.hunger(6).saturation(0.4f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 600, 0), 1.0F).build();
	public static final Food BEEF_STEW = (new Food.Builder())
			.hunger(10).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();
	public static final Food CHICKEN_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.8f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();
	public static final Food VEGETABLE_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.8f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();
	public static final Food FISH_STEW = (new Food.Builder())
			.hunger(10).saturation(0.8f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();
	public static final Food FRIED_RICE = (new Food.Builder())
			.hunger(10).saturation(0.8f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 2400, 0), 1.0F).build();
	public static final Food PUMPKIN_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();
	public static final Food BAKED_COD_STEW = (new Food.Builder())
			.hunger(10).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();
	public static final Food NOODLE_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.COMFORT.get(), 2400, 0), 1.0F).build();

	// Plated Foods
	public static final Food BACON_AND_EGGS = (new Food.Builder())
			.hunger(9).saturation(0.6f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 1200, 0), 1.0F).build();
	public static final Food STEAK_AND_POTATOES = (new Food.Builder())
			.hunger(12).saturation(0.8f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 2400, 0), 1.0F).build();
	public static final Food PASTA_WITH_MEATBALLS = (new Food.Builder())
			.hunger(12).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food PASTA_WITH_MUTTON_CHOP = (new Food.Builder())
			.hunger(12).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food ROASTED_MUTTON_CHOPS = (new Food.Builder())
			.hunger(12).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food VEGETABLE_NOODLES = (new Food.Builder())
			.hunger(14).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food RATATOUILLE = (new Food.Builder())
			.hunger(9).saturation(0.6f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 1200, 0), 1.0F).build();
	public static final Food SQUID_INK_PASTA = (new Food.Builder())
			.hunger(14).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food GRILLED_SALMON = (new Food.Builder())
			.hunger(12).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();

	// Feast Portions
	public static final Food ROAST_CHICKEN = (new Food.Builder())
			.hunger(14).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food STUFFED_PUMPKIN = (new Food.Builder())
			.hunger(14).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food HONEY_GLAZED_HAM = (new Food.Builder())
			.hunger(14).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food SHEPHERDS_PIE = (new Food.Builder())
			.hunger(14).saturation(0.9f)
			.effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();

	public static final Food DOG_FOOD = (new Food.Builder())
			.hunger(4).saturation(0.2f).meat().build();
}
