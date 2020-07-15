package vectorwing.farmersdelight.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ToolItem;

import java.util.Set;

public class KnifeItem extends ToolItem
{
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.HAY_BLOCK, Blocks.CAKE);

	public KnifeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
	{
		super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, builder);
	}
}
