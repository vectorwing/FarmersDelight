package vectorwing.farmersdelight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class StarParticle extends SingleQuadParticle
{
	protected StarParticle(ClientLevel level, double posX, double posY, double posZ, SpriteSet sprites) {
		super(level, posX, posY, posZ, 0.0D, 0.0D, 0.0D, sprites.first());
		this.xd *= 0.01F;
		this.yd *= 0.01F;
		this.zd *= 0.01F;
		this.yd += 0.1D;
		this.quadSize *= 1.5F;
		this.lifetime = 16;
		this.hasPhysics = false;
	}

	@Override
	public Layer getLayer() {
		return Layer.OPAQUE;
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		return this.quadSize * Mth.clamp(((float) this.age + scaleFactor) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.move(this.xd, this.yd, this.zd);
			if (this.y == this.yo) {
				this.xd *= 1.1D;
				this.zd *= 1.1D;
			}

			this.xd *= 0.86F;
			this.yd *= 0.86F;
			this.zd *= 0.86F;
			if (this.onGround) {
				this.xd *= 0.7F;
				this.zd *= 0.7F;
			}
		}
	}

	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet spriteSet;

		public Provider(SpriteSet sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			StarParticle particle = new StarParticle(level, x, y + 0.3D, z, spriteSet);
			particle.setColor(1.0F, 1.0F, 1.0F);
			return particle;
		}
	}
}
