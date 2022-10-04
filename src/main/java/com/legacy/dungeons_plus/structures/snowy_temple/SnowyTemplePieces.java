package com.legacy.dungeons_plus.structures.snowy_temple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class SnowyTemplePieces
{
	private static final ResourceLocation[] BASES = DungeonsPlus.locateAllPrefix("snowy_temple/", "temple_entry");
	private static final ResourceLocation[] FLOORS = DungeonsPlus.locateAllPrefix("snowy_temple/", "temple_room_0", "temple_room_1", "temple_room_2", "temple_room_3", "temple_room_4", "temple_room_5", "temple_room_6", "temple_room_7");
	private static final ResourceLocation[] TOP_FLOORS = DungeonsPlus.locateAllPrefix("snowy_temple/", "temple_top");

	private static final ResourceLocation[] PATHS = DungeonsPlus.locateAllPrefix("snowy_temple/", "path");
	private static final ResourceLocation[] ICE = DungeonsPlus.locateAllPrefix("snowy_temple/", "ice_0", "ice_1", "ice_2");

	public static void assemble(StructureTemplateManager structureManager, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, RandomSource rand)
	{
		Direction dir = switch (rotation)
		{
		case NONE -> Direction.NORTH;
		case CLOCKWISE_90 -> Direction.EAST;
		case CLOCKWISE_180 -> Direction.SOUTH;
		case COUNTERCLOCKWISE_90 -> Direction.WEST;
		};

		BlockPos floorPos = pos;

		pieces.addPiece(new Piece(structureManager, Util.getRandom(BASES, rand), floorPos, rotation, rand));
		floorPos = floorPos.above(13);
		int roomHeight = 11;
		boolean flipped = true;
		Rotation flippedRot = rotation.getRotated(Rotation.CLOCKWISE_180);
		int maxFloors = rand.nextInt(2) + 3;
		List<ResourceLocation> unusedFloors = new ArrayList<>(Arrays.asList(FLOORS));
		for (int floor = 0; floor < maxFloors && unusedFloors.size() > 0; floor++)
		{
			ResourceLocation floorName = Util.getRandom(unusedFloors, rand);
			pieces.addPiece(new Piece(structureManager, floorName, floorPos, flipped ? flippedRot : rotation, rand));
			unusedFloors.remove(floorName);
			floorPos = floorPos.above(roomHeight);
			flipped = !flipped;
		}

		pieces.addPiece(new Piece(structureManager, Util.getRandom(TOP_FLOORS, rand), floorPos, rotation, rand));

		pieces.addPiece(new Piece(structureManager, Util.getRandom(PATHS, rand), pos.offset(11, 0, 2).relative(dir, 22), rotation, rand));

		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 16).relative(Rotation.COUNTERCLOCKWISE_90.rotate(dir), 8), rotation, rand));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 16).relative(Rotation.CLOCKWISE_90.rotate(dir), 8), rotation, rand));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 30).relative(Rotation.COUNTERCLOCKWISE_90.rotate(dir), 12), rotation, rand));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 30).relative(Rotation.CLOCKWISE_90.rotate(dir), 12), rotation, rand));
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		private static final String DECAY_KEY = "decay";
		/**
		 * Represented through binary data. Each bit is used to convert a different type
		 * of wool. The number will range from 0 to 7.
		 */
		private final int decay;

		public Piece(StructureTemplateManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation, RandomSource rand)
		{
			super(DPStructures.SNOWY_TEMPLE.getPieceType().get(), 0, structureManager, location, pos);
			this.rotation = rotation;
			this.decay = rand.nextInt(8); // Produced binary 000 (0) to 111 (7)
			this.setupPlaceSettings(structureManager);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(DPStructures.SNOWY_TEMPLE.getPieceType().get(), tag, context.structureTemplateManager());
			this.decay = tag.getInt(DECAY_KEY);
			this.setupPlaceSettings(context.structureTemplateManager());
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext level, CompoundTag tag)
		{
			super.addAdditionalSaveData(level, tag);
			tag.putInt(DECAY_KEY, this.decay);
		}

		@Override
		protected StructurePlaceSettings getPlaceSettings(StructureTemplateManager structureManager)
		{
			StructurePlaceSettings settings = new StructurePlaceSettings();
			settings.setKeepLiquids(false);

			Vec3i size = structureManager.get(this.makeTemplateLocation()).get().getSize();
			settings.setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2));

			if (this.templateName.contains("snowy_temple/path") || this.templateName.contains("snowy_temple/ice_"))
			{
				settings.addProcessor(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1));
			}

			return settings;
		}

		@Override
		public BlockState modifyState(ServerLevelAccessor level, RandomSource rand, BlockPos pos, BlockState originalState)
		{
			// Decay packed ice with light blue, white, and light gray
			BlockState air = Blocks.AIR.defaultBlockState();
			BlockState ice = Blocks.ICE.defaultBlockState();
			BlockState packedIce = Blocks.PACKED_ICE.defaultBlockState();

			if (originalState.is(Blocks.LIGHT_BLUE_WOOL))
				return (this.decay & 1) == 1 ? air : ice;
			if (originalState.is(Blocks.WHITE_WOOL))
				return (this.decay & 2) == 2 ? air : packedIce;
			if (originalState.is(Blocks.LIGHT_GRAY_WOOL))
				return (this.decay & 4) == 4 ? air : packedIce;

			return originalState;
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds)
		{
		}
	}
}
