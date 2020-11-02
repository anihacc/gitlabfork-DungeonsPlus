package com.legacy.dungeons_plus.structures;

import java.util.Random;

import com.legacy.dungeons_plus.DPLoot;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.worldgen.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.worldgen.jigsaw.GelConfigJigsawStructure;
import com.mojang.serialization.Codec;

import net.minecraft.loot.LootTables;
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

public class BiggerDungeonStructure extends GelConfigJigsawStructure
{
	public BiggerDungeonStructure(Codec<VillageConfig> codec, StructureConfig config)
	{
		super(codec, config, 20, true, false);
	}

	@Override
	public int getSeed()
	{
		return 973181;
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
		public IStructurePieceType getStructurePieceType()
		{
			return DungeonsPlus.Structures.BIGGER_DUNGEON.getPieceType();
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				this.setAir(world, pos);
				String[] data = key.split("-");

				/**
				 * Generates the chests with a 50% chance, or if they are a map chest.
				 */
				if (rand.nextBoolean() || data[0].contains("map"))
				{
					ResourceLocation lootTable = LootTables.CHESTS_SIMPLE_DUNGEON;
					if (data[0].contains(":"))
					{
						switch (data[0].split(":")[1])
						{
						case "huskmap":
							lootTable = DPLoot.BuriedDungeon.CHEST_HUSK_MAP;
							break;
						case "husk":
							lootTable = DPLoot.BuriedDungeon.CHEST_HUSK;
							break;
						case "straymap":
							lootTable = DPLoot.BuriedDungeon.CHEST_STRAY_MAP;
							break;
						case "stray":
							lootTable = DPLoot.BuriedDungeon.CHEST_STRAY;
							break;
						}
					}
					DPUtil.placeLootChest(lootTable, world, rand, pos, this.rotation, data[1]);
				}
			}
			if (key.contains("spawner"))
			{
				String[] data = key.split("-");
				DPUtil.placeSpawner(data[1], world, rand, pos);
			}
		}
	}
}
