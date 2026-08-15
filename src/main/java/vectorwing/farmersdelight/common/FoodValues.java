package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class FoodValues
{
	public record FoodEffect(MobEffectInstance effect, float probability) {}

	public static final int BRIEF_DURATION = 600;    // 30 seconds
	public static final int SHORT_DURATION = 1200;    // 1 minute
	public static final int MEDIUM_DURATION = 3600;    // 3 minutes
	public static final int LONG_DURATION = 6000;    // 5 minutes

	public static MobEffectInstance nourishment(int duration) {
		return new MobEffectInstance(ModEffects.NOURISHMENT, duration, 0, false, false);
	}

	// Raw Crops
	public static final FoodProperties CABBAGE = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.4f).build();
	public static final FoodProperties TOMATO = (new FoodProperties.Builder())
			.nutrition(1).saturationModifier(0.3f).build();
	public static final FoodProperties ONION = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.4f).build();

	// Drinks (mostly for effects)
	public static final FoodProperties APPLE_CIDER = (new FoodProperties.Builder())
			.alwaysEdible().build();

	// Basic Foods
	public static final FoodProperties FRIED_EGG = (new FoodProperties.Builder())
			.nutrition(4).saturationModifier(0.4f).build();
	public static final FoodProperties TOMATO_SAUCE = (new FoodProperties.Builder())
			.nutrition(4).saturationModifier(0.4f).build();
	public static final FoodProperties WHEAT_DOUGH = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.3f).build();
	public static final FoodProperties RAW_PASTA = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.3F).build();
	public static final FoodProperties PIE_CRUST = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.2f).build();
	public static final FoodProperties PUMPKIN_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.3f).build();
	public static final FoodProperties CABBAGE_LEAF = (new FoodProperties.Builder())
			.nutrition(1).saturationModifier(0.4f).build();
	public static final FoodProperties MINCED_BEEF = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.3f).build();
	public static final FoodProperties BEEF_PATTY = (new FoodProperties.Builder())
			.nutrition(4).saturationModifier(0.8f).build();
	public static final FoodProperties CHICKEN_CUTS = (new FoodProperties.Builder())
			.nutrition(1).saturationModifier(0.3f).build();
	public static final FoodProperties COOKED_CHICKEN_CUTS = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.6f).build();
	public static final FoodProperties BACON = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.3f).build();
	public static final FoodProperties COOKED_BACON = (new FoodProperties.Builder())
			.nutrition(4).saturationModifier(0.8f).build();
	public static final FoodProperties COD_SLICE = (new FoodProperties.Builder())
			.nutrition(1).saturationModifier(0.1f).build();
	public static final FoodProperties COOKED_COD_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.5f).build();
	public static final FoodProperties SALMON_SLICE = (new FoodProperties.Builder())
			.nutrition(1).saturationModifier(0.1f).build();
	public static final FoodProperties COOKED_SALMON_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.8f).build();
	public static final FoodProperties MUTTON_CHOPS = (new FoodProperties.Builder())
			.nutrition(1).saturationModifier(0.3f).build();
	public static final FoodProperties COOKED_MUTTON_CHOPS = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.8f).build();
	public static final FoodProperties HAM = (new FoodProperties.Builder())
			.nutrition(5).saturationModifier(0.3f).build();
	public static final FoodProperties SMOKED_HAM = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.8f).build();

	// Sweets
	public static final FoodProperties POPSICLE = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.2f).alwaysEdible().build();
	public static final FoodProperties COOKIES = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.1f).build();
	public static final FoodProperties CAKE_SLICE = (new FoodProperties.Builder())
			.nutrition(2).saturationModifier(0.1f)
			.build();
	public static final FoodProperties PIE_SLICE = (new FoodProperties.Builder())
			.nutrition(3).saturationModifier(0.3f)
			.build();
	public static final FoodProperties FRUIT_SALAD = (new FoodProperties.Builder())
			.nutrition(6).saturationModifier(0.6f)
			.build();
	public static final FoodProperties GLOW_BERRY_CUSTARD = (new FoodProperties.Builder())
			.nutrition(7).saturationModifier(0.6f).alwaysEdible()
			.build();

	// Handheld Foods
	public static final FoodProperties MIXED_SALAD = (new FoodProperties.Builder())
			.nutrition(6).saturationModifier(0.6f)
			.build();
	public static final FoodProperties NETHER_SALAD = (new FoodProperties.Builder())
			.nutrition(5).saturationModifier(0.4f)
			.build();
	public static final FoodProperties BARBECUE_STICK = (new FoodProperties.Builder())
			.nutrition(8).saturationModifier(0.9f).build();
	public static final FoodProperties EGG_SANDWICH = (new FoodProperties.Builder())
			.nutrition(8).saturationModifier(0.8f).build();
	public static final FoodProperties CHICKEN_SANDWICH = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.8f).build();
	public static final FoodProperties HAMBURGER = (new FoodProperties.Builder())
			.nutrition(11).saturationModifier(0.8f).build();
	public static final FoodProperties BACON_SANDWICH = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.8f).build();
	public static final FoodProperties MUTTON_WRAP = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.8f).build();
	public static final FoodProperties DUMPLINGS = (new FoodProperties.Builder())
			.nutrition(8).saturationModifier(0.8f).build();
	public static final FoodProperties STUFFED_POTATO = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.7f).build();
	public static final FoodProperties CABBAGE_ROLLS = (new FoodProperties.Builder())
			.nutrition(5).saturationModifier(0.5f).build();
	public static final FoodProperties SALMON_ROLL = (new FoodProperties.Builder())
			.nutrition(7).saturationModifier(0.6f).build();
	public static final FoodProperties COD_ROLL = (new FoodProperties.Builder())
			.nutrition(7).saturationModifier(0.6f).build();
	public static final FoodProperties KELP_ROLL = new FoodProperties(12, 2.4f, false);
	public static final FoodProperties KELP_ROLL_SLICE = (new FoodProperties.Builder())
			.nutrition(6).saturationModifier(0.5f).build();

	// Bowl Foods
	public static final FoodProperties COOKED_RICE = (new FoodProperties.Builder())
			.nutrition(6).saturationModifier(0.4f)
			.build();
	public static final FoodProperties BONE_BROTH = (new FoodProperties.Builder())
			.nutrition(8).saturationModifier(0.7f)
			.build();
	public static final FoodProperties BEEF_STEW = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties VEGETABLE_SOUP = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties FISH_STEW = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties ONION_SOUP = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties CHICKEN_SOUP = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties FRIED_RICE = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties PUMPKIN_SOUP = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties BAKED_COD_STEW = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties NOODLE_SOUP = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();

	// Plated Foods
	public static final FoodProperties BACON_AND_EGGS = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.6f)
			.build();
	public static final FoodProperties RATATOUILLE = (new FoodProperties.Builder())
			.nutrition(10).saturationModifier(0.6f)
			.build();
	public static final FoodProperties STEAK_AND_POTATOES = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties PASTA_WITH_MEATBALLS = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties PASTA_WITH_MUTTON_CHOP = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties MUSHROOM_RICE = (new FoodProperties.Builder())
			.nutrition(12).saturationModifier(0.8f)
			.build();
	public static final FoodProperties ROASTED_MUTTON_CHOPS = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties VEGETABLE_NOODLES = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties SQUID_INK_PASTA = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties GRILLED_SALMON = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();

	// Feast Portions
	public static final FoodProperties ROAST_CHICKEN = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties STUFFED_PUMPKIN = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties HONEY_GLAZED_HAM = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties SHEPHERDS_PIE = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();
	public static final FoodProperties GLEAMING_SALAD = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f)
			.build();

	public static final FoodProperties DOG_FOOD = (new FoodProperties.Builder())
			.nutrition(4).saturationModifier(0.2f).build();

	// Vanilla SoupItems
	public static final Map<Item, MobEffectInstance> VANILLA_SOUP_EFFECTS = (new ImmutableMap.Builder<Item, MobEffectInstance>())
			.put(Items.MUSHROOM_STEW, nourishment(MEDIUM_DURATION))
			.put(Items.BEETROOT_SOUP, nourishment(MEDIUM_DURATION))
			.put(Items.RABBIT_STEW, nourishment(LONG_DURATION))
			.build();

	public static final FoodProperties RABBIT_STEW_BUFF = (new FoodProperties.Builder())
			.nutrition(14).saturationModifier(0.75f).build();

	private static final Map<FoodProperties, List<FoodEffect>> FOOD_EFFECTS = createFoodEffects();

	public static List<FoodEffect> effects(FoodProperties food) {
		return FOOD_EFFECTS.getOrDefault(food, List.of());
	}

	public static Consumable consumable(FoodProperties food) {
		return addEffects(Consumables.defaultFood(), food).build();
	}

	public static Consumable drinkConsumable(FoodProperties food) {
		return addEffects(Consumables.defaultDrink(), food).build();
	}

	private static Consumable.Builder addEffects(Consumable.Builder builder, FoodProperties food) {
		for (FoodEffect effect : effects(food)) {
			builder.onConsume(new ApplyStatusEffectsConsumeEffect(effect.effect(), effect.probability()));
		}
		return builder;
	}

	private static Map<FoodProperties, List<FoodEffect>> createFoodEffects() {
		IdentityHashMap<FoodProperties, List<FoodEffect>> effects = new IdentityHashMap<>();
		effects.put(APPLE_CIDER, effect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0)));
		effects.put(WHEAT_DOUGH, effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F));
		effects.put(RAW_PASTA, effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F));
		effects.put(CHICKEN_CUTS, effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F));
		effects.put(CAKE_SLICE, effect(new MobEffectInstance(MobEffects.SPEED, 400, 0, false, false)));
		effects.put(PIE_SLICE, effect(new MobEffectInstance(MobEffects.SPEED, 600, 0, false, false)));
		effects.put(FRUIT_SALAD, effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)));
		effects.put(GLOW_BERRY_CUSTARD, effect(new MobEffectInstance(MobEffects.GLOWING, 100, 0)));
		effects.put(MIXED_SALAD, effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)));
		effects.put(NETHER_SALAD, effect(new MobEffectInstance(MobEffects.NAUSEA, 240, 0), 0.3F));
		effects.put(COOKED_RICE, effect(nourishment(BRIEF_DURATION)));
		effects.put(BONE_BROTH, effect(nourishment(SHORT_DURATION)));
		effects.put(BEEF_STEW, effect(nourishment(MEDIUM_DURATION)));
		effects.put(VEGETABLE_SOUP, effect(nourishment(MEDIUM_DURATION)));
		effects.put(FISH_STEW, effect(nourishment(MEDIUM_DURATION)));
		effects.put(CHICKEN_SOUP, effect(nourishment(LONG_DURATION)));
		effects.put(FRIED_RICE, effect(nourishment(LONG_DURATION)));
		effects.put(PUMPKIN_SOUP, effect(nourishment(LONG_DURATION)));
		effects.put(BAKED_COD_STEW, effect(nourishment(LONG_DURATION)));
		effects.put(NOODLE_SOUP, effect(nourishment(LONG_DURATION)));
		effects.put(BACON_AND_EGGS, effect(nourishment(SHORT_DURATION)));
		effects.put(RATATOUILLE, effect(nourishment(SHORT_DURATION)));
		effects.put(STEAK_AND_POTATOES, effect(nourishment(MEDIUM_DURATION)));
		effects.put(PASTA_WITH_MEATBALLS, effect(nourishment(MEDIUM_DURATION)));
		effects.put(PASTA_WITH_MUTTON_CHOP, effect(nourishment(MEDIUM_DURATION)));
		effects.put(MUSHROOM_RICE, effect(nourishment(MEDIUM_DURATION)));
		effects.put(ROASTED_MUTTON_CHOPS, effect(nourishment(LONG_DURATION)));
		effects.put(VEGETABLE_NOODLES, effect(nourishment(LONG_DURATION)));
		effects.put(SQUID_INK_PASTA, effect(nourishment(LONG_DURATION)));
		effects.put(GRILLED_SALMON, effect(nourishment(MEDIUM_DURATION)));
		effects.put(ROAST_CHICKEN, effect(nourishment(LONG_DURATION)));
		effects.put(STUFFED_PUMPKIN, effect(nourishment(LONG_DURATION)));
		effects.put(HONEY_GLAZED_HAM, effect(nourishment(LONG_DURATION)));
		effects.put(SHEPHERDS_PIE, effect(nourishment(LONG_DURATION)));
		effects.put(GLEAMING_SALAD, effect(nourishment(LONG_DURATION)));
		effects.put(RABBIT_STEW_BUFF, effect(nourishment(LONG_DURATION)));
		return Collections.unmodifiableMap(effects);
	}

	private static List<FoodEffect> effect(MobEffectInstance effect) {
		return effect(effect, 1.0F);
	}

	private static List<FoodEffect> effect(MobEffectInstance effect, float probability) {
		return List.of(new FoodEffect(effect, probability));
	}
}
