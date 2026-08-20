package vectorwing.farmersdelight.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import vectorwing.farmersdelight.common.registry.ModAdvancements;

import java.util.Optional;

public class CuttingBoardTrigger extends SimpleCriterionTrigger<CuttingBoardTrigger.TriggerInstance>
{
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, TriggerInstance::test);
	}

	public static record TriggerInstance(
			Optional<ContextAwarePredicate> player) implements SimpleInstance
	{
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
				builder -> builder.group(
								EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
						.apply(builder, TriggerInstance::new)
		);

		public static Criterion<TriggerInstance> simple() {
			return ModAdvancements.USE_CUTTING_BOARD.get().createCriterion(
					new TriggerInstance(Optional.empty())
			);
		}

		public boolean test() {
			return true;
		}
	}
}


