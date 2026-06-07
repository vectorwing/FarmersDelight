package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

import java.util.Optional;
import java.util.function.Consumer;

// TODO this needs a corresponding client item when model datagen is fixed
public record SkilletItemRenderer(SpriteGetter spriteGetter) implements SpecialModelRenderer<SkilletItemRenderer.SkilletItemRenderArgument>
{
	public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

	@Override
	public void submit(SkilletItemRenderArgument argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		Minecraft mc = Minecraft.getInstance();

		float animation = 0;

		// Render item
		if (argument.maybeIngredientRenderState.isPresent()) {
			poseStack.pushPose();
			poseStack.translate(0.5, 1 / 16f, 0.5);

			long gameTime = mc.level.getGameTime();
			if (argument.flipTime.isPresent() && !argument.inGui()) {
				long time = argument.flipTime().get();
				float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
				animation = ((gameTime - time) + partialTicks) / SkilletItem.FLIP_TIME;
				animation = Mth.clamp(animation, 0, 1);
				float maxH = 0.4F;
				poseStack.translate(0, maxH * Mth.sin(animation * Mth.PI), 0);
				float rotationAnimation = argument.skilletFlipped ? animation + 1.0F : animation;
				poseStack.mulPose(Axis.XP.rotationDegrees(180 * rotationAnimation));
			} else {
				poseStack.mulPose(Axis.XP.rotationDegrees(argument.skilletFlipped ? 180 : 0));
			}

			poseStack.mulPose(Axis.XP.rotationDegrees(90));
			poseStack.scale(0.5F, 0.5F, 0.5F);

			// Render item
			if (!argument.inGui()) {
				argument.maybeIngredientRenderState().get().submit(
					poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
			}

			poseStack.popPose();
		}

		poseStack.pushPose();

		// Render block
		if (animation != 0 && argument.firstperson) {
			poseStack.translate(0, 0, 1);
			poseStack.mulPose(Axis.XN.rotationDegrees(Mth.sin(animation * Mth.TWO_PI) * 15));
			poseStack.translate(0F, 0, -1);
			poseStack.translate(0, 0, -Mth.sin(animation * Mth.PI) * 0.2);
		}
		argument.blockRenderState.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);


		poseStack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> consumer) {
		// TODO???
	}

	@Override
	public SkilletItemRenderer.@Nullable SkilletItemRenderArgument extractArgument(ItemStack stack) {
		BlockState blockstate = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
		BlockModelRenderState blockRenderState = new BlockModelRenderState();
		Minecraft.getInstance().getBlockModelResolver().update(blockRenderState, blockstate, BLOCK_DISPLAY_CONTEXT);

		ItemStackWrapper ingredientWrapper = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT.get(), ItemStackWrapper.EMPTY);
		ItemStack ingredientStack = ingredientWrapper.getStack();
		Optional<ItemStackRenderState> maybeIngredientRenderState = Optional.empty();
		if (!ingredientStack.isEmpty()) {
			ItemStackRenderState ingredientRenderState = new ItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForTopItem(
				ingredientRenderState,
				ingredientStack,
				ItemDisplayContext.FIXED,
				null, // TODO is this fine?
				null,
				0
			);
			maybeIngredientRenderState = Optional.of(ingredientRenderState);
		}

		Optional<Long> flipTime;
		if (stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
			flipTime = Optional.of(stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get()));
		} else {
			flipTime = Optional.empty();
		}

		// TODO how get?
		boolean inGui = false;
		boolean firstperson = false;
		boolean skilletFlipped = stack.getOrDefault(ModDataComponents.SKILLET_FLIPPED.get(), false);

		return new SkilletItemRenderArgument(blockRenderState, maybeIngredientRenderState, flipTime, skilletFlipped, inGui, firstperson);
	}

	public record SkilletItemRenderArgument(
		BlockModelRenderState blockRenderState,
		Optional<ItemStackRenderState> maybeIngredientRenderState,
		Optional<Long> flipTime,
		boolean skilletFlipped,
		boolean inGui,
		boolean firstperson) {

	}

	public record ArmPoseTransformer(long skilletFlipTimestamp) implements IArmPoseTransformer {
		@Override
		public void applyTransform(HumanoidModel<?> model, HumanoidRenderState entity, HumanoidArm arm) {
			ItemStack stack = arm == HumanoidArm.LEFT ? entity.leftHandItemStack : entity.rightHandItemStack;
			Minecraft mc = Minecraft.getInstance();

			if (stack.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
				long time = stack.get(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get());
				float partialTicks = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
				float animation = ((mc.level.getGameTime() - time) + partialTicks) / SkilletItem.FLIP_TIME;
				animation = Mth.clamp(animation, 0, 1);

				if (arm == HumanoidArm.LEFT) {
					model.leftArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
				} else {
					model.rightArm.xRot = (-Mth.sin(animation * Mth.TWO_PI) * 15 - 20) * (float) (Math.PI / 180.0);
				}
			}
		}
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked<SkilletItemRenderArgument> {
		public static final Unbaked INSTANCE = new Unbaked();
		public static final MapCodec<SkilletItemRenderer.Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public MapCodec<? extends SpecialModelRenderer.Unbaked<SkilletItemRenderArgument>> type() {
			return MAP_CODEC;
		}

		@Override
		public SpecialModelRenderer<SkilletItemRenderArgument> bake(BakingContext ctx) {
			return new SkilletItemRenderer(ctx.sprites());
		}
	}
}
