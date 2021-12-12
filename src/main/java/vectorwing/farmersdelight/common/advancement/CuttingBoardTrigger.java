package vectorwing.farmersdelight.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import vectorwing.farmersdelight.FarmersDelight;

public class CuttingBoardTrigger extends SimpleCriterionTrigger<CuttingBoardTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "use_cutting_board");

	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, Instance::test);
	}

	@Override
	protected Instance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext conditionsParser) {
		return new CuttingBoardTrigger.Instance(player);
	}

	public static class Instance extends AbstractCriterionTriggerInstance
	{
		public Instance(EntityPredicate.Composite player) {
			super(CuttingBoardTrigger.ID, player);
		}

		public static CuttingBoardTrigger.Instance simple() {
			return new CuttingBoardTrigger.Instance(EntityPredicate.Composite.ANY);
		}

		public boolean test() {
			return true;
		}
	}
}
