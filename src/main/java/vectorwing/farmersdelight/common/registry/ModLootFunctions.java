package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.function.CopyMealFunction;
import vectorwing.farmersdelight.common.loot.function.CopySkilletFunction;
import vectorwing.farmersdelight.common.loot.function.SmokerCookFunction;

public class ModLootFunctions
{
	public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(BuiltInRegistries.LOOT_FUNCTION_TYPE.key(), FarmersDelight.MODID);

	public static final RegistryObject<LootItemFunctionType> COPY_MEAL = LOOT_FUNCTIONS.register("copy_meal", () -> new LootItemFunctionType(CopyMealFunction.CODEC));
	public static final RegistryObject<LootItemFunctionType> COPY_SKILLET = LOOT_FUNCTIONS.register("copy_skillet", () -> new LootItemFunctionType(CopySkilletFunction.CODEC));
	public static final RegistryObject<LootItemFunctionType> SMOKER_COOK = LOOT_FUNCTIONS.register("smoker_cook", () -> new LootItemFunctionType(SmokerCookFunction.CODEC));
}
