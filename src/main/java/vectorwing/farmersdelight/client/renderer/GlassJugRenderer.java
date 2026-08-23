package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.textures.FluidSpriteCache;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;

public class GlassJugRenderer implements BlockEntityRenderer<JugBlockEntity>
{
	public GlassJugRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(JugBlockEntity jug, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		FluidTank tank = jug.getFluidTank();
		FluidStack fluid = tank.getFluid();
		Level level = jug.getLevel();
		if (fluid.isEmpty() || level == null) return;

		FluidState fluidState = fluid.getFluid().defaultFluidState();
		IClientFluidTypeExtensions fluidExtension = IClientFluidTypeExtensions.of(fluid.getFluid());
		int tint = fluidExtension.getTintColor(fluidState, level, jug.getBlockPos());
		ResourceLocation fluidTexture = fluidExtension.getStillTexture(fluidState, level, jug.getBlockPos());
		RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluidState);

		float fillPercentage = (float) tank.getFluidAmount() / tank.getCapacity();
		renderFluidTank(poseStack, 4, 12, 1, 10, fillPercentage, bufferSource, renderType, calculateGlowLight(packedLight, fluid.getFluidType().getLightLevel()), fluidTexture, tint);
	}

	public static void renderFluidTank(PoseStack poseStack, float startXZ, float endXZ, float startY, float maxHeight, float fillPercentage, MultiBufferSource buffer, RenderType renderType, int light, ResourceLocation texture, int tint) {
		RenderType bufferType = RenderTypeHelper.getEntityRenderType(renderType, true);
		VertexConsumer consumer = buffer.getBuffer(bufferType);
		PoseStack.Pose pose = poseStack.last();

		float minXZ = getPositionFrom16px(startXZ);
		float minY = getPositionFrom16px(startY);
		float maxXZ = getPositionFrom16px(endXZ);
		float maxY = getPositionFrom16px(startY + (maxHeight * fillPercentage));

		TextureAtlasSprite sprite = FluidSpriteCache.getSprite(texture);
		float minU = sprite.getU(minXZ);
		float maxU = sprite.getU(maxXZ);
		float minV = sprite.getV(maxY);
		float maxV = sprite.getV(minY);

		consumer.addVertex(pose, minXZ, maxY, minXZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);
		consumer.addVertex(pose, minXZ, minY, minXZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);
		consumer.addVertex(pose, minXZ, minY, maxXZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);
		consumer.addVertex(pose, minXZ, maxY, maxXZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, -1F, 0F, 0F);

		// East
		consumer.addVertex(pose, maxXZ, maxY, maxXZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);
		consumer.addVertex(pose, maxXZ, minY, maxXZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);
		consumer.addVertex(pose, maxXZ, minY, minXZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);
		consumer.addVertex(pose, maxXZ, maxY, minXZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1F, 0F, 0F);

		// North
		consumer.addVertex(pose, maxXZ, maxY, minXZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);
		consumer.addVertex(pose, maxXZ, minY, minXZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);
		consumer.addVertex(pose, minXZ, minY, minXZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);
		consumer.addVertex(pose, minXZ, maxY, minXZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, -1F);

		// South
		consumer.addVertex(pose, minXZ, maxY, maxXZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);
		consumer.addVertex(pose, minXZ, minY, maxXZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);
		consumer.addVertex(pose, maxXZ, minY, maxXZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);
		consumer.addVertex(pose, maxXZ, maxY, maxXZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 0F, 1F);

		minU = sprite.getU(minXZ);
		maxU = sprite.getU(maxXZ);
		minV = sprite.getV(minXZ);
		maxV = sprite.getV(maxXZ);

		// Up
		consumer.addVertex(pose, maxXZ, maxY, maxXZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);
		consumer.addVertex(pose, maxXZ, maxY, minXZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);
		consumer.addVertex(pose, minXZ, maxY, minXZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);
		consumer.addVertex(pose, minXZ, maxY, maxXZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, 1F, 0F);

		// Down
		consumer.addVertex(pose, minXZ, minY, maxXZ).setColor(tint).setUv(minU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
		consumer.addVertex(pose, minXZ, minY, minXZ).setColor(tint).setUv(minU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
		consumer.addVertex(pose, maxXZ, minY, minXZ).setColor(tint).setUv(maxU, minV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
		consumer.addVertex(pose, maxXZ, minY, maxXZ).setColor(tint).setUv(maxU, maxV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0F, -1F, 0F);
	}

	public static float getPositionFrom16px(float pixelPos) {
		return pixelPos / 16;
	}

	public static int calculateGlowLight(int combinedLight, int glow) {
		//Only factor the glow into the block light portion
		return (combinedLight & 0xFFFF0000) | Math.max(Math.min(glow, 15) << 4, combinedLight & 0xFFFF);
	}
}
