package vectorwing.farmersdelight.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.integration.emi.FDRecipeCategories;
import vectorwing.farmersdelight.integration.emi.widget.FDTankWidget;

import java.util.List;

public class FluidFillingEmiRecipe implements EmiRecipe
{
	private final ResourceLocation id;
	private final EmiIngredient fluid;
	private final EmiIngredient input;
	private final EmiStack output;

	public FluidFillingEmiRecipe(ResourceLocation id, EmiIngredient input, EmiIngredient fluid, EmiStack output) {
		this.id = id;
		this.fluid = fluid;
		this.input = input;
		this.output = output;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return FDRecipeCategories.FLUID_FILLING;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return List.of(input);
	}

	@Override
	public List<EmiStack> getOutputs() {
		return List.of(output);
	}

	@Override
	public int getDisplayWidth() {
		return 105;
	}

	@Override
	public int getDisplayHeight() {
		return 18;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.PLUS, 22, 3);
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 1);

		widgets.addSlot(input, 0, 0);
		widgets.add(new FDTankWidget(fluid, 39, 0, 1000));
		widgets.addSlot(output, 87, 0).recipeContext(this);
	}
}
