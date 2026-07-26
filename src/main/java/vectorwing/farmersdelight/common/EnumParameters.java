package vectorwing.farmersdelight.common;

import net.minecraft.client.model.HumanoidModel;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class EnumParameters
{
	public static final EnumProxy<HumanoidModel.ArmPose> PROXY_SKILLET_FLIP = new EnumProxy<>(
			HumanoidModel.ArmPose.class, false, false, (IArmPoseTransformer) (model, renderState, arm) -> {}
	);
}
