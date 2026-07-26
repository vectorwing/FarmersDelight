package vectorwing.farmersdelight.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.level.AlterGroundEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEvents
{
	@SubscribeEvent
	public static void keepRichSoilUnderGiantTrees(AlterGroundEvent event) {
		AlterGroundEvent.StateProvider originalProvider = event.getStateProvider();
		event.setStateProvider((level, random, pos) -> {
			BlockPos placementPos = findGroundPlacementPos(event, pos);
			if (placementPos != null && level.getBlockState(placementPos).is(ModBlocks.RICH_SOIL.get())) {
				return null;
			}
			return originalProvider.getState(level, random, pos);
		});
	}

	@SubscribeEvent
	public static void handleVanillaSoupEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntity();

		if (Configuration.ENABLE_RABBIT_STEW_BUFF.get() && food.equals(Items.RABBIT_STEW)) {
			return;
		}

		if (Configuration.ENABLE_VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			MobEffectInstance soupEffect = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

			if (soupEffect != null) {
				entity.addEffect(new MobEffectInstance(soupEffect));
			}
		}
	}

	private static BlockPos findGroundPlacementPos(AlterGroundEvent event, BlockPos pos) {
		for (int i = 2; i >= -3; i--) {
			BlockPos blockPos = pos.above(i);
			BlockState state = event.getContext().level().getBlockState(blockPos);
			if (state.is(ModBlocks.RICH_SOIL.get())) {
				return blockPos;
			}
			if (!event.getContext().isAir(blockPos) && i < 0) {
				return null;
			}
		}
		return null;
	}
}
