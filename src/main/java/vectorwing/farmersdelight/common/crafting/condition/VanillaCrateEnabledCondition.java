package vectorwing.farmersdelight.common.crafting.condition;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.Configuration;

public class VanillaCrateEnabledCondition implements ICondition
{

	public static final MapCodec<VanillaCrateEnabledCondition> CODEC = MapCodec.unit(new VanillaCrateEnabledCondition());

	public VanillaCrateEnabledCondition() {
	}

	@Override
	public boolean test(@NotNull IContext context) {
		return Configuration.ENABLE_VANILLA_CROP_CRATES.get();
	}

	@Override
	public @NotNull MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

}