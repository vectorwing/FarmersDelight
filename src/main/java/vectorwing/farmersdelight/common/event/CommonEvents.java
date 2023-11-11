package vectorwing.farmersdelight.common.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
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
				for (Pair<MobEffectInstance, Float> pair : soupEffects.getEffects()) {
					entity.addEffect(pair.getFirst());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onAnimalsJoinWorld(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof PathfinderMob mob) {
			if (mob.getType().is(ModTags.HORSE_FEED_TEMPTED)) {
				int priority = getTemptGoalPriority(mob);
				if (priority >= 0)
					mob.goalSelector.addGoal(priority, new TemptGoal(mob, 1.25D, Ingredient.of(ModItems.HORSE_FEED.get()), false));
			}
			if (mob instanceof Rabbit rabbit) {
				int priority = getTemptGoalPriority(rabbit);
				if (priority >= 0)
					rabbit.goalSelector.addGoal(priority, new TemptGoal(rabbit, 1.0D, Ingredient.of(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get()), false));
			}
		}
	}

	public static int getTemptGoalPriority(Mob mob) {
		return mob.goalSelector.getAvailableGoals().stream()
				.filter(goal -> goal.getGoal() instanceof TemptGoal)
				.findFirst()
				.map(WrappedGoal::getPriority)
				.orElse(-1);
	}
}
