package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Optional;

public class SkilletBlockEntity extends SyncedBlockEntity implements HeatableBlockEntity, Clearable
{
	private final ItemStacksResourceHandler inventory = createHandler();
	private int cookingTime;
	private int cookingTimeTotal;

	private ItemStack skilletStack;
	private int fireAspectLevel;

	private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> quickCheck;

	public SkilletBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.SKILLET.get(), pos, state);
		skilletStack = new ItemStack(ModItems.SKILLET.get());
		quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
	}

	public static void cookingTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
		boolean isHeated = skillet.isHeated(level, pos);

		if (state.getValue(SkilletBlock.WATERLOGGED)) {
			if (ItemUtils.doesInventoryHaveItems(skillet.inventory)) {
				ItemUtils.dropItems(level, pos, skillet.inventory);
				skillet.inventoryChanged();
			}
		} else if (isHeated) {
			ItemStack cookingStack = skillet.getStoredStack();
			if (cookingStack.isEmpty()) {
				skillet.cookingTime = 0;
			} else if (level instanceof ServerLevel sLevel) {
				skillet.cookAndOutputItems(cookingStack, sLevel);
			}
		} else if (skillet.cookingTime > 0) {
			skillet.cookingTime = Mth.clamp(skillet.cookingTime - 2, 0, skillet.cookingTimeTotal);
		}
	}

	public static void animationTick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity skillet) {
		if (skillet.isHeated(level, pos) && skillet.hasStoredStack()) {
			RandomSource random = level.getRandom();
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
				double motionX = level.getRandom().nextFloat() - 0.5F;
				double motionY = level.getRandom().nextFloat() * 0.5F + 0.2f;
				double motionZ = level.getRandom().nextFloat() - 0.5F;
				level.addParticle(ParticleTypes.ENCHANTED_HIT, x, y, z, motionX, motionY, motionZ);
			}
		}

	}

	private void cookAndOutputItems(ItemStack cookingStack, ServerLevel level) {
		++cookingTime;
		if (cookingTime >= cookingTimeTotal) {
			Optional<RecipeHolder<CampfireCookingRecipe>> recipe = getMatchingRecipe(cookingStack, level);
			if (recipe.isPresent()) {
				ItemStack resultStack = recipe.get().value().assemble(new SingleRecipeInput(cookingStack));
				Direction direction = getBlockState().getValue(SkilletBlock.FACING).getClockWise();
				ItemUtils.spawnItemEntity(level, resultStack.copy(),
						worldPosition.getX() + 0.5, worldPosition.getY() + 0.3, worldPosition.getZ() + 0.5,
						direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);

				cookingTime = 0;
				try (Transaction tx = Transaction.openRoot()) {
					inventory.extract(0, inventory.getResource(0), 1, tx);
					tx.commit();
				}
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

	private Optional<RecipeHolder<CampfireCookingRecipe>> getMatchingRecipe(ItemStack stack, ServerLevel serverLevel) {
		if (level == null) return Optional.empty();
		return this.quickCheck.getRecipeFor(new SingleRecipeInput(stack), serverLevel);
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		inventory.deserialize(input);
		cookingTime = input.getInt("CookTime").orElse(0);
		cookingTimeTotal = input.getInt("CookTimeTotal").orElse(0);
		skilletStack = input.read("Skillet", ItemStack.CODEC).orElse(ItemStack.EMPTY);
		fireAspectLevel = ItemUtils.getValidatedEnchantmentLevel(Enchantments.FIRE_ASPECT, level.registryAccess(), skilletStack);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		inventory.serialize(output);
		output.putInt("CookTime", cookingTime);
		output.putInt("CookTimeTotal", cookingTimeTotal);
		if (!skilletStack.isEmpty()) {
			output.store("Skillet", ItemStack.CODEC, skilletStack);
		}
	}

	public ItemStack getSkilletAsItem() {
		return skilletStack;
	}

	public void setSkilletItem(ItemStack stack) {
		skilletStack = stack.copy();
		fireAspectLevel = ItemUtils.getValidatedEnchantmentLevel(Enchantments.FIRE_ASPECT, level.registryAccess(), stack);
		inventoryChanged();
	}

	public ItemStack addItemToCook(ItemStack addedStack, Player player, ServerLevel serverLevel) {
		Optional<RecipeHolder<CampfireCookingRecipe>> recipe = getMatchingRecipe(addedStack, serverLevel);
		if (recipe.isPresent() && getStoredStack().isEmpty()) {
			if (getBlockState().getValue(SkilletBlock.WATERLOGGED)) {
				player.sendOverlayMessage(TextUtils.block("skillet.underwater"));
				return addedStack;
			}
			boolean wasEmpty = getStoredStack().isEmpty();
			ItemStack remainderStack;
			try (Transaction tx = Transaction.openRoot()) {
				int inserted = inventory.insert(0, ItemResource.of(addedStack), addedStack.count(), tx);
				remainderStack = addedStack.copyWithCount(addedStack.count() - inserted);
				tx.commit();
			}
			if (!ItemStack.matches(remainderStack, addedStack)) {
				cookingTimeTotal = SkilletBlock.getSkilletCookingTime(recipe.get().value().cookingTime(), fireAspectLevel);
				cookingTime = 0;
				if (wasEmpty && level != null && isHeated(level, worldPosition)) {
					level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
				}
				return remainderStack;
			}
		} else {
			player.sendOverlayMessage(TextUtils.block("skillet.invalid_item"));
		}
		return addedStack;
	}

	public ItemStack removeItem() {
		ItemStack withdrawn;
		try (Transaction tx = Transaction.openRoot()){
			ItemResource content = inventory.getResource(0);
			int amount = inventory.extract(content, getStoredStack().getMaxStackSize(), tx);
			withdrawn = content.toStack(amount);
			tx.commit();
		}
		return withdrawn;
	}

	public ItemStacksResourceHandler getInventory() {
		return inventory;
	}

	public ItemStack getStoredStack() {
		return inventory.getResource(0).toStack(inventory.getAmountAsInt(0));
	}

	public boolean hasStoredStack() {
		return !getStoredStack().isEmpty();
	}

	private ItemStacksResourceHandler createHandler() {
		return new ItemStacksResourceHandler(0)
		{
			@Override
			protected void onContentsChanged(int index, ItemStack previousContents) {
				inventoryChanged();
			}
		};
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
	}

	@Override
	public void clearContent() {
		ItemUtils.clearItems(inventory);
	}
}
