package vectorwing.farmersdelight.client.item;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Credits to the Botania Team for the class reference!
 */

@SuppressWarnings("deprecation")
public class SkilletModel implements IBakedModel
{
	private final ModelBakery bakery;
	private final IBakedModel originalModel;
	private final IBakedModel cookingModel;

	public SkilletModel(ModelBakery bakery, IBakedModel originalModel, IBakedModel cookingModel) {
		this.bakery = bakery;
		this.originalModel = Preconditions.checkNotNull(originalModel);
		this.cookingModel = Preconditions.checkNotNull(cookingModel);
	}

	private final ItemOverrideList itemOverrides = new ItemOverrideList()
	{
		@Nonnull
		@Override
		public IBakedModel resolve(@Nonnull IBakedModel model, ItemStack stack, @Nullable ClientWorld worldIn, @Nullable LivingEntity entityIn) {
			CompoundNBT tag = stack.getOrCreateTag();

			if (tag.contains("Cooking")) {
				ItemStack ingredientStack = ItemStack.of(tag.getCompound("Cooking"));
				return SkilletModel.this.getCookingModel(ingredientStack);
			}

			return originalModel;
		}
	};

	@Nonnull
	@Override
	public ItemOverrideList getOverrides() {
		return itemOverrides;
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Nonnull
	@Override
	public ItemCameraTransforms getTransforms() {
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

	private static class CompositeBakedModel extends WrappedItemModel<IBakedModel>
	{
		private final List<BakedQuad> genQuads = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> faceQuads = new EnumMap<>(Direction.class);

		public CompositeBakedModel(ModelBakery bakery, ItemStack ingredientStack, IBakedModel skillet) {
			super(skillet);

			ResourceLocation ingredientLocation = ForgeRegistries.ITEMS.getKey(ingredientStack.getItem());
			IUnbakedModel ingredientUnbaked = bakery.getModel(new ModelResourceLocation(ingredientLocation, "inventory"));
			IModelTransform transform = new SimpleModelTransform(
					new TransformationMatrix(
							new Vector3f(0.0F, -0.4F, 0.0F),
							Vector3f.XP.rotationDegrees(270),
							new Vector3f(0.625F, 0.625F, 0.625F), null));
			ResourceLocation name = new ResourceLocation(FarmersDelight.MODID, "skillet_with_" + ingredientLocation.toString().replace(':', '_'));

			IBakedModel ingredientBaked;
			if (ingredientUnbaked instanceof BlockModel && ((BlockModel) ingredientUnbaked).getRootModel() == ModelBakery.GENERATION_MARKER) {
				BlockModel bm = (BlockModel) ingredientUnbaked;
				ingredientBaked = new ItemModelGenerator()
						.generateBlockModel(ModelLoader.defaultTextureGetter(), bm)
						.bake(bakery, bm, ModelLoader.defaultTextureGetter(), transform, name, false);
			} else {
				ingredientBaked = ingredientUnbaked.bake(bakery, ModelLoader.defaultTextureGetter(), transform, name);
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
		public List<BakedQuad> getQuads(BlockState state, Direction face, @Nonnull Random rand) {
			return face == null ? genQuads : faceQuads.get(face);
		}

		@Override
		public IBakedModel handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType, MatrixStack stack) {
			super.handlePerspective(cameraTransformType, stack);
			return this;
		}
	}
}
