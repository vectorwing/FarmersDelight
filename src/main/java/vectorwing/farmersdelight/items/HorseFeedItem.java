package vectorwing.farmersdelight.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HorseFeedItem extends MealItem {
    public HorseFeedItem(Properties builder) {
        super(builder);
    }

    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (target instanceof HorseEntity) {
            HorseEntity horse = (HorseEntity) target;
            if (horse.isAlive() && horse.isTame()) {
                horse.setJumping(true);
                target.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 2));
                target.addPotionEffect(new EffectInstance(Effects.SPEED, 6000, 2));
                target.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 6000, 1));
                if (stack.getContainerItem() != ItemStack.EMPTY) {
                    playerIn.addItemStackToInventory(stack.getContainerItem());
                }
                stack.shrink(1);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.PASS;
        }
        return ActionResultType.PASS;
    }
}
