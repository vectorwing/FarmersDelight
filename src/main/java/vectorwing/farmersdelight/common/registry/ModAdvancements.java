package vectorwing.farmersdelight.common.registry;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.advancement.CuttingBoardTrigger;

import java.util.function.Supplier;

public class ModAdvancements
{
	public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, FarmersDelight.MODID);

	public static final Supplier<CuttingBoardTrigger> USE_CUTTING_BOARD = TRIGGERS.register("use_cutting_board", CuttingBoardTrigger::new);
}
