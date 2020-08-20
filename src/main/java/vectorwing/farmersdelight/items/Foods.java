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

	// Basic Foods
	public static final Food FRIED_EGG = (new Food.Builder())
			.hunger(4).saturation(0.4f).build();
	public static final Food TOMATO_SAUCE = (new Food.Builder())
			.hunger(2).saturation(0.4f).build();
	public static final Food CAKE_SLICE = (new Food.Builder())
			.hunger(2).saturation(0.1f).fastToEat().build();
	public static final Food COOKIES = (new Food.Builder())
			.hunger(2).saturation(0.1f).fastToEat().build();
	public static final Food PUMPKIN_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.3f).build();
	public static final Food MINCED_BEEF = (new Food.Builder())
			.hunger(2).saturation(0.3f).meat().build();
	public static final Food BEEF_PATTY = (new Food.Builder())
			.hunger(4).saturation(0.8f).meat().build();
	public static final Food CHICKEN_CUTS = (new Food.Builder())
			.hunger(1).saturation(0.3f).effect(() -> new EffectInstance(Effects.HUNGER, 600, 0), 0.3F).meat().build();
	public static final Food COOKED_CHICKEN_CUTS = (new Food.Builder())
			.hunger(3).saturation(0.6f).meat().build();
	public static final Food COD_SLICE = (new Food.Builder())
			.hunger(1).saturation(0.1f).build();
	public static final Food COOKED_COD_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.5f).build();
	public static final Food SALMON_SLICE = (new Food.Builder())
			.hunger(1).saturation(0.1f).build();
	public static final Food COOKED_SALMON_SLICE = (new Food.Builder())
			.hunger(3).saturation(0.8f).build();

	// Handheld Foods
	public static final Food MIXED_SALAD = (new Food.Builder())
			.hunger(6).saturation(0.6f).effect(() -> new EffectInstance(Effects.REGENERATION, 200, 0), 1.0F).build();
	public static final Food BARBECUE_STICK = (new Food.Builder())
			.hunger(7).saturation(0.7f).build();
	public static final Food EGG_SANDWICH = (new Food.Builder())
			.hunger(8).saturation(0.8f).build();
	public static final Food CHICKEN_SANDWICH = (new Food.Builder())
			.hunger(10).saturation(0.8f).build();
	public static final Food HAMBURGER = (new Food.Builder())
			.hunger(10).saturation(0.8f).build();
	public static final Food DUMPLINGS = (new Food.Builder())
			.hunger(8).saturation(0.8f).build();
	public static final Food STUFFED_POTATO = (new Food.Builder())
			.hunger(10).saturation(0.7f).build();
	public static final Food STUFFED_PUMPKIN = (new Food.Builder())
			.hunger(16).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 12000, 0), 1.0F).build();

	// Bowl Foods
	public static final Food BEEF_STEW = (new Food.Builder())
			.hunger(10).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food CHICKEN_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.8f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food VEGETABLE_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.8f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food FISH_STEW = (new Food.Builder())
			.hunger(10).saturation(0.8f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food FRIED_RICE = (new Food.Builder())
			.hunger(10).saturation(0.8f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food PUMPKIN_SOUP = (new Food.Builder())
			.hunger(10).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food STEAK_AND_POTATOES = (new Food.Builder())
			.hunger(10).saturation(0.8f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food HONEY_GLAZED_HAM = (new Food.Builder())
			.hunger(14).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 9600, 0), 1.0F).build();
	public static final Food PASTA_WITH_MEATBALLS = (new Food.Builder())
			.hunger(12).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 9600, 0), 1.0F).build();
	public static final Food PASTA_WITH_MUTTON_CHOP = (new Food.Builder())
			.hunger(12).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 9600, 0), 1.0F).build();
	public static final Food VEGETABLE_NOODLES = (new Food.Builder())
			.hunger(14).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 9600, 0), 1.0F).build();
	public static final Food SHEPHERDS_PIE = (new Food.Builder())
			.hunger(14).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 9600, 0), 1.0F).build();
	public static final Food RATATOUILLE = (new Food.Builder())
			.hunger(9).saturation(0.6f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 4800, 0), 1.0F).build();
	public static final Food SQUID_INK_PASTA = (new Food.Builder())
			.hunger(14).saturation(0.9f).effect(() -> new EffectInstance(ModEffects.NOURISHED.get(), 9600, 0), 1.0F).build();
}
