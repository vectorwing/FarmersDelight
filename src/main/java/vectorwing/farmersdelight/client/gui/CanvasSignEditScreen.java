package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

import javax.annotation.Nullable;

public class CanvasSignEditScreen extends SignEditScreen
{
	@Nullable
	protected SignRenderer.SignModel signModel;
	@Nullable
	protected DyeColor dye;
	protected final boolean isFrontText;

	public CanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
		Block block = signBlockEntity.getBlockState().getBlock();
		if (block instanceof CanvasSign canvasSign) {
			this.dye = canvasSign.getBackgroundColor();
		}
		this.isFrontText = isFront;
	}

	protected void init() {
		super.init();
		this.signModel = SignRenderer.createSignModel(this.minecraft.getEntityModels(), this.woodType);
	}

	protected void renderSignBackground(GuiGraphics guiGraphics, BlockState state) {
		if (this.signModel != null) {
			boolean flag = state.getBlock() instanceof StandingSignBlock;
			guiGraphics.pose().translate(0.0F, 31.0F, 0.0F);
			if (!isFrontText) {
				guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(180));
			}
			guiGraphics.pose().scale(MAGIC_SCALE_NUMBER, MAGIC_SCALE_NUMBER, -MAGIC_SCALE_NUMBER);
			Material material = ModAtlases.getCanvasSignMaterial(dye);
			VertexConsumer vertexconsumer = material.buffer(guiGraphics.bufferSource(), this.signModel::renderType);
			this.signModel.stick.visible = flag;
			this.signModel.root.render(guiGraphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
		}
	}
}
