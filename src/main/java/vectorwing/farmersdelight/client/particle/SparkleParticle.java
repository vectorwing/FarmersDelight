package vectorwing.farmersdelight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SparkleParticle extends SingleQuadParticle
{
	private final SpriteSet sprites;

	protected SparkleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float gravity) {
		super(level, x, y, z, 0.0, 0.0, 0.0, sprites.first());
		this.lifetime = 4;
		this.quadSize *= 0.75F;
		this.sprites = sprites;
		this.setSpriteFromAge(sprites);
	}

	public int getLightColor(float partialTick) {
		return 15728880;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.sprites);
		}
	}

	@Override
	public Layer getLayer() {
		return Layer.OPAQUE;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			return new SparkleParticle(level, x, y, z, this.sprites, 0.0F);
		}
	}
}
