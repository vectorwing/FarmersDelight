package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotContainer;

public class ModContainerTypes
{
	public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, FarmersDelight.MODID);

	public static final RegistryObject<MenuType<CookingPotContainer>> COOKING_POT = CONTAINER_TYPES
			.register("cooking_pot", () -> IForgeMenuType.create(CookingPotContainer::new));
}
