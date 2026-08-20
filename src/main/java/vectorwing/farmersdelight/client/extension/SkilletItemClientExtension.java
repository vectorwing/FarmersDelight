package vectorwing.farmersdelight.client.extension;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.SkilletItemRenderer;
import vectorwing.farmersdelight.common.EnumParameters;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class SkilletItemClientExtension implements IClientItemExtensions
{
	BlockEntityWithoutLevelRenderer renderer = new SkilletItemRenderer();

	@Override
	public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
		return renderer;
	}

	@Override
	public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity living, InteractionHand hand, ItemStack stack) {
		return stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get()) ? EnumParameters.PROXY_SKILLET_FLIP.getValue() : null;
	}
}
