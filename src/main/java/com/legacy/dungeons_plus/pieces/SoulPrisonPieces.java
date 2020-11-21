package com.legacy.dungeons_plus.pieces;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPLoot;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.access_helpers.SpawnerAccessHelper;
import com.legacy.structure_gel.worldgen.GelPlacementSettings;
import com.legacy.structure_gel.worldgen.processors.RandomBlockSwapProcessor;
import com.legacy.structure_gel.worldgen.processors.RandomStateSwapProcessor;
import com.legacy.structure_gel.worldgen.structure.GelTemplateStructurePiece;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
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
	private static final ResourceLocation TOP_GEL = locate("main/top_gel");
	private static final ResourceLocation[] BOTTOM = new ResourceLocation[] { locate("main/bottom_0") };
	private static final ResourceLocation UNDER_MAIN = locate("main/under");
	private static final ResourceLocation[] TURRETS = new ResourceLocation[] {locate("turrets/turret_0"), locate("turrets/turret_1")};
	
	public static void assemble(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> structurePieces, Random rand)
	{
		pos = pos.add(-12, 0, -12);
		structurePieces.add(new Piece(templateManager, Util.getRandomObject(BOTTOM, rand), pos, rotation));
		structurePieces.add(new Piece(templateManager, TOP_GEL, pos.up(11), rotation));
		structurePieces.add(new Piece(templateManager, TOP, pos.up(11), rotation));
		for (int i = 1; i < pos.getY(); i++)
			structurePieces.add(new Piece(templateManager, UNDER_MAIN, pos.down(i), rotation, 1));

		pos = pos.add(12, 0, 12);
		int range = 32;
		structurePieces.add(new Piece(templateManager, Util.getRandomObject(TURRETS, rand), pos.add(range, 0, rand.nextInt(20) - 10), Rotation.randomRotation(rand)));
		structurePieces.add(new Piece(templateManager, Util.getRandomObject(TURRETS, rand), pos.add(-range, 0, rand.nextInt(20) - 10), Rotation.randomRotation(rand)));
		structurePieces.add(new Piece(templateManager, Util.getRandomObject(TURRETS, rand), pos.add(rand.nextInt(20) - 10, 0, range), Rotation.randomRotation(rand)));
		structurePieces.add(new Piece(templateManager, Util.getRandomObject(TURRETS, rand), pos.add(rand.nextInt(20) - 10, 0, -range), Rotation.randomRotation(rand)));
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
		protected void handleDataMarker(String key, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox bounds)
		{
			if (key.equals("guard"))
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				WitherSkeletonEntity wither = EntityType.WITHER_SKELETON.create(world.getWorld());
				wither.setPosition(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
				wither.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
				wither.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
				wither.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
				wither.enablePersistence();
				world.addEntity(wither);
			}
			else if (key.equals("ghast"))
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				GhastEntity ghast = EntityType.GHAST.create(world.getWorld());
				ghast.setPosition(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
				ghast.enablePersistence();
				world.addEntity(ghast);
			}
			else if (key.equals("spawner"))
			{
				DPUtil.placeSpawner(EntityType.GHAST, world, pos);
				if (world.getTileEntity(pos) instanceof MobSpawnerTileEntity)
				{
					MobSpawnerTileEntity tile = (MobSpawnerTileEntity) world.getTileEntity(pos);
					SpawnerAccessHelper.setRequiredPlayerRange(tile, 32);
					SpawnerAccessHelper.setMaxNearbyEntities(tile, 10);
					SpawnerAccessHelper.setSpawnCount(tile, 5);
					SpawnerAccessHelper.setSpawnRange(tile, 16);
				}
			}
			else if (key.contains("chest"))
			{
				DPUtil.placeLootChest(DPLoot.SoulPrison.CHEST_COMMON, world, rand, pos, rotation, key.split("-")[1]);
			}
		}
	}
}
