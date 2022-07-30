package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;

public class ModEntityTypes
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FarmersDelight.MODID);

	public static final RegistryObject<EntityType<RottenTomatoEntity>> ROTTEN_TOMATO = ENTITIES.register("rotten_tomato", () -> (
			EntityType.Builder.<RottenTomatoEntity>of(RottenTomatoEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10)
					.build("rotten_tomato")));
}
