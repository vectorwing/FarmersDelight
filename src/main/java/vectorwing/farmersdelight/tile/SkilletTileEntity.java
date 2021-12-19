package vectorwing.farmersdelight.tile;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.SkilletBlock;
import vectorwing.farmersdelight.mixin.accessors.RecipeManagerAccessor;
import vectorwing.farmersdelight.registry.ModParticleTypes;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.utils.ItemUtils;
import vectorwing.farmersdelight.utils.TextUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class SkilletTileEntity extends FDSyncedTileEntity implements ITickableTileEntity, IHeatableTileEntity
{
	private final ItemStackHandler inventory = createHandler();
	private int cookingTime;
	private int cookingTimeTotal;
	private ResourceLocation lastRecipeID;

	private ItemStack skilletStack;
	private int fireAspectLevel;

	public SkilletTileEntity() {
		super(ModTileEntityTypes.SKILLET_TILE.get());
		skilletStack = ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		if (level == null) return;

		boolean isHeated = isHeated(level, worldPosition);
		if (!level.isClientSide) {
			if (isHeated) {
				ItemStack cookingStack = getStoredStack();
				if (cookingStack.isEmpty()) {
					cookingTime = 0;
				} else {
					cookAndOutputItems(cookingStack);
				}
			} else if (cookingTime > 0) {
				cookingTime = MathHelper.clamp(cookingTime - 2, 0, cookingTimeTotal);
			}
		} else {
			if (isHeated && hasStoredStack()) {
				addCookingParticles();
			}
		}
	}

	private void cookAndOutputItems(ItemStack cookingStack) {
		if (level == null) return;

		++cookingTime;
		if (cookingTime >= cookingTimeTotal) {
			Inventory wrapper = new Inventory(cookingStack);
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

	private Optional<CampfireCookingRecipe> getMatchingRecipe(IInventory recipeWrapper) {
		if (level == null) return Optional.empty();

		if (lastRecipeID != null) {
			IRecipe<IInventory> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
					.getRecipeMap(IRecipeType.CAMPFIRE_COOKING)
					.get(lastRecipeID);
			if (recipe instanceof CampfireCookingRecipe && recipe.matches(recipeWrapper, level)) {
				return Optional.of((CampfireCookingRecipe) recipe);
			}
		}

		Optional<CampfireCookingRecipe> recipe = level.getRecipeManager().getRecipeFor(IRecipeType.CAMPFIRE_COOKING, recipeWrapper, level);
		if (recipe.isPresent()) {
			lastRecipeID = recipe.get().getId();
			return recipe;
		}

		return Optional.empty();
	}

	private void addCookingParticles() {
		if (level != null) {
			Random random = level.random;
			if (random.nextFloat() < 0.2F) {
				double x = (double) worldPosition.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) worldPosition.getY() + 0.1D;
				double z = (double) worldPosition.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionY = random.nextBoolean() ? 0.015D : 0.005D;
				level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
			}
			if (fireAspectLevel > 0 && random.nextFloat() < fireAspectLevel * 0.05F) {
				double x = (double) worldPosition.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) worldPosition.getY() + 0.1D;
				double z = (double) worldPosition.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionX = level.random.nextFloat() - 0.5F;
				double motionY = level.random.nextFloat() * 0.5F + 0.2f;
				double motionZ = level.random.nextFloat() - 0.5F;
				level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
			}
		}
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		cookingTime = compound.getInt("CookTime");
		cookingTimeTotal = compound.getInt("CookTimeTotal");
		skilletStack = ItemStack.of(compound.getCompound("Skillet"));
		fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, skilletStack);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put("Inventory", inventory.serializeNBT());
		compound.putInt("CookTime", cookingTime);
		compound.putInt("CookTimeTotal", cookingTimeTotal);
		compound.put("Skillet", skilletStack.save(new CompoundNBT()));
		return compound;
	}

	public CompoundNBT writeSkilletItem(CompoundNBT compound) {
		compound.put("Skillet", skilletStack.save(new CompoundNBT()));
		return compound;
	}

	public void setSkilletItem(ItemStack stack) {
		skilletStack = stack.copy();
		fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
		inventoryChanged();
	}

	public ItemStack addItemToCook(ItemStack addedStack, @Nullable PlayerEntity player) {
		Optional<CampfireCookingRecipe> recipe = getMatchingRecipe(new Inventory(addedStack));
		if (recipe.isPresent()) {
			cookingTimeTotal = SkilletBlock.getSkilletCookingTime(recipe.get().getCookingTime(), fireAspectLevel);
			boolean wasEmpty = getStoredStack().isEmpty();
			ItemStack remainderStack = inventory.insertItem(0, addedStack.copy(), false);
			if (!ItemStack.matches(remainderStack, addedStack)) {
				lastRecipeID = recipe.get().getId();
				cookingTime = 0;
				if (wasEmpty && level != null && isHeated(level, worldPosition)) {
					level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
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
