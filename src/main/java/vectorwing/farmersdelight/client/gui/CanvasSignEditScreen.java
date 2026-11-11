package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

import javax.annotation.Nullable;

// TODO i'm pretty sure this doesn't display the right graphic. For now, it compiles
public class CanvasSignEditScreen extends SignEditScreen
{
	@Nullable
	protected Model.Simple signModel;
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

	@Override
	protected void init() {
		super.init();
		PlainSignBlock.Attachment attachment = PlainSignBlock.getAttachmentPoint(this.sign.getBlockState());
		this.signModel = StandingSignRenderer.createSignModel(this.minecraft.getEntityModels(), WoodType.SPRUCE, attachment);
	}

	protected void extractSignBackground(GuiGraphicsExtractor graphics) {
		if (this.signModel != null) {
			int centerX = this.width / 2;
			int x0 = centerX - 48;
			int x1 = centerX + 48;
			graphics.sign(this.signModel, MAGIC_SCALE_NUMBER, this.woodType, x0, 66, x1, 168);
		}
	}
}
