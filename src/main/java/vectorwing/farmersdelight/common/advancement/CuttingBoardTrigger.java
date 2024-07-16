package vectorwing.farmersdelight.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import vectorwing.farmersdelight.common.registry.ModAdvancements;

import java.util.Optional;

public class CuttingBoardTrigger extends SimpleCriterionTrigger<CuttingBoardTrigger.TriggerInstance>
{
	@Override
	public Codec<TriggerInstance> codec() {
		return CuttingBoardTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, TriggerInstance::test);
	}

	public static record TriggerInstance(
			Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance
	{
		public static final Codec<CuttingBoardTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
				builder -> builder.group(
								EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(CuttingBoardTrigger.TriggerInstance::player))
						.apply(builder, CuttingBoardTrigger.TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> simple() {
			return ModAdvancements.USE_CUTTING_BOARD.get().createCriterion(
					new CuttingBoardTrigger.TriggerInstance(Optional.empty())
			);
		}

		public boolean test() {
			return true;
		}
	}
}
