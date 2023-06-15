//package vectorwing.farmersdelight.integration.jei.resource;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import mezz.jei.api.gui.drawable.IDrawable;
//import net.minecraft.MethodsReturnNonnullByDefault;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.renderer.entity.ItemRenderer;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.world.item.ItemStack;
//import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
//
//import javax.annotation.ParametersAreNonnullByDefault;
//import java.util.function.Supplier;
//
//@ParametersAreNonnullByDefault
//@MethodsReturnNonnullByDefault
//public class CuttingBoardDrawable implements IDrawable
//{
//	private final Supplier<ItemStack> supplier;
//	private ItemStack stack;
//
//	public CuttingBoardDrawable(Supplier<ItemStack> supplier) {
//		this.supplier = supplier;
//	}
//
//	@Override
//	public int getWidth() {
//		return 48;
//	}
//
//	@Override
//	public int getHeight() {
//		return 48;
//	}
//
//	@Override
//	public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
//		if (stack == null) {
//			stack = supplier.get();
//		}
////		RenderSystem.pushMatrix();
////		RenderSystem.multMatrix(matrixStack.last().pose());
////		RenderSystem.enableDepthTest();
////		Lighting.turnBackOn();
////		RenderSystem.pushMatrix();
////		RenderSystem.translated(xOffset, yOffset, 0);
////		RenderSystem.pushMatrix();
////		RenderSystem.translated(1, 1, 0);
//
//		Minecraft minecraft = Minecraft.getInstance();
//		ItemRenderer itemRenderer = minecraft.getItemRenderer();
//		itemRenderer.blitOffset += 50.0F;
//		BakedModel bakedmodel = itemRenderer.getModel(stack, null, null, 0);
//		TextureManager textureManager = Minecraft.getInstance().textureManager;
//		ClientRenderUtils.renderItemIntoGUIScalable(stack, 48.0F, 48.0F, bakedmodel, itemRenderer, textureManager);
//		itemRenderer.blitOffset -= 50.0F;
//
////		RenderSystem.popMatrix();
////		RenderSystem.popMatrix();
////		RenderSystem.disableBlend();
////		Lighting.turnOff();
////		RenderSystem.popMatrix();
//	}
//}
