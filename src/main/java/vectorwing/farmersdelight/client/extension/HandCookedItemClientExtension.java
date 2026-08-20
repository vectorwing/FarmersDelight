package vectorwing.farmersdelight.client.extension;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.EnumParameters;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class HandCookedItemClientExtension implements IClientItemExtensions
{
	@Override
	public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
		ItemInHandRenderer itemInHandRenderer = Minecraft.getInstance().gameRenderer.itemInHandRenderer;
		HumanoidArm usingArm = player.getUsedItemHand() == InteractionHand.MAIN_HAND
			? player.getMainArm()
			: player.getMainArm().getOpposite();
		if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && usingArm == arm && itemInHand.getUseAnimation() == UseAnim.NONE) {
			itemInHandRenderer.applyItemArmTransform(poseStack, arm, equipProcess);
			poseStack.mulPose(Axis.XN.rotationDegrees(75));
			return true;
		}

		return false;
	}

	@Override
	public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity living, InteractionHand hand, ItemStack stack) {
		return stack.has(ModDataComponents.COOKING_TIME_LENGTH.get()) ? EnumParameters.PROXY_HAND_COOKING.getValue() : null;
	}
}
