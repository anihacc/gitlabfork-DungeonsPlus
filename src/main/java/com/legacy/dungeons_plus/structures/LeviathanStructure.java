package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.data.managers.DPSpawners;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class LeviathanStructure extends GelConfigJigsawStructure
{
	public LeviathanStructure(Codec<JigsawConfiguration> codec, StructureConfig config)
	{
		super(codec, config, -1, true, true, context -> true, Piece::new);
		this.spawns.put(MobCategory.MONSTER, ImmutableList.of(new SpawnerData(EntityType.HUSK, 1, 4, 4)));
	}

	public static class Piece extends AbstractGelStructurePiece
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
			return DPStructures.LEVIATHAN.getPieceType();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				String[] data = key.split("-");
				DPUtil.createChest(this::createChest, level, bounds, rand, pos, DPLoot.Leviathan.CHEST_COMMON, this.rotation, data);
			}
			if (key.equals("spawner"))
			{
				DPUtil.placeSpawner(level, pos, DPSpawners.LEVIATHAN_HUSK);
			}
		}
	}
}
