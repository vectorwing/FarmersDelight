package vectorwing.farmersdelight.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.tile.CookingPotTileEntity;
import vectorwing.farmersdelight.tile.SkilletTileEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.loot.LootFunction.Builder;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopySkilletFunction extends LootFunction
{
	public static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "copy_skillet");

	private CopySkilletFunction(ILootCondition[] conditions) {
		super(conditions);
	}

	public static Builder<?> builder() {
		return simpleBuilder(CopySkilletFunction::new);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		TileEntity tile = context.getParamOrNull(LootParameters.BLOCK_ENTITY);
		if (tile instanceof SkilletTileEntity) {
			CompoundNBT tag = ((SkilletTileEntity) tile).writeSkilletItem(new CompoundNBT());
			if (!tag.isEmpty()) {
				stack = ItemStack.of(tag.getCompound("Skillet"));
			}
		}
		return stack;
	}

	@Override
	@Nullable
	public LootFunctionType getType() {
		return null;
	}

	public static class Serializer extends LootFunction.Serializer<CopySkilletFunction>
	{
		@Override
		public CopySkilletFunction deserialize(JsonObject json, JsonDeserializationContext context, ILootCondition[] conditions) {
			return new CopySkilletFunction(conditions);
		}
	}
}
