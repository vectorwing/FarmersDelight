package vectorwing.farmersdelight.common.registry;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.AddLootTableModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;

public class ModLootModifiers
{
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, FarmersDelight.MODID);

	public static final RegistryObject<GlobalLootModifierSerializer<?>> ADD_ITEM = LOOT_MODIFIERS.register("add_item", AddItemModifier.Serializer::new);
	public static final RegistryObject<GlobalLootModifierSerializer<?>> ADD_LOOT_TABLE = LOOT_MODIFIERS.register("add_loot_table", AddLootTableModifier.Serializer::new);
	public static final RegistryObject<GlobalLootModifierSerializer<?>> PASTRY_SLICING = LOOT_MODIFIERS.register("pastry_slicing", PastrySlicingModifier.Serializer::new);
}
