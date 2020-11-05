package com.legacy.dungeons_plus.pieces;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.worldgen.GelPlacementSettings;
import com.legacy.structure_gel.worldgen.processors.RandomBlockSwapProcessor;
import com.legacy.structure_gel.worldgen.processors.RandomStateSwapProcessor;
import com.legacy.structure_gel.worldgen.structure.GelTemplateStructurePiece;

import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
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
	private static final ResourceLocation MAIN = locate("main");
	private static final ResourceLocation MAIN_UNDER = locate("underneath");
	private static final ResourceLocation[] CELLS = new ResourceLocation[] {};
	
	public static void assemble(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> structurePieces, Random rand)
	{
		structurePieces.add(new Piece(templateManager, MAIN, pos, rotation));
		
		// Add cells around main
	}

	private static ResourceLocation locate(String name)
	{
		return DungeonsPlus.locate("soul_prison/" + name);
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(DungeonsPlus.Structures.SOUL_PRISON.getPieceType(), location, 0);
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setupTemplate(templateManager);
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
			placementSettings.addProcessor(new RandomStateSwapProcessor(Blocks.POLISHED_BASALT.getDefaultState().with(RotatedPillarBlock.AXIS, Axis.Y), 0.40F, Blocks.BASALT.getDefaultState().with(RotatedPillarBlock.AXIS, Axis.Y)));
			placementSettings.addProcessor(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new BlockMatchRuleTest(Blocks.QUARTZ_SLAB), new BlockMatchRuleTest(Blocks.LAVA), Blocks.LAVA.getDefaultState()))));
		}
		
		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb)
		{
			// guard, spawner, chest-facing
		}
	}
}
