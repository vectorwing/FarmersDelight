package vectorwing.farmersdelight.common.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEvents
{
	@SubscribeEvent
	public static void handleVanillaSoupEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntity();

		if (Configuration.RABBIT_STEW_JUMP_BOOST.get() && food.equals(Items.RABBIT_STEW)) {
			entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 1));
		}

		if (Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			FoodProperties soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

			if (soupEffects != null) {
				for (FoodProperties.PossibleEffect effect : soupEffects.effects()) {
					entity.addEffect(effect.effect());
				}
			}
		}
	}

//	@SubscribeEvent
//	public static void onAnimalsJoinWorld(EntityJoinLevelEvent event) {
//		if (event.getEntity() instanceof PathfinderMob mob) {
//			if (mob.getType().is(ModTags.HORSE_FEED_TEMPTED)) {
//				int priority = getTemptGoalPriority(mob);
//				if (priority >= 0)
//					mob.goalSelector.addGoal(priority, new TemptGoal(mob, 1.25D, Ingredient.of(ModItems.HORSE_FEED.get()), false));
//			}
//		}
//	}
//
//	public static int getTemptGoalPriority(Mob mob) {
//		return mob.goalSelector.getAvailableGoals().stream()
//				.filter(goal -> goal.getGoal() instanceof TemptGoal)
//				.findFirst()
//				.map(WrappedGoal::getPriority)
//				.orElse(-1);
//	}
}
