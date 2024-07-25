package vectorwing.farmersdelight.common.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public class ItemStackWrapper
{
	public static final ItemStackWrapper EMPTY = new ItemStackWrapper(ItemStack.EMPTY);
	public static final Codec<ItemStackWrapper> CODEC = ItemStack.OPTIONAL_CODEC.xmap(ItemStackWrapper::new, ItemStackWrapper::getStack);
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackWrapper> STREAM_CODEC =
			ItemStack.OPTIONAL_STREAM_CODEC.map(ItemStackWrapper::new, (itemStackWrapper) -> itemStackWrapper.itemStack);

	private final ItemStack itemStack;
	private final int hashCode;

	public ItemStackWrapper(ItemStack stack) {
		this.itemStack = stack;
		this.hashCode = ItemStack.hashItemAndComponents(stack);
	}

	public ItemStack getStack() {
		return this.itemStack;
	}

	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ItemStackWrapper itemStackWrapper) {
			return ItemStack.matches(this.itemStack, itemStackWrapper.getStack());
		}
		return false;
	}
}
