package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.container.CookingPotContainer;
import vectorwing.farmersdelight.utils.Text;

import java.util.ArrayList;
import java.util.List;

public class CookingPotScreen extends ContainerScreen<CookingPotContainer>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(FarmersDelight.MODID, "textures/gui/cooking_pot.png");

	public CookingPotScreen(CookingPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	protected void renderHoveredToolTip(int mouseX, int mouseY) {
		if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
			if (this.hoveredSlot.slotNumber == 6) {
				List<String> tooltip = new ArrayList<>();

				ItemStack meal = this.hoveredSlot.getStack();
				String container = meal.getContainerItem() != ItemStack.EMPTY ? meal.getContainerItem().getItem().getName().getFormattedText() : "";
				String served = Text.getTranslation("container.cooking_pot.served_on", container).applyTextStyle(TextFormatting.GRAY).getFormattedText();
				tooltip.add(meal.getItem().getName().applyTextStyle(meal.getRarity().color).getFormattedText());
				tooltip.add(served);

				this.renderTooltip(tooltip, mouseX, mouseY);
			} else {
				this.renderTooltip(this.hoveredSlot.getStack(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString(this.title.getFormattedText(), 28.0f, 6.0f, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0f, (float)(this.ySize - 96 + 2), 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// Render UI background
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.blit(x, y, 0, 0, this.xSize, this.ySize);

		// Render heat indicator
		if (((CookingPotContainer)this.container).isHeated()) {
			this.blit(x + 47, y + 55, 176, 0, 17, 15);
		}

		// Render progress arrow
		int l = ((CookingPotContainer)this.container).getCookProgressionScaled();
		this.blit(this.guiLeft + 89, this.guiTop + 25, 176, 15, l + 1, 16);
	}
}
