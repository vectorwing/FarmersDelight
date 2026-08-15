package vectorwing.farmersdelight.common.item.component;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record CookingPotTooltipComponent(ItemStack mealStack) implements TooltipComponent
{
}
