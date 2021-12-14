package vectorwing.farmersdelight.client.model;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Credits to the Botania Team for the class reference!
 */

@SuppressWarnings("deprecation")
public class SkilletModel implements BakedModel
{
	private final ModelBakery bakery;
	private final BakedModel originalModel;
	private final BakedModel cookingModel;

	public SkilletModel(ModelBakery bakery, BakedModel originalModel, BakedModel cookingModel) {
		this.bakery = bakery;
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.cookingModel = Preconditions.checkNotNull(cookingModel);
	}

	private final ItemOverrides itemOverrides = new ItemOverrides()
	{
		@Nonnull
		@Override
		public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel worldIn, @Nullable LivingEntity entityIn, int seed) {
			CompoundTag tag = stack.getOrCreateTag();

			if (tag.contains("Cooking")) {
				ItemStack ingredientStack = ItemStack.of(tag.getCompound("Cooking"));
				return SkilletModel.this.getCookingModel(ingredientStack);
			}

			return originalModel;
		}
	};

	@Nonnull
	@Override
	public ItemOverrides getOverrides() {
		return itemOverrides;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Nonnull
	@Override
	public ItemTransforms getTransforms() {
		return originalModel.getTransforms();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return originalModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return originalModel.isCustomRenderer();
	}

	@Nonnull
	@Override
	public TextureAtlasSprite getParticleIcon() {
		return originalModel.getParticleIcon();
	}

	private final HashMap<Item, CompositeBakedModel> cache = new HashMap<>();

	private CompositeBakedModel getCookingModel(ItemStack ingredientStack) {
		return cache.computeIfAbsent(ingredientStack.getItem(), p -> new CompositeBakedModel(bakery, ingredientStack, cookingModel));
	}

	private static class CompositeBakedModel extends WrappedItemModel<BakedModel>
	{
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		public CompositeBakedModel(ModelBakery bakery, ItemStack ingredientStack, BakedModel skillet) {
			super(skillet);

			ResourceLocation ingredientLocation = ForgeRegistries.ITEMS.getKey(ingredientStack.getItem());
			UnbakedModel ingredientUnbaked = bakery.getModel(new ModelResourceLocation(ingredientLocation, "inventory"));
			ModelState transform = new SimpleModelState(
					new Transformation(
							new Vector3f(0.0F, -0.4F, 0.0F),
							Vector3f.XP.rotationDegrees(270),
							new Vector3f(0.625F, 0.625F, 0.625F), null));
			ResourceLocation name = new ResourceLocation(FarmersDelight.MODID, "skillet_with_" + ingredientLocation.toString().replace(':', '_'));

			BakedModel ingredientBaked;
			if (ingredientUnbaked instanceof BlockModel bm && ((BlockModel) ingredientUnbaked).getRootModel() == ModelBakery.GENERATION_MARKER) {
				ingredientBaked = new ItemModelGenerator()
						.generateBlockModel(ForgeModelBakery.defaultTextureGetter(), bm)
						.bake(bakery, bm, ForgeModelBakery.defaultTextureGetter(), transform, name, false);
			} else {
				ingredientBaked = ingredientUnbaked.bake(bakery, ForgeModelBakery.defaultTextureGetter(), transform, name);
			}

			for (Direction e : Direction.values()) {
				faceQuads.put(e, new ArrayList<>());
			}

			Random rand = new Random(0);
			genQuads.addAll(ingredientBaked.getQuads(null, null, rand));

			for (Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(ingredientBaked.getQuads(null, e, rand));
			}

			rand.setSeed(0);
			genQuads.addAll(skillet.getQuads(null, null, rand));
			for (Direction e : Direction.values()) {
				rand.setSeed(0);
				faceQuads.get(e).addAll(skillet.getQuads(null, e, rand));
			}
		}

		@Override
		public boolean isCustomRenderer() {
			return originalModel.isCustomRenderer();
		}

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, @Nonnull Random rand) {
			return face == null ? genQuads : faceQuads.get(face);
		}

		@Override
		public BakedModel handlePerspective(@Nonnull ItemTransforms.TransformType cameraTransformType, PoseStack stack) {
			super.handlePerspective(cameraTransformType, stack);
			return this;
		}
	}
}
