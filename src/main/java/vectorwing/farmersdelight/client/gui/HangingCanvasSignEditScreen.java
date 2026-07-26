package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class HangingCanvasSignEditScreen extends HangingSignEditScreen
{
	public HangingCanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
	}
}
