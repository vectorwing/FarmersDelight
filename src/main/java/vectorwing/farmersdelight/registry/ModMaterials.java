package vectorwing.farmersdelight.registry;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

public class ModMaterials
{
	public static final IItemTier FLINT = new IItemTier() {
		@Override
		public int getMaxUses() { return 131; }

		@Override
		public float getEfficiency() { return 4.0F; }

		@Override
		public float getAttackDamage() { return 1.0F; }

		@Override
		public int getHarvestLevel() { return 1; }

		@Override
		public int getEnchantability() { return 5; }

		@Override
		public Ingredient getRepairMaterial() {
			return Ingredient.fromItems(Items.FLINT);
		}
	};
}
