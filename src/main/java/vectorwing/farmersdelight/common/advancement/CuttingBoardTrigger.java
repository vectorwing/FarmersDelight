package vectorwing.farmersdelight.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModAdvancements;

import java.util.Optional;

public class CuttingBoardTrigger extends SimpleCriterionTrigger<CuttingBoardTrigger.TriggerInstance>
{
	public static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "use_cutting_board");

	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, TriggerInstance::test);
	}

	@Override
	protected TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext conditionsParser) {
		return new TriggerInstance(predicate);
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance
	{
		public TriggerInstance(Optional<ContextAwarePredicate> predicate) {
			super(predicate);
		}

		public static Criterion<TriggerInstance> simple() {
			return ModAdvancements.CUTTING_BOARD.createCriterion(new TriggerInstance(Optional.empty()));
		}

		public boolean test() {
			return true;
		}
	}
}
