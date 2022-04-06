package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class SkilletBlock extends BaseEntityBlock
{
	public static final int MINIMUM_COOKING_TIME = 60;

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty SUPPORT = BooleanProperty.create("support");

	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

	public SkilletBlock() {
		super(Properties.of(Material.METAL)
				.strength(0.5F, 6.0F)
				.sound(SoundType.LANTERN));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SUPPORT, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof SkilletBlockEntity skilletEntity) {
			if (!worldIn.isClientSide) {
				ItemStack heldStack = player.getItemInHand(handIn);
				EquipmentSlot heldSlot = handIn.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
				if (heldStack.isEmpty()) {
					ItemStack extractedStack = skilletEntity.removeItem();
					if (!player.isCreative()) {
						player.setItemSlot(heldSlot, extractedStack);
					}
					return InteractionResult.SUCCESS;
				} else {
					ItemStack remainderStack = skilletEntity.addItemToCook(heldStack, player);
					if (remainderStack.getCount() != heldStack.getCount()) {
						if (!player.isCreative()) {
							player.setItemSlot(heldSlot, remainderStack);
						}
						worldIn.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
						return InteractionResult.SUCCESS;
					}
				}
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof SkilletBlockEntity) {
				Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((SkilletBlockEntity) tileEntity).getInventory().getStackInSlot(0));
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(true) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection())
				.setValue(SUPPORT, getTrayState(context.getLevel(), context.getClickedPos()));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (facing.getAxis().equals(Direction.Axis.Y)) {
			return state.setValue(SUPPORT, getTrayState(world, currentPos));
		}
		return state;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(worldIn, pos, state);
		SkilletBlockEntity skilletEntity = (SkilletBlockEntity) worldIn.getBlockEntity(pos);
		CompoundTag nbt = new CompoundTag();
		if (skilletEntity != null) {
			skilletEntity.writeSkilletItem(nbt);
		}
		if (!nbt.isEmpty()) {
			stack = ItemStack.of(nbt.getCompound("Skillet"));
		}
		return stack;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, SUPPORT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof SkilletBlockEntity skilletEntity) {
			if (skilletEntity.isCooking()) {
				double x = (double) pos.getX() + 0.5D;
				double y = pos.getY();
				double z = (double) pos.getZ() + 0.5D;
				if (rand.nextInt(10) == 0) {
					worldIn.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS, 0.4F, rand.nextFloat() * 0.2F + 0.9F, false);
				}
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.SKILLET.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		if (level.isClientSide) {
			return createTickerHelper(blockEntity, ModBlockEntityTypes.SKILLET.get(), SkilletBlockEntity::animationTick);
		} else {
			return createTickerHelper(blockEntity, ModBlockEntityTypes.SKILLET.get(), SkilletBlockEntity::cookingTick);
		}
	}

	private boolean getTrayState(LevelAccessor world, BlockPos pos) {
		return world.getBlockState(pos.below()).is(ModTags.TRAY_HEAT_SOURCES);
	}

	/**
	 * Calculates the total cooking time for the Skillet, affected by Fire Aspect.
	 * Assuming a default of 30 seconds (600 ticks), the time is divided by 5, then reduced further per level of Fire Aspect, to a minimum of 3 seconds.
	 * Times are always rounded to a multiple of 20, to ensure exact seconds.
	 */
	public static int getSkilletCookingTime(int originalCookingTime, int fireAspectLevel) {
		int cookingTime = originalCookingTime > 0 ? originalCookingTime : 600;
		int cookingSeconds = cookingTime / 20;
		float cookingTimeReduction = 0.2F;

		if (fireAspectLevel > 0) {
			cookingTimeReduction -= fireAspectLevel * 0.05;
		}

		int result = (int) (cookingSeconds * cookingTimeReduction) * 20;

		return Mth.clamp(result, MINIMUM_COOKING_TIME, originalCookingTime);
	}
}
