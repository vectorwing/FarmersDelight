package vectorwing.farmersdelight.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends AbstractRecipeBookScreen<CookingPotMenu>
{
	private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/cooking_pot.png");
	private static final Rectangle HEAT_ICON = new Rectangle(47, 55, 17, 15);
	private static final Rectangle PROGRESS_ARROW = new Rectangle(89, 25, 0, 17);

	private boolean widthTooNarrow;

	public CookingPotScreen(CookingPotMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, new CookingPotRecipeBookComponent(screenContainer), inv, titleIn);
	}

	@Override
	public void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.titleLabelX = 28;
		this.setInitialFocus(this.recipeBookComponent);
	}

	@Override
	protected ScreenPosition getRecipeBookButtonPosition() {
		return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49);
	}

	@Override
	public void containerTick() {
		super.containerTick();
		this.recipeBookComponent.tick();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);
	}

	@Override
	public void extractContents(GuiGraphicsExtractor gui, int mouseX, int mouseY, float a) {
		super.extractContents(gui, mouseX, mouseY, a);
		this.renderMealDisplayTooltip(gui, mouseX, mouseY);
		this.renderHeatIndicatorTooltip(gui, mouseX, mouseY);
	}

	private void renderHeatIndicatorTooltip(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
			String key = "cooking_pot." + (this.menu.isHeated() ? "heated" : "not_heated");
			ClientTooltipComponent tooltip = ClientTooltipComponent.create(TextUtils.container(key).getVisualOrderText());
			gui.tooltip(this.font, List.of(tooltip), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
		}
	}

	protected void renderMealDisplayTooltip(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		if (this.minecraft != null && this.minecraft.player != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
			if (this.hoveredSlot.index == 6) {
				List<ClientTooltipComponent> tooltip = new ArrayList<>();

				ItemStack mealStack = this.hoveredSlot.getItem();
				tooltip.add(ClientTooltipComponent.create(Component.translatable(mealStack.getItem().getDescriptionId()).withStyle(mealStack.getRarity().getStyleModifier()).getVisualOrderText()));

				ItemStack containerStack = this.menu.blockEntity.getContainer();
				if (!containerStack.isEmpty()) {
					String container = !containerStack.isEmpty() ? Component.translatable(containerStack.getItem().getDescriptionId()).getString() : "";
					tooltip.add(ClientTooltipComponent.create(TextUtils.container("cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY).getVisualOrderText()));
				}

				gui.tooltip(font, tooltip, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
			} else {
				gui.tooltip(font, List.of(), mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null, this.hoveredSlot.getItem());
			}
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		super.extractLabels(gui, mouseX, mouseY);
		gui.text(this.font, this.playerInventoryTitle, 8, (this.imageHeight - 96 + 2), 4210752, false);
	}


	@Override
	public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float a) {
		// Render UI background

		//TODO i don't know the equivalent of this
		//RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		if (this.minecraft == null)
			return;

		gui.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

		// Render heat icon
		if (this.menu.isHeated()) {
			gui.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 176, 0, HEAT_ICON.width, HEAT_ICON.height, 256, 256);
		}

		// Render progress arrow
		int l = this.menu.getCookProgressionScaled();
		gui.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176, 15, l + 1, PROGRESS_ARROW.height, 256, 256);
	}

	@Override
	protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
		return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {

		if (this.recipeBookComponent.mouseClicked(event, doubleClick)) {
			this.setFocused(this.recipeBookComponent);
			return true;
		}
		return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(event, doubleClick);
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int x, int y) {
		boolean flag = mouseX < (double) x || mouseY < (double) y || mouseX >= (double) (x + this.imageWidth) || mouseY >= (double) (y + this.imageHeight);
		return flag && this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
		super.slotClicked(slot, slotId, buttonNum, containerInput);
		this.recipeBookComponent.slotClicked(slot);
	}

	@Override
	public void recipesUpdated() {
		this.recipeBookComponent.recipesUpdated();
	}
}
