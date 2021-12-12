package vectorwing.farmersdelight.common.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopySkilletFunction extends LootItemConditionalFunction
{
	public static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "copy_skillet");

	private CopySkilletFunction(LootItemCondition[] conditions) {
		super(conditions);
	}

	public static Builder<?> builder() {
		return simpleBuilder(CopySkilletFunction::new);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		BlockEntity tile = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
		if (tile instanceof SkilletBlockEntity) {
			CompoundTag tag = ((SkilletBlockEntity) tile).writeSkilletItem(new CompoundTag());
			if (!tag.isEmpty()) {
				stack = ItemStack.of(tag.getCompound("Skillet"));
			}
		}
		return stack;
	}

	@Override
	@Nullable
	public LootItemFunctionType getType() {
		return null;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<CopySkilletFunction>
	{
		@Override
		public CopySkilletFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
			return new CopySkilletFunction(conditions);
		}
	}
}
