package vectorwing.farmersdelight.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
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

public class DogFoodItem extends MealItem
{
	public DogFoodItem(Properties builder)
	{
		super(builder);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class DogFoodEvent {
		@SubscribeEvent
		public static void onDogFoodApplied(PlayerInteractEvent.EntityInteract event) {
			PlayerEntity player = event.getPlayer();
			Entity target = event.getTarget();
			ItemStack itemStack = event.getItemStack();

			if (target instanceof WolfEntity) {
				WolfEntity wolf = (WolfEntity)target;
				if (wolf.isAlive() && wolf.isTamed() && itemStack.getItem().equals(ModItems.DOG_FOOD.get())) {
					wolf.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 2));
					wolf.addPotionEffect(new EffectInstance(Effects.STRENGTH, 6000, 1));
					wolf.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 6000, 1));
					wolf.world.playSound(null, target.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);

					for(int i = 0; i < 5; ++i) {
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

	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof WolfEntity) {
			WolfEntity wolf = (WolfEntity)target;
			return wolf.isAlive() && wolf.isTamed();
		}
		return false;
	}
}
