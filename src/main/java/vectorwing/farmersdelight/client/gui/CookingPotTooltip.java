package vectorwing.farmersdelight.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CookingPotTooltip implements ClientTooltipComponent
{
	private static final int ITEM_SIZE = 16;
	private static final int MARGIN = 4;

	private final ItemStack mealStack;

	public CookingPotTooltip(CookingPotTooltipComponent tooltip) {
		this.mealStack = tooltip.mealStack;
	}

	@Override
	public int getHeight(Font font) {
		return mealStack.isEmpty() ? getLineHeight(font) : getLineHeight(font) + ITEM_SIZE;
	}

	@Override
	public int getWidth(Font font) {
		if (!mealStack.isEmpty()) {
			MutableComponent textServingsOf = mealStack.getCount() == 1
					? TextUtils.tooltip("cooking_pot.single_serving")
					: TextUtils.tooltip("cooking_pot.many_servings", mealStack.getCount());
			return Math.max(font.width(textServingsOf), font.width(mealStack.getHoverName()) + 20);
		} else {
			return font.width(TextUtils.tooltip("cooking_pot.empty"));
		}
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor gui) {
		if (mealStack.isEmpty()) return;
		gui.item(mealStack, x, y + getLineHeight(font), 0);
	}

	@Override
	public void extractText(GuiGraphicsExtractor gui, Font font, int x, int y) {
		Integer color = ChatFormatting.GRAY.getColor();
		int gray = color == null ? -1 : color;

		// TODO this might not work as expected. I'm guessing with most of the methods.
		if (!mealStack.isEmpty()) {
			MutableComponent textServingsOf = mealStack.getCount() == 1
				? TextUtils.tooltip("cooking_pot.single_serving")
				: TextUtils.tooltip("cooking_pot.many_servings", mealStack.getCount());

			gui.text(font, textServingsOf, x, y, gray);
			gui.text(font, mealStack.getHoverName(), x + ITEM_SIZE + MARGIN, y + getLineHeight(font) + MARGIN, -1);
		} else {
			MutableComponent textEmpty = TextUtils.tooltip("cooking_pot.empty");
			gui.text(font, textEmpty, x, y, gray, true);
		}
	}

	private int getLineHeight(Font font) {
		return font.lineHeight + 1;
	}

	public record CookingPotTooltipComponent(ItemStack mealStack) implements TooltipComponent
	{
	}
}
