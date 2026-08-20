package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import vectorwing.farmersdelight.common.block.entity.container.JugMenu;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.awt.*;

public class JugScreen extends AbstractContainerScreen<JugMenu>
{
	private static final ResourceLocation TEXTURE_BACKGROUND = RecipeUtils.FDLocation("textures/gui/jug.png");
	private static final ResourceLocation TEXTURE_BUCKET_METER = RecipeUtils.FDLocation("textures/gui/sprites/jug/bucket_meter.png");
	private static final ResourceLocation TEXTURE_BOTTLE_METER = RecipeUtils.FDLocation("textures/gui/sprites/jug/bottle_meter.png");
	private static final ResourceLocation TEXTURE_FLUID_RULER = RecipeUtils.FDLocation("textures/gui/sprites/jug/fluid_ruler.png");

	private static final Rectangle BUCKET_METER = new Rectangle(104, 46, 16, 16);
	private static final Rectangle BOTTLE_METER = new Rectangle(104, 66, 16, 16);
	private static final Rectangle FLUID_RULER = new Rectangle(76, 16, 24, 66);

	public JugScreen(JugMenu screenContainer, Inventory inventory, Component title) {
		super(screenContainer, inventory, title);
		this.imageHeight = 178;
		this.titleLabelX = 78;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		if (this.minecraft == null)
			return;

		guiGraphics.blit(TEXTURE_BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		guiGraphics.blit(TEXTURE_BUCKET_METER, this.leftPos + BUCKET_METER.x, this.topPos + BUCKET_METER.y,
			0, 0, 16, 16, 16, 16);
		guiGraphics.blit(TEXTURE_BOTTLE_METER, this.leftPos + BOTTLE_METER.x, this.topPos + BOTTLE_METER.y,
			0, 0, 16, 16, 16, 16);

		// TODO: Render the stored fluid between these!

		guiGraphics.blit(TEXTURE_FLUID_RULER, this.leftPos + FLUID_RULER.x, this.topPos + FLUID_RULER.y,
			0, 0, FLUID_RULER.width, FLUID_RULER.height, FLUID_RULER.width, FLUID_RULER.height);
	}
}
