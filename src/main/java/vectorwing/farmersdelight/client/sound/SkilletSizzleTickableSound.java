package vectorwing.farmersdelight.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.tile.SkilletTileEntity;

public class SkilletSizzleTickableSound extends AbstractTickableSoundInstance
{
	private final SkilletTileEntity skillet;
	private final BlockPos pos;

	public SkilletSizzleTickableSound(SkilletTileEntity skillet) {
		super(ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS);
		this.volume = 0.5F;
		this.looping = true;
		this.delay = 0;
		this.skillet = skillet;
		this.pos = this.skillet.getBlockPos();
		this.x = this.pos.getX();
		this.y = this.pos.getY();
		this.z = this.pos.getZ();
	}

	@Override
	public void tick() {
		if (this.skillet.isRemoved() || !this.skillet.isHeated() || !this.skillet.hasStoredStack()) {
			this.stop();
		} else {
			this.x = this.pos.getX();
			this.y = this.pos.getY();
			this.z = this.pos.getZ();
		}
	}
}
