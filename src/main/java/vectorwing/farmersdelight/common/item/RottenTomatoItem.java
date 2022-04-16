package vectorwing.farmersdelight.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;

public class RottenTomatoItem extends Item
{
	public RottenTomatoItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack heldStack = pPlayer.getItemInHand(pHand);
		pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.random.nextFloat() * 0.4F + 0.8F));
		if (!pLevel.isClientSide) {
			RottenTomatoEntity projectile = new RottenTomatoEntity(pLevel, pPlayer);
			projectile.setItem(heldStack);
			projectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
			pLevel.addFreshEntity(projectile);
		}

		pPlayer.awardStat(Stats.ITEM_USED.get(this));
		if (!pPlayer.getAbilities().instabuild) {
			heldStack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(heldStack, pLevel.isClientSide());
	}
}
