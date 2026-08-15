package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.state.CanvasSign;

public class HangingCanvasSignEditScreen extends HangingSignEditScreen
{
	private static final String TEXTURE_PATH = "textures/gui/hanging_signs/";

	private final Identifier texture;

	public HangingCanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
		this.texture = getTexture(signBlockEntity.getBlockState());
	}

	@Override
	protected void extractSignBackground(GuiGraphicsExtractor graphics) {
		graphics.pose().translate(0.0F, -13.0F);
		graphics.pose().scale(MAGIC_BACKGROUND_SCALE, MAGIC_BACKGROUND_SCALE);
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, -8, -8, 0.0F, 0.0F, 16, 16, 16, 16);
	}

	private static Identifier getTexture(BlockState state) {
		DyeColor backgroundColor = state.getBlock() instanceof CanvasSign canvasSign ? canvasSign.getBackgroundColor() : null;
		String textureName = backgroundColor != null ? "canvas_" + backgroundColor.getName() : "canvas";
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, TEXTURE_PATH + textureName + ".png");
	}
}
