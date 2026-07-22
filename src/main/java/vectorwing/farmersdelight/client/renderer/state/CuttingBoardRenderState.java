package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class CuttingBoardRenderState extends BlockEntityRenderState {
	public final Random random = new Random();
    public ItemStackRenderState displayItem;
    public ItemStack boardStack;
    public Direction direction;
    public boolean isItemCarvingBoard;
}

