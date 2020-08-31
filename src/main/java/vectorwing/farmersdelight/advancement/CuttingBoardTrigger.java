package vectorwing.farmersdelight.advancement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class CuttingBoardTrigger extends AbstractCriterionTrigger<CuttingBoardTrigger.Instance> {
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "consume_item");

	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new CuttingBoardTrigger.Instance();
	}

	public void trigger(ServerPlayerEntity player) {
		this.func_227070_a_(player.getAdvancements(), Instance::test);
	}

	public static class Instance extends CriterionInstance {
		public Instance() {
			super(CuttingBoardTrigger.ID);
		}

		public boolean test() {
			return true;
		}
	}
}
