package vectorwing.farmersdelight.mixin.accessors;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Credits to MehVahdJukaar, creator of Supplementaries, for the implementation!
 */

@Mixin(ChickenEntity.class)
public interface ChickenEntityAccessor
{
	@Accessor("FOOD_ITEMS")
	static void setFoodItems(Ingredient ingredient) {
		throw new AssertionError();
	}

	@Accessor("FOOD_ITEMS")
	static Ingredient getFoodItems() {
		throw new AssertionError();
	}
}
