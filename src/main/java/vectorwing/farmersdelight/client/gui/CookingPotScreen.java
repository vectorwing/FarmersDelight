package vectorwing.farmersdelight.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends AbstractContainerScreen<CookingPotMenu> implements RecipeUpdateListener
{
	private static final WidgetSprites RECIPE_BUTTON = new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/button"), Identifier.withDefaultNamespace("recipe_book/button"));
	private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/cooking_pot.png");
	private static final Rectangle HEAT_ICON = new Rectangle(47, 55, 17, 15);
	private static final Rectangle PROGRESS_ARROW = new Rectangle(89, 25, 0, 17);

	private final CookingPotRecipeBookComponent recipeBookComponent = new CookingPotRecipeBookComponent(this.menu);
	private boolean widthTooNarrow;

	public CookingPotScreen(CookingPotMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.titleLabelX = 28;
		this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow);
		this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
		if (Configuration.ENABLE_COOKING_POT_RECIPE_BOOK.get()) {
			this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, RECIPE_BUTTON, (button) ->
			{
				this.recipeBookComponent.toggleVisibility();
				this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
				button.setPosition(this.leftPos + 5, this.height / 2 - 49);
			}));
			this.addWidget(this.recipeBookComponent);
			this.setInitialFocus(this.recipeBookComponent);
		} else {
			this.recipeBookComponent.hide();
			this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		this.recipeBookComponent.tick();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor gui, final int mouseX, final int mouseY, float partialTicks) {
		if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
			this.extractBackground(gui, mouseX, mouseY, partialTicks);
		} else {
			super.extractContents(gui, mouseX, mouseY, partialTicks);
		}

		gui.nextStratum();
		this.recipeBookComponent.extractRenderState(gui, mouseX, mouseY, partialTicks);
		gui.nextStratum();
		this.extractCarriedItem(gui, mouseX, mouseY);
		this.extractSnapbackItem(gui);
		this.extractTooltip(gui, mouseX, mouseY);
		this.recipeBookComponent.extractTooltip(gui, mouseX, mouseY, this.hoveredSlot);
	}

	@Override
	protected void extractSlots(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		super.extractSlots(gui, mouseX, mouseY);
		this.recipeBookComponent.extractGhostRecipe(gui, true);
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		if (this.isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
			String key = "cooking_pot." + (this.menu.isHeated() ? "heated" : "not_heated");
			gui.setTooltipForNextFrame(this.font, TextUtils.container(key), mouseX, mouseY);
			return;
		}

		if (this.minecraft != null && this.minecraft.player != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem() && this.hoveredSlot.index == CookingPotMenu.INDEX_MEAL) {
			List<Component> tooltip = new ArrayList<>();

			ItemStack mealStack = this.hoveredSlot.getItem();
			tooltip.add(mealStack.getItem().getName(mealStack).copy().withStyle(mealStack.getRarity().getStyleModifier()));

			ItemStack containerStack = this.menu.blockEntity.getContainer();
			if (!containerStack.isEmpty()) {
				String container = containerStack.getItem().getName(containerStack).getString();
				tooltip.add(TextUtils.container("cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
			}

			gui.setComponentTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
			return;
		}

		super.extractTooltip(gui, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor gui, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(gui, mouseX, mouseY, partialTicks);
		if (this.minecraft == null)
			return;

		// Render UI background
		gui.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		// Render heat icon
		if (this.menu.isHeated()) {
			gui.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON.x, this.topPos + HEAT_ICON.y, 176.0F, 0.0F, HEAT_ICON.width, HEAT_ICON.height, 256, 256);
		}

		// Render progress arrow
		int l = this.menu.getCookProgressionScaled();
		gui.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW.x, this.topPos + PROGRESS_ARROW.y, 176.0F, 15.0F, l + 1, PROGRESS_ARROW.height, 256, 256);
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
	public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
		return this.recipeBookComponent.mouseDragged(event, dx, dy) || super.mouseDragged(event, dx, dy);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		return this.recipeBookComponent.charTyped(event) || super.charTyped(event);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		return this.recipeBookComponent.keyPressed(event) || super.keyPressed(event);
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

	@Override
	public void fillGhostRecipe(RecipeDisplay display) {
		this.recipeBookComponent.fillGhostRecipe(display);
	}
}
