package vectorwing.farmersdelight.common.loot.modifier;

import com.google.gson.JsonObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.PieBlock;

import javax.annotation.Nonnull;
import java.util.List;

public class PastrySlicingModifier extends LootModifier
{
	public static final int MAX_CAKE_BITES = 7;
	public static final int MAX_PIE_BITES = 4;
	private final Item pastrySlice;

	/**
	 * This loot modifier drops a slice for every remaining bite of a broken pastry block.
	 * If the block is a CakeBlock, it drops up to 7 slices.
	 * If the block is a PieBlock, it drops up to 4 slices.
	 * Otherwise, this does nothing.
	 */
	protected PastrySlicingModifier(LootItemCondition[] conditionsIn, Item pastrySliceIn) {
		super(conditionsIn);
		this.pastrySlice = pastrySliceIn;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
		if (state != null) {
			Block targetBlock = state.getBlock();
			if (targetBlock instanceof CakeBlock) {
				int bites = state.getValue(CakeBlock.BITES);
				generatedLoot.add(new ItemStack(pastrySlice, MAX_CAKE_BITES - bites));
			} else if (targetBlock instanceof PieBlock) {
				int bites = state.getValue(PieBlock.BITES);
				generatedLoot.add(new ItemStack(pastrySlice, MAX_PIE_BITES - bites));
			}
		}

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<PastrySlicingModifier>
	{
		@Override
		public PastrySlicingModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
			Item pastrySlice = ForgeRegistries.ITEMS.getValue(new ResourceLocation((GsonHelper.getAsString(object, "slice"))));
			return new PastrySlicingModifier(conditions, pastrySlice);
		}

		@Override
		public JsonObject write(PastrySlicingModifier instance) {
			return new JsonObject();
		}
	}
}
