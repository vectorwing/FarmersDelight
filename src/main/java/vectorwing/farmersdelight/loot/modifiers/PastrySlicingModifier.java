package vectorwing.farmersdelight.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.blocks.PieBlock;
import vectorwing.farmersdelight.registry.ModItems;

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
	protected PastrySlicingModifier(ILootCondition[] conditionsIn, Item pastrySliceIn) {
		super(conditionsIn);
		this.pastrySlice = pastrySliceIn;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		BlockState state = context.get(LootParameters.BLOCK_STATE);
		if (state != null) {
			Block targetBlock = state.getBlock();
			if (targetBlock instanceof CakeBlock) {
				int bites = state.get(CakeBlock.BITES);
				generatedLoot.add(new ItemStack(pastrySlice, MAX_CAKE_BITES - bites));
			} else if (targetBlock instanceof PieBlock) {
				int bites = state.get(PieBlock.BITES);
				generatedLoot.add(new ItemStack(pastrySlice, MAX_PIE_BITES - bites));
			}
		}

		return generatedLoot;
	}

	public static class CakeSliceSerializer extends GlobalLootModifierSerializer<PastrySlicingModifier>
	{
		@Override
		public PastrySlicingModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			Item pastrySlice = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getString(object, "slice"))));
			return new PastrySlicingModifier(conditions, pastrySlice);
		}

		@Override
		public JsonObject write(PastrySlicingModifier instance) {
			return new JsonObject();
		}
	}
}
