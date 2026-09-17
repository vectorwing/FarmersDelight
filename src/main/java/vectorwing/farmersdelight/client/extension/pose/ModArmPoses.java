package vectorwing.farmersdelight.client.extension.pose;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class ModArmPoses
{
	public static class HandCookingArmPose implements IArmPoseTransformer
	{
		@Override
		public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
			ItemStack stack = entity.getUseItem();
			if (!stack.has(ModDataComponents.COOKING_TIME_LENGTH.get())) {
				return;
			}

			if (arm == HumanoidArm.LEFT) {
				model.leftArm.xRot = model.leftArm.xRot * 0.5F - (float) (Math.PI / 4);
			} else {
				model.rightArm.xRot = model.rightArm.xRot * 0.5F - (float) (Math.PI / 4);
			}
		}
	}

	public static class SkilletArmPose implements IArmPoseTransformer
	{
		@Override
		public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
			ItemStack stack = entity.getUseItem();
			if (!stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
				return;
			}

			long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
			float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
			float animation = ((entity.level().getGameTime() - time) + partialTicks) / SkilletItem.FLIP_TIME;
			animation = Mth.clamp(animation, 0, 1);

			if (arm == HumanoidArm.LEFT) {
				model.leftArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
			} else {
				model.rightArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
			}
		}
	}
}
