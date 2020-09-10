package vectorwing.farmersdelight.crafting;

import com.google.common.collect.Lists;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vectorwing.farmersdelight.items.KnifeItem;
import vectorwing.farmersdelight.registry.ModRecipeSerializers;

import javax.annotation.Nonnull;
import java.util.List;

public class KnifeRepairRecipe extends SpecialRecipe {
	public KnifeRepairRecipe(ResourceLocation idIn) {
		super(idIn);
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInventory inv) {
		return NonNullList.withSize(inv.getHeight() * inv.getWidth(), ItemStack.EMPTY);
	}

	private boolean isValidInput(ItemStack stack) {
		return stack.getItem() instanceof KnifeItem && ((KnifeItem) stack.getItem()).isCustomRepairable(stack);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		List<ItemStack> list = Lists.newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			if (!itemstack.isEmpty() && isValidInput(itemstack)) {
				list.add(itemstack);
				if (list.size() > 1) {
					ItemStack itemstack1 = list.get(0);
					if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1) {
						return false;
					}
				}
			}
		}

		return list.size() == 2;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		List<ItemStack> list = Lists.newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			if (!itemstack.isEmpty() && isValidInput(itemstack)) {
				list.add(itemstack);
				if (list.size() > 1) {
					ItemStack itemstack1 = list.get(0);
					if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getCount() != 1 || itemstack.getCount() != 1) {
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (list.size() == 2) {
			ItemStack itemstack3 = list.get(0);
			ItemStack itemstack4 = list.get(1);
			if (itemstack3.getItem() == itemstack4.getItem() && itemstack3.getCount() == 1 && itemstack4.getCount() == 1) {
				Item item = itemstack3.getItem();
				int j = itemstack3.getMaxDamage() - itemstack3.getDamage();
				int k = itemstack3.getMaxDamage() - itemstack4.getDamage();
				int l = j + k + itemstack3.getMaxDamage() * 5 / 100;
				int i1 = itemstack3.getMaxDamage() - l;
				if (i1 < 0) {
					i1 = 0;
				}

				ItemStack itemstack2 = new ItemStack(itemstack3.getItem());
				itemstack2.setDamage(i1);
				return itemstack2;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.KNIFE_REPAIR.get();
	}
}
