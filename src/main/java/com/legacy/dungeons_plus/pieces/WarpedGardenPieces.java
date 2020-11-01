package com.legacy.dungeons_plus.pieces;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPLoot;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.worldgen.GelPlacementSettings;
import com.legacy.structure_gel.worldgen.processors.RandomBlockSwapProcessor;
import com.legacy.structure_gel.worldgen.processors.RemoveGelStructureProcessor;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.RuleEntry;
import net.minecraft.world.gen.feature.template.RuleStructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WarpedGardenPieces
{
	private static final ResourceLocation[] FLOWERY = new ResourceLocation[] { locate("flower_0"), locate("flower_1"), locate("flower_2"), locate("flower_3") };
	private static final ResourceLocation[] LOOT = new ResourceLocation[] { locate("portal"), locate("ship"), locate("nylium"), locate("volcano") };

	public static void assemble(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> structurePieces, Random rand)
	{
		structurePieces.add(new Piece(templateManager, LOOT[rand.nextInt(LOOT.length)], pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), rotation));
		int offset = 20;
		List<BlockPos> positions = Arrays.asList(pos.add(-offset, 0, rand.nextInt(7) - 3), pos.add(offset, 0, rand.nextInt(7) - 3), pos.add(rand.nextInt(7) - 3, 0, offset), pos.add(rand.nextInt(7) - 3, 0, -offset));
		positions.forEach(p -> structurePieces.add(new Piece(templateManager, getRandomPiece(rand), p, rotation)));
	}

	public static ResourceLocation locate(String name)
	{
		return DungeonsPlus.locate("warped_garden/" + name);
	}

	public static ResourceLocation getRandomPiece(Random rand)
	{
		int i = rand.nextInt(FLOWERY.length + LOOT.length);
		if (i < FLOWERY.length)
			return FLOWERY[i];
		return LOOT[i - FLOWERY.length];
	}

	public static class Piece extends TemplateStructurePiece
	{
		private final ResourceLocation location;
		private final Rotation rotation;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(DungeonsPlus.Structures.WARPED_GARDEN.getPieceType(), 0);
			this.location = location;
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setupTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbtCompound)
		{
			super(DungeonsPlus.Structures.WARPED_GARDEN.getPieceType(), nbtCompound);
			this.location = new ResourceLocation(nbtCompound.getString("Template"));
			this.rotation = Rotation.valueOf(nbtCompound.getString("Rot"));
			this.setupTemplate(templateManager);
		}

		private void setupTemplate(TemplateManager templateManager)
		{
			Template template = templateManager.getTemplateDefaulted(this.location);
			BlockPos sizePos = templateManager.getTemplate(this.location).getSize();
			BlockPos centerPos = new BlockPos(sizePos.getX() / 2, 0, sizePos.getZ() / 2);
			PlacementSettings placementSettings = new GelPlacementSettings().setMaintainWater(false).setRotation(this.rotation).setMirror(Mirror.NONE).setCenterOffset(centerPos);
			placementSettings.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
			placementSettings.addProcessor(RemoveGelStructureProcessor.INSTANCE);
			placementSettings.addProcessor(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new BlockMatchRuleTest(Blocks.BLUE_STAINED_GLASS), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState()))));
			placementSettings.addProcessor(new RandomBlockSwapProcessor(Blocks.BLUE_STAINED_GLASS, Blocks.AIR));
			placementSettings.addProcessor(new RandomBlockSwapProcessor(Blocks.OBSIDIAN, 0.35F, Blocks.CRYING_OBSIDIAN));

			this.setup(template, this.templatePosition, placementSettings);
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.location.toString());
			tagCompound.putString("Rot", this.rotation.name());
		}

		// Adjust the height to be under water
		@Override
		public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen, Random rand, MutableBoundingBox bounds, ChunkPos chunkPos, BlockPos pos)
		{
			int y = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, this.templatePosition.getX() + 6, this.templatePosition.getZ() + 6);
			this.templatePosition = new BlockPos(this.templatePosition.getX(), y, this.templatePosition.getZ());
			if (this.templatePosition.getY() + this.template.getSize().getY() > 60)
				this.templatePosition = new BlockPos(this.templatePosition.getX(), 60 - this.template.getSize().getY(), this.templatePosition.getZ());
			return super.func_230383_a_(world, structureManager, chunkGen, rand, bounds, chunkPos, pos);
		}

		@Override
		protected void handleDataMarker(String key, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox sbb)
		{
			if (key.contains("chest"))
			{
				String[] data = key.split("-");
				DPUtil.placeLootChest(DPLoot.WARPED_GARDEN, world, rand, pos, this.rotation, data[1]);
			}
			if (key.equals("spawner"))
			{
				CompoundNBT nbt = new CompoundNBT();
				CompoundNBT entityNbt = new CompoundNBT();
				entityNbt.putString("id", EntityType.DROWNED.getRegistryName().toString());
				entityNbt.putBoolean("Glowing", true);
				nbt.put("Entity", nbt);
				DPUtil.placeSpawner(EntityType.DROWNED, world, rand, pos, nbt);
			}
		}
	}
}
