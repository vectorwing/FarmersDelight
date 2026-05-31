package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.condition.ToolTagCondition;

import java.util.function.Supplier;

public class ModLootConditions
{
	public static final DeferredRegister<MapCodec<? extends LootItemCondition>> LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, FarmersDelight.MODID);

	public static final Supplier<MapCodec<? extends LootItemCondition>> TOOL_TAG = LOOT_CONDITIONS.register("tool_tag", () -> ToolTagCondition.CODEC);
}
