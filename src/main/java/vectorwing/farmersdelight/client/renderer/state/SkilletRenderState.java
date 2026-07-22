package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class SkilletRenderState extends BlockEntityRenderState {
    public ItemStackRenderState displayItem;
    public ItemStack stack;
    public Direction direction;
}

