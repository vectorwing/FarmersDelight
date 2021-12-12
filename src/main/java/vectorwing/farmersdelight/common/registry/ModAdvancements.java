package vectorwing.farmersdelight.common.registry;

import net.minecraft.advancements.CriteriaTriggers;
import vectorwing.farmersdelight.common.advancement.CuttingBoardTrigger;

public class ModAdvancements
{
	public static CuttingBoardTrigger CUTTING_BOARD = new CuttingBoardTrigger();

	public static void register() {
		CriteriaTriggers.register(CUTTING_BOARD);
	}
}
