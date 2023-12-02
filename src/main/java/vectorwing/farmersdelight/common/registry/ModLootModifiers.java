package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.AddLootTableModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;

import java.util.function.Supplier;

public class ModLootModifiers
{
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FarmersDelight.MODID);

	public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_LOOT_TABLE = LOOT_MODIFIERS.register("add_loot_table", AddLootTableModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> PASTRY_SLICING = LOOT_MODIFIERS.register("pastry_slicing", PastrySlicingModifier.CODEC);
}
