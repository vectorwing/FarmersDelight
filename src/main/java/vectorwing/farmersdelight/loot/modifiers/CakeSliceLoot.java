package vectorwing.farmersdelight.loot.modifiers;

import vectorwing.farmersdelight.init.ModItems;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class CakeSliceLoot
{
	public static class CakeSliceSerializer extends GlobalLootModifierSerializer<CakeSliceModifier>
	{

		@Override
		public CakeSliceModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition)
		{
			return new CakeSliceModifier(ailootcondition);
		}
	}

	private static class CakeSliceModifier extends LootModifier
	{
		private final int TOTAL_SLICES = 7;
		protected CakeSliceModifier(ILootCondition[] conditionsIn)
		{
			super(conditionsIn);
		}

		@Nonnull
		@Override
		protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
		{
			BlockState state = context.get(LootParameters.BLOCK_STATE);
			if(state.has(BlockStateProperties.BITES_0_6)) {
				int bites = state.get(BlockStateProperties.BITES_0_6);
				generatedLoot.add(new ItemStack(ModItems.CAKE_SLICE.get(), TOTAL_SLICES - bites));
			}
			return generatedLoot;
		}
	}
}
