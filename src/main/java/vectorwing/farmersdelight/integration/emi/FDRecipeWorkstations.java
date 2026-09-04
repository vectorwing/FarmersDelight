package vectorwing.farmersdelight.integration.emi;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FDRecipeWorkstations {
    public static final EmiStack COOKING_POT = EmiStack.of(ModItems.COOKING_POT.get());
    public static final EmiStack CUTTING_BOARD = EmiStack.of(ModItems.CUTTING_BOARD.get());
    public static final EmiStack ORGANIC_COMPOST = EmiStack.of(ModItems.ORGANIC_COMPOST.get());
    public static final EmiStack JUG = EmiStack.of(ModItems.JUG.get());
    public static final EmiStack GLASS_JUG = EmiStack.of(ModItems.GLASS_JUG.get());

    public static final EmiStack FLUID_EMPTYING = EmiStack.of(Items.BUCKET);
    public static final EmiStack FLUID_FILLING = EmiStack.of(Items.WATER_BUCKET);
}
