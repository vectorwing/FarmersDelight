package vectorwing.farmersdelight.tile;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.SkilletBlock;
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
	private CampfireCookingRecipe currentRecipe;

	private ItemStack skilletStack;
	private int fireAspectLevel;

	public SkilletTileEntity() {
		super(ModTileEntityTypes.SKILLET_TILE.get());
		skilletStack = ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		if (world == null) return;

		boolean isHeated = isHeated(world, pos);
		if (!world.isRemote) {
			if (isHeated) {
				ItemStack cookingStack = getStoredStack();
				if (cookingStack.isEmpty()) {
					cookingTime = 0;
				} else if (canCook(cookingStack, world)) {
					++cookingTime;
					if (cookingTime >= getCookingTime()) {
						ItemStack resultStack = currentRecipe.getCraftingResult(new Inventory(cookingStack));

						Direction direction = getBlockState().get(SkilletBlock.HORIZONTAL_FACING).getOpposite().rotateYCCW(); // TODO: Reorient Skilet, maybe?
						ItemUtils.spawnItemEntity(world, resultStack.copy(),
								pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5,
								direction.getXOffset() * 0.08F, 0.25F, direction.getZOffset() * 0.08F);

						cookingTime = 0;
						inventory.extractItem(0, 1, false);
					}
				}
			} else if (cookingTime > 0) {
				cookingTime = MathHelper.clamp(cookingTime - 2, 0, getCookingTime());
			}
		} else {
			if (isHeated && hasStoredStack()) {
				addCookingParticles();
			}
		}
	}

	public boolean isCooking() {
		return isHeated() && hasStoredStack();
	}

	public boolean isHeated() {
		if (world != null) {
			return isHeated(world, pos);
		}
		return false;
	}

	public boolean canCook(ItemStack stack, World world) {
		if (currentRecipe != null && currentRecipe.matches(new Inventory(stack), world)) {
			return true;
		} else {
			Optional<CampfireCookingRecipe> recipe = findMatchingRecipe(stack);
			if (recipe.isPresent()) {
				currentRecipe = recipe.get();
				return true;
			}
		}
		return false;
	}

	public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
		return world == null
				? Optional.empty()
				: world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(itemStackIn), world);
	}

	private void addCookingParticles() {
		if (world != null) {
			Random random = world.rand;
			if (random.nextFloat() < 0.2F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.1D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionY = random.nextBoolean() ? 0.015D : 0.005D;
				world.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
			}
			if (fireAspectLevel > 0 && random.nextFloat() < fireAspectLevel * 0.05F) {
				double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double y = (double) pos.getY() + 0.1D;
				double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
				double motionX = world.rand.nextFloat() - 0.5F;
				double motionY = world.rand.nextFloat() * 0.5F + 0.2f;
				double motionZ = world.rand.nextFloat() - 0.5F;
				world.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
			}
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		cookingTime = compound.getInt("CookTime");
		skilletStack = ItemStack.read(compound.getCompound("Skillet"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Inventory", inventory.serializeNBT());
		compound.putInt("CookTime", cookingTime);
		compound.put("Skillet", skilletStack.write(new CompoundNBT()));
		return compound;
	}

	public CompoundNBT writeSkilletItem(CompoundNBT compound) {
		compound.put("Skillet", skilletStack.write(new CompoundNBT()));
		return compound;
	}

	public void setSkilletItem(ItemStack stack) {
		skilletStack = stack.copy();
		fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
		inventoryChanged();
	}

	public int getCookingTime() {
		int cookingTimeReduction = 0;
		if (fireAspectLevel > 0) {
			cookingTimeReduction = ((MathHelper.clamp(fireAspectLevel, 0, 2) * 20) + 20);
		}
		return 120 - cookingTimeReduction;
	}

	public ItemStack addItemToCook(ItemStack addedStack, @Nullable PlayerEntity player) {
		Optional<CampfireCookingRecipe> recipe = findMatchingRecipe(addedStack);
		if (recipe.isPresent()) {
			boolean wasEmpty = getStoredStack().isEmpty();
			ItemStack remainderStack = inventory.insertItem(0, addedStack.copy(), false);
			if (remainderStack != addedStack) {
				currentRecipe = recipe.get();
				cookingTime = 0;
				if (wasEmpty && world != null && isHeated(world, pos)) {
					world.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
				}
				return remainderStack;
			}
		} else if (player != null) {
			player.sendStatusMessage(TextUtils.getTranslation("block.skillet.invalid_item"), true);
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
	public void remove() {
		super.remove();
	}
}
