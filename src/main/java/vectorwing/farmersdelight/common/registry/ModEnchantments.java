package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.item.enchantment.BackstabbingEnchantment;

import java.util.function.Supplier;

public class ModEnchantments
{
	public static final EnchantmentCategory KNIFE = EnchantmentCategory.create("knife", item -> item instanceof KnifeItem);

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, FarmersDelight.MODID);

	public static final Supplier<Enchantment> BACKSTABBING = ENCHANTMENTS.register("backstabbing",
			() -> new BackstabbingEnchantment(Enchantment.Rarity.UNCOMMON, KNIFE, EquipmentSlot.MAINHAND));
}
