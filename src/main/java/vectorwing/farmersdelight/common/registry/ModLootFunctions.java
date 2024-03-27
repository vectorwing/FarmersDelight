package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.function.CopyMealFunction;
import vectorwing.farmersdelight.common.loot.function.CopySkilletFunction;
import vectorwing.farmersdelight.common.loot.function.SmokerCookFunction;

import java.util.function.Supplier;

public class ModLootFunctions
{
	public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registry.LOOT_FUNCTION_TYPE.key(), FarmersDelight.MODID);

	public static final Supplier<LootItemFunctionType> COPY_MEAL = LOOT_FUNCTIONS.register("copy_meal", () -> new LootItemFunctionType(new CopyMealFunction.Serializer()));
	public static final Supplier<LootItemFunctionType> COPY_SKILLET = LOOT_FUNCTIONS.register("copy_skillet", () -> new LootItemFunctionType(new CopySkilletFunction.Serializer()));
	public static final Supplier<LootItemFunctionType> SMOKER_COOK = LOOT_FUNCTIONS.register("smoker_cook", () -> new LootItemFunctionType(new SmokerCookFunction.Serializer()));
}
