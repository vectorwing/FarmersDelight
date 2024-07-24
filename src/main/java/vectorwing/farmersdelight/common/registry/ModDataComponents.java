package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
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

	// Cooking Pot
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> MEAL = DATA_COMPONENTS.registerComponentType(
			"meal", builder -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> CONTAINER = DATA_COMPONENTS.registerComponentType(
			"container", builder -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);

	// Skillet
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COOKING_TIME_LENGTH = DATA_COMPONENTS.registerComponentType(
			"cooking_time_length", (builder) -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
	);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStackWrapper>> SKILLET_INGREDIENT = DATA_COMPONENTS.registerComponentType(
			"skillet_ingredient", (builder) -> builder.persistent(ItemStackWrapper.CODEC).networkSynchronized(ItemStackWrapper.STREAM_CODEC).cacheEncoding()
	);

	// Enchantment Effects
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> BACKSTABBING = ENCHANTMENT_EFFECT_COMPONENTS.registerComponentType(
			"backstabbing", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()
			));
}
