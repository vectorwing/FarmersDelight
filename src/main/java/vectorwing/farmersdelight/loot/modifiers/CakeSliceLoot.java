package vectorwing.farmersdelight.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vectorwing.farmersdelight.init.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

public class CakeSliceLoot {
    public static class CakeSliceSerializer extends GlobalLootModifierSerializer<CakeSliceModifier> {

        @Override
        public CakeSliceModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new CakeSliceModifier(ailootcondition);
        }
    }

    private static class CakeSliceModifier extends LootModifier {

        protected CakeSliceModifier(ILootCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            BlockState state = context.get(LootParameters.BLOCK_STATE);
            if (state == null)
                return generatedLoot;
            int bites = state.func_235903_d_(BlockStateProperties.BITES_0_6).orElse(-1);
            int TOTAL_SLICES = 7;
            if (bites != -1)
                generatedLoot.add(new ItemStack(ModItems.CAKE_SLICE.get(), TOTAL_SLICES - bites));
            return generatedLoot;
        }
    }
}
