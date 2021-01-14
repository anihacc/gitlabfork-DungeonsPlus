package com.legacy.dungeons_plus.pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.legacy.dungeons_plus.DPLoot;
import com.legacy.dungeons_plus.DPUtil;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.structure_gel.access_helpers.SpawnerAccessHelper;
import com.legacy.structure_gel.worldgen.GelPlacementSettings;
import com.legacy.structure_gel.worldgen.processors.RandomBlockSwapProcessor;
import com.legacy.structure_gel.worldgen.structure.GelTemplateStructurePiece;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.RuleEntry;
import net.minecraft.world.gen.feature.template.RuleStructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class WarpedGardenPieces
{
	private static final ResourceLocation[] FLOWERY = new ResourceLocation[] { locate("flower_0"), locate("flower_1"), locate("flower_2"), locate("flower_3") };
	private static final ResourceLocation[] LOOT = new ResourceLocation[] { locate("portal"), locate("ship"), locate("nylium"), locate("volcano") };
	private static final List<WeightedSpawnerEntity> SPAWNER_SPAWNS = new ArrayList<>();

	public static void assemble(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> structurePieces, Random rand)
	{
		structurePieces.add(new Piece(templateManager, LOOT[rand.nextInt(LOOT.length)], pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1), rotation));
		int offset = 20;
		List<BlockPos> positions = Arrays.asList(pos.add(-offset, 0, rand.nextInt(7) - 3), pos.add(offset, 0, rand.nextInt(7) - 3), pos.add(rand.nextInt(7) - 3, 0, offset), pos.add(rand.nextInt(7) - 3, 0, -offset));
		positions.forEach(p -> structurePieces.add(new Piece(templateManager, getRandomPiece(rand), p, rotation)));
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
		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(DungeonsPlus.Structures.WARPED_GARDEN.getPieceType(), location, 0);
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setupTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbtCompound)
		{
			super(DungeonsPlus.Structures.WARPED_GARDEN.getPieceType(), nbtCompound);
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
			placementSettings.addProcessor(new RuleStructureProcessor(ImmutableList.of(new RuleEntry(new BlockMatchRuleTest(Blocks.BLUE_STAINED_GLASS), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState()))));
			placementSettings.addProcessor(new RandomBlockSwapProcessor(Blocks.BLUE_STAINED_GLASS, Blocks.AIR));
			placementSettings.addProcessor(new RandomBlockSwapProcessor(Blocks.OBSIDIAN, 0.35F, Blocks.CRYING_OBSIDIAN));
		}

		// Adjust the height to be under water
		@Override
		public boolean generate(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGen, Random rand, MutableBoundingBox bounds, ChunkPos chunkPos, BlockPos pos)
		{
			int y = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, this.templatePosition.getX() + 6, this.templatePosition.getZ() + 6);
			this.templatePosition = new BlockPos(this.templatePosition.getX(), y, this.templatePosition.getZ());
			if (this.templatePosition.getY() + this.template.getSize().getY() > 60)
				this.templatePosition = new BlockPos(this.templatePosition.getX(), 60 - this.template.getSize().getY(), this.templatePosition.getZ());
			return super.generate(world, structureManager, chunkGen, rand, bounds, chunkPos, pos);
		}

		@Override
		protected void handleDataMarker(String key, BlockPos pos, IServerWorld world, Random rand, MutableBoundingBox sbb)
		{
			if (key.contains("chest"))
			{
				String[] data = key.split("-");
				DPUtil.placeLootChest(DPLoot.WarpedGarden.CHEST_COMMON, world, rand, pos, this.rotation, data[1]);
			}
			if (key.equals("spawner"))
			{
				if (SPAWNER_SPAWNS.isEmpty())
				{
					CompoundNBT goldAxeNBT = new ItemStack(Items.GOLDEN_AXE).write(new CompoundNBT());
					goldAxeNBT.putInt("Damage", 10);

					for (Block coral : BlockTags.CORAL_BLOCKS.getAllElements())
					{
						CompoundNBT entityNBT = new CompoundNBT();

						ListNBT handItems = new ListNBT();
						handItems.add(goldAxeNBT);
						handItems.add(new ItemStack(coral).write(new CompoundNBT()));
						entityNBT.put("HandItems", handItems);

						ListNBT handDropChances = new ListNBT();
						handDropChances.add(FloatNBT.valueOf(0.085F)); // Main hand
						handDropChances.add(FloatNBT.valueOf(1.0F)); // Off hand
						entityNBT.put("HandDropChances", handDropChances);

						SPAWNER_SPAWNS.add(SpawnerAccessHelper.createSpawnerEntity(1, EntityType.DROWNED, entityNBT));
					}

					CompoundNBT entityNBT = new CompoundNBT();

					ListNBT handItems = new ListNBT();
					handItems.add(goldAxeNBT);
					entityNBT.put("HandItems", handItems);

					int coralCount = BlockTags.CORAL_BLOCKS.getAllElements().size();
					SPAWNER_SPAWNS.add(SpawnerAccessHelper.createSpawnerEntity(coralCount == 0 ? 1 : coralCount * 5, EntityType.DROWNED, entityNBT));
				}

				Collections.shuffle(SPAWNER_SPAWNS, rand);

				DPUtil.placeSpawner(SPAWNER_SPAWNS, world, pos);
			}
			if (key.equals("drowned"))
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				DrownedEntity drowned = EntityType.DROWNED.create(world.getWorld());
				ItemStack goldAxe = new ItemStack(Items.GOLDEN_AXE);
				goldAxe.setDamage(10);
				drowned.setItemStackToSlot(EquipmentSlotType.MAINHAND, goldAxe);
				drowned.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));
				drowned.enablePersistence();
				drowned.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.addEntity(drowned);
			}
		}
	}
}
