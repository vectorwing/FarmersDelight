package vectorwing.farmersdelight.items;

import net.minecraft.item.Food;

public class Foods
{
	public static final Food CABBAGE = (new Food.Builder())
			.hunger(2).saturation(0.4f)
			.build();
	public static final Food TOMATO = (new Food.Builder())
			.hunger(1).saturation(0.3f)
			.build();
	public static final Food ONION = (new Food.Builder())
			.hunger(2).saturation(0.4f)
			.build();

}
