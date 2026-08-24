package vectorwing.farmersdelight.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import vectorwing.farmersdelight.client.renderer.GlassJugRenderer;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class GlassJugItemRenderer extends BlockEntityWithoutLevelRenderer
{
	public GlassJugItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
		BlockItem item = ((BlockItem) stack.getItem());
		BlockState state = item.getBlock().defaultBlockState();

		SimpleFluidContent content = stack.getOrDefault(ModDataComponents.FLUID_TANK, SimpleFluidContent.EMPTY);

		if (!content.isEmpty()) {
			IClientFluidTypeExtensions fluidExtension = IClientFluidTypeExtensions.of(content.getFluid());
			ResourceLocation fluidTexture = fluidExtension.getStillTexture();
			int tint = fluidExtension.getTintColor();
			RenderType renderType = ItemBlockRenderTypes.getRenderLayer(content.getFluid().defaultFluidState());
			float fillPercentage = (float) content.getAmount() / JugBlockEntity.JUG_CAPACITY;

			GlassJugRenderer.renderFluidTank(poseStack, 4, 12, 1, 10, fillPercentage, buffer, renderType,
				GlassJugRenderer.calculateGlowLight(light, content.getFluidType().getLightLevel()), fluidTexture, tint);
		}

		poseStack.pushPose();
		Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffer, light, overlay);
		poseStack.popPose();
	}
}
