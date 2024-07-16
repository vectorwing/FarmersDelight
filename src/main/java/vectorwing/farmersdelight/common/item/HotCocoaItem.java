package vectorwing.farmersdelight.common.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

import java.util.ArrayList;
import java.util.Iterator;

public class HotCocoaItem extends DrinkableItem
{
	public HotCocoaItem(Item.Properties properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
		Iterator<MobEffectInstance> itr = consumer.getActiveEffects().iterator();
		ArrayList<Holder<MobEffect>> compatibleEffects = new ArrayList<>();

		while (itr.hasNext()) {
			MobEffectInstance effect = itr.next();
			if (effect.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL) && effect.getCures().contains(EffectCures.MILK)) {
				compatibleEffects.add(effect.getEffect());
			}
		}

		if (compatibleEffects.size() > 0) {
			MobEffectInstance selectedEffect = consumer.getEffect(compatibleEffects.get(level.random.nextInt(compatibleEffects.size())));
			if (selectedEffect != null && !net.neoforged.neoforge.event.EventHooks.onEffectRemoved(consumer, selectedEffect, EffectCures.MILK)) {
				consumer.removeEffect(selectedEffect.getEffect());
			}
		}
	}
}
