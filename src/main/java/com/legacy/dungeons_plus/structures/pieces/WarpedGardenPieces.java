package com.legacy.dungeons_plus.structures.pieces;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPLoot;
import com.legacy.dungeons_plus.data.managers.DPSpawners;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

public class WarpedGardenPieces
{
	private static final ResourceLocation[] FLOWERY = new ResourceLocation[] { locate("flower_0"), locate("flower_1"), locate("flower_2"), locate("flower_3") };
	private static final ResourceLocation[] LOOT = new ResourceLocation[] { locate("portal"), locate("ship"), locate("nylium"), locate("volcano") };

	public static void assemble(StructureManager structureMananger, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, Random rand)
	{
		pieces.addPiece(new Piece(structureMananger, LOOT[rand.nextInt(LOOT.length)], pos.offset(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), rotation));
		int offset = 20;
		List<BlockPos> positions = Arrays.asList(pos.offset(-offset, 0, rand.nextInt(7) - 3), pos.offset(offset, 0, rand.nextInt(7) - 3), pos.offset(rand.nextInt(7) - 3, 0, offset), pos.offset(rand.nextInt(7) - 3, 0, -offset));
		positions.forEach(p -> pieces.addPiece(new Piece(structureMananger, getRandomPiece(rand), p, rotation)));
	}

	private static ResourceLocation locate(String name)
	{
		return DungeonsPlus.locate("warped_garden/" + name);
	}

	private static ResourceLocation getRandomPiece(Random rand)
	{
		int i = rand.nextInt(FLOWERY.length + LOOT.length);
		return i < FLOWERY.length ? FLOWERY[i] : LOOT[i - FLOWERY.length];
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		public Piece(StructureManager structureMananger, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(DPStructures.WARPED_GARDEN.getPieceType(), 0, structureMananger, location, pos);
			this.rotation = rotation;
			this.setupPlaceSettings(structureMananger);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(DPStructures.WARPED_GARDEN.getPieceType(), tag, context.structureManager());
			this.setupPlaceSettings(context.structureManager());
		}

		@Override
		protected StructurePlaceSettings getPlaceSettings(StructureManager structureManager)
		{
			StructurePlaceSettings settings = new StructurePlaceSettings();
			Vec3i size = structureManager.get(this.makeTemplateLocation()).get().getSize();
			settings.setKeepLiquids(false);
			settings.setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2));

			settings.addProcessor(RemoveGelStructureProcessor.INSTANCE);
			settings.addProcessor(new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.BLUE_STAINED_GLASS), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState()))));
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.BLUE_STAINED_GLASS, Blocks.AIR));
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.OBSIDIAN, 0.35F, Blocks.CRYING_OBSIDIAN));

			return settings;
		}

		// Place underwater
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager structureManager, ChunkGenerator chunkGen, Random rand, BoundingBox bounds, ChunkPos chunkPos, BlockPos pos)
		{
			int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX() + 6, this.templatePosition.getZ() + 6);
			this.templatePosition = new BlockPos(this.templatePosition.getX(), y, this.templatePosition.getZ());
			
			if (this.templatePosition.getY() + this.template.getSize().getY() > 60)
				this.templatePosition = new BlockPos(this.templatePosition.getX(), 60 - this.template.getSize().getY(), this.templatePosition.getZ());
			
			super.postProcess(level, structureManager, chunkGen, rand, bounds, chunkPos, pos);
		}

		@Override
		protected void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.contains("chest"))
			{
				String[] data = key.split("-");
				DPUtil.createChest(this::createChest, level, bounds, rand, pos, DPLoot.WarpedGarden.CHEST_COMMON, this.rotation, data);
			}
			if (key.equals("spawner"))
			{
				DPUtil.placeSpawner(level, pos, DPSpawners.WARPED_GARDEN_DROWNED);
			}
			if (key.equals("drowned"))
			{
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				Drowned drowned = EntityType.DROWNED.create(level.getLevel());
				ItemStack goldAxe = new ItemStack(Items.GOLDEN_AXE);
				goldAxe.setDamageValue(10);
				drowned.setItemSlot(EquipmentSlot.MAINHAND, goldAxe);
				drowned.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
				drowned.setPersistenceRequired();
				drowned.setPos(pos.getX(), pos.getY(), pos.getZ());
				level.addFreshEntity(drowned);
			}
		}
	}
}
