package vectorwing.farmersdelight.integration.emi.widget;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.TankWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

/**
 * Fixed-size TankWidget, with a drawn fluid ruler overlay.
 * Used by Farmer's Delight to represent a "fluid stack" in EMI widgets.
 */
public class FDTankWidget extends TankWidget
{
	private static final ResourceLocation FLUID_SLOT_OVERLAY = RecipeUtils.FDLocation("textures/gui/sprites/fluid_slot_overlay.png");

	public FDTankWidget(EmiIngredient stack, int x, int y, long capacity) {
		super(stack, x, y, 18, 18, capacity);
	}

	@Override
	public void drawStack(GuiGraphics draw, int mouseX, int mouseY, float delta) {
		super.drawStack(draw, mouseX, mouseY, delta);
		draw.blit(FLUID_SLOT_OVERLAY, bounds.x(), bounds.y(), 0, 0, bounds.width(), bounds.height(), 18, 18);
	}
}
