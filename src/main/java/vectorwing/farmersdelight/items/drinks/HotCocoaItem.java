package vectorwing.farmersdelight.items.drinks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class HotCocoaItem extends DrinkItem
{
	public HotCocoaItem(Item.Properties properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, World worldIn, LivingEntity consumer) {
		Iterator<EffectInstance> itr = consumer.getActiveEffects().iterator();
		ArrayList<Effect> compatibleEffects = new ArrayList<>();

		while (itr.hasNext()) {
			EffectInstance effect = itr.next();
			if (effect.getEffect().getCategory().equals(EffectType.HARMFUL) && effect.isCurativeItem(new ItemStack(Items.MILK_BUCKET))) {
				compatibleEffects.add(effect.getEffect());
			}
		}

		if (compatibleEffects.size() > 0) {
			EffectInstance selectedEffect = consumer.getEffect(compatibleEffects.get(worldIn.random.nextInt(compatibleEffects.size())));
			if (selectedEffect != null && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent(consumer, selectedEffect))) {
				consumer.removeEffect(selectedEffect.getEffect());
			}
		}
	}
}
