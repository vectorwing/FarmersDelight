package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;

public class ModDataComponents
{
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(FarmersDelight.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> MEAL = DATA_COMPONENTS.registerComponentType(
			"meal", builder -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> CONTAINER = DATA_COMPONENTS.registerComponentType(
			"container", builder -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);
}
