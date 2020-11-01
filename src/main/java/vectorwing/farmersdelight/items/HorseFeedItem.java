package vectorwing.farmersdelight.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.registry.ModParticleTypes;
import vectorwing.farmersdelight.utils.MathUtils;

public class HorseFeedItem extends MealItem
{
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
					horse.addPotionEffect(new EffectInstance(Effects.SPEED, 6000, 2));
					horse.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 6000, 1));
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

	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof HorseEntity) {
			HorseEntity horse = (HorseEntity)target;
			return horse.isAlive() && horse.isTame();
		}
		return false;
	}
}
