package vectorwing.farmersdelight.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import vectorwing.farmersdelight.FarmersDelight;

public class CuttingBoardTrigger extends SimpleCriterionTrigger<CuttingBoardTrigger.TriggerInstance>
{
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "use_cutting_board");

	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, TriggerInstance::test);
	}

	@Override
	protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext conditionsParser) {
		return new TriggerInstance(player);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance
	{
		public TriggerInstance(ContextAwarePredicate player) {
			super(CuttingBoardTrigger.ID, player);
		}

		public static TriggerInstance simple() {
			return new TriggerInstance(ContextAwarePredicate.ANY);
		}

		public boolean test() {
			return true;
		}
	}
}
