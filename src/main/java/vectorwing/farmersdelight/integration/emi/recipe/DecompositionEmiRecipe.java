package vectorwing.farmersdelight.integration.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.integration.emi.FDRecipeCategories;
import vectorwing.farmersdelight.integration.emi.FDRecipeWorkstations;

import java.util.List;

public class DecompositionEmiRecipe implements EmiRecipe {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/jei/decomposition.png");

    private static final EmiStack RICH_SOIL = EmiStack.of(ModItems.RICH_SOIL.get());
    private static final EmiIngredient ACCELERATORS = EmiIngredient.of(ModTags.COMPOST_ACTIVATORS);

    private static final ClientTooltipComponent LIGHT_TOOLTIP = createTooltip(".light");
    private static final ClientTooltipComponent FLUID_TOOLTIP = createTooltip(".fluid");
    private static final ClientTooltipComponent ACCELERATORS_TOOLTIP = createTooltip(".accelerators");

    @Override
    public EmiRecipeCategory getCategory() {
        return FDRecipeCategories.DECOMPOSITION;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return FarmersDelight.res("/decomposition/dummy");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(FDRecipeWorkstations.ORGANIC_COMPOST);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(RICH_SOIL);
    }

    @Override
    public int getDisplayWidth() {
        return 102;
    }

    @Override
    public int getDisplayHeight() {
        return 62;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0, 102, 41, 8, 9);

        addSlot(widgets, FDRecipeWorkstations.ORGANIC_COMPOST, 0, 16);
        addSlot(widgets, RICH_SOIL, 84, 16).recipeContext(this);
        addSlot(widgets, ACCELERATORS, 55, 44);

        widgets.addTooltip((mouseX, mouseY) -> {
            if (ClientRenderUtils.isCursorInsideBounds(32, 30, 11, 11, mouseX, mouseY)) {
                return List.of(LIGHT_TOOLTIP);
            } else if (ClientRenderUtils.isCursorInsideBounds(45, 30, 11, 11, mouseX, mouseY)) {
                return List.of(FLUID_TOOLTIP);
            } else if (ClientRenderUtils.isCursorInsideBounds(59, 30, 11, 11, mouseX, mouseY)) {
                return List.of(ACCELERATORS_TOOLTIP);
            }
            return List.of();
        }, 0, 0, widgets.getWidth(), widgets.getHeight());
    }

    private SlotWidget addSlot(WidgetHolder widgets, EmiIngredient ingredient, int x, int y) {
        return widgets.addSlot(ingredient, x, y).backgroundTexture(BACKGROUND, 119, 0);
    }

    private static ClientTooltipComponent createTooltip(@NotNull String suffix) {
        return ClientTooltipComponent.create(Component.translatable(FarmersDelight.MODID + ".jei.decomposition" + suffix).getVisualOrderText());
    }
}
