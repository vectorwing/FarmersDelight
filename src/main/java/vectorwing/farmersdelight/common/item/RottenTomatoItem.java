package vectorwing.farmersdelight.common.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

import net.minecraft.world.item.Item.Properties;

public class RottenTomatoItem extends Item implements ProjectileItem
{
	public RottenTomatoItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack heldStack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ENTITY_ROTTEN_TOMATO_THROW.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
		if (!level.isClientSide) {
			RottenTomatoEntity projectile = new RottenTomatoEntity(level, player);
			projectile.setItem(heldStack);
			projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			level.addFreshEntity(projectile);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			heldStack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide());
	}

	@Override
	public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
		RottenTomatoEntity rottenTomato = new RottenTomatoEntity(level, position.x(), position.y(), position.z());
		rottenTomato.setItem(itemStack);
		return rottenTomato;
	}
}
