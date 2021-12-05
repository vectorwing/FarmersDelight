package vectorwing.farmersdelight.items;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

public class DogFoodItem extends ConsumableItem
{
	public static final List<EffectInstance> EFFECTS = Lists.newArrayList(
			new EffectInstance(Effects.MOVEMENT_SPEED, 6000, 0),
			new EffectInstance(Effects.DAMAGE_BOOST, 6000, 0),
			new EffectInstance(Effects.DAMAGE_RESISTANCE, 6000, 0));

	public DogFoodItem(Properties properties) {
		super(properties);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class DogFoodEvent
	{
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onDogFoodApplied(PlayerInteractEvent.EntityInteract event) {
			PlayerEntity player = event.getPlayer();
			Entity target = event.getTarget();
			ItemStack itemStack = event.getItemStack();

			if (target instanceof LivingEntity && ModTags.DOG_FOOD_USERS.contains(target.getType())) {
				LivingEntity entity = (LivingEntity) target;
				boolean isTameable = entity instanceof TameableEntity;

				if (entity.isAlive() && (!isTameable || ((TameableEntity) entity).isTame()) && itemStack.getItem().equals(ModItems.DOG_FOOD.get())) {
					entity.setHealth(entity.getMaxHealth());
					for (EffectInstance effect : EFFECTS) {
						entity.addEffect(new EffectInstance(effect));
					}
					entity.level.playSound(null, target.blockPosition(), SoundEvents.GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);

					for (int i = 0; i < 5; ++i) {
						double xSpeed = MathUtils.RAND.nextGaussian() * 0.02D;
						double ySpeed = MathUtils.RAND.nextGaussian() * 0.02D;
						double zSpeed = MathUtils.RAND.nextGaussian() * 0.02D;
						entity.level.addParticle(ModParticleTypes.STAR.get(), entity.getRandomX(1.0D), entity.getRandomY() + 0.5D, entity.getRandomZ(1.0D), xSpeed, ySpeed, zSpeed);
					}

					if (itemStack.getContainerItem() != ItemStack.EMPTY && !player.isCreative()) {
						player.addItem(itemStack.getContainerItem());
						itemStack.shrink(1);
					}

					event.setCancellationResult(ActionResultType.SUCCESS);
					event.setCanceled(true);
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (!Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			return;
		}

		IFormattableTextComponent textWhenFeeding = TextUtils.getTranslation("tooltip.dog_food.when_feeding");
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
		if (target instanceof WolfEntity) {
			WolfEntity wolf = (WolfEntity) target;
			if (wolf.isAlive() && wolf.isTame()) {
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
