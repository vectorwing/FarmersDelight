package vectorwing.farmersdelight.blocks.signs;

import net.minecraft.item.DyeColor;

import javax.annotation.Nullable;

public interface ICanvasSign
{
	/**
	 * Returns this sign's background dye color. If null, the sign is uncolored (beige).
	 */
	@Nullable
	DyeColor getBackgroundColor();
}
