package vectorwing.farmersdelight.entity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import vectorwing.farmersdelight.registry.ModEntityTypes;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.registry.ModSounds;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RottenTomatoEntity extends ProjectileItemEntity
{
	public RottenTomatoEntity(EntityType<? extends RottenTomatoEntity> entityType, World world) {
		super(entityType, world);
	}

	public RottenTomatoEntity(World world, LivingEntity entity) {
		super(ModEntityTypes.ROTTEN_TOMATO.get(), entity, world);
	}

	public RottenTomatoEntity(World world, double x, double y, double z) {
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
			IParticleData iparticledata = new ItemParticleData(ParticleTypes.ITEM, entityStack);

			for (int i = 0; i < 12; ++i) {
				this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(),
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F,
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F + 0.1F,
						((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F);
			}
		}
	}

	@Override
	protected void onHitEntity(EntityRayTraceResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = pResult.getEntity();
		entity.hurt(DamageSource.thrown(this, this.getOwner()), 0);
		this.playSound(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
	}

	@Override
	protected void onHit(RayTraceResult pResult) {
		super.onHit(pResult);
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
			this.playSound(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			this.remove();
		}
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
