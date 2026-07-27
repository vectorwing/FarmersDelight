package vectorwing.farmersdelight.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
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
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends AbstractContainerScreen<CookingPotMenu> implements RecipeUpdateListener
{
	private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "textures/gui/cooking_pot.png");
	private static final int HEAT_ICON_X = 47;
	private static final int HEAT_ICON_Y = 55;
	private static final int HEAT_ICON_WIDTH = 17;
	private static final int HEAT_ICON_HEIGHT = 15;
	private static final int PROGRESS_ARROW_X = 89;
	private static final int PROGRESS_ARROW_Y = 25;
	private static final int PROGRESS_ARROW_HEIGHT = 17;

	private final CookingPotRecipeBookComponent recipeBookComponent;
	private boolean widthTooNarrow;
	private boolean recipeBookEnabled;

	public CookingPotScreen(CookingPotMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.recipeBookComponent = new CookingPotRecipeBookComponent(menu);
	}

	@Override
	protected void init() {
		super.init();
		this.widthTooNarrow = this.width < 379;
		this.recipeBookEnabled = Configuration.ENABLE_COOKING_POT_RECIPE_BOOK.get();
		this.titleLabelX = 28;
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow);
			this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
			this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, button -> {
				this.recipeBookComponent.toggleVisibility();
				this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
				button.setPosition(this.leftPos + 5, this.height / 2 - 49);
			}));
			this.addWidget(this.recipeBookComponent);
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		if (this.recipeBookEnabled && this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
			this.extractBackground(graphics, mouseX, mouseY, a);
		} else {
			super.extractContents(graphics, mouseX, mouseY, a);
		}

		graphics.nextStratum();
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.extractRenderState(graphics, mouseX, mouseY, a);
			graphics.nextStratum();
		}
		this.extractCarriedItem(graphics, mouseX, mouseY);
		this.extractTooltip(graphics, mouseX, mouseY);
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.extractTooltip(graphics, mouseX, mouseY, this.hoveredSlot);
		}
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		if (this.menu.isHeated()) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + HEAT_ICON_X, this.topPos + HEAT_ICON_Y, 176.0F, 0.0F, HEAT_ICON_WIDTH, HEAT_ICON_HEIGHT, 256, 256);
		}

		int progress = this.menu.getCookProgressionScaled();
		graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, this.leftPos + PROGRESS_ARROW_X, this.topPos + PROGRESS_ARROW_Y, 176.0F, 15.0F, progress + 1, PROGRESS_ARROW_HEIGHT, 256, 256);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor gui, int mouseX, int mouseY) {
		gui.text(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
		gui.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
	}

	@Override
	protected void extractSlots(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		super.extractSlots(graphics, mouseX, mouseY);
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.extractGhostRecipe(graphics, false);
		}
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		if (this.minecraft != null && this.minecraft.player != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
			if (this.hoveredSlot.index == CookingPotMenu.INDEX_MEAL) {
				graphics.setComponentTooltipForNextFrame(this.font, this.getMealDisplayTooltip(), mouseX, mouseY);
			} else {
				super.extractTooltip(graphics, mouseX, mouseY);
			}
		} else {
			super.extractTooltip(graphics, mouseX, mouseY);
		}

		if (this.isHovering(HEAT_ICON_X, HEAT_ICON_Y, HEAT_ICON_WIDTH, HEAT_ICON_HEIGHT, mouseX, mouseY)) {
			String key = "cooking_pot." + (this.menu.isHeated() ? "heated" : "not_heated");
			graphics.setTooltipForNextFrame(this.font, TextUtils.container(key), mouseX, mouseY);
		}
	}

	private List<Component> getMealDisplayTooltip() {
		List<Component> tooltip = new ArrayList<>();
		ItemStack mealStack = this.hoveredSlot.getItem();
		tooltip.add(mealStack.getHoverName());

		ItemStack containerStack = this.menu.blockEntity.getContainer();
		if (!containerStack.isEmpty()) {
			tooltip.add(TextUtils.container("cooking_pot.served_on", containerStack.getHoverName().getString()).withStyle(ChatFormatting.GRAY));
		}
		return tooltip;
	}

	@Override
	protected boolean isHovering(int left, int top, int width, int height, double mouseX, double mouseY) {
		return (!this.recipeBookEnabled || !this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(left, top, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (this.recipeBookEnabled && this.recipeBookComponent.mouseClicked(event, doubleClick)) {
			this.setFocused(this.recipeBookComponent);
			return true;
		}
		return this.recipeBookEnabled && this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
		return this.recipeBookEnabled && this.recipeBookComponent.mouseDragged(event, dx, dy) || super.mouseDragged(event, dx, dy);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		return this.recipeBookEnabled && this.recipeBookComponent.keyPressed(event) || super.keyPressed(event);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		return this.recipeBookEnabled && this.recipeBookComponent.charTyped(event) || super.charTyped(event);
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int left, int top) {
		return super.hasClickedOutside(mouseX, mouseY, left, top)
				&& (!this.recipeBookEnabled || this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight));
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int buttonNum, ContainerInput containerInput) {
		super.slotClicked(slot, slotId, buttonNum, containerInput);
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.slotClicked(slot);
		}
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.tick();
		}
	}

	@Override
	public void recipesUpdated() {
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.recipesUpdated();
		}
	}

	@Override
	public void fillGhostRecipe(RecipeDisplay display) {
		if (this.recipeBookEnabled) {
			this.recipeBookComponent.fillGhostRecipe(display);
		}
	}
}
