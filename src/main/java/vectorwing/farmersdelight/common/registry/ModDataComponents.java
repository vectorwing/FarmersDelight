package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.utility.RegistryUtils;

import java.util.List;

public class ModDataComponents
{
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(FarmersDelight.MODID);
	public static final RegistryUtils.EnchantmentEffectComponents ENCHANTMENT_EFFECT_COMPONENTS = RegistryUtils.createEnchantmentEffectComponents(FarmersDelight.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> MEAL = DATA_COMPONENTS.registerComponentType(
			"meal", builder -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> CONTAINER = DATA_COMPONENTS.registerComponentType(
			"container", builder -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> BACKSTABBING = ENCHANTMENT_EFFECT_COMPONENTS.registerComponentType(
			"backstabbing", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()
			));
}
