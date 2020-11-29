package vectorwing.farmersdelight.items;

import mcp.MethodsReturnNonnullByDefault;
import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HorseFeedItem extends MealItem
{
	public static final List<EffectInstance> EFFECTS = Lists.newArrayList(
			new EffectInstance(Effects.SPEED, 6000, 1),
			new EffectInstance(Effects.JUMP_BOOST, 6000, 0));

	public HorseFeedItem(Properties builder)
	{
		super(builder);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class HorseFeedEvent {
		@SubscribeEvent
		public static void onHorseFeedApplied(PlayerInteractEvent.EntityInteract event) {
			PlayerEntity player = event.getPlayer();
			Entity target = event.getTarget();
			ItemStack itemStack = event.getItemStack();

			if (target instanceof AbstractHorseEntity) {
				AbstractHorseEntity horse = (AbstractHorseEntity)target;
				if (horse.isAlive() && horse.isTame() && itemStack.getItem().equals(ModItems.HORSE_FEED.get())) {
					horse.setHealth(horse.getMaxHealth());
					for(EffectInstance effect : EFFECTS) {
						horse.addPotionEffect(new EffectInstance(effect));
					}
					horse.world.playSound(null, target.getPosition(), SoundEvents.ENTITY_HORSE_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);

					for(int i = 0; i < 5; ++i) {
						double d0 = MathUtils.RAND.nextGaussian() * 0.02D;
						double d1 = MathUtils.RAND.nextGaussian() * 0.02D;
						double d2 = MathUtils.RAND.nextGaussian() * 0.02D;
						horse.world.addParticle(ModParticleTypes.STAR_PARTICLE.get(), horse.getPosXRandom(1.0D), horse.getPosYRandom() + 0.5D, horse.getPosZRandom(1.0D), d0, d1, d2);
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

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		ITextComponent whenFeeding = TextUtils.getTranslation("tooltip.horse_feed.when_feeding");
		tooltip.add(whenFeeding.applyTextStyle(TextFormatting.GRAY));

		List<Tuple<String, AttributeModifier>> list1 = Lists.newArrayList();

		for(EffectInstance effectinstance : EFFECTS) {
			ITextComponent effectDescription = new StringTextComponent(" ");
			ITextComponent effectName = new TranslationTextComponent(effectinstance.getEffectName());
			effectDescription.appendSibling(effectName);
			Effect effect = effectinstance.getPotion();
			Map<IAttribute, AttributeModifier> map = effect.getAttributeModifierMap();
			if (!map.isEmpty()) {
				for(Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
					AttributeModifier attributemodifier = entry.getValue();
					AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
					list1.add(new Tuple<>(entry.getKey().getName(), attributemodifier1));
				}
			}

			if (effectinstance.getAmplifier() > 0) {
				effectDescription.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
			}

			if (effectinstance.getDuration() > 20) {
				effectDescription.appendText(" (").appendText(EffectUtils.getPotionDurationString(effectinstance, 1.0F)).appendText(")");
			}

			tooltip.add(effectDescription.applyTextStyle(effect.getEffectType().getColor()));
		}
	}

	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof HorseEntity) {
			HorseEntity horse = (HorseEntity)target;
			return horse.isAlive() && horse.isTame();
		}
		return ActionResultType.PASS;
	}
}
