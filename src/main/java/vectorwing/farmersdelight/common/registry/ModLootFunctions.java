package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.function.CopySkilletFunction;
import vectorwing.farmersdelight.common.loot.function.SmokerCookFunction;

import java.util.function.Supplier;

public class ModLootFunctions
{
	public static final DeferredRegister<MapCodec<? extends LootItemFunction>> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, FarmersDelight.MODID);

	public static final Supplier<MapCodec<CopySkilletFunction>> COPY_SKILLET = LOOT_FUNCTIONS.register("copy_skillet", () -> CopySkilletFunction.CODEC);
	public static final Supplier<MapCodec<SmokerCookFunction>> SMOKER_COOK = LOOT_FUNCTIONS.register("smoker_cook", () -> SmokerCookFunction.CODEC);
}
