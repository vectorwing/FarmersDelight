package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ModMaterials
{
	public static final Tier FLINT = new Tier()
	{
		@Override
		public int getUses() {
			return 131;
		}

		@Override
		public float getSpeed() {
			return 4.0F;
		}

		@Override
		public float getAttackDamageBonus() {
			return 1.0F;
		}

		@Override
		public int getLevel() {
			return 1;
		}

		@Override
		public int getEnchantmentValue() {
			return 5;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(Items.FLINT);
		}
	};
}
