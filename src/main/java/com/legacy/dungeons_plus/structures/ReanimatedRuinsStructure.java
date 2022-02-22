package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class ReanimatedRuinsStructure extends GelConfigJigsawStructure
{
	public ReanimatedRuinsStructure(Codec<JigsawConfiguration> codec, StructureConfig config)
	{
		super(codec, config, 20, true, false, context -> true, Piece::new);
	}

	public static final class Piece extends AbstractGelStructurePiece
	{
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
			return DPStructures.REANIMATED_RUINS.getPieceType();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				this.setAir(level, pos);
				String[] data = key.split("-");

				/**
				 * Generates the chests with a 70% chance, or if they are a map chest.
				 */
				if (rand.nextFloat() < 0.70F || data[0].contains("map"))
				{
					ResourceLocation lootTable = DPLoot.ReanimatedRuins.CHEST_COMMON;
					if (data[0].contains(":"))
					{
						lootTable = switch (data[0].split(":")[1])
						{
						case "huskmap" -> DPLoot.ReanimatedRuins.CHEST_HUSK_MAP;
						case "husk" -> DPLoot.ReanimatedRuins.CHEST_HUSK;
						case "straymap" -> DPLoot.ReanimatedRuins.CHEST_STRAY_MAP;
						case "stray" -> DPLoot.ReanimatedRuins.CHEST_STRAY;
						default -> lootTable;
						};
					}
					//DPUtil.createChest(this::createChest, level, bounds, rand, pos, lootTable, this.rotation, data);
				}
			}
			if (key.contains("spawner"))
			{
				// TODO spawners
				String[] data = key.split("-");
				//DPUtil.placeSpawner(data[1], level, pos);
			}
			if (key.equals("monster_box"))
			{
				DPUtil.placeMonsterBox(level, pos, rand);
			}
		}
	}
}