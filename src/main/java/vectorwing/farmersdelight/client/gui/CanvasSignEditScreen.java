package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.SignBlockEntity;

/**
 * Edit screen for canvas (standing/wall) signs.
 * <p>
 * In MC 26.1 the standing-sign preview is rendered through the picture-in-picture
 * {@code GuiSignRenderer}, which always resolves its texture from
 * {@code Sheets.getSignSprite(woodType)} and offers no hook to substitute the dyed
 * canvas material. The previous implementation rendered the sign model directly with a
 * custom {@code Material}; that path no longer exists ({@code GuiGraphics#pose()} is now a
 * 2D {@code Matrix3x2fStack} and {@code SignRenderer} was removed). We therefore fall back
 * to the vanilla standing-sign preview (correct wood type, no canvas tint). The in-world
 * block still renders its canvas texture via {@code CanvasSignRenderer}.
 * <p>
 * To restore the dyed preview, register a custom canvas sign
 * {@code PictureInPictureRenderer} (RegisterPictureInPictureRenderersEvent) that uses
 * {@code ModAtlases.getCanvasSignMaterial(dye)} and submit it from {@code extractSignBackground}.
 */
public class CanvasSignEditScreen extends SignEditScreen
{
	public CanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
	}
}
