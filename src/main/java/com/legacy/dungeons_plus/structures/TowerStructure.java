package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPLoot;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.worldgen.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.worldgen.jigsaw.GelConfigJigsawStructure;
import com.mojang.serialization.Codec;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager.IPieceFactory;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class TowerStructure extends GelConfigJigsawStructure
{
	public TowerStructure(Codec<VillageConfig> codec, StructureConfig config)
	{
		super(codec, config, 0, true, true);
	}

	@Override
	public int getSeed()
	{
		return 155166;
	}

	@Override
	public IPieceFactory getPieceType()
	{
		return Piece::new;
	}

	public static final class Piece extends AbstractGelStructurePiece
	{
		public Piece(TemplateManager templateManager, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox bounds)
		{
			super(templateManager, jigsawPiece, pos, groundLevelDelta, rotation, bounds);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbt)
		{
			super(templateManager, nbt);
		}

		@Override
		public IStructurePieceType getType()
		{
			return DungeonsPlus.Structures.TOWER.getPieceType();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				String[] data = key.split("-");
				ResourceLocation lootTable = DPLoot.CHESTS_SIMPLE_DUNGEON;
				if (data[0].contains(":"))
				{
					switch (data[0].split(":")[1])
					{
					case "vex":
						lootTable = DPLoot.Tower.CHEST_VEX_MAP;
						break;
					case "map":
						lootTable = DPLoot.Tower.CHEST_VEX;
						break;
					}
				}
				DPUtil.createChest(this::createChest, world, bounds, rand, pos, lootTable, this.rotation, data);
			}
			if (key.contains("spawner"))
			{
				String[] data = key.split("-");
				DPUtil.placeSpawner(data[1], world, pos);
			}
			/**
			 * Creating entities is a little simpler with the createEntity method. Doing
			 * this will automatically create the entity and set it's position and rotation
			 * based on the structure.
			 * 
			 * Entities are spawned facing south by default with the rotation argument being
			 * the rotation of the structure to offset them. Do Rotation.add to the rotation
			 * value passed in to rotate it according to how yours needs to be facing.
			 */
			if (key.equals("armor_stand"))
			{
				setAir(world, pos);

				ArmorStandEntity entity = createEntity(EntityType.ARMOR_STAND, world, pos, this.rotation);
				entity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));

				for (Item item : ImmutableList.of(Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS))
					if (rand.nextFloat() < 0.25)
						entity.setItemSlot(MobEntity.getEquipmentSlotForItem(new ItemStack(item)), new ItemStack(item));

				world.addFreshEntity(entity);
			}
		}
	}
}
