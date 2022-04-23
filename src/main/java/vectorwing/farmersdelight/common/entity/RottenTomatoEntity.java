package vectorwing.farmersdelight.common.entity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import vectorwing.farmersdelight.common.registry.ModEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.registry.ModSounds;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RottenTomatoEntity extends ThrowableItemProjectile
{
	public RottenTomatoEntity(EntityType<? extends RottenTomatoEntity> entityType, Level world) {
		super(entityType, world);
	}

	public RottenTomatoEntity(Level world, LivingEntity entity) {
		super(ModEntityTypes.ROTTEN_TOMATO.get(), entity, world);
	}

	public RottenTomatoEntity(Level world, double x, double y, double z) {
		super(ModEntityTypes.ROTTEN_TOMATO.get(), x, y, z, world);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.ROTTEN_TOMATO.get();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte pId) {
		ItemStack entityStack = new ItemStack(this.getDefaultItem());
		if (pId == 3) {
			ParticleOptions iparticledata = new ItemParticleOption(ParticleTypes.ITEM, entityStack);

			for (int i = 0; i < 12; ++i) {
				this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(),
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F,
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F + 0.1F,
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = pResult.getEntity();
		entity.hurt(DamageSource.thrown(this, this.getOwner()), 0);
		this.playSound(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
	}

	@Override
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.playSound(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			this.discard();
		}
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
