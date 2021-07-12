package vectorwing.farmersdelight.client.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.tile.SkilletTileEntity;

public class SkilletSizzleTickableSound extends TickableSound
{
	private final SkilletTileEntity skillet;
	private final BlockPos pos;

	public SkilletSizzleTickableSound(SkilletTileEntity skillet) {
		super(ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS);
		this.volume = 0.5F;
		this.repeat = true;
		this.repeatDelay = 0;
		this.skillet = skillet;
		this.pos = this.skillet.getPos();
		this.x = this.pos.getX();
		this.y = this.pos.getY();
		this.z = this.pos.getZ();
	}

	@Override
	public void tick() {
		if (this.skillet.isRemoved() || !this.skillet.isHeated() || !this.skillet.hasStoredStack()) {
			this.finishPlaying();
		} else {
			this.x = this.pos.getX();
			this.y = this.pos.getY();
			this.z = this.pos.getZ();
		}
	}
}
