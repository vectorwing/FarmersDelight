package vectorwing.farmersdelight.common.loot.function;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopySkilletFunction extends LootItemConditionalFunction
{
	public static final Identifier ID = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "copy_skillet");
	public static final MapCodec<CopySkilletFunction> CODEC = RecordCodecBuilder.mapCodec(
		i -> commonFields(i).apply(i, CopySkilletFunction::new)
	);

	private CopySkilletFunction(List<LootItemCondition> predicates) {
		super(predicates);
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return CODEC;
	}

	public static Builder<?> builder() {
		return simpleBuilder(CopySkilletFunction::new);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (context.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof SkilletBlockEntity skillet) {
			stack = skillet.getSkilletAsItem();
		}
		return stack;
	}
}
