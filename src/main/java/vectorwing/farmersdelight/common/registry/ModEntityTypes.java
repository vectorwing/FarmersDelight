package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;

import java.util.function.Supplier;

public class ModEntityTypes
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, FarmersDelight.MODID);

	public static final Supplier<EntityType<RottenTomatoEntity>> ROTTEN_TOMATO = ENTITIES.register("rotten_tomato", () -> (
			EntityType.Builder.<RottenTomatoEntity>of(RottenTomatoEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10)
					.build("rotten_tomato")));
}
