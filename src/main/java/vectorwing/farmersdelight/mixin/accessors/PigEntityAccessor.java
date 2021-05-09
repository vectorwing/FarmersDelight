package vectorwing.farmersdelight.mixin.accessors;

import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Credits to MehVahdJukaar, creator of Supplementaries, for the implementation!
 */

@Mixin(PigEntity.class)
public interface PigEntityAccessor
{
	@Accessor("TEMPTATION_ITEMS")
	static void setFoodItems(Ingredient ingredient) {
		throw new AssertionError();
	}

	@Accessor("TEMPTATION_ITEMS")
	static Ingredient getFoodItems() {
		throw new AssertionError();
	}
}
