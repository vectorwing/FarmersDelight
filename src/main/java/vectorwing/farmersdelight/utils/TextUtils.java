package vectorwing.farmersdelight.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * Util for obtaining and formatting ITextComponents for use across the mod.
 */
public class TextUtils
{
	/**
	 * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
	 */
	public static ITextComponent getTranslation(String key, Object... args) {
		return new TranslationTextComponent(FarmersDelight.MODID + "." + key, args);
	}
}
