package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Map;
import java.util.Optional;

public class FoodValues
{
	public static final int BRIEF_DURATION = 600;    // 30 seconds
	public static final int SHORT_DURATION = 1200;    // 1 minute
	public static final int MEDIUM_DURATION = 3600;    // 3 minutes
	public static final int LONG_DURATION = 6000;    // 5 minutes

	// Raw Crops
	public static final FoodProperties CABBAGE = food().nutrition(4).saturation(2).build();
	public static final FoodProperties TOMATO = food().nutrition(1).saturation(1).build();
	public static final FoodProperties ONION = food().nutrition(2).saturation(1).build();

	// Drinks (mostly for effects)
	public static final FoodProperties MELON_JUICE =
		food().nutrition(1).saturation(4).alwaysEdible().build();
	public static final FoodProperties HOT_COCOA =
		food().nutrition(3).saturation(6).alwaysEdible().build();
	public static final FoodProperties APPLE_CIDER =
		food().nutrition(1).saturation(4).alwaysEdible()
			.effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, MEDIUM_DURATION, 0), 1.0F).build();

	// Basic Foods
	public static final FoodProperties FRIED_EGG =
		food().nutrition(4).saturation(3.5F).build();
	public static final FoodProperties TOMATO_SAUCE =
		food().nutrition(4).saturation(3.5F).usingConvertsTo(Items.BOWL).build();
	public static final FoodProperties WHEAT_DOUGH =
		unsafeFood().nutrition(2).saturation(1.5F).build();
	public static final FoodProperties RAW_PASTA =
		unsafeFood().nutrition(2).saturation(1.5F).build();
	public static final FoodProperties PIE_CRUST =
		food().nutrition(2).saturation(1).build();
	public static final FoodProperties PUMPKIN_SLICE =
		food().nutrition(3).saturation(2).build();
	public static final FoodProperties CABBAGE_LEAF =
		food().nutrition(2).saturation(1.0F).fast().build();

	// Meat cuts (modifiers based on vanilla items)
	public static final FoodProperties MINCED_BEEF =
		food().nutrition(2).saturationModifier(0.3f).fast().build();
	public static final FoodProperties BEEF_PATTY =
		food().nutrition(4).saturationModifier(0.8f).fast().build();
	public static final FoodProperties CHICKEN_CUTS =
		unsafeFood().nutrition(1).saturationModifier(0.15F).fast().build();
	public static final FoodProperties COOKED_CHICKEN_CUTS =
		food().nutrition(3).saturationModifier(0.6f).fast().build();
	public static final FoodProperties BACON =
		food().nutrition(2).saturationModifier(0.3f).fast().build();
	public static final FoodProperties COOKED_BACON =
		food().nutrition(4).saturationModifier(0.8f).fast().build();
	public static final FoodProperties COD_SLICE =
		food().nutrition(1).saturationModifier(0.1f).fast().build();
	public static final FoodProperties COOKED_COD_SLICE =
		food().nutrition(3).saturationModifier(0.5f).fast().build();
	public static final FoodProperties SALMON_SLICE =
		food().nutrition(1).saturationModifier(0.1f).fast().build();
	public static final FoodProperties COOKED_SALMON_SLICE =
		food().nutrition(3).saturationModifier(0.8f).fast().build();
	public static final FoodProperties MUTTON_CHOPS =
		food().nutrition(1).saturationModifier(0.3f).fast().build();
	public static final FoodProperties COOKED_MUTTON_CHOPS =
		food().nutrition(3).saturationModifier(0.8f).fast().build();
	public static final FoodProperties HAM =
		food().nutrition(5).saturationModifier(0.3f).build();
	public static final FoodProperties SMOKED_HAM =
		food().nutrition(10).saturationModifier(0.8f).build();

	// Sweets
	public static final FoodProperties POPSICLE =
		food().nutrition(3).saturation(1.5F).fast().alwaysEdible().build();
	public static final FoodProperties COOKIES =
		food().nutrition(2).saturationModifier(0.1f).alwaysEdible().fast().build();
	public static final FoodProperties CAKE_SLICE =
		food().nutrition(3).saturation(3).alwaysEdible().fast()
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0, false, false), 1.0F).build();
	public static final FoodProperties PIE_SLICE =
		food().nutrition(5).saturation(5).alwaysEdible().fast()
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false), 1.0F).build();
	public static final FoodProperties PUMPKIN_PIE_SLICE =
		food().nutrition(3).saturation(2).alwaysEdible().fast()
			.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false), 1.0F).build();
	public static final FoodProperties FRUIT_SALAD =
		food().nutrition(8).saturation(10).usingConvertsTo(Items.BOWL).build();
	public static final FoodProperties GLOW_BERRY_CUSTARD =
		food().nutrition(8).saturation(9).alwaysEdible().usingConvertsTo(Items.GLASS_BOTTLE)
			.effect(() -> new MobEffectInstance(MobEffects.GLOWING, 100, 0), 1.0F).build();

	// Snacks
	public static final FoodProperties RAW_SKEWER =
		food().nutrition(4).saturationModifier(0.4f).build();
	public static final FoodProperties COOKED_SKEWER =
		food().nutrition(8).saturationModifier(0.9f).build();
	public static final FoodProperties EGG_SANDWICH =
		food().nutrition(8).saturationModifier(0.8f).build();
	public static final FoodProperties CHICKEN_SANDWICH =
		food().nutrition(10).saturationModifier(0.8f).build();
	public static final FoodProperties HAMBURGER =
		food().nutrition(11).saturationModifier(0.8f).build();
	public static final FoodProperties BACON_SANDWICH =
		food().nutrition(10).saturationModifier(0.8f).build();
	public static final FoodProperties MUTTON_WRAP =
		food().nutrition(10).saturationModifier(0.8f).build();
	public static final FoodProperties DUMPLINGS =
		food().nutrition(8).saturationModifier(0.8f).build();
	public static final FoodProperties STUFFED_POTATO =
		food().nutrition(10).saturationModifier(0.7f).build();
	public static final FoodProperties CABBAGE_ROLLS =
		food().nutrition(5).saturationModifier(0.5f).build();
	public static final FoodProperties SALMON_ROLL =
		food().nutrition(7).saturationModifier(0.6f).build();
	public static final FoodProperties COD_ROLL =
		food().nutrition(7).saturationModifier(0.6f).build();
	public static final FoodProperties KELP_ROLL =
		food().nutrition(12).saturation(12).eatSeconds(2.4F).build();
	public static final FoodProperties KELP_ROLL_SLICE =
		food().nutrition(6).saturation(6).fast().build();

	// Salads
	public static final FoodProperties MIXED_SALAD =
		food().nutrition(8).saturation(10).usingConvertsTo(Items.BOWL).build();
	public static final FoodProperties NETHER_SALAD =
		food().nutrition(5).saturationModifier(0.4f).usingConvertsTo(Items.BOWL)
			.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 240, 0), 0.3F).build();

	// Crude Meals (tier 1)
	public static final FoodProperties COOKED_RICE = meal(BRIEF_DURATION).nutrition(6).saturation(5).build();
	public static final FoodProperties BONE_BROTH = meal(SHORT_DURATION).nutrition(8).saturation(10).build();
	public static final FoodProperties BACON_AND_EGGS = crudeMeal().build();
	public static final FoodProperties RATATOUILLE = crudeMeal().build();

	// Hearty Meals (tier 2)
	public static final FoodProperties BEEF_STEW = heartyMeal().build();
	public static final FoodProperties VEGETABLE_SOUP = heartyMeal().build();
	public static final FoodProperties FISH_STEW = heartyMeal().build();
	public static final FoodProperties ONION_SOUP = heartyMeal().build();
	public static final FoodProperties CHICKEN_SOUP = heartyMeal().build();
	public static final FoodProperties FRIED_RICE = heartyMeal().build();
	public static final FoodProperties STEAK_AND_POTATOES = heartyMeal().build();
	public static final FoodProperties PASTA_WITH_MEATBALLS = heartyMeal().build();
	public static final FoodProperties PASTA_WITH_MUTTON_CHOP = heartyMeal().build();
	public static final FoodProperties MUSHROOM_RICE = heartyMeal().build();
	public static final FoodProperties GRILLED_SALMON = heartyMeal().build();

	// Fancy Foods (tier 3)
	public static final FoodProperties PUMPKIN_SOUP = fancyMeal().build();
	public static final FoodProperties BAKED_COD_STEW = fancyMeal().build();
	public static final FoodProperties NOODLE_SOUP = fancyMeal().build();
	public static final FoodProperties ROASTED_MUTTON_CHOPS = fancyMeal().build();
	public static final FoodProperties VEGETABLE_NOODLES = fancyMeal().build();
	public static final FoodProperties SQUID_INK_PASTA = fancyMeal().build();

	// Feast Portions (tier 3)
	public static final FoodProperties ROAST_CHICKEN = fancyMeal().build();
	public static final FoodProperties STUFFED_PUMPKIN = fancyMeal().build();
	public static final FoodProperties HONEY_GLAZED_HAM = fancyMeal().build();
	public static final FoodProperties SHEPHERDS_PIE = fancyMeal().build();
	public static final FoodProperties GLEAMING_SALAD = fancyMeal().build();

	public static final FoodProperties DOG_FOOD = food().nutrition(4).saturation(2).usingConvertsTo(Items.BOWL).build();

	// Vanilla SoupItems
	public static final Map<Item, FoodProperties> VANILLA_SOUP_EFFECTS = (new ImmutableMap.Builder<Item, FoodProperties>())
		.put(Items.MUSHROOM_STEW, crudeMeal().build())
		.put(Items.BEETROOT_SOUP, crudeMeal().build())
		.put(Items.RABBIT_STEW, fancyMeal().build())
		.build();

	public static final FoodProperties RABBIT_STEW_BUFF = fancyMeal().build();

	public static FoodBuilder food() {
		return new FoodBuilder();
	}

	public static FoodBuilder unsafeFood() {
		return food().effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F);
	}

	public static FoodBuilder meal(int nourishmentDuration) {
		return food().usingConvertsTo(Items.BOWL)
			.effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT, nourishmentDuration, 0, false, false), 1.0F);
	}

	public static FoodBuilder crudeMeal() {
		return meal(SHORT_DURATION).nutrition(10).saturation(12);
	}

	public static FoodBuilder heartyMeal() {
		return meal(MEDIUM_DURATION).nutrition(12).saturation(16);
	}

	public static FoodBuilder fancyMeal() {
		return meal(LONG_DURATION).nutrition(14).saturation(20);
	}

	public static MobEffectInstance nourishment(int duration) {
		return new MobEffectInstance(ModEffects.NOURISHMENT, duration, 0, false, false);
	}

	/**
	 * Variant of {@link FoodProperties.Builder} which lets you directly define the saturation value.
	 */
	public static class FoodBuilder
	{
		private int nutrition;
		private float saturation;
		private boolean canAlwaysEat;
		private float eatSeconds = 1.6F;
		private ItemStack usingConvertsTo = ItemStack.EMPTY;
		private final ImmutableList.Builder<FoodProperties.PossibleEffect> effects = ImmutableList.builder();

		public FoodBuilder nutrition(int nutrition) {
			this.nutrition = nutrition;
			return this;
		}

		/**
		 * Defines the saturation value directly.
		 */
		public FoodBuilder saturation(float saturation) {
			this.saturation = saturation;
			return this;
		}

		/**
		 * Calculates saturation by multiplying the nutrition value (nutrition * modifier * 2).
		 */
		public FoodBuilder saturationModifier(float saturationModifier) {
			this.saturation = FoodConstants.saturationByModifier(this.nutrition, saturationModifier);
			return this;
		}

		public FoodBuilder alwaysEdible() {
			this.canAlwaysEat = true;
			return this;
		}

		public FoodBuilder eatSeconds(float eatSeconds) {
			this.eatSeconds = eatSeconds;
			return this;
		}

		public FoodBuilder fast() {
			this.eatSeconds = 0.8F;
			return this;
		}

		public FoodBuilder effect(java.util.function.Supplier<MobEffectInstance> effect, float probability) {
			this.effects.add(new FoodProperties.PossibleEffect(effect, probability));
			return this;
		}

		public FoodBuilder usingConvertsTo(ItemLike item) {
			this.usingConvertsTo = new ItemStack(item);
			return this;
		}

		public FoodProperties build() {
			return new FoodProperties(this.nutrition, this.saturation, this.canAlwaysEat, this.eatSeconds, usingConvertsTo.isEmpty() ? Optional.empty() : Optional.of(usingConvertsTo), this.effects.build());
		}
	}
}
