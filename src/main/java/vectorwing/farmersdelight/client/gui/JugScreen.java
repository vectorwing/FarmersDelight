package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import vectorwing.farmersdelight.client.utility.ScreenUtils;
import vectorwing.farmersdelight.common.block.entity.container.JugMenu;
import vectorwing.farmersdelight.common.utility.FluidUtils;
import vectorwing.farmersdelight.common.utility.RecipeUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.awt.*;

public class JugScreen extends AbstractContainerScreen<JugMenu>
{
	private static final ResourceLocation TEXTURE_BACKGROUND = RecipeUtils.FDLocation("textures/gui/jug.png");
	private static final ResourceLocation TEXTURE_BUCKET_METER = RecipeUtils.FDLocation("textures/gui/sprites/jug/bucket_meter.png");
	private static final ResourceLocation TEXTURE_BOTTLE_METER = RecipeUtils.FDLocation("textures/gui/sprites/jug/bottle_meter.png");
	private static final ResourceLocation TEXTURE_FLUID_RULER = RecipeUtils.FDLocation("textures/gui/sprites/jug/fluid_ruler.png");
	private static final ResourceLocation TEXTURE_SOAKING_PROGRESS = RecipeUtils.FDLocation("textures/gui/sprites/jug/soaking_progress.png");

	private static final Rectangle BUCKET_METER = new Rectangle(104, 46, 16, 16);
	private static final Rectangle BOTTLE_METER = new Rectangle(104, 66, 16, 16);
	private static final Rectangle FLUID_RULER = new Rectangle(76, 16, 24, 66);
	private static final Rectangle FLUID_RATIO = new Rectangle(104, 46, 32, 37);
	private static final Rectangle SOAKING_PROGRESS = new Rectangle(53, 36, 12, 24);

	private static final int FLUID_METER_HEIGHT = 64;

	public JugScreen(JugMenu screenContainer, Inventory inventory, Component title) {
		super(screenContainer, inventory, title);
		this.imageHeight = 178;
		this.titleLabelX = 78;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		int titleCenterOffset = (-this.font.width(title)) / 2;
		this.titleLabelX = 88 + titleCenterOffset;
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		this.renderFluidMeterTooltip(guiGraphics, mouseX, mouseY);
		this.renderFluidRatioTooltip(guiGraphics, mouseX, mouseY);
	}

	private void renderFluidMeterTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		if (this.isHovering(FLUID_RULER.x, FLUID_RULER.y, FLUID_RULER.width, FLUID_RULER.height, mouseX, mouseY)) {
			FluidStack fluid = this.menu.fluidTank.getFluid();
			gui.renderTooltip(this.font, TextUtils.container(fluid.isEmpty() ? "jug.empty" : "jug.fluid", fluid.getFluidType().getDescription(), fluid.getAmount()), mouseX, mouseY);
		}
	}

	private void renderFluidRatioTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		if (this.isHovering(FLUID_RATIO.x, FLUID_RATIO.y, FLUID_RATIO.width, FLUID_RATIO.height, mouseX, mouseY)) {
			// TODO: The bucket/bottle ratio should probably be configurable, even if recipes don't need to stick to it.
			gui.renderTooltip(this.font, TextUtils.container("jug.ratio", 4), mouseX, mouseY);
		}
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

		FluidStack fluidStack = menu.fluidTank.getFluid();
		int fluidAmount = menu.fluidTank.getFluidAmount();
		if (!fluidStack.isEmpty()) {
			IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluidStack.getFluid());
			TextureAtlasSprite fluidSprite = this.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(clientFluid.getStillTexture(fluidStack));
			int tintColor = clientFluid.getTintColor(fluidStack);

			float alpha = ((tintColor >> 24) & 0xFF) / 255f;
			float red = ((tintColor >> 16) & 0xFF) / 255f;
			float green = ((tintColor >> 8) & 0xFF) / 255f;
			float blue = (tintColor & 0xFF) / 255f;

			float fillLevel = (float) fluidAmount / menu.fluidTank.getCapacity();
			int meterHeight = (int) (fillLevel * FLUID_METER_HEIGHT);
			guiGraphics.setColor(red, green, blue, alpha);
			ScreenUtils.drawTiledSprite(guiGraphics, this.leftPos + 77, this.topPos + 81, 0, 22, meterHeight, fluidSprite, 16, 16, 0, ScreenUtils.TilingDirection.UP_RIGHT);

			// Draw a "surface tension" line at the top of the fluid?

			guiGraphics.setColor(1, 1, 1, 1);
		}

		guiGraphics.blit(TEXTURE_FLUID_RULER, this.leftPos + FLUID_RULER.x, this.topPos + FLUID_RULER.y,
			0, 0, FLUID_RULER.width, FLUID_RULER.height, FLUID_RULER.width, FLUID_RULER.height);

		int progress = this.menu.getProgressScaled();
		guiGraphics.blit(TEXTURE_SOAKING_PROGRESS, this.leftPos + SOAKING_PROGRESS.x, this.topPos + SOAKING_PROGRESS.y,
			0, 0, SOAKING_PROGRESS.width, progress + 1, SOAKING_PROGRESS.width, SOAKING_PROGRESS.height);

		guiGraphics.drawString(font, String.valueOf(FluidUtils.getBucketAmount(fluidAmount)), this.leftPos + 121, this.topPos + 51, 16777215, true);
		guiGraphics.drawString(font, String.valueOf(FluidUtils.getBottleAmount(fluidAmount)), this.leftPos + 121, this.topPos + 71, 16777215, true);
	}
}
