package vectorwing.farmersdelight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SparkleParticle extends SingleQuadParticle
{
	private final SpriteSet sprites;

	protected SparkleParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float gravity) {
		super(level, x, y, z, 0.0, 0.0, 0.0, sprites.first());
		this.lifetime = 4;
		this.quadSize *= 0.75F;
		this.sprites = sprites;
		this.setSpriteFromAge(this.sprites);
	}

	protected int getLightCoords(final float a) {
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
	protected @NonNull Layer getLayer() {
		return Layer.OPAQUE;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType>
	{
		private final SpriteSet sprites;

		public Factory(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
			return new SparkleParticle(level, x, y, z, sprites, 0.0F);
		}
	}
}


