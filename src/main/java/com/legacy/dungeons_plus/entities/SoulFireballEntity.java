package com.legacy.dungeons_plus.entities;

import com.legacy.dungeons_plus.registry.DPDamageSource;
import com.legacy.dungeons_plus.registry.DPEntityTypes;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class SoulFireballEntity extends Fireball
{
	private static final int DEFAULT_FUSE = 20;
	private int explosionPower = 1;
	private boolean hasFlame = false;
	private boolean isMulti = false;
	private int knockbackPower = 0;
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

	public void setKnockback(int knockbackLevel)
	{
		this.knockbackPower = knockbackLevel;
	}

	public void setIsMultishot(boolean isMulti)
	{
		this.isMulti = isMulti;
		this.explosionPower = isMulti ? 0 : this.explosionPower;
	}

	public void setHasFlame(boolean hasFlame)
	{
		this.hasFlame = hasFlame;
	}

	@Override
	protected void onHit(HitResult hitResult)
	{
		if (hitResult instanceof EntityHitResult entityHitResult)
		{
			var hitEntity = entityHitResult.getEntity();
			if ((hitEntity instanceof SoulFireballEntity || hitEntity == this.getOwner()) && this.tickCount < 5)
				return;
		}
		super.onHit(hitResult); // onHitEntity runs during this
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
			float damage = this.isMulti ? 6.0F + (this.random.nextFloat() * 2.0F) : 2.0F;
			target.hurt(DPDamageSource.fireballExplosion(this, owner), damage);
			if (this.hasFlame)
				target.setSecondsOnFire(3);
			if (owner instanceof LivingEntity livingOwner)
				this.doEnchantDamageEffects(livingOwner, target);
		}
	}

	@Override
	public boolean hurt(DamageSource damageSource, float damage)
	{
		Entity owner = this.getOwner();
		Entity damager = damageSource.getEntity();
		if (damager != owner || (owner == null && damager == null))
		{
			this.fuse = DEFAULT_FUSE;
			this.explosionPower = Math.min(this.explosionPower + 1, 8);
			// Owner set in super
			return super.hurt(damageSource, damage);
		}
		return false;
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
			serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), this.explosionPower, false, Explosion.BlockInteraction.NONE);

			if (this.knockbackPower > 0)
			{
				double k = this.knockbackPower * 0.2 + 1.0;
				double r = this.isMulti ? 2 : 4;
				Vec3 range = new Vec3(r, r, r);
				Vec3 pos = this.position();
				for (Entity e : this.level.getEntitiesOfClass(Entity.class, new AABB(pos.subtract(range), pos.add(range))))
				{
					if (e instanceof SoulFireballEntity)
						continue;
					double knockback = e instanceof LivingEntity living ? ProtectionEnchantment.getExplosionKnockbackAfterDampener(living, k) : k;
					Vec3 motion = pos.subtract(0, 1, 0).subtract(e.position()).normalize().scale(-knockback);
					e.setDeltaMovement(0, 0, 0);
					e.setDeltaMovement(motion.x, 0.35 * this.knockbackPower, motion.z);
					if (e instanceof Player)
						e.hurtMarked = true;
				}
			}

			if (this.hasFlame)
			{
				BlockPos p = this.blockPosition();
				if (this.level.getBlockState(p).isAir() && this.level.getBlockState(p.below()).isSolidRender(this.level, p.below()))
					this.level.setBlockAndUpdate(p, BaseFireBlock.getState(this.level, p));
				int fireRange = this.isMulti ? 1 : 2;
				for (BlockPos bPos : BlockPos.withinManhattan(p, fireRange, fireRange, fireRange))
					if (this.random.nextInt(10) == 0 && this.level.getBlockState(bPos).isAir() && this.level.getBlockState(bPos.below()).isSolidRender(this.level, bPos.below()))
						this.level.setBlockAndUpdate(bPos, BaseFireBlock.getState(this.level, bPos));
			}
			serverLevel.sendParticles(ParticleTypes.SOUL, this.getX(), this.getY() + 0.5, this.getZ(), this.isMulti ? 5 : 15, 0, 0, 0, this.isMulti ? 0.07 : 0.2);
			this.discard();

		}
	}

	private static final String POWER_KEY = "power", FUSE_KEY = "fuse", HAS_FLAME_KEY = "has_flame",
			IS_MULTI = "is_multi", KNOCKBACK_KEY = "knockback";

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putByte(POWER_KEY, (byte) this.explosionPower);
		tag.putInt(FUSE_KEY, this.fuse);
		tag.putBoolean(HAS_FLAME_KEY, this.hasFlame);
		tag.putBoolean(IS_MULTI, this.isMulti);
		tag.putInt(KNOCKBACK_KEY, this.knockbackPower);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		if (tag.contains(POWER_KEY, Tag.TAG_BYTE))
			this.explosionPower = tag.getByte(POWER_KEY);
		if (tag.contains(FUSE_KEY, Tag.TAG_INT))
			this.fuse = tag.getInt(FUSE_KEY);
		if (tag.contains(HAS_FLAME_KEY, Tag.TAG_BYTE))
			this.hasFlame = tag.getBoolean(HAS_FLAME_KEY);
		if (tag.contains(IS_MULTI, Tag.TAG_BYTE))
			this.isMulti = tag.getBoolean(IS_MULTI);
		if (tag.contains(KNOCKBACK_KEY, Tag.TAG_INT))
			this.knockbackPower = tag.getInt(KNOCKBACK_KEY);
	}

	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
