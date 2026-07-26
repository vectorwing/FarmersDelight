package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class CanvasSignEditScreen extends SignEditScreen
{
	public CanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
	}
}
