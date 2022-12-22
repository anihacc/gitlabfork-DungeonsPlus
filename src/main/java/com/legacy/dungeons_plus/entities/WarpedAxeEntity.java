package com.legacy.dungeons_plus.entities;

import javax.annotation.Nullable;

import com.legacy.dungeons_plus.DPConfig;
import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.data.advancement.ThrownItemHitBlockTrigger;
import com.legacy.dungeons_plus.items.WarpedAxeItem;
import com.legacy.dungeons_plus.registry.DPDamageSource;
import com.legacy.dungeons_plus.registry.DPEntityTypes;
import com.legacy.dungeons_plus.registry.DPItems;
import com.legacy.dungeons_plus.registry.DPSoundEvents;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class WarpedAxeEntity extends AbstractArrow
{
	private static final EntityDataAccessor<ItemStack> ID_AXE_STACK = SynchedEntityData.defineId(WarpedAxeEntity.class, EntityDataSerializers.ITEM_STACK);
	private static final EntityDataAccessor<Float> ID_ROTATION = SynchedEntityData.defineId(WarpedAxeEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> ID_IN_BLOCK = SynchedEntityData.defineId(WarpedAxeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> ID_TELEPORT_PLAYER = SynchedEntityData.defineId(WarpedAxeEntity.class, EntityDataSerializers.BOOLEAN);

	private boolean dealtDamage;
	public int clientSideReturnAxeTickCount;

	public int spinRot = 0, oldSpinRot = 0;

	public WarpedAxeEntity(EntityType<? extends WarpedAxeEntity> type, Level level)
	{
		super(type, level);
	}

	public WarpedAxeEntity(Level level, LivingEntity owner, ItemStack stack)
	{
		super(DPEntityTypes.WARPED_AXE.get(), owner, level);
		this.entityData.set(ID_AXE_STACK, stack.copy());
		this.entityData.set(ID_ROTATION, owner.getYHeadRot());
	}

	public WarpedAxeEntity(PlayMessages.SpawnEntity spawnEntity, Level level)
	{
		this(DPEntityTypes.WARPED_AXE.get(), level);
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(ID_AXE_STACK, DPItems.WARPED_AXE.get().getDefaultInstance());
		this.entityData.define(ID_ROTATION, 0.0F);
		this.entityData.define(ID_IN_BLOCK, false);
		this.entityData.define(ID_TELEPORT_PLAYER, true);
	}

	public ItemStack getAxe()
	{
		return this.entityData.get(ID_AXE_STACK);
	}

	public void setAxe(ItemStack stack)
	{
		this.entityData.set(ID_AXE_STACK, stack.copy());
	}

	public float getRenderRotation()
	{
		return this.entityData.get(ID_ROTATION);
	}

	public boolean isInBlock()
	{
		return this.entityData.get(ID_IN_BLOCK);
	}

	public boolean shouldTeleportOwner()
	{
		return this.entityData.get(ID_TELEPORT_PLAYER);
	}

	public void setTeleportsOwner(boolean shouldTeleportOwner)
	{
		this.entityData.set(ID_TELEPORT_PLAYER, shouldTeleportOwner);
	}

	@Nullable
	private Vec3 oldPos = null;

	@Override
	public void tick()
	{
		if (this.inGround != this.isInBlock())
			this.entityData.set(ID_IN_BLOCK, this.inGround);

		if (this.inGroundTime > 4)
			this.dealtDamage = true;

		this.oldSpinRot = this.spinRot;
		if (!this.inGround)
		{
			this.spinRot += 45;

			if (this.oldPos != null)
			{
				if (this.shouldTeleportOwner())
				{
					var pos = this.position();
					double distScale = 1.0;
					Vec3 motionVec = this.position().subtract(this.oldPos).normalize().scale(3);
					int amount = this.random.nextInt(3) + 2;
					for (int i = 0; i < amount; i++)
						this.level.addParticle(ParticleTypes.REVERSE_PORTAL, pos.x + (this.random.nextDouble() - 0.5) * distScale, pos.y + (this.random.nextDouble() - 0.5) * distScale, pos.z + (this.random.nextDouble() - 0.5) * distScale, motionVec.x, motionVec.y, motionVec.z);
				}
			}
		}
		this.oldPos = this.position();
		Entity owner = this.getOwner();
		int loyaltyLevel = EnchantmentHelper.getLoyalty(this.getAxe());
		boolean voidReturn = this.position().y < this.level.dimensionType().minY() && DPConfig.COMMON.loyaltyReturnsFromVoid.get();
		if (loyaltyLevel > 0 && (this.dealtDamage || this.isNoPhysics() || voidReturn) && owner != null)
		{
			if (!this.isAcceptibleReturnOwner())
			{
				if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED)
				{
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}

				this.discard();
			}
			else
			{
				this.setNoPhysics(true);
				Vec3 vec3 = owner.getEyePosition().subtract(this.position());
				this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) loyaltyLevel, this.getZ());
				if (this.level.isClientSide)
				{
					this.yOld = this.getY();
				}

				double returnSpeed = 0.05D * loyaltyLevel;
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(returnSpeed)));
				if (this.clientSideReturnAxeTickCount == 0)
				{
					this.playSound(DPSoundEvents.WARPED_AXE_RETURN.get(), 10.0F, 1.0F);
				}

				++this.clientSideReturnAxeTickCount;
			}
		}

		super.tick();
	}

	private boolean isAcceptibleReturnOwner()
	{
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive())
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		return false;
	}

	@Override
	protected ItemStack getPickupItem()
	{
		return this.getAxe().copy();
	}

	@Nullable
	@Override
	protected EntityHitResult findHitEntity(Vec3 vec1, Vec3 vec2)
	{
		return this.dealtDamage ? null : super.findHitEntity(vec1, vec2);
	}

	@Override
	protected void onHitBlock(BlockHitResult hitResult)
	{
		BlockState state = this.level.getBlockState(hitResult.getBlockPos());
		if (this.getOwner() instanceof ServerPlayer player)
			ThrownItemHitBlockTrigger.TRIGGER.trigger(player, this.getAxe(), state);
		super.onHitBlock(hitResult);
		if (state.is(DPTags.Blocks.WARPED_AXE_TELEPORTS_TO))
		{
			this.teleportOwner(this.getOwner());
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult)
	{
		Entity hitEntity = hitResult.getEntity();
		ItemStack stack = this.getAxe();
		Item item = stack.getItem();
		float damage;
		if (item == DPItems.WARPED_AXE.get())
			damage = WarpedAxeItem.BASE_DAMAGE;
		else if (item instanceof SwordItem sword)
			damage = sword.getDamage();
		else if (item instanceof DiggerItem digger)
			damage = digger.getAttackDamage();
		else
			damage = 6.0F;

		if (hitEntity instanceof LivingEntity livingEntity)
			damage += EnchantmentHelper.getDamageBonus(this.getAxe(), livingEntity.getMobType()) / 2;

		if (!this.shouldTeleportOwner())
			damage /= 2;

		Entity owner = this.getOwner();
		this.dealtDamage = true;
		if (hitEntity.hurt(DPDamageSource.warpedAxe(this, owner, stack), damage))
		{
			if (hitEntity.getType().is(DPTags.EntityTypes.WARPED_AXE_IMMUNE))
				return;

			this.teleportOwner(owner);

			if (hitEntity instanceof LivingEntity livingHit)
			{
				var enchantments = EnchantmentHelper.getEnchantments(stack);
				int fireAspect = enchantments.getOrDefault(Enchantments.FIRE_ASPECT, 0);
				if (fireAspect > 0)
					livingHit.setSecondsOnFire(fireAspect * 4);

				EnchantmentHelper.doPostHurtEffects(livingHit, owner);
				if (owner instanceof LivingEntity livingOwner)
					EnchantmentHelper.doPostDamageEffects(livingOwner, livingHit);
				this.doPostHurtEffects(livingHit);
			}
		}

		this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
		this.playSound(DPSoundEvents.WARPED_AXE_HIT.get(), 1.0F, 1.0F);
	}

	private void teleportOwner(@Nullable Entity owner)
	{
		if (owner == null || !this.shouldTeleportOwner())
			return;
		var pos = this.position();
		owner.teleportTo(pos.x, pos.y, pos.z);
		owner.resetFallDistance();
		owner.hurt(DamageSource.FALL, 5.0F);
		level.broadcastEntityEvent(this, (byte) 46);
		if (owner instanceof Player player)
			player.getCooldowns().addCooldown(this.getAxe().getItem(), 60);
		owner.gameEvent(GameEvent.TELEPORT);
		this.playSound(DPSoundEvents.WARPED_AXE_TELEPORT.get(), 1.0F, 1.0F);
	}

	@Override
	protected boolean tryPickup(Player player)
	{
		return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent()
	{
		return DPSoundEvents.WARPED_AXE_LAND.get();
	}

	@Override
	public void playerTouch(Player player)
	{
		if (this.ownedBy(player) || this.getOwner() == null)
			super.playerTouch(player);
	}

	private static final String DEALT_DAMAGE_KEY = "dealt_damage", STACK_KEY = "stack",
			SHOULD_TELEPORT = "can_teleport";

	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		this.dealtDamage = tag.getBoolean(DEALT_DAMAGE_KEY);
		ItemStack stack = ItemStack.of(tag.getCompound(STACK_KEY));
		if (!stack.isEmpty())
			this.setAxe(stack);
		this.setTeleportsOwner(tag.getBoolean(SHOULD_TELEPORT));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putBoolean(DEALT_DAMAGE_KEY, this.dealtDamage);
		ItemStack stack = this.getAxe();
		if (!stack.isEmpty())
			tag.put(STACK_KEY, stack.save(new CompoundTag()));
		tag.putBoolean(SHOULD_TELEPORT, this.shouldTeleportOwner());
	}

	@Override
	public void tickDespawn()
	{
		int loyalty = EnchantmentHelper.getLoyalty(this.getAxe());
		if (this.pickup != AbstractArrow.Pickup.ALLOWED || loyalty <= 0)
			super.tickDespawn();
	}

	@Override
	public boolean shouldRender(double x, double y, double z)
	{
		return true;
	}

	@Override // Can't go as fast as a trident in water, but it can still be used
	protected float getWaterInertia()
	{
		return 0.90F;
	}

	@Override
	public boolean canChangeDimensions()
	{
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
