package com.legacy.dungeons_plus.structures;

import java.util.List;
import java.util.Random;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraftforge.registries.ForgeRegistries;

public class TowerStructure extends GelConfigJigsawStructure
{
	public TowerStructure(Codec<JigsawConfiguration> codec, StructureConfig config)
	{
		super(codec, config, 0, true, true, context -> true, Piece::new);
	}

	public static final class Piece extends AbstractGelStructurePiece
	{
		private static final ItemStack[] ARMOR_STAND_ITEMS = List.of(Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_BOOTS, Items.GOLDEN_LEGGINGS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS).stream().map(ItemStack::new).toArray(ItemStack[]::new);

		public Piece(StructureManager template, StructurePoolElement piece, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds)
		{
			super(template, piece, pos, groundLevelDelta, rotation, bounds);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(context, tag);
		}

		@Override
		public StructurePieceType getType()
		{
			return DPStructures.TOWER.getPieceType();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.startsWith("chest"))
			{
				String[] data = key.split("-");
				ResourceLocation lootTable = switch (data[1])
				{
				case "vex" -> DPLoot.Tower.CHEST_VEX;
				case "vex_map" -> DPLoot.Tower.CHEST_VEX_MAP;
				default -> DPLoot.Tower.CHEST_COMMON;
				};

				//DPUtil.setLoot(level, rand, pos, pos.below(), lootTable);
			}
			if (key.startsWith("barrel"))
			{
				//DPUtil.setLoot(level, rand, pos, pos.below(), DPLoot.Tower.CHEST_BARREL);
			}
			if (key.startsWith("spawner"))
			{
				String[] data = key.split("-");
				//DPUtil.placeSpawner(level, pos, data[1]);
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
			if (key.startsWith("armor_stand"))
			{
				this.setAir(level, pos);

				ArmorStand entity = this.createEntity(EntityType.ARMOR_STAND, level, pos, this.rotation);
				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));

				for (ItemStack item : ARMOR_STAND_ITEMS)
					if (rand.nextFloat() < 0.25)
						entity.setItemSlot(Mob.getEquipmentSlotForItem(item), item);

				level.addFreshEntity(entity);
			}
			if (key.startsWith("waystone"))
			{
				DPUtil.placeWaystone(level, pos, rand, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key.split("-")[1])));
			}
		}
	}
}
