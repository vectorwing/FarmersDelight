package vectorwing.farmersdelight.client.model;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

public class SkilletCookingConditionalItemModelProperty implements ConditionalItemModelProperty {
    public static final MapCodec<SkilletCookingConditionalItemModelProperty> MAP_CODEC = MapCodec.unit(new SkilletCookingConditionalItemModelProperty());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i, ItemDisplayContext itemDisplayContext) {
        return stack.get(ModDataComponents.SKILLET_INGREDIENT.get()) != null;
    }

    @Override
    public MapCodec<SkilletCookingConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }
}

