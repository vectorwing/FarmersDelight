package vectorwing.farmersdelight.common.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;
import vectorwing.farmersdelight.common.item.capability.StaticFluidItemCapability;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEvents
{
	@SubscribeEvent
	public static void registerItemCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(ModDataComponents.FLUID_TANK, stack, JugBlockEntity.JUG_CAPACITY),
			ModItems.JUG.get(),
			ModItems.GLASS_JUG.get());
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new StaticFluidItemCapability(NeoForgeMod.MILK.get(), stack, Items.GLASS_BOTTLE, 250), ModItems.MILK_BOTTLE.get());
	}

	@SubscribeEvent
	public static void handleVanillaSoupEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntity();

		if (Configuration.ENABLE_RABBIT_STEW_BUFF.get() && food.equals(Items.RABBIT_STEW)) {
			return;
		}

		if (Configuration.ENABLE_VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			FoodProperties soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

			if (soupEffects != null) {
				for (FoodProperties.PossibleEffect effect : soupEffects.effects()) {
					entity.addEffect(effect.effect());
				}
			}
		}
	}
}
