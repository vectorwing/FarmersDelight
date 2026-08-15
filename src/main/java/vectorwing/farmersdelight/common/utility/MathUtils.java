package vectorwing.farmersdelight.common.utility;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Util for providing and calculating math-related objects across the mod.
 */
public class MathUtils
{
	public static final Random RAND = new Random();

	/**
	 * Calculates a comparator signal using a transfer item handler.
	 *
	 * @param handler The inventory to compare.
	 * @return The redstone signal strength.
	 */
	public static int calcRedstoneFromItemHandler(@Nullable ResourceHandler<ItemResource> handler) {
		if (handler == null) {
			return 0;
		} else {
			int i = 0;
			float f = 0.0F;

			for (int j = 0; j < handler.size(); ++j) {
				ItemResource resource = handler.getResource(j);
				if (!resource.isEmpty()) {
					int capacity = handler.getCapacityAsInt(j, resource);
					if (capacity > 0) {
						f += (float) handler.getAmountAsInt(j) / (float) capacity;
					}
					++i;
				}
			}

			f = handler.size() == 0 ? 0.0F : f / (float) handler.size();
			return net.minecraft.util.Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
		}
	}
}
