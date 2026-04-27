package vectorwing.farmersdelight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SteamParticle extends SingleQuadParticle
{
//	private final SpriteSet sprites;

	protected SteamParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
		super(level, x, y, z, sprites.first());
		this.scale(2.0F);
		this.setSize(0.25F, 0.25F);

		this.lifetime = this.random.nextInt(50) + 80;

		this.gravity = 3.0E-6F;
		this.xd = motionX;
		this.yd = motionY + (double) (this.random.nextFloat() / 500.0F);
		this.zd = motionZ;

//		this.sprites = sprites;
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
			this.xd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
			this.zd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
			this.yd -= this.gravity;
			this.move(this.xd, this.yd, this.zd);
			if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
				this.alpha -= 0.02F;
			}
		} else {
			this.remove();
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
			SteamParticle particle = new SteamParticle(level, x, y + 0.3D, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
			particle.setAlpha(0.6F);
			return particle;
		}
	}
}
