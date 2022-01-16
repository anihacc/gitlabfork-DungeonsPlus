package com.legacy.dungeons_plus.structures;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.legacy.dungeons_plus.DPLoot;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.api.block_entity.SpawnerAccessHelper;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class SnowyTempleStructure extends GelConfigJigsawStructure
{
	public SnowyTempleStructure(Codec<JigsawConfiguration> codec, StructureConfig config)
	{
		super(codec, config, 0, true, true, context -> true, Piece::new);
		this.spawns.put(MobCategory.MONSTER, List.of(new SpawnerData(EntityType.STRAY, 1, 2, 4)));
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
			return DungeonsPlus.Structures.SNOWY_TEMPLE.getPieceType();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				String[] data = key.split("-");
				DPUtil.createChest(this::createChest, level, bounds, rand, pos, DPLoot.SnowyTemple.CHEST_COMMON, this.rotation, data);
			}
			if (key.contains("spawner"))
			{
				SpawnData spawnData = SpawnerAccessHelper.createSpawnerEntity(EntityType.STRAY, new CompoundTag(), Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 8), new InclusiveRange<>(0, 14))));
				DPUtil.placeSpawner(spawnData, level, pos);
			}
		}
	}
}
