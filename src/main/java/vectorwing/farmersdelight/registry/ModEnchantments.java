package vectorwing.farmersdelight.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.enchantments.BackstabbingEnchantment;
import vectorwing.farmersdelight.utils.ModTags;

public class ModEnchantments
{
	public static final EnchantmentType KNIFE = EnchantmentType.create("knife", item -> item.isIn(ModTags.KNIVES));

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FarmersDelight.MODID);

	public static final RegistryObject<Enchantment> BACKSTABBING = ENCHANTMENTS.register("backstabbing",
			() -> new BackstabbingEnchantment(Enchantment.Rarity.UNCOMMON, KNIFE, EquipmentSlotType.MAINHAND));
}
