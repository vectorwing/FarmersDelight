package vectorwing.farmersdelight.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MealItem extends Item
{
	/**
	 * Items that are foods, and need a container to be held.
	 * When eaten, they deposit containers into the player's inventory, allowing them to set maximum stack sizes above 1.
	 */
	public MealItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
		ItemStack container = stack.getContainerItem();

		ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
		if (entityLiving instanceof PlayerEntity && ((PlayerEntity) entityLiving).abilities.isCreativeMode) {
			return itemstack;
		}

		if (player == null || !player.abilities.isCreativeMode) {
			if (itemstack.isEmpty()) {
				return container;
			}

			if (player != null) {
				player.inventory.addItemStackToInventory(container);
			}
		}
		return stack;
	}
}
