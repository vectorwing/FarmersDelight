package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.PlainSignBlock;

public class StandingCanvasSignRenderState extends CanvasSignRenderState {
    public PlainSignBlock.Attachment attachmentType = PlainSignBlock.Attachment.GROUND;
}

