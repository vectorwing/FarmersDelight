package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CuttingBoardBlockEntity extends SyncedBlockEntity
{
	private final ItemStackHandler inventory;
	private final LazyOptional<IItemHandler> inputHandler;
	private ResourceLocation lastRecipeID;

	private boolean isItemCarvingBoard;

	public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.CUTTING_BOARD.get(), pos, state);
		inventory = createHandler();
		inputHandler = LazyOptional.of(() -> inventory);
		isItemCarvingBoard = false;
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		isItemCarvingBoard = compound.getBoolean("IsItemCarved");
		inventory.deserializeNBT(compound.getCompound("Inventory"));
	}

	@Override
	public void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put("Inventory", inventory.serializeNBT());
		compound.putBoolean("IsItemCarved", isItemCarvingBoard);
	}

	public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
		if (level == null) return false;
		if (isItemCarvingBoard) return false;

		Optional<CuttingBoardRecipe> matchingRecipe = getMatchingRecipe(new RecipeWrapper(inventory), toolStack, player);

		matchingRecipe.ifPresent(recipe -> {
			List<ItemStack> results = recipe.rollResults(level.random, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, toolStack));
			for (ItemStack resultStack : results) {
				Direction direction = getBlockState().getValue(CuttingBoardBlock.FACING).getCounterClockWise();
				ItemUtils.spawnItemEntity(level, resultStack.copy(),
						worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
						direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
			}
			if (player != null) {
				toolStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
			} else {
				if (toolStack.hurt(1, level.random, null)) {
					toolStack.setCount(0);
				}
			}
			if (level instanceof ServerLevel serverLevel) {
				spawnCuttingParticles(serverLevel, getBlockPos(), getStoredItem());
			}
			playProcessingSound(recipe.getSoundEventID(), toolStack, getStoredItem());
			inventory.extractItem(0, 1, false);
			if (player instanceof ServerPlayer) {
				ModAdvancements.CUTTING_BOARD.trigger((ServerPlayer) player);
				if (!getStoredItem().isEmpty()) {
					player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.remaining_items", getStoredItem().getCount()), true);
				}
			}
		});

		return matchingRecipe.isPresent();
	}

	private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack toolStack, @Nullable Player player) {
		if (level == null) return Optional.empty();

		if (lastRecipeID != null) {
			Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
					.getRecipeMap(ModRecipeTypes.CUTTING.get())
					.get(lastRecipeID);
			if (recipe instanceof CuttingBoardRecipe && recipe.matches(recipeWrapper, level) && ((CuttingBoardRecipe) recipe).getTool().test(toolStack)) {
				return Optional.of((CuttingBoardRecipe) recipe);
			}
		}

		List<CuttingBoardRecipe> recipeList = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CUTTING.get(), recipeWrapper, level);
		if (recipeList.isEmpty()) {
			if (player != null)
				player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.invalid_item"), true);
			return Optional.empty();
		}
		Optional<CuttingBoardRecipe> recipe = recipeList.stream().filter(cuttingRecipe -> cuttingRecipe.getTool().test(toolStack)).findFirst();
		if (recipe.isEmpty()) {
			if (player != null)
				player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.invalid_tool"), true);
			return Optional.empty();
		}
		lastRecipeID = recipe.get().getId();
		return recipe;
	}

	public void playProcessingSound(String soundEventID, ItemStack tool, ItemStack boardItem) {
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundEventID));

		if (sound != null) {
			playSound(sound, 1.0F, 1.0F);
		} else if (tool.is(Tags.Items.SHEARS)) {
			playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
		} else if (tool.is(ForgeTags.TOOLS_KNIVES)) {
			playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
		} else if (boardItem.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			SoundType soundType = block.defaultBlockState().getSoundType();
			playSound(soundType.getBreakSound(), 1.0F, 0.8F);
		} else {
			playSound(SoundEvents.WOOD_BREAK, 1.0F, 0.8F);
		}
	}

	public void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack) {
		level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.05D);
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		if (level != null)
			level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, sound, SoundSource.BLOCKS, volume, pitch);
	}

	public boolean canAddItem(ItemStack addedStack) {
		if (isItemCarvingBoard || addedStack.isEmpty()) {
			return false;
		}
		return inventory.insertItem(0, addedStack.copy(), true).getCount() != addedStack.getCount();
	}

	public ItemStack addItem(ItemStack addedStack) {
		if (!isItemCarvingBoard) {
			return inventory.insertItem(0, addedStack.copy(), false);
		}
		return addedStack;
	}

	public ItemStack removeItem() {
		isItemCarvingBoard = false;
		return inventory.extractItem(0, getMaxStackSize(), false);
	}

	public boolean carveToolOnBoard(ItemStack toolStack) {
		if (toolStack.getItem() instanceof TieredItem || toolStack.getItem() instanceof TridentItem || toolStack.getItem() instanceof ShearsItem) {
			if (addItem(toolStack) == ItemStack.EMPTY) {
				isItemCarvingBoard = true;
				return true;
			}
		}
		return false;
	}

	public ItemStack getStoredItem() {
		return inventory.getStackInSlot(0);
	}

	public int getMaxStackSize() {
		return inventory.getSlotLimit(0);
	}

	public boolean isEmpty() {
		return inventory.getStackInSlot(0).isEmpty();
	}

	public boolean isItemCarvingBoard() {
		return isItemCarvingBoard;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
			return inputHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		inputHandler.invalidate();
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
}
