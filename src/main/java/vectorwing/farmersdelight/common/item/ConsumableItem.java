package vectorwing.farmersdelight.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Consumer;

public class ConsumableItem extends Item {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;

    public ConsumableItem(Properties properties) {
        this(properties, true, false);
    }

    public ConsumableItem(Properties properties, boolean hasFoodEffectTooltip) {
        this(properties, hasFoodEffectTooltip, false);
    }

    public ConsumableItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(properties);
        this.hasFoodEffectTooltip = hasFoodEffectTooltip;
        this.hasCustomTooltip = hasCustomTooltip;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (!level.isClientSide()) {
            affectConsumer(stack, level, consumer);
        }

        ItemStackTemplate container = stack.getCraftingRemainder();
        if (stack.has(DataComponents.FOOD) || stack.has(DataComponents.CONSUMABLE)) {
            stack = super.finishUsingItem(stack, level, consumer);
        } else if (consumer instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        if (container != null) {
            ItemStack remainder = container.create();
            if (stack.isEmpty()) {
                return remainder;
            }
            if (consumer instanceof Player player && !player.getAbilities().instabuild && !player.getInventory().add(remainder)) {
                player.drop(remainder, false);
            }
        }
        return stack;
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltipAdder, TooltipFlag flag) {
        if (!Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            return;
        }
        if (hasCustomTooltip) {
            MutableComponent text = TextUtils.tooltip(BuiltInRegistries.ITEM.getKey(this).getPath());
            tooltipAdder.accept(text.withStyle(ChatFormatting.BLUE));
        }
        if (hasFoodEffectTooltip) {
            TextUtils.addFoodEffectTooltip(stack, tooltipAdder, 1.0F, context.tickRate());
        }
    }
}
