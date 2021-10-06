package vectorwing.farmersdelight.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class SteamParticle extends SpriteTexturedParticle
{
	protected SteamParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(world, x, y, z);
		this.multiplyParticleScaleBy(2.0F);
		this.setSize(0.25F, 0.25F);

		this.maxAge = this.rand.nextInt(50) + 80;

		this.particleGravity = 3.0E-6F;
		this.motionX = motionX;
		this.motionY = motionY + (double)(this.rand.nextFloat() / 500.0F);
		this.motionZ = motionZ;
	}

	@Override
	@Nonnull
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ < this.maxAge && !(this.particleAlpha <= 0.0F)) {
			this.motionX += this.rand.nextFloat() / 5000.0F * (float)(this.rand.nextBoolean() ? 1 : -1);
			this.motionZ += this.rand.nextFloat() / 5000.0F * (float)(this.rand.nextBoolean() ? 1 : -1);
			this.motionY -= this.particleGravity;
			this.move(this.motionX, this.motionY, this.motionZ);
			if (this.age >= this.maxAge - 60 && this.particleAlpha > 0.01F) {
				this.particleAlpha -= 0.02F;
			}
		} else {
			this.setExpired();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType>
	{
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite sprite) {
			this.spriteSet = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SteamParticle particle = new SteamParticle(worldIn, x, y + 0.3D, z, xSpeed, ySpeed, zSpeed);
			particle.setAlphaF(0.6F);
			particle.selectSpriteRandomly(this.spriteSet);
			return particle;
		}
	}
}
