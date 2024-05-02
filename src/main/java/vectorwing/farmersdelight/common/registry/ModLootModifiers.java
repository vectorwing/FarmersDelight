package vectorwing.farmersdelight.common.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.AddLootTableModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;

public class ModLootModifiers
{
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FarmersDelight.MODID);

	public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_LOOT_TABLE = LOOT_MODIFIERS.register("add_loot_table", AddLootTableModifier.CODEC);
	public static final Supplier<Codec<? extends IGlobalLootModifier>> PASTRY_SLICING = LOOT_MODIFIERS.register("pastry_slicing", PastrySlicingModifier.CODEC);
}
