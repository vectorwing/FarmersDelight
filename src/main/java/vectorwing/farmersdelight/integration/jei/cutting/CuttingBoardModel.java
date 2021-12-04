package vectorwing.farmersdelight.integration.jei.cutting;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.utils.ClientRenderUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class CuttingBoardModel implements IDrawable
{
	private final Supplier<ItemStack> supplier;
	private ItemStack stack;

	public CuttingBoardModel(Supplier<ItemStack> supplier) {
		this.supplier = supplier;
	}

	@Override
	public int getWidth() {
		return 48;
	}

	@Override
	public int getHeight() {
		return 48;
	}

	@Override
	public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
		if (stack == null) {
			stack = supplier.get();
		}
		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(matrixStack.last().pose());
		RenderSystem.enableDepthTest();
		RenderHelper.turnBackOn();
		RenderSystem.pushMatrix();
		RenderSystem.translated(xOffset, yOffset, 0);
		RenderSystem.pushMatrix();
		RenderSystem.translated(1, 1, 0);

		Minecraft minecraft = Minecraft.getInstance();
		ItemRenderer itemRenderer = minecraft.getItemRenderer();
		itemRenderer.blitOffset += 50.0F;
		IBakedModel bakedmodel = itemRenderer.getModel(stack, null, null);
		TextureManager textureManager = Minecraft.getInstance().textureManager;
		ClientRenderUtils.renderItemIntoGUIScalable(stack, 48, 48, bakedmodel, itemRenderer, textureManager);
		itemRenderer.blitOffset -= 50.0F;

		RenderSystem.popMatrix();
		RenderSystem.popMatrix();
		RenderSystem.disableBlend();
		RenderHelper.turnOff();
		RenderSystem.popMatrix();
	}
}
