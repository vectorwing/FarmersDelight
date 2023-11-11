package vectorwing.farmersdelight.common.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopySkilletFunction extends LootItemConditionalFunction
{
	public static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "copy_skillet");
	public static final Codec<CopySkilletFunction> CODEC = RecordCodecBuilder.create(
			p_298131_ -> commonFields(p_298131_).apply(p_298131_, CopySkilletFunction::new)
	);

	private CopySkilletFunction(List<LootItemCondition> conditions) {
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
	public LootItemFunctionType getType() {
		return ModLootFunctions.COPY_SKILLET.get();
	}
}
