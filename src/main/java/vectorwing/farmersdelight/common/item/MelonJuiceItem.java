package vectorwing.farmersdelight.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class MelonJuiceItem extends DrinkItem
{
	public MelonJuiceItem(Properties properties) {
		super(properties, TextUtils.tooltip("melon_juice").withStyle(ChatFormatting.BLUE));
	}

	@Override
	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
		consumer.heal(2.0F);
	}
}
