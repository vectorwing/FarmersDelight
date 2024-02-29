package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.condition.VanillaCrateEnabledCondition;

import java.util.function.Supplier;

public class ModConditionCodecs {
    public static final DeferredRegister<Codec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, FarmersDelight.MODID);

    public static final Supplier<Codec<? extends ICondition>> VANILLA_CRATE_ENABLED = CONDITION_CODECS.register("vanilla_crates_enabled", () -> VanillaCrateEnabledCondition.CODEC);
}
