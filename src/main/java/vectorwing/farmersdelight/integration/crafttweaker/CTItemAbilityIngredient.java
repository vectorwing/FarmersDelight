package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.ingredient.condition.IngredientConditions;
import com.blamejared.crafttweaker.api.ingredient.transformer.IngredientTransformers;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ItemAbility;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.common.crafting.ingredient.ItemAbilityIngredient;

@Document("mods/FarmersDelight/ToolActionIngredient")
@ZenRegister
@ZenCodeType.Name("mods.farmersdelight.ToolActionIngredient")
public class CTToolActionIngredient implements IIngredient
{

	public static final String PREFIX = "toolingredient";
	private final ItemAbilityIngredient ingredient;

	private final IngredientConditions conditions = new IngredientConditions();
	private final IngredientTransformers transformers = new IngredientTransformers();

	public CTToolActionIngredient(ItemAbility toolAction) {
		this(new ItemAbilityIngredient(toolAction));
	}

	public CTToolActionIngredient(ItemAbilityIngredient ingredient) {
		this.ingredient = ingredient;
	}

	@Override
	public boolean matches(IItemStack stack) {
		return ingredient.test(stack.getInternal());
	}

	@Override
	public Ingredient asVanillaIngredient() {
		return ingredient.toVanilla();
	}

	@Override
	public String getCommandString() {
		return "<" + PREFIX + ":" + ingredient.itemAbility().name() + ">";
	}

	@Override
	public IItemStack[] getItems() {
		ItemStack[] stacks = (ItemStack[]) ingredient.getItems().toArray();
		IItemStack[] out = new IItemStack[stacks.length];
		for (int i = 0; i < stacks.length; i++) {
			out[i] = new MCItemStackMutable(stacks[i]);
		}
		return out;
	}

	@Override
	public IngredientTransformers transformers() {
		return transformers;
	}

	@Override
	public IngredientConditions conditions() {
		return conditions;
	}


	@ZenCodeType.Expansion("crafttweaker.api.tool.ToolAction")
	@ZenRegister
	public static class ExpandToolAction
	{
		// Support the syntax:
		// <tooltype:axe> as IIngredient
		@ZenCodeType.Method
		@ZenCodeType.Caster(implicit = true)
		public static IIngredient asIIngredient(ItemAbility internal) {
			return new CTToolActionIngredient(internal);
		}

	}
}
