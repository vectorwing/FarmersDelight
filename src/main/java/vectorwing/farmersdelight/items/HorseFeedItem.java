package vectorwing.farmersdelight.items;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.registry.ModParticleTypes;
import vectorwing.farmersdelight.setup.Configuration;
import vectorwing.farmersdelight.utils.MathUtils;
import vectorwing.farmersdelight.utils.TextUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;
import java.util.List;

public class HorseFeedItem extends Item
{
	public static final List<EffectInstance> EFFECTS = Lists.newArrayList(
			new EffectInstance(Effects.MOVEMENT_SPEED, 6000, 1),
			new EffectInstance(Effects.JUMP, 6000, 0));

	public HorseFeedItem(Properties properties) {
		super(properties);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class HorseFeedEvent
	{
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onHorseFeedApplied(PlayerInteractEvent.EntityInteract event) {
			PlayerEntity player = event.getPlayer();
			Entity target = event.getTarget();
			ItemStack itemStack = event.getItemStack();

			if (target instanceof LivingEntity && ModTags.HORSE_FEED_USERS.contains(target.getType())) {
				LivingEntity entity = (LivingEntity) target;
				boolean isTameable = entity instanceof AbstractHorseEntity;

				if (entity.isAlive() && (!isTameable || ((AbstractHorseEntity) entity).isTamed()) && itemStack.getItem().equals(ModItems.HORSE_FEED.get())) {
					entity.setHealth(entity.getMaxHealth());
					for (EffectInstance effect : EFFECTS) {
						entity.addEffect(new EffectInstance(effect));
					}
					entity.level.playSound(null, target.blockPosition(), SoundEvents.HORSE_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);

					for (int i = 0; i < 5; ++i) {
						double d0 = MathUtils.RAND.nextGaussian() * 0.02D;
						double d1 = MathUtils.RAND.nextGaussian() * 0.02D;
						double d2 = MathUtils.RAND.nextGaussian() * 0.02D;
						entity.level.addParticle(ModParticleTypes.STAR.get(), entity.getRandomX(1.0D), entity.getRandomY() + 0.5D, entity.getRandomZ(1.0D), d0, d1, d2);
					}

					if (!player.isCreative()) {
						itemStack.shrink(1);
					}

					event.setCancellationResult(ActionResultType.SUCCESS);
					event.setCanceled(true);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			return;
		}

		IFormattableTextComponent textWhenFeeding = TextUtils.getTranslation("tooltip.horse_feed.when_feeding");
		tooltip.add(textWhenFeeding.withStyle(TextFormatting.GRAY));

		for (EffectInstance effectInstance : EFFECTS) {
			IFormattableTextComponent effectDescription = new StringTextComponent(" ");
			IFormattableTextComponent effectName = new TranslationTextComponent(effectInstance.getDescriptionId());
			effectDescription.append(effectName);
			Effect effect = effectInstance.getEffect();

			if (effectInstance.getAmplifier() > 0) {
				effectDescription.append(" ").append(new TranslationTextComponent("potion.potency." + effectInstance.getAmplifier()));
			}

			if (effectInstance.getDuration() > 20) {
				effectDescription.append(" (").append(EffectUtils.formatDuration(effectInstance, 1.0F)).append(")");
			}

			tooltip.add(effectDescription.withStyle(effect.getCategory().getTooltipFormatting()));
		}
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof HorseEntity) {
			HorseEntity horse = (HorseEntity) target;
			if (horse.isAlive() && horse.isTamed()) {
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
