package com.legacy.dungeons_plus.structures.reanimated_ruins;

import java.util.Random;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.block_entity.BlockEntityAccessHelper;
import com.legacy.structure_gel.api.config.StructureConfig;
import com.legacy.structure_gel.api.structure.GelConfigJigsawStructure;
import com.legacy.structure_gel.api.structure.base.IPieceBuilderModifier;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.ExtendedJigsawConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class ReanimatedRuinsStructure extends GelConfigJigsawStructure<ReanimatedRuinsStructure.Configuration>
{
	public ReanimatedRuinsStructure(Codec<ReanimatedRuinsStructure.Configuration> codec, StructureConfig config)
	{
		super(codec, config, 75/*20*/, true, false, context -> true, Piece::new);
	}

	@Override
	public void modifyPieceBuilder(StructurePiecesBuilder pieces, ReanimatedRuinsStructure.Configuration config)
	{
		for (var p : IPieceBuilderModifier.getPieces(pieces))
			if (p instanceof ReanimatedRuinsStructure.Piece piece)
				piece.type = config.type;

		// TODO Remove pieces that go above ground or below bedrock
	}

	public static final class Piece extends AbstractGelStructurePiece
	{
		private ReanimatedRuinsType type = ReanimatedRuinsType.MOSSY;

		public Piece(StructureManager structureManager, StructurePoolElement poolElement, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds)
		{
			super(structureManager, poolElement, pos, groundLevelDelta, rotation, bounds);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(context, tag);
			this.type = ReanimatedRuinsType.byName(tag.getString("type"));
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super.addAdditionalSaveData(context, tag);
			tag.putString("type", this.type.getSerializedName());
		}

		@Override
		public StructurePieceType getType()
		{
			return DPStructures.REANIMATED_RUINS.getPieceType();
		}

		@Override
		public BlockState modifyState(ServerLevelAccessor level, Random rand, BlockPos pos, BlockState original)
		{
			return this.type.modifierMap.modifiy(original, rand);
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.startsWith("chest"))
			{
				this.setAir(level, pos);

				if (rand.nextFloat() < 0.60F || key.contains("always"))
				{
					ResourceLocation loot = rand.nextFloat() < 0.50F ? loot = this.type.loot : DPLoot.ReanimatedRuins.CHEST_COMMON;
					RandomizableContainerBlockEntity.setLootTable(level, rand, pos.below(), loot);
				}
				else
				{
					this.setAir(level, pos.below());
				}
			}
			if (key.startsWith("spawner"))
			{
				this.setAir(level, pos);

				if (rand.nextFloat() < 0.60F || key.contains("always"))
					BlockEntityAccessHelper.placeDynamicSpawner(level, pos, this.type.spawner);
			}
			if (key.equals("decor"))
			{
				this.setAir(level, pos);

				this.type.decorate(level, pos, rand);
			}
			if (key.equals("monster_box"))
			{
				DPUtil.placeMonsterBox(level, pos, rand);
			}
		}
	}

	public static class Configuration extends ExtendedJigsawConfiguration
	{
		//@formatter:off
		public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance ->
		{
			return instance.group(
					StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool), 
					Codec.intRange(0, 32).fieldOf("size").forGetter(JigsawConfiguration::maxDepth),
					StringRepresentable.fromEnum(ReanimatedRuinsType::values, ReanimatedRuinsType::byName).fieldOf("type").forGetter(c -> c.type))
					.apply(instance, Configuration::new);
		});
		//@formatter:on

		private final ReanimatedRuinsType type;

		public Configuration(Holder<StructureTemplatePool> startPool, int maxDepth, ReanimatedRuinsType type)
		{
			super(startPool, maxDepth);
			this.type = type;
		}
	}
}
