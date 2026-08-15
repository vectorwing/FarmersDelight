package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

import java.util.function.Consumer;

public class SkilletItemRenderer implements SpecialModelRenderer<ItemStack>
{
	@Override
	public @Nullable ItemStack extractArgument(ItemStack stack) {
		ItemStack ingredient = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT, ItemStackWrapper.EMPTY).getStack();
		return ingredient.isEmpty() ? null : ingredient.copy();
	}

	@Override
	public void submit(@Nullable ItemStack ingredient, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords,
			boolean hasFoil, int outlineColor) {
		if (ingredient == null || ingredient.isEmpty()) {
			return;
		}

		ItemStackRenderState ingredientState = new ItemStackRenderState();
		int seed = Item.getId(ingredient.getItem()) + ingredient.getDamageValue();
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getItemModelResolver().updateForTopItem(ingredientState, ingredient, ItemDisplayContext.FIXED, minecraft.level, null, seed);

		poseStack.pushPose();
		poseStack.translate(0.5D, 1.0D / 16.0D, 0.5D);
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		poseStack.scale(0.5F, 0.5F, 0.5F);
		ingredientState.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);
		poseStack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked<ItemStack>
	{
		public static final MapCodec<SkilletItemRenderer.Unbaked> MAP_CODEC = MapCodec.unit(new SkilletItemRenderer.Unbaked());

		@Override
		public @Nullable SkilletItemRenderer bake(SpecialModelRenderer.BakingContext context) {
			return new SkilletItemRenderer();
		}

		@Override
		public MapCodec<SkilletItemRenderer.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
