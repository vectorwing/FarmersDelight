package vectorwing.farmersdelight.loot.functions;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import vectorwing.farmersdelight.tile.CookingPotTileEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopyMealFunction extends LootFunction {
    private CopyMealFunction(ILootCondition[] conditions) {
        super(conditions);
    }

    public static LootFunction.Builder<?> builder() {
        return builder(CopyMealFunction::new);
    }

	@Override
	protected ItemStack doApply(ItemStack stack, LootContext context)
	{
		TileEntity tile = context.get(LootParameters.BLOCK_ENTITY);
		if (tile instanceof CookingPotTileEntity) {
			CompoundNBT tag = ((CookingPotTileEntity) tile).writeMeal(new CompoundNBT());
			if (!tag.isEmpty()) {
				stack.setTagInfo("BlockEntityTag", tag);
			}
		}
		return stack;
	}

    @Override
    public LootFunctionType func_230425_b_() {
        return null;
    }
}
