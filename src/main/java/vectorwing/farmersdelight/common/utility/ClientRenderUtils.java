package vectorwing.farmersdelight.common.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

/**
 * Util for helping with rendering elements across the mod.
 */
public class ClientRenderUtils
{
	public static boolean isCursorInsideBounds(int iconX, int iconY, int iconWidth, int iconHeight, double cursorX, double cursorY) {
		return iconX <= cursorX && cursorX < iconX + iconWidth && iconY <= cursorY && cursorY < iconY + iconHeight;
	}

	// Necessary to make sure that the server won't load this value whilst it is in a server loaded class.
	public static Player getClientPlayerHack() {
		return Minecraft.getInstance().player;
	}
}
