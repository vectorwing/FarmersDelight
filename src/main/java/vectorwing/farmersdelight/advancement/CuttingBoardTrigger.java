package vectorwing.farmersdelight.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class CuttingBoardTrigger extends AbstractCriterionTrigger<CuttingBoardTrigger.Instance> {
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "use_cutting_board");

	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayerEntity player) {
		this.triggerListeners(player, Instance::test);
	}

	@Override
	protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate player, ConditionArrayParser conditionsParser) {
		return new CuttingBoardTrigger.Instance(player);
	}

	public static class Instance extends CriterionInstance {
		public Instance(EntityPredicate.AndPredicate player) {
			super(CuttingBoardTrigger.ID, player);
		}

		public static CuttingBoardTrigger.Instance simple() {
			return new CuttingBoardTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND);
		}

		public boolean test() {
			return true;
		}
	}
}
