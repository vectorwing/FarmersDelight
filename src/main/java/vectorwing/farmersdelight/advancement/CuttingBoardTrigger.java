package vectorwing.farmersdelight.advancement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class CuttingBoardTrigger extends AbstractCriterionTrigger<CuttingBoardTrigger.Instance> {
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "use_cutting_board");

	public ResourceLocation getId() {
		return ID;
	}

//	@Override
//	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
//		return new CuttingBoardTrigger.Instance();
//	}

	public void trigger(ServerPlayerEntity player) {
		this.triggerListeners(player, Instance::test);
	}

	@Override
	protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
		return new CuttingBoardTrigger.Instance(entityPredicate);
	}

	public static class Instance extends CriterionInstance {
		public Instance(EntityPredicate.AndPredicate entityPredicate) {
			super(CuttingBoardTrigger.ID, entityPredicate);
		}

		public boolean test() {
			return true;
		}
	}
}
