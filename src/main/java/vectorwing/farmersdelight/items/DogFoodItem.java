package vectorwing.farmersdelight.items;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
import vectorwing.farmersdelight.utils.MathUtils;
import vectorwing.farmersdelight.utils.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class DogFoodItem extends MealItem
{
	public static final List<EffectInstance> EFFECTS = Lists.newArrayList(
			new EffectInstance(Effects.SPEED, 6000, 0),
			new EffectInstance(Effects.STRENGTH, 6000, 0),
			new EffectInstance(Effects.RESISTANCE, 6000, 0));

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

			if (target instanceof WolfEntity) {
				WolfEntity wolf = (WolfEntity) target;
				if (wolf.isAlive() && wolf.isTamed() && itemStack.getItem().equals(ModItems.DOG_FOOD.get())) {
					wolf.setHealth(wolf.getMaxHealth());
					for (EffectInstance effect : EFFECTS) {
						wolf.addPotionEffect(new EffectInstance(effect));
					}
					wolf.world.playSound(null, target.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);

					for (int i = 0; i < 5; ++i) {
						double d0 = MathUtils.RAND.nextGaussian() * 0.02D;
						double d1 = MathUtils.RAND.nextGaussian() * 0.02D;
						double d2 = MathUtils.RAND.nextGaussian() * 0.02D;
						wolf.world.addParticle(ModParticleTypes.STAR_PARTICLE.get(), wolf.getPosXRandom(1.0D), wolf.getPosYRandom() + 0.5D, wolf.getPosZRandom(1.0D), d0, d1, d2);
					}

					if (itemStack.getContainerItem() != ItemStack.EMPTY && !player.isCreative()) {
						player.addItemStackToInventory(itemStack.getContainerItem());
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		IFormattableTextComponent whenFeeding = TextUtils.getTranslation("tooltip.dog_food.when_feeding");
		tooltip.add(whenFeeding.mergeStyle(TextFormatting.GRAY));

		for (EffectInstance effectinstance : EFFECTS) {
			IFormattableTextComponent effectDescription = new StringTextComponent(" ");
			IFormattableTextComponent effectName = new TranslationTextComponent(effectinstance.getEffectName());
			effectDescription.append(effectName);
			Effect effect = effectinstance.getPotion();

			if (effectinstance.getAmplifier() > 0) {
				effectDescription.appendString(" ").append(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
			}

			if (effectinstance.getDuration() > 20) {
				effectDescription.appendString(" (").appendString(EffectUtils.getPotionDurationString(effectinstance, 1.0F)).appendString(")");
			}

			tooltip.add(effectDescription.mergeStyle(effect.getEffectType().getColor()));
		}
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof WolfEntity) {
			WolfEntity wolf = (WolfEntity) target;
			if (wolf.isAlive() && wolf.isTamed()) {
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
