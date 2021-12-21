package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class SkilletBlockEntity extends SyncedBlockEntity implements HeatableBlockEntity
{
	private final ItemStackHandler inventory = createHandler();
	private int cookingTime;
	private int cookingTimeTotal;
	private ResourceLocation lastRecipeID;

	private ItemStack skilletStack;
	private int fireAspectLevel;

	public SkilletBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.SKILLET.get(), pos, state);
		skilletStack = ItemStack.EMPTY;
	}

	public static void cookingTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
		boolean isHeated = skillet.isHeated(level, pos);
		if (isHeated) {
			ItemStack cookingStack = skillet.getStoredStack();
			if (cookingStack.isEmpty()) {
				skillet.cookingTime = 0;
			} else {
				skillet.cookAndOutputItems(cookingStack);
			}
		} else if (skillet.cookingTime > 0) {
			skillet.cookingTime = Mth.clamp(skillet.cookingTime - 2, 0, skillet.cookingTimeTotal);
		}
	}

	public static void animationTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
		if (skillet.isHeated(level, pos) && skillet.hasStoredStack()) {
			Random random = level.random;
			if (random.nextFloat() < 0.2F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.1D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionY = random.nextBoolean() ? 0.015D : 0.005D;
				level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
			}
			if (skillet.fireAspectLevel > 0 && random.nextFloat() < skillet.fireAspectLevel * 0.05F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.1D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionX = level.random.nextFloat() - 0.5F;
				double motionY = level.random.nextFloat() * 0.5F + 0.2f;
				double motionZ = level.random.nextFloat() - 0.5F;
				level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
			}
		}

	}

	private void cookAndOutputItems(ItemStack cookingStack) {
		if (level == null) return;

		++cookingTime;
		if (cookingTime >= cookingTimeTotal) {
			SimpleContainer wrapper = new SimpleContainer(cookingStack);
			Optional<CampfireCookingRecipe> recipe = getMatchingRecipe(wrapper);
			if (recipe.isPresent()) {
				ItemStack resultStack = recipe.get().assemble(wrapper);
				Direction direction = getBlockState().getValue(SkilletBlock.FACING).getClockWise();
				ItemUtils.spawnItemEntity(level, resultStack.copy(),
						worldPosition.getX() + 0.5, worldPosition.getY() + 0.3, worldPosition.getZ() + 0.5,
						direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);

				cookingTime = 0;
				inventory.extractItem(0, 1, false);
			}
		}
	}

	public boolean isCooking() {
		return isHeated() && hasStoredStack();
	}

	public boolean isHeated() {
		if (level != null) {
			return isHeated(level, worldPosition);
		}
		return false;
	}

	private Optional<CampfireCookingRecipe> getMatchingRecipe(Container recipeWrapper) {
		if (level == null) return Optional.empty();

		if (lastRecipeID != null) {
			Recipe<Container> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
					.getRecipeMap(RecipeType.CAMPFIRE_COOKING)
					.get(lastRecipeID);
			if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, level)) {
				return Optional.of((CampfireCookingRecipe) recipe);
			}
		}

		Optional<CampfireCookingRecipe> recipe = level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, recipeWrapper, level);
		if (recipe.isPresent()) {
			lastRecipeID = recipe.get().getId();
			return recipe;
		}

		return Optional.empty();
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		cookingTime = compound.getInt("CookTime");
		cookingTimeTotal = compound.getInt("CookTimeTotal");
		skilletStack = ItemStack.of(compound.getCompound("Skillet"));
		fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, skilletStack);
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put("Inventory", inventory.serializeNBT());
		compound.putInt("CookTime", cookingTime);
		compound.putInt("CookTimeTotal", cookingTimeTotal);
		compound.put("Skillet", skilletStack.save(new CompoundTag()));
	}

	public CompoundTag writeSkilletItem(CompoundTag compound) {
		compound.put("Skillet", skilletStack.save(new CompoundTag()));
		return compound;
	}

	public void setSkilletItem(ItemStack stack) {
		skilletStack = stack.copy();
		fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
		inventoryChanged();
	}

	public ItemStack addItemToCook(ItemStack addedStack, @Nullable Player player) {
		Optional<CampfireCookingRecipe> recipe = getMatchingRecipe(new SimpleContainer(addedStack));
		if (recipe.isPresent()) {
			cookingTimeTotal = SkilletBlock.getSkilletCookingTime(recipe.get().getCookingTime(), fireAspectLevel);
			boolean wasEmpty = getStoredStack().isEmpty();
			ItemStack remainderStack = inventory.insertItem(0, addedStack.copy(), false);
			if (!ItemStack.matches(remainderStack, addedStack)) {
				lastRecipeID = recipe.get().getId();
				cookingTime = 0;
				if (wasEmpty && level != null && isHeated(level, worldPosition)) {
					level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
				}
				return remainderStack;
			}
		} else if (player != null) {
			player.displayClientMessage(TextUtils.getTranslation("block.skillet.invalid_item"), true);
		}
		return addedStack;
	}

	public ItemStack removeItem() {
		return inventory.extractItem(0, getStoredStack().getMaxStackSize(), false);
	}

	public IItemHandler getInventory() {
		return inventory;
	}

	public ItemStack getStoredStack() {
		return inventory.getStackInSlot(0);
	}

	public boolean hasStoredStack() {
		return !getStoredStack().isEmpty();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
	}
}
