package com.legacy.dungeons_plus.structures.reanimated_ruins;

import java.util.List;

import com.legacy.dungeons_plus.registry.DPJigsawTypes;
import com.legacy.dungeons_plus.registry.DPLoot;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.block_entity.BlockEntityAccessHelper;
import com.legacy.structure_gel.api.structure.ExtendedJigsawStructure.IPieceFactory;
import com.legacy.structure_gel.api.structure.base.IPieceBuilderModifier;
import com.legacy.structure_gel.api.structure.jigsaw.AbstractGelStructurePiece;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.IJigsawCapability;
import com.legacy.structure_gel.api.structure.jigsaw.JigsawCapability.JigsawType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class ReanimatedRuinsStructure
{
	public static record Capability(ReanimatedRuinsType type) implements IJigsawCapability
	{
		public static final Codec<Capability> CODEC = RecordCodecBuilder.create(instance ->
		{
			return instance.group(ReanimatedRuinsType.CODEC.fieldOf("ruins_type").forGetter(cap ->
			{
				return cap.type;
			})).apply(instance, Capability::new);
		});

		@Override
		public JigsawType<?> getType()
		{
			return DPJigsawTypes.REANIMATED_RUINS;
		}

		@Override
		public IPieceFactory getPieceFactory()
		{
			return (structureManager, poolElement, pos, ground, rotation, bounds) -> new Piece(structureManager, poolElement, pos, ground, rotation, bounds, this.type);
		}

		@Override
		public void modifyPieceBuilder(StructurePiecesBuilder pieceBuilder, Structure.GenerationContext context)
		{
			List<StructurePiece> pieces = IPieceBuilderModifier.getPieces(pieceBuilder);
			pieces.removeIf(p ->
			{
				BoundingBox bounds = p.getBoundingBox();
				int minX = bounds.minX();
				int maxY = bounds.maxY();
				int minZ = bounds.maxZ();

				if (maxY > context.chunkGenerator().getBaseHeight(minX, minZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()) - 8)
				{
					// System.out.println("Removed a piece at " + new BlockPos(minX, maxY, minZ));
					return true;
				}
				return false;
			});
		}
	}

	public static final class Piece extends AbstractGelStructurePiece
	{
		private static final String TYPE_KEY = "type";
		private final ReanimatedRuinsType type;

		public Piece(StructureTemplateManager structureManager, StructurePoolElement poolElement, BlockPos pos, int groundLevelDelta, Rotation rotation, BoundingBox bounds, ReanimatedRuinsType type)
		{
			super(structureManager, poolElement, pos, groundLevelDelta, rotation, bounds);
			this.type = type;
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(context, tag);
			this.type = ReanimatedRuinsType.byName(tag.getString(TYPE_KEY));
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super.addAdditionalSaveData(context, tag);
			tag.putString(TYPE_KEY, this.type.getSerializedName());
		}

		@Override
		public StructurePieceType getType()
		{
			return DPStructures.REANIMATED_RUINS.getPieceType().get();
		}

		@Override
		public BlockState modifyState(ServerLevelAccessor level, RandomSource rand, BlockPos pos, BlockState original)
		{
			return this.type.modifierMap.modifiy(original, rand);
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds)
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
		}
	}
}
