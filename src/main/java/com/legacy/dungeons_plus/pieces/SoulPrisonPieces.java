package com.legacy.dungeons_plus.pieces;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.worldgen.GelPlacementSettings;
import com.legacy.structure_gel.worldgen.processors.RandomBlockSwapProcessor;
import com.legacy.structure_gel.worldgen.processors.RandomStateSwapProcessor;
import com.legacy.structure_gel.worldgen.structure.GelTemplateStructurePiece;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.RuleEntry;
import net.minecraft.world.gen.feature.template.RuleStructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SoulPrisonPieces
{
	private static final ResourceLocation TOP = locate("main/top_0");
	private static final ResourceLocation[] BOTTOM = new ResourceLocation[] { locate("main/bottom_0") };
	private static final ResourceLocation UNDER_MAIN = locate("main/under");
	private static final ResourceLocation[] CELLS = new ResourceLocation[] { locate("cells/cell_0") };

	public static void assemble(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> structurePieces, Random rand)
	{
		structurePieces.add(new Piece(templateManager, BOTTOM[rand.nextInt(BOTTOM.length)], pos, rotation));
		structurePieces.add(new Piece(templateManager, TOP, pos.up(11), rotation));
		for (int i = 1; i < pos.getY(); i++)
			structurePieces.add(new Piece(templateManager, UNDER_MAIN, pos.down(i), rotation, 1));

		int range = 24;
		structurePieces.add(new Piece(templateManager, CELLS[rand.nextInt(CELLS.length)], pos.add(range, 0, 0), rotation));
		structurePieces.add(new Piece(templateManager, CELLS[rand.nextInt(CELLS.length)], pos.add(-range, 0, 0), rotation));
		structurePieces.add(new Piece(templateManager, CELLS[rand.nextInt(CELLS.length)], pos.add(0, 0, range), rotation));
		structurePieces.add(new Piece(templateManager, CELLS[rand.nextInt(CELLS.length)], pos.add(0, 0, -range), rotation));
		
		// Add cells around main
	}

	private static ResourceLocation locate(String name)
	{
		return DungeonsPlus.locate("soul_prison/" + name);
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation, int componentType)
		{
			super(DungeonsPlus.Structures.SOUL_PRISON.getPieceType(), location, componentType);
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setupTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			this(templateManager, location, pos, rotation, 0);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbtCompound)
		{
			super(DungeonsPlus.Structures.SOUL_PRISON.getPieceType(), nbtCompound);
			this.setupTemplate(templateManager);
		}

		@Override
		public PlacementSettings createPlacementSettings(TemplateManager templateManager)
		{
			BlockPos sizePos = templateManager.getTemplate(this.name).getSize();
			BlockPos centerPos = new BlockPos(sizePos.getX() / 2, 0, sizePos.getZ() / 2);
			return new GelPlacementSettings().setMaintainWater(false).setRotation(this.rotation).setMirror(Mirror.NONE).setCenterOffset(centerPos);
		}

		@Override
		public void addProcessors(TemplateManager templateManager, PlacementSettings placementSettings)
		{
			super.addProcessors(templateManager, placementSettings);
			placementSettings.addProcessor(new RandomBlockSwapProcessor(Blocks.QUARTZ_BRICKS, 0.10F, Blocks.CHISELED_QUARTZ_BLOCK));
			placementSettings.addProcessor(new RandomStateSwapProcessor(Blocks.POLISHED_BASALT.getDefaultState().with(RotatedPillarBlock.AXIS, Axis.Y), 0.55F, Blocks.BASALT.getDefaultState().with(RotatedPillarBlock.AXIS, Axis.Y)));
			placementSettings.addProcessor(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new BlockMatchRuleTest(Blocks.QUARTZ_SLAB), new BlockMatchRuleTest(Blocks.LAVA), Blocks.LAVA.getDefaultState()))));
			placementSettings.addProcessor(new RandomBlockSwapProcessor(Blocks.ORANGE_STAINED_GLASS, Blocks.LAVA));
		}

		@Override
		@Nullable
		public BlockState modifyState(IServerWorld world, Random rand, BlockPos pos, BlockState originalState)
		{
			BlockState worldState = world.getBlockState(pos);
			if (this.componentType == 1 && !(worldState.getBlock() == Blocks.LAVA || worldState.getMaterial() == Material.AIR))
				return null;
			return originalState;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb)
		{
			// guard (wither skeleton w/ bow)
			// spawner (ghast long range)
			// chest-facing
		}
	}
}
