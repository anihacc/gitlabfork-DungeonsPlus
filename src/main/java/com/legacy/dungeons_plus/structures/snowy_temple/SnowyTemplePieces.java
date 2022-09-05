package com.legacy.dungeons_plus.structures.snowy_temple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class SnowyTemplePieces
{
	private static final ResourceLocation[] BASES = DungeonsPlus.locateAllPrefix("snowy_temple/", "temple_entry");
	private static final ResourceLocation[] FLOORS = DungeonsPlus.locateAllPrefix("snowy_temple/", "temple_room_0", "temple_room_1", "temple_room_2", "temple_room_3", "temple_room_4", "temple_room_5", "temple_room_6", "temple_room_7");
	private static final ResourceLocation[] TOP_FLOORS = DungeonsPlus.locateAllPrefix("snowy_temple/", "temple_top");

	private static final ResourceLocation[] PATHS = DungeonsPlus.locateAllPrefix("snowy_temple/", "path");
	private static final ResourceLocation[] ICE = DungeonsPlus.locateAllPrefix("snowy_temple/", "ice_0", "ice_1", "ice_2");

	public static void assemble(StructureManager structureManager, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, Random rand)
	{
		Direction dir = switch (rotation)
		{
		case NONE -> Direction.NORTH;
		case CLOCKWISE_90 -> Direction.EAST;
		case CLOCKWISE_180 -> Direction.SOUTH;
		case COUNTERCLOCKWISE_90 -> Direction.WEST;
		};
		
		BlockPos floorPos = pos;

		pieces.addPiece(new Piece(structureManager, Util.getRandom(BASES, rand), floorPos, rotation));
		floorPos = floorPos.above(13);
		int roomHeight = 11;
		boolean flipped = true;
		Rotation flippedRot = rotation.getRotated(Rotation.CLOCKWISE_180);
		int maxFloors = rand.nextInt(2) + 3;
		List<ResourceLocation> unusedFloors = new ArrayList<>(Arrays.asList(FLOORS));
		for (int floor = 0; floor < maxFloors && unusedFloors.size() > 0; floor++)
		{
			ResourceLocation floorName = Util.getRandom(unusedFloors, rand);
			pieces.addPiece(new Piece(structureManager, floorName, floorPos, flipped ? flippedRot : rotation));
			unusedFloors.remove(floorName);
			floorPos = floorPos.above(roomHeight);
			flipped = !flipped;
		}

		pieces.addPiece(new Piece(structureManager, Util.getRandom(TOP_FLOORS, rand), floorPos, rotation));

		pieces.addPiece(new Piece(structureManager, Util.getRandom(PATHS, rand), pos.offset(11, 0, 2).relative(dir, 22), rotation));
		
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 16).relative(Rotation.COUNTERCLOCKWISE_90.rotate(dir), 8), rotation));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 16).relative(Rotation.CLOCKWISE_90.rotate(dir), 8), rotation));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 30).relative(Rotation.COUNTERCLOCKWISE_90.rotate(dir), 12), rotation));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(ICE, rand), pos.offset(11, 0, 11).relative(dir, 30).relative(Rotation.CLOCKWISE_90.rotate(dir), 12), rotation));
	}

	public static class Piece extends GelTemplateStructurePiece
	{

		public Piece(StructureManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(DPStructures.SNOWY_TEMPLE.getPieceType(), 0, structureManager, location, pos);
			this.rotation = rotation;
			this.setupPlaceSettings(structureManager);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(DPStructures.SNOWY_TEMPLE.getPieceType(), tag, context.structureManager());
			this.setupPlaceSettings(context.structureManager());
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext level, CompoundTag tag)
		{
			super.addAdditionalSaveData(level, tag);
		}

		@Override
		protected StructurePlaceSettings getPlaceSettings(StructureManager structureManager)
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
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
		}
	}
}
