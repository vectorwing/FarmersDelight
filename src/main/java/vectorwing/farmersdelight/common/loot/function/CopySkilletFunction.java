package vectorwing.farmersdelight.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopySkilletFunction extends LootItemConditionalFunction
{
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "copy_skillet");
	public static final MapCodec<CopySkilletFunction> CODEC = RecordCodecBuilder.mapCodec(
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
		if (tile instanceof SkilletBlockEntity blockEntity) {
			stack = blockEntity.getSkilletAsItem();
		}
		return stack;
	}

	@Override
	public LootItemFunctionType<CopySkilletFunction> getType() {
		return ModLootFunctions.COPY_SKILLET.get();
	}
}
