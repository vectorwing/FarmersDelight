package vectorwing.farmersdelight.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.RecipeUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.emi.FDRecipeCategories;

import java.util.List;

public class SoakingEmiRecipe implements EmiRecipe
{
	private static final ResourceLocation BACKGROUND = RecipeUtils.FDLocation("textures/gui/jei/soaking.png");
	private static final ResourceLocation FLUID_RULER = RecipeUtils.FDLocation("textures/gui/sprites/jug/fluid_ruler.png");
	private static final ResourceLocation FLUID_CONSUMED = RecipeUtils.FDLocation("textures/gui/sprites/jug/fluid_consumed.png");
	private static final ResourceLocation FLUID_NOT_CONSUMED = RecipeUtils.FDLocation("textures/gui/sprites/jug/fluid_not_consumed.png");

	private final ResourceLocation id;
	private final EmiIngredient fluid;
	private final EmiIngredient input;
	private final EmiStack output;
	private final boolean consumesFluid;

	public SoakingEmiRecipe(ResourceLocation id, EmiIngredient fluid, EmiIngredient input, EmiStack output, boolean consumesFluid) {
		this.id = id;
		this.fluid = fluid;
		this.input = input;
		this.output = output;
		this.consumesFluid = consumesFluid;
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return FDRecipeCategories.SOAKING;
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
		return 63;
	}

	@Override
	public int getDisplayHeight() {
		return 66;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int width = getDisplayWidth();
		int height = getDisplayHeight();
		widgets.addTexture(BACKGROUND, 0, 0, width, height, 0, 0, width, height, width, height);

		widgets.addSlot(input, 0, 0).drawBack(false);
		widgets.addSlot(output, 0, 48).drawBack(false).recipeContext(this);
		widgets.addTank(fluid, 26, 0, 24, 66, JugBlockEntity.JUG_CAPACITY).drawBack(false);
		widgets.addTexture(FLUID_RULER, 26, 0, 24, 66, 0, 0, 24, 66, 24, 66);
		widgets.addTexture(consumesFluid ? FLUID_CONSUMED : FLUID_NOT_CONSUMED, 50, 51, 13, 15, 0, 0, 13, 15, 13, 15);

		widgets.addTooltip((mouseX, mouseY) -> {
			if (ClientRenderUtils.isCursorInsideBounds(50, 51, 13, 15, mouseX, mouseY)) {
				return List.of(createConsumeFluidTooltip(consumesFluid));
			}
			return List.of();
		}, 0, 0, widgets.getWidth(), widgets.getHeight());
	}

	private static ClientTooltipComponent createConsumeFluidTooltip(boolean consumesFluid) {
		return ClientTooltipComponent.create(TextUtils.JEI("soaking." + (consumesFluid ? "fluid_consumed" : "fluid_not_consumed")).getVisualOrderText());
	}
}
