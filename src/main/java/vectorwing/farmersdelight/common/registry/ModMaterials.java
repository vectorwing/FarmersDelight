package vectorwing.farmersdelight.common.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

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
		public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 5;
		}

		@Override
		public @NotNull Ingredient getRepairIngredient() {
			return Ingredient.of(Items.FLINT);
		}
	};
}
