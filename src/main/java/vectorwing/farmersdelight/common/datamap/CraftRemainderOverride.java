package vectorwing.farmersdelight.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * <p>Datamap for defining that an item should leave behind a container when used in crafting.</p>
 *
 * <p>This exists as an alternative for items which should leave a craft remainder, but aren't programmed to do so, such as
 * several items from Minecraft. It will also <b>override an existing craft remainder</b>, if the given item has one defined in code.</p>
 *
 * <p>If the item is from your mod, I suggest registering the craft remainder as an
 * item property instead (see {@link Item.Properties#craftRemainder}).</p>
 *
 * <p>Used by the following workstations:</p>
 * <li><b>Cooking Pot</b> - Spawns the remainder if the given item is used as a cooking ingredient.
 *
 * @param remainderItem The item to be left behind after use (example: Bowl, Glass Bottle etc)
 */
public record CraftRemainderOverride(Item remainderItem)
{
	public static final Codec<CraftRemainderOverride> CRAFT_REMAINDER_CODEC = BuiltInRegistries.ITEM.byNameCodec()
		.xmap(CraftRemainderOverride::new, CraftRemainderOverride::remainderItem);
	public static final Codec<CraftRemainderOverride> CODEC = Codec.withAlternative(
		RecordCodecBuilder.create(in -> in.group(
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("remainder_item").forGetter(CraftRemainderOverride::remainderItem)).apply(in, CraftRemainderOverride::new)),
		CRAFT_REMAINDER_CODEC);
}
