//package vectorwing.farmersdelight.integration.crafttweaker;
//
//import com.blamejared.crafttweaker.api.annotations.BracketResolver;
//import com.blamejared.crafttweaker.api.annotations.ZenRegister;
//import com.blamejared.crafttweaker.api.item.IIngredient;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
//import com.blamejared.crafttweaker_annotations.annotations.Document;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraftforge.common.ToolType;
//import org.openzen.zencode.java.ZenCodeType;
//import vectorwing.farmersdelight.common.crafting.ingredients.ToolIngredient;
//
///**
// * An ingredient that matches any item with the given tool type.
// */
//@Document("mods/farmersdelight/ToolIngredient")
//@ZenRegister
//@ZenCodeType.Name("mods.farmersdelight.ToolIngredient")
//public class MCToolIngredient implements IIngredient
//{
//    public static final String PREFIX = "toolingredient";
//    private final ToolIngredient ingredient;
//
//    public MCToolIngredient(ToolIngredient ingredient) {
//        this.ingredient = ingredient;
//    }
//
//    public MCToolIngredient(ToolType type) {
//        this(new ToolIngredient(type));
//    }
//
//    /**
//     * Get a tool ingredient from a tool type name.
//     *
//     * @param type The name of the type
//     * @return The ingredient
//     *
//     * @docParam type "axe"
//     */
//    @ZenCodeType.Method
//    @BracketResolver(PREFIX)
//    public static MCToolIngredient getToolIngredient(String type) {
//        return getToolIngredient(ToolType.get(type));
//    }
//
//    /**
//     * Get a tool ingredient from a tool type.
//     *
//     * @param type The tool type
//     * @return The ingredient
//     *
//     * @docParam type <tooltype:axe>
//     */
//    @ZenCodeType.Method
//    public static MCToolIngredient getToolIngredient(ToolType type) {
//        return new MCToolIngredient(type);
//    }
//
//    @Override
//    public boolean matches(IItemStack stack, boolean ignoreDamage) {
//        return ingredient.test(stack.getInternal());
//    }
//
//    @Override
//    public Ingredient asVanillaIngredient() {
//        return ingredient;
//    }
//
//    @Override
//    public String getCommandString() {
//        return "<" + PREFIX + ":" + ingredient.toolType.getName() + ">";
//    }
//
//    @Override
//    public IItemStack[] getItems() {
//        ItemStack[] stacks = ingredient.getItems();
//        IItemStack[] out = new IItemStack[stacks.length];
//        for (int i = 0; i < stacks.length; i++) {
//            out[i] = new MCItemStackMutable(stacks[i]);
//        }
//        return out;
//    }
//
//    // Requires ToolType support in CraftTweaker:
//    // https://github.com/CraftTweaker/CraftTweaker/pull/1237
//    @ZenCodeType.Expansion("crafttweaker.api.tool.ToolType")
//    public static class ExpandToolType {
//        // Support the syntax:
//        // <tooltype:axe> as IIngredient
//        @ZenCodeType.Caster
//        public static IIngredient asIIngredient(ToolType _this) {
//            return new MCToolIngredient(_this);
//        }
//    }
//}
