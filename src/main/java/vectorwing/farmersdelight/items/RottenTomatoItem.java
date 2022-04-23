package vectorwing.farmersdelight.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import vectorwing.farmersdelight.entity.RottenTomatoEntity;
import vectorwing.farmersdelight.registry.ModSounds;

public class RottenTomatoItem extends Item
{
	public RottenTomatoItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
		ItemStack heldStack = pPlayer.getItemInHand(pHand);
		pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.ENTITY_ROTTEN_TOMATO_THROW.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!pLevel.isClientSide) {
			RottenTomatoEntity projectile = new RottenTomatoEntity(pLevel, pPlayer);
			projectile.setItem(heldStack);
			projectile.shootFromRotation(pPlayer, pPlayer.xRot, pPlayer.yRot, 0.0F, 1.5F, 1.0F);
			pLevel.addFreshEntity(projectile);
		}

		pPlayer.awardStat(Stats.ITEM_USED.get(this));
		if (!pPlayer.abilities.instabuild) {
			heldStack.shrink(1);
		}

		return ActionResult.sidedSuccess(heldStack, pLevel.isClientSide());
	}
}
