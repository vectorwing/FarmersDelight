package vectorwing.farmersdelight.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CookingPotTooltip implements ClientTooltipComponent
{
	private static final int ITEM_SIZE = 16;
	private static final int MARGIN = 4;

	private final int textSpacing = Minecraft.getInstance().font.lineHeight + 1;
	private final ItemStack mealStack;

	public CookingPotTooltip(CookingPotTooltipComponent tooltip) {
		this.mealStack = tooltip.mealStack;
	}

	@Override
	public int getHeight() {
		return mealStack.isEmpty() ? textSpacing : textSpacing + ITEM_SIZE;
	}

	@Override
	public int getWidth(Font font) {
		if (!mealStack.isEmpty()) {
			MutableComponent textServingsOf = mealStack.getCount() == 1
					? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
					: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getCount());
			return Math.max(font.width(textServingsOf), font.width(mealStack.getHoverName()) + 20);
		} else {
			return font.width(TextUtils.getTranslation("tooltip.cooking_pot.empty"));
		}
	}

	@Override
	public void renderImage(Font font, int mouseX, int mouseY, GuiGraphics gui) {
		if (mealStack.isEmpty()) return;
		gui.renderItem(mealStack, mouseX, mouseY + textSpacing, 0);
	}

	@Override
	public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
		Integer color = ChatFormatting.GRAY.getColor();
		int gray = color == null ? -1 : color;

		if (!mealStack.isEmpty()) {
			MutableComponent textServingsOf = mealStack.getCount() == 1
					? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
					: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getCount());

			font.drawInBatch(textServingsOf, (float) x, (float) y, gray, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
			font.drawInBatch(mealStack.getHoverName(), x + ITEM_SIZE + MARGIN, y + textSpacing + MARGIN, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
		} else {
			MutableComponent textEmpty = TextUtils.getTranslation("tooltip.cooking_pot.empty");
			font.drawInBatch(textEmpty, x, y, gray, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
		}
	}

	public static record CookingPotTooltipComponent(ItemStack mealStack) implements TooltipComponent
	{
	}
}
