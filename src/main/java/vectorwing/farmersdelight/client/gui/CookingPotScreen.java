package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.tile.container.CookingPotContainer;
import vectorwing.farmersdelight.utils.TextUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CookingPotScreen extends ContainerScreen<CookingPotContainer>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(FarmersDelight.MODID, "textures/gui/cooking_pot.png");

	public CookingPotScreen(CookingPotContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.guiLeft = 0;
		this.guiTop = 0;
		this.xSize = 176;
		this.ySize = 166;
		this.titleX = 28;
	}

	@Override
	public void render(MatrixStack ms, final int mouseX, final int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderMealDisplayTooltip(ms, mouseX, mouseY);
	}

	protected void renderMealDisplayTooltip(MatrixStack ms, int mouseX, int mouseY) {
		if (this.minecraft != null && this.minecraft.player != null && this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
			if (this.hoveredSlot.slotNumber == 6) {
				List<ITextComponent> tooltip = new ArrayList<>();

				ItemStack meal = this.hoveredSlot.getStack();
				tooltip.add(((IFormattableTextComponent) meal.getItem().getName()).mergeStyle(meal.getRarity().color));

				ItemStack containerItem = this.container.tileEntity.getContainer();
				String container = !containerItem.isEmpty() ? containerItem.getItem().getName().getString() : "";

				tooltip.add(TextUtils.getTranslation("container.cooking_pot.served_on", container).mergeStyle(TextFormatting.GRAY));

				this.func_243308_b(ms, tooltip, mouseX, mouseY);
			} else {
				this.renderTooltip(ms, this.hoveredSlot.getStack(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(ms, mouseX, mouseY);
		this.font.drawString(ms, this.playerInventory.getDisplayName().getString(), 8.0f, (float) (this.ySize - 96 + 2), 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
		// Render UI background
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (this.minecraft == null)
			return;

		this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.blit(ms, x, y, 0, 0, this.xSize, this.ySize);

		// Render heat indicator
		if (this.container.isHeated()) {
			this.blit(ms, x + 47, y + 55, 176, 0, 17, 15);
		}

		// Render progress arrow
		int l = this.container.getCookProgressionScaled();
		this.blit(ms, this.guiLeft + 89, this.guiTop + 25, 176, 15, l + 1, 17);
	}
}
