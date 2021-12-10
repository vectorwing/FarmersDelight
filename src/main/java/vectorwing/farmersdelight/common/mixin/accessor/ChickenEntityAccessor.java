package vectorwing.farmersdelight.common.mixin.accessor;

import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Credits to MehVahdJukaar, creator of Supplementaries, for the implementation!
 */

@Mixin(Chicken.class)
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
