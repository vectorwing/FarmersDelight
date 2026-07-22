package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class StoveRenderState extends BlockEntityRenderState {
    public ItemStackRenderState[] stoveStacks;
    public Direction facing;
}

