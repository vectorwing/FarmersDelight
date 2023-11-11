package vectorwing.farmersdelight.common.crafting.condition;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.conditions.ICondition;
import vectorwing.farmersdelight.common.Configuration;

public class VanillaCrateEnabledCondition implements ICondition
{

	public static final Codec<VanillaCrateEnabledCondition> CODEC = Codec.unit(new VanillaCrateEnabledCondition());

	public VanillaCrateEnabledCondition() {
	}

	@Override
	public boolean test(IContext context) {
		return Configuration.ENABLE_VANILLA_CROP_CRATES.get();
	}

	@Override
	public Codec<? extends ICondition> codec() {
		return CODEC;
	}

}