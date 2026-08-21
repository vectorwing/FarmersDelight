package vectorwing.farmersdelight.client.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;

public class ScreenUtils
{
	public static void drawTiledSprite(GuiGraphics guiGraphics, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite,
									   int textureWidth, int textureHeight, int zLevel, TilingDirection tilingDirection) {
		drawTiledSprite(guiGraphics, xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, textureWidth, textureHeight, zLevel, tilingDirection, true);
	}

	public static void drawTiledSprite(GuiGraphics guiGraphics, int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite,
									   int textureWidth, int textureHeight, int zLevel, TilingDirection tilingDirection, boolean blend) {
		if (desiredWidth == 0 || desiredHeight == 0 || textureWidth == 0 || textureHeight == 0) {
			return;
		}
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, sprite.atlasLocation());
		int xTileCount = desiredWidth / textureWidth;
		int xRemainder = desiredWidth - (xTileCount * textureWidth);
		int yTileCount = desiredHeight / textureHeight;
		int yRemainder = desiredHeight - (yTileCount * textureHeight);
		int yStart = yPosition + yOffset;
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		if (blend) {
			RenderSystem.enableBlend();
		}
		//Note: We still use the tesselator as that is what GuiGraphics#innerBlit does
		BufferBuilder vertexBuffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		Matrix4f matrix4f = guiGraphics.pose().last().pose();
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			int width = (xTile == xTileCount) ? xRemainder : textureWidth;
			if (width == 0) {
				break;
			}
			int x = xPosition + (xTile * textureWidth);
			int maskRight = textureWidth - width;
			int shiftedX = x + textureWidth - maskRight;
			float uLocalDif = uDif * maskRight / textureWidth;
			float uLocalMin;
			float uLocalMax;
			if (tilingDirection.right) {
				uLocalMin = uMin;
				uLocalMax = uMax - uLocalDif;
			} else {
				uLocalMin = uMin + uLocalDif;
				uLocalMax = uMax;
			}
			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int height = (yTile == yTileCount) ? yRemainder : textureHeight;
				if (height == 0) {
					//Note: We don't want to fully break out because our height will be zero if we are looking to
					// draw the remainder, but there is no remainder as it divided evenly
					break;
				}
				int y = yStart - ((yTile + 1) * textureHeight);
				int maskTop = textureHeight - height;
				float vLocalDif = vDif * maskTop / textureHeight;
				float vLocalMin;
				float vLocalMax;
				if (tilingDirection.down) {
					vLocalMin = vMin;
					vLocalMax = vMax - vLocalDif;
				} else {
					vLocalMin = vMin + vLocalDif;
					vLocalMax = vMax;
				}
				vertexBuffer.addVertex(matrix4f, x, y + textureHeight, zLevel)
					.setUv(uLocalMin, vLocalMax);
				vertexBuffer.addVertex(matrix4f, shiftedX, y + textureHeight, zLevel)
					.setUv(uLocalMax, vLocalMax);
				vertexBuffer.addVertex(matrix4f, shiftedX, y + maskTop, zLevel)
					.setUv(uLocalMax, vLocalMin);
				vertexBuffer.addVertex(matrix4f, x, y + maskTop, zLevel)
					.setUv(uLocalMin, vLocalMin);
			}
		}
		BufferUploader.drawWithShader(vertexBuffer.buildOrThrow());
		if (blend) {
			RenderSystem.disableBlend();
		}
	}

	public enum TilingDirection {
		/**
		 * Textures are being tiled/filled from top left to bottom right.
		 */
		DOWN_RIGHT(true, true),
		/**
		 * Textures are being tiled/filled from top right to bottom left.
		 */
		DOWN_LEFT(true, false),
		/**
		 * Textures are being tiled/filled from bottom left to top right.
		 */
		UP_RIGHT(false, true),
		/**
		 * Textures are being tiled/filled from bottom right to top left.
		 */
		UP_LEFT(false, false);

		private final boolean down;
		private final boolean right;

		TilingDirection(boolean down, boolean right) {
			this.down = down;
			this.right = right;
		}
	}
}
