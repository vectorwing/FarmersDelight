package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractStoveBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AbstractStoveBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false)
        );
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(LIT)) {
            if (tryToExtinguish(state, level, pos, player, hand, hit)) return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            if (tryToIgnite(state, level, pos, player, hand, hit)) return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (tryToPlaceFoodItem(state, level, pos, player, hand, hit)) return InteractionResult.sidedSuccess(level.isClientSide());
        return InteractionResult.PASS;
    }

    protected boolean tryToIgnite(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item heldItem = heldStack.getItem();

        if (heldStack.canPerformAction(ToolActions.SHOVEL_DIG)) {
            this.extinguish(player, state, level, pos);
            heldStack.hurtAndBreak(1, player, (action) -> {
                action.broadcastBreakEvent(hand);
            });
            return true;
        }

        if (heldItem == Items.WATER_BUCKET) {
            if (!level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            this.extinguish(player, state, level, pos);

            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));
            }

            return true;
        }

        return false;
    }

    protected boolean tryToExtinguish(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        Item heldItem = heldStack.getItem();

        if (heldItem instanceof FlintAndSteelItem) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
            this.ignite(state, level, pos);
            heldStack.hurtAndBreak(1, player, (action) -> {
                action.broadcastBreakEvent(hand);
            });
            return true;
        }

        if (heldItem instanceof FireChargeItem) {
            if (!level.isClientSide) {
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
            }
            this.ignite(state, level, pos);
            if (!player.getAbilities().instabuild) {
                heldStack.shrink(1);
            }
            return true;
        }

        return false;
    }
      
    protected boolean tryToPlaceFoodItem(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof AbstractStoveBlockEntity<?,?> stoveBlockEntity)) return false;

        ItemStack itemstack = player.getItemInHand(hand);
        Optional<? extends AbstractCookingRecipe> recipe = stoveBlockEntity.getCookableRecipe(itemstack);
        if (recipe.isEmpty()) return false;

        if (level.isClientSide()) return true;

        return stoveBlockEntity.placeFood(
                player,
                player.getAbilities().instabuild ? itemstack.copy() : itemstack,
                recipe.get().getCookingTime()
        );
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LIT, true);
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        boolean isLit = level.getBlockState(pos).getValue(LIT);
        if (isLit && !entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(ModDamageTypes.getSimpleDamageSource(level, ModDamageTypes.STOVE_BURN), 1.0F);
        }

        super.stepOn(level, pos, state, entity);
    }

    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) return;

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof AbstractStoveBlockEntity<?,?> stoveBlockEntity) {
            Containers.dropContents(level, pos, stoveBlockEntity.getItems());
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockState rotate(BlockState p_48722_, Rotation rotation) {
        return p_48722_.setValue(FACING, rotation.rotate(p_48722_.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING, LIT);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (state.getValue(CampfireBlock.LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY();
            double z = pos.getZ() + 0.5;
            if (randomSource.nextInt(10) == 0) {
                level.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = (Direction)state.getValue(HorizontalDirectionalBlock.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double horizontalOffset = randomSource.nextDouble() * 0.6 - 0.3;
            double xOffset = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : horizontalOffset;
            double yOffset = randomSource.nextDouble() * 6.0 / 16.0;
            double zOffset = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : horizontalOffset;
            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
        }

    }

    public void extinguish(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, false), 2);
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F, false);

        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof AbstractStoveBlockEntity<?,?> stoveBlockEntity) {
            stoveBlockEntity.extinguish();
        }

        level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
    }

    public void ignite(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(LIT, true), 11);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createStoveTicker(Level level, BlockState state, BlockEntityType<T> tickerBlockEntityType, BlockEntityType<? extends AbstractStoveBlockEntity<?,?>> thisBlockEntityType) {
        if (level.isClientSide) return createTickerHelper(
                tickerBlockEntityType,
                thisBlockEntityType,
                AbstractStoveBlockEntity::particleTick
        );

        return createTickerHelper(
                tickerBlockEntityType,
                thisBlockEntityType,
                state.getValue(LIT)
                        ? AbstractStoveBlockEntity::cookTick
                        : AbstractStoveBlockEntity::cooldownTick
        );
    }

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
        return state.getValue(LIT) ? BlockPathTypes.DAMAGE_FIRE : null;
    }
}
