package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

import java.util.function.Supplier;

public class ModMenuTypes
{
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, FarmersDelight.MODID);

	public static final Supplier<MenuType<CookingPotMenu>> COOKING_POT = MENU_TYPES
			.register("cooking_pot", () -> IMenuTypeExtension.create(CookingPotMenu::new));
}
