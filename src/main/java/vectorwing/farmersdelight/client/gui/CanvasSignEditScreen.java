package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.state.CanvasSign;

public class CanvasSignEditScreen extends SignEditScreen
{
	private static final String TEXTURE_PATH = "textures/block/";
	private static final int TEXTURE_SIZE = 32;
	private static final int BOARD_U = 0;
	private static final int FRONT_BOARD_V = 2;
	private static final int BACK_BOARD_V = 16;
	private static final int BOARD_WIDTH = 24;
	private static final int BOARD_HEIGHT = 12;
	private static final int POST_U = 28;
	private static final int FRONT_POST_V = 0;
	private static final int BACK_POST_V = 16;
	private static final int POST_WIDTH = 2;
	private static final int POST_HEIGHT = 14;

	private final Identifier texture;
	private final boolean showPost;
	private final boolean isFront;

	public CanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
		BlockState state = signBlockEntity.getBlockState();
		this.texture = getTexture(state);
		this.showPost = state.getBlock() instanceof StandingSignBlock;
		this.isFront = isFront;
	}

	@Override
	protected void extractSignBackground(GuiGraphicsExtractor graphics) {
		graphics.pose().translate(0.0F, 27.0F);
		graphics.pose().scale(MAGIC_BACKGROUND_SCALE, MAGIC_BACKGROUND_SCALE);
		int boardV = this.isFront ? FRONT_BOARD_V : BACK_BOARD_V;
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, -12, -13, BOARD_U, boardV, BOARD_WIDTH, BOARD_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
		if (this.showPost) {
			int postV = this.isFront ? FRONT_POST_V : BACK_POST_V;
			graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, -1, -1, POST_U, postV, POST_WIDTH, POST_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
		}
	}

	private static Identifier getTexture(BlockState state) {
		DyeColor backgroundColor = state.getBlock() instanceof CanvasSign canvasSign ? canvasSign.getBackgroundColor() : null;
		String textureName = backgroundColor != null ? backgroundColor.getName() + "_canvas_sign" : "canvas_sign";
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, TEXTURE_PATH + textureName + ".png");
	}
}
