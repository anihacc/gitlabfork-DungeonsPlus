package com.legacy.dungeons_plus.structures.tower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.structure.GelTemplateStructurePiece;
import com.legacy.structure_gel.api.structure.base.IModifyState;
import com.legacy.structure_gel.api.structure.processor.RandomBlockSwapProcessor;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.registries.ForgeRegistries;

public class TowerPieces
{
	private static final ResourceLocation[] BASES = DungeonsPlus.locateAllPrefix("tower/base/", "camp", "flooded", "infested");
	private static final ResourceLocation[] FLOORS = DungeonsPlus.locateAllPrefix("tower/floor/", "zombie_0", "skeleton_0", "spider_0", "zombie_1", "skeleton_1", "spider_1", "zombie_2", "skeleton_2", "spider_2");
	private static final ResourceLocation[] TOP_FLOORS = DungeonsPlus.locateAllPrefix("tower/top_floor/", "vex_0", "vex_1", "vex_2");
	private static final ResourceLocation[] TOPS = DungeonsPlus.locateAllPrefix("tower/top/", "full", "decayed");

	public static void assemble(StructureTemplateManager structureManager, BlockPos pos, Rotation rotation, StructurePiecesBuilder pieces, RandomSource rand)
	{
		boolean infested = rand.nextFloat() < 0.3;

		pieces.addPiece(new Piece(structureManager, Util.getRandom(BASES, rand), pos, rotation, infested));

		int maxFloors = rand.nextInt(3) + 3;
		List<ResourceLocation> unusedFloors = new ArrayList<>(Arrays.asList(FLOORS));
		for (int floor = 0; floor < maxFloors && unusedFloors.size() > 0; floor++)
		{
			ResourceLocation floorName = Util.getRandom(unusedFloors, rand);
			pieces.addPiece(new Piece(structureManager, floorName, pos = pos.above(6), rotation, infested));
			unusedFloors.remove(floorName);
		}

		pieces.addPiece(new Piece(structureManager, Util.getRandom(TOP_FLOORS, rand), pos = pos.above(6), rotation, infested));
		pieces.addPiece(new Piece(structureManager, Util.getRandom(TOPS, rand), pos = pos.above(6), rotation, infested));
	}

	public static class Piece extends GelTemplateStructurePiece
	{
		private static final String INFESTED_KEY = "infested";
		
		private static final ItemStack[] ARMOR_STAND_ITEMS = List.of(Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.CHAINMAIL_BOOTS, Items.GOLDEN_LEGGINGS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS).stream().map(ItemStack::new).toArray(ItemStack[]::new);
		private static final float MOSS_CHANCE = 0.2F;
		private static final float MOSS_BRICK_CHANCE = 0.15F;
		private static final float CRACK_BRICK_CHANCE = 0.15F;
		private static final float INFEST_CHANCE = 0.07F;

		private final boolean infested;

		public Piece(StructureTemplateManager structureManager, ResourceLocation location, BlockPos pos, Rotation rotation, boolean infested)
		{
			super(DPStructures.TOWER.getPieceType().get(), 0, structureManager, location, pos);
			this.rotation = rotation;
			this.infested = infested;
			this.setupPlaceSettings(structureManager);
		}

		public Piece(StructurePieceSerializationContext context, CompoundTag tag)
		{
			super(DPStructures.TOWER.getPieceType().get(), tag, context.structureTemplateManager());
			this.infested = tag.getBoolean(INFESTED_KEY);
			this.setupPlaceSettings(context.structureTemplateManager());
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext level, CompoundTag tag)
		{
			super.addAdditionalSaveData(level, tag);
			tag.putBoolean(INFESTED_KEY, this.infested);
		}

		@Override
		protected StructurePlaceSettings getPlaceSettings(StructureTemplateManager structureManager)
		{
			StructurePlaceSettings settings = new StructurePlaceSettings();
			settings.setKeepLiquids(false);

			Vec3i size = structureManager.get(this.makeTemplateLocation()).get().getSize();
			settings.setRotationPivot(new BlockPos(size.getX() / 2, 0, size.getZ() / 2));

			if (this.templateName.contains("top/decayed"))
			{
				settings.addProcessor(new RandomBlockSwapProcessor(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR));
			}

			return settings;
		}

		@Override
		public BlockState modifyState(ServerLevelAccessor level, RandomSource rand, BlockPos pos, BlockState originalState)
		{
			BlockState newState = this.modifyStateFirstPass(rand, originalState);
			if (this.infested)
				newState = this.modifyStateInfest(rand, newState);
			return newState;
		}

		private BlockState modifyStateFirstPass(RandomSource rand, BlockState originalState)
		{
			Block originalBlock = originalState.getBlock();

			if (originalBlock == Blocks.COBBLESTONE && rand.nextFloat() < MOSS_CHANCE)
			{
				return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
			}
			else if (originalBlock == Blocks.COBBLESTONE_SLAB && rand.nextFloat() < MOSS_CHANCE)
			{
				return IModifyState.mergeStates(Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState(), originalState);
			}
			else if (originalBlock == Blocks.COBBLESTONE_STAIRS && rand.nextFloat() < MOSS_CHANCE)
			{
				return IModifyState.mergeStates(Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState(), originalState);
			}
			else if (originalBlock == Blocks.STONE_BRICKS)
			{
				float r = rand.nextFloat();
				if (r < MOSS_BRICK_CHANCE)
					return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
				else if (r < MOSS_BRICK_CHANCE + CRACK_BRICK_CHANCE)
					return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
			}
			else if (originalBlock == Blocks.STONE_BRICK_SLAB && rand.nextFloat() < MOSS_BRICK_CHANCE)
			{
				return IModifyState.mergeStates(Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState(), originalState);
			}
			else if (originalBlock == Blocks.STONE_BRICK_STAIRS && rand.nextFloat() < MOSS_BRICK_CHANCE)
			{
				return IModifyState.mergeStates(Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState(), originalState);
			}
			else if (originalBlock == Blocks.SMOOTH_BASALT && rand.nextFloat() < 0.2F)
			{
				return Blocks.DEEPSLATE.defaultBlockState();
			}
			else if (originalBlock == Blocks.DEEPSLATE_BRICKS && rand.nextFloat() < CRACK_BRICK_CHANCE)
			{
				return Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
			}

			return originalState;
		}

		private BlockState modifyStateInfest(RandomSource rand, BlockState originalState)
		{
			if (originalState.is(BlockTags.STONE_BRICKS) && rand.nextFloat() < 0.06)
			{
				return Blocks.COBWEB.defaultBlockState();
			}
			else if (InfestedBlock.isCompatibleHostBlock(originalState) && rand.nextFloat() < INFEST_CHANCE)
			{
				return InfestedBlock.infestedStateByHost(originalState);
			}

			return originalState;
		}

		@Override
		public void handleDataMarker(String key, BlockPos pos, ServerLevelAccessor level, RandomSource rand, BoundingBox bounds)
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
