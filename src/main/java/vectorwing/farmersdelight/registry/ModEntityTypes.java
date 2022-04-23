package vectorwing.farmersdelight.registry;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.entity.RottenTomatoEntity;

public class ModEntityTypes
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, FarmersDelight.MODID);

	public static final RegistryObject<EntityType<RottenTomatoEntity>> ROTTEN_TOMATO = ENTITIES.register("rotten_tomato", () -> (
			EntityType.Builder.<RottenTomatoEntity>of(RottenTomatoEntity::new, EntityClassification.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(4)
					.updateInterval(10)
					.build("rotten_tomato")));
}
