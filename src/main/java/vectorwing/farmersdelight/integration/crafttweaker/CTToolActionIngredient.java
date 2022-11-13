package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ToolAction;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

//@Document("mods/farmersdelight/ToolActionIngredient")
//@ZenRegister
//@ZenCodeType.Name("mods.farmersdelight.ToolActionIngredient")
public class CTToolActionIngredient implements IIngredient {

    public static final String PREFIX = "toolingredient";
    private final ToolActionIngredient ingredient;

    public CTToolActionIngredient(ToolAction toolAction) {
        this(new ToolActionIngredient(toolAction));
    }

    public CTToolActionIngredient(ToolActionIngredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(IItemStack stack, boolean ignoreDamage) {
        return ingredient.test(stack.getInternal());
    }

    @Override
    public Ingredient asVanillaIngredient() {
        return ingredient;
    }

    @Override
    public String getCommandString() {
        return "<" + PREFIX + ":" + ingredient.toolAction.name() + ">";
    }

    @Override
    public IItemStack[] getItems() {
        ItemStack[] stacks = ingredient.getItems();
        IItemStack[] out = new IItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            out[i] = new MCItemStackMutable(stacks[i]);
        }
        return out;
    }


    @ZenCodeType.Expansion("crafttweaker.api.tool.ToolAction")
    @ZenRegister
    public static class ExpandToolAction {
        // Support the syntax:
        // <tooltype:axe> as IIngredient
        @ZenCodeType.Method
        @ZenCodeType.Caster(implicit = true)
        public static IIngredient asIIngredient(ToolAction internal) {
            return new CTToolActionIngredient(internal);
        }

    }
}
