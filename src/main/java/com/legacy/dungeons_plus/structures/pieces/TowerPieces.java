package com.legacy.dungeons_plus.structures.pieces;

import java.util.List;
import java.util.Random;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.registries.ForgeRegistries;

public class TowerPieces
{
	private static final ResourceLocation[] BASES = DungeonsPlus.locateAllPrefix("tower/base/", "camp");
	private static final ResourceLocation[] FLOORS = DungeonsPlus.locateAllPrefix("tower/floor/", "zombie_0", "skeleton_0", "spider_0");
	private static final ResourceLocation[] VEXES = DungeonsPlus.locateAllPrefix("tower/floor/", "vex_0");
	private static final ResourceLocation[] TOPS = DungeonsPlus.locateAllPrefix("tower/top/", "full", "decayed");

	public static void assemble(StructureManager structureManager, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, Random rand)
	{
		pieces.addPiece(new Piece(structureManager, Util.getRandom(BASES, rand), pos, rotation));

		int maxFloors = rand.nextInt(3) + 3;
		for (int floor = 0; floor < maxFloors; floor++)
		{
			pieces.addPiece(new Piece(structureManager, Util.getRandom(FLOORS, rand), pos = pos.above(6), rotation));
		}

		pieces.addPiece(new Piece(structureManager, Util.getRandom(VEXES, rand), pos = pos.above(6), rotation));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(TOPS, rand), pos = pos.above(6), rotation));
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		private static final ItemStack[] ARMOR_STAND_ITEMS = List.of(Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_BOOTS, Items.GOLDEN_LEGGINGS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS).stream().map(ItemStack::new).toArray(ItemStack[]::new);

		public Piece(StructureManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(DPStructures.TOWER.getPieceType(), 0, structureManager, location, pos);
			this.rotation = rotation;
			this.setupPlaceSettings(structureManager);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(DPStructures.TOWER.getPieceType(), tag, context.structureManager());
			this.setupPlaceSettings(context.structureManager());
		}

		@Override
		protected StructurePlaceSettings getPlaceSettings(StructureManager structureManager)
		{
			StructurePlaceSettings settings = new StructurePlaceSettings();
			settings.setKeepLiquids(false);

			Vec3i size = structureManager.get(this.makeTemplateLocation()).get().getSize();
			settings.setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2));

			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.MOSSY_COBBLESTONE));
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.STONE_BRICKS, 0.15F, Blocks.MOSSY_STONE_BRICKS));
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.STONE_BRICKS, 0.3F, Blocks.CRACKED_STONE_BRICKS));
			settings.addProcessor(new RandomBlockSwapProcessor(Blocks.SMOOTH_BASALT, 0.2F, Blocks.DEEPSLATE));
			
			if (this.templateName.contains("top/decayed"))
			{
				settings.addProcessor(new RandomBlockSwapProcessor(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR));
			}

			return settings;
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, Random rand, BoundingBox bounds)
		{
			if (key.equals("armor_stand"))
			{
				level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);

				ArmorStand entity = EntityType.ARMOR_STAND.create(level.getLevel());
				entity.moveTo(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, this.rotation.rotate(Direction.SOUTH).toYRot(), 0);

				entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));

				for (ItemStack item : ARMOR_STAND_ITEMS)
					if (rand.nextFloat() < 0.25)
						entity.setItemSlot(Mob.getEquipmentSlotForItem(item), item);

				level.addFreshEntity(entity);
			}
			if (key.startsWith("waystone"))
			{
				DPUtil.placeWaystone(level, pos, rand, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key.split("-")[1])));
			}
		}
	}
}
