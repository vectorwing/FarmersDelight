package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

public class ModCreativeTabs
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmersDelight.MODID);

	public static final Supplier<CreativeModeTab> TAB_FARMERS_DELIGHT = CREATIVE_TABS.register(FarmersDelight.MODID,
			() -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.farmersdelight"))
					.icon(() -> new ItemStack(ModBlocks.STOVE.get()))
					.displayItems((parameters, output) -> ModItems.CREATIVE_TAB_ITEMS.forEach((item) -> output.accept(item.get())))
					.build());
}
