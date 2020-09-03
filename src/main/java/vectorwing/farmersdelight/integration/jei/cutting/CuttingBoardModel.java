package vectorwing.farmersdelight.integration.jei.cutting;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vectorwing.farmersdelight.utils.Utils;

import java.util.function.Supplier;

public class CuttingBoardModel implements IDrawable
{
	private Supplier<ItemStack> supplier;
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
	public void draw(MatrixStack ms, int xOffset, int yOffset) {
		if (stack == null) {
			stack = supplier.get();
		}

		RenderHelper.enableStandardItemLighting();
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableDepthTest();
		RenderSystem.pushMatrix();
		RenderSystem.translated(xOffset, yOffset, 0);

		RenderSystem.pushMatrix();
		RenderSystem.translated(1, 1, 0);

		ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
		TextureManager textureManager = Minecraft.getInstance().textureManager;
		IBakedModel bakedmodel = renderer.getItemModelWithOverrides(stack, (World)null, (LivingEntity)null);
		Utils.renderItemIntoGUIScalable(stack, 48, 48, bakedmodel, renderer, textureManager);

		RenderSystem.popMatrix();

		RenderSystem.popMatrix();
		RenderSystem.enableBlend();
	}
}
