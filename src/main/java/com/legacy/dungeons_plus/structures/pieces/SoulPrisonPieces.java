package com.legacy.dungeons_plus.structures.pieces;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPLoot;
import com.legacy.dungeons_plus.data.managers.DPSpawners;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;
import com.legacy.structure_gel.api.structure.processor.RandomStateSwapProcessor;
import com.legacy.structure_gel.api.structure.processor.RemoveGelStructureProcessor;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.material.Material;

public class SoulPrisonPieces
{
	private static final ResourceLocation TOP = locate("main/top_0");
	private static final ResourceLocation TOP_GEL = locate("main/top_gel");
	private static final ResourceLocation[] BOTTOM = new ResourceLocation[] { locate("main/bottom_0") };
	private static final ResourceLocation UNDER_MAIN = locate("main/under");
	private static final ResourceLocation[] TURRETS = new ResourceLocation[] { locate("turrets/turret_0"), locate("turrets/turret_1") };

	public static void assemble(StructureManager structureManager, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, Random rand)
	{
		pos = pos.offset(-12, 0, -12);
		pieces.addPiece(new Piece(structureManager, Util.getRandom(BOTTOM, rand), pos, rotation));
		pieces.addPiece(new Piece(structureManager, TOP_GEL, pos.above(11), rotation));
		pieces.addPiece(new Piece(structureManager, TOP, pos.above(11), rotation));
		for (int i = 1; i < pos.getY(); i++)
			pieces.addPiece(new Piece(structureManager, UNDER_MAIN, pos.below(i), rotation, 1));

		pos = pos.offset(12, 0, 12);
		int range = 32;
		pieces.addPiece(new Piece(structureManager, Util.getRandom(TURRETS, rand), pos.offset(range, 0, rand.nextInt(20) - 10), Rotation.getRandom(rand)));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(TURRETS, rand), pos.offset(-range, 0, rand.nextInt(20) - 10), Rotation.getRandom(rand)));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(TURRETS, rand), pos.offset(rand.nextInt(20) - 10, 0, range), Rotation.getRandom(rand)));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(TURRETS, rand), pos.offset(rand.nextInt(20) - 10, 0, -range), Rotation.getRandom(rand)));
	}

	private static ResourceLocation locate(String name)
	{
		return DungeonsPlus.locate("soul_prison/" + name);
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		public Piece(StructureManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation, int componentType)
		{
			super(DPStructures.SOUL_PRISON.getPieceType(), 0, structureManager, location, pos);
			this.rotation = rotation;
			this.setupPlaceSettings(structureManager);
		}

		public Piece(StructureManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			this(structureManager, location, pos, rotation, 0);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(DPStructures.SOUL_PRISON.getPieceType(), tag, context.structureManager());
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
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.QUARTZ_BRICKS, 0.10F, Blocks.CHISELED_QUARTZ_BLOCK));
			settings.addProcessor(new RandomStateSwapProcessor(Blocks.POLISHED_BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Axis.Y), 0.55F, Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Axis.Y)));
			settings.addProcessor(new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.QUARTZ_SLAB), new BlockMatchTest(Blocks.LAVA), Blocks.LAVA.defaultBlockState()))));
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.ORANGE_STAINED_GLASS, Blocks.LAVA));

			return settings;
		}

		@Override
		@Nullable
		public BlockState modifyState(ServerLevelAccessor world, Random rand, BlockPos pos, BlockState originalState)
		{
			BlockState worldState = world.getBlockState(pos);
			if (this.genDepth == 1 && !(worldState.getBlock() == Blocks.LAVA || worldState.getMaterial() == Material.AIR))
				return null;
			return originalState;
		}

		@Override
		protected void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			ServerLevel serverLevel = level.getLevel();
			if (key.equals("guard"))
			{
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				WitherSkeleton wither = EntityType.WITHER_SKELETON.create(serverLevel);
				wither.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
				wither.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				wither.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				wither.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
				wither.setPersistenceRequired();
				level.addFreshEntity(wither);
			}
			else if (key.equals("ghast"))
			{
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				Ghast ghast = EntityType.GHAST.create(serverLevel);
				ghast.setPos(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
				ghast.setPersistenceRequired();
				level.addFreshEntity(ghast);
			}
			else if (key.equals("spawner"))
			{
				DPUtil.placeSpawner(level, pos, DPSpawners.SOUL_PRISON_GHAST);
			}
			else if (key.contains("chest"))
			{
				String[] data = key.split("-");
				DPUtil.createChest(this::createChest, level, bounds, rand, pos, DPLoot.SoulPrison.CHEST_COMMON, this.rotation, data);
			}
		}
	}
}
