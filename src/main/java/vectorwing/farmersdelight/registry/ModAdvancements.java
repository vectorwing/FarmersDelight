package vectorwing.farmersdelight.registry;

import net.minecraft.advancements.CriteriaTriggers;
import vectorwing.farmersdelight.advancement.CuttingBoardTrigger;

public class ModAdvancements {
	public static CuttingBoardTrigger CUTTING_BOARD = new CuttingBoardTrigger();

	public static void register() {
		CUTTING_BOARD = CriteriaTriggers.register(CUTTING_BOARD);
	}
}
