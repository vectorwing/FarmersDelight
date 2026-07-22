package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;

/**
 * 26.1 fallback editor for standing canvas signs.
 * Custom canvas background rendering will be restored after the new GUI PIP renderer is wired.
 */
public class CanvasSignEditScreen extends SignEditScreen
{
	public CanvasSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean isTextFilteringEnabled) {
		super(sign, isFrontText, isTextFilteringEnabled);
	}
}
