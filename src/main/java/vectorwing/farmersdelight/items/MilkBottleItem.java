package vectorwing.farmersdelight.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Iterator;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MilkBottleItem extends Item
{
    public MilkBottleItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;

        if (!worldIn.isRemote) {
            Iterator<EffectInstance> itr = entityLiving.getActivePotionEffects().iterator();
            ArrayList<Effect> compatibleEffects = new ArrayList<>();

            // Select eligible effects
            while (itr.hasNext()) {
                EffectInstance effect = itr.next();
                if (effect.isCurativeItem(new ItemStack(Items.MILK_BUCKET))) {
                    compatibleEffects.add(effect.getPotion());
                }
            }

            // Randomly pick one, then remove
            if (compatibleEffects.size() > 0) {
                EffectInstance selectedEffect = entityLiving.getActivePotionEffect(compatibleEffects.get(worldIn.rand.nextInt(compatibleEffects.size())));
                if (selectedEffect != null && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent(entityLiving, selectedEffect))) {
                    entityLiving.removePotionEffect(selectedEffect.getPotion());
                }
            }
        }

        if (player instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
        }

        if (player != null) {
            player.addStat(Stats.ITEM_USED.get(this));
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }
        }

        if (player == null || !player.abilities.isCreativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (player != null) {
                player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return DrinkHelper.startDrinking(worldIn, playerIn, handIn);
    }
}
