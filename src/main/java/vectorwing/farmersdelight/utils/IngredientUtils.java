package vectorwing.farmersdelight.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Util for manipulating ingredients
 */
public class IngredientUtils
{
    public static Ingredient getToolTypeIngredient(ToolType type) {
        return Ingredient.fromStacks(ForgeRegistries.ITEMS.getValues().stream()
                .filter((item) -> item.getToolTypes(new ItemStack(item)).contains(type))
                .map(ItemStack::new));
    }
}
