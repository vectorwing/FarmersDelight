package vectorwing.farmersdelight.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.container.CookingPotContainer;

public class ModContainerTypes {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, FarmersDelight.MODID);

    public static final RegistryObject<ContainerType<CookingPotContainer>> COOKING_POT = CONTAINER_TYPES
            .register("cooking_pot", () -> IForgeContainerType.create(CookingPotContainer::new));
}
