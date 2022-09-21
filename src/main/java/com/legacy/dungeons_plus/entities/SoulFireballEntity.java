package com.legacy.dungeons_plus.entities;

import com.legacy.dungeons_plus.registry.DPEntityTypes;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class SoulFireballEntity extends Fireball
{
	private static final int DEFAULT_FUSE = 20;
	private int explosionPower = 1;
	private int fuse = DEFAULT_FUSE;

	public SoulFireballEntity(EntityType<? extends SoulFireballEntity> type, Level level)
	{
		super(type, level);
	}

	public SoulFireballEntity(Level level, LivingEntity owner, double dx, double dy, double dz, int power)
	{
		super(DPEntityTypes.SOUL_FIREBALL.get(), owner, dx, dy, dz, level);
		this.explosionPower = power;
	}

	public SoulFireballEntity(PlayMessages.SpawnEntity spawnEntity, Level level)
	{
		this(DPEntityTypes.SOUL_FIREBALL.get(), level);
	}

	@Override
	protected void onHit(HitResult hitResult)
	{
		super.onHit(hitResult);
		this.explode();
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult)
	{
		super.onHitEntity(hitResult);
		if (!this.level.isClientSide)
		{
			Entity target = hitResult.getEntity();
			Entity owner = this.getOwner();
			target.hurt(DamageSource.fireball(this, owner), 4.0F);
			if (owner instanceof LivingEntity livingOwner)
				this.doEnchantDamageEffects(livingOwner, target);
		}
	}
	
	@Override
	public boolean hurt(DamageSource damageSource, float damage)
	{
		this.fuse = DEFAULT_FUSE;
		this.explosionPower = Math.min(this.explosionPower + 1, 5);
		return super.hurt(damageSource, damage);
	}

	@Override
	protected ParticleOptions getTrailParticle()
	{
		return ParticleTypes.SOUL_FIRE_FLAME;
	}

	@Override
	public void tick()
	{
		super.tick();
		if (this.fuse <= 0)
			this.explode();
		this.fuse--;
	}

	private void explode()
	{
		if (this.level instanceof ServerLevel serverLevel)
		{
			serverLevel.explode(this, this.getX(), this.getY() + 0.2, this.getZ(), (float) this.explosionPower, false, Explosion.BlockInteraction.NONE);
			serverLevel.sendParticles(ParticleTypes.SOUL, this.getX(), this.getY() + 0.2, this.getZ(), 15, 0, 0, 0, 0.2);
			this.discard();
		}
	}

	private static final String POWER_KEY = "power", FUSE_KEY = "fuse";

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putByte(POWER_KEY, (byte) this.explosionPower);
		tag.putInt(FUSE_KEY, this.fuse);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		if (tag.contains(POWER_KEY, Tag.TAG_BYTE))
			this.explosionPower = tag.getByte(POWER_KEY);
		if (tag.contains(FUSE_KEY, Tag.TAG_INT))
			this.fuse = tag.getInt(FUSE_KEY);
	}

	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
