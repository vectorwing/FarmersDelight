package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

public class ModMenuTypes
{
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FarmersDelight.MODID);

	public static final RegistryObject<MenuType<CookingPotMenu>> COOKING_POT = MENU_TYPES
			.register("cooking_pot", () -> IMenuTypeExtension.create(CookingPotMenu::new));
}
