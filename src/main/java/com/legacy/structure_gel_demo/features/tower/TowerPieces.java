package com.legacy.structure_gel_demo.features.tower;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.legacy.structure_gel.structures.jigsaw.JigsawPoolBuilder;
import com.legacy.structure_gel.structures.jigsaw.JigsawRegistryHelper;
import com.legacy.structure_gel.structures.processors.RandomBlockSwapProcessor;
import com.legacy.structure_gel_demo.SGDemoMod;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest;
import net.minecraft.world.gen.feature.template.RandomBlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleEntry;
import net.minecraft.world.gen.feature.template.RuleStructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class TowerPieces
{
	public static void init(ChunkGenerator<?> chunkGen, TemplateManager template, BlockPos pos, List<StructurePiece> pieces, SharedSeedRandom seed)
	{
		JigsawManager.func_214889_a(SGDemoMod.locate("tower/root"), 7, TowerPieces.Piece::new, chunkGen, template, pos, pieces, seed);
	}

	static
	{
		/**
		 * This is a processor from Structure Gel API for making a single block swap. It
		 * exists as a shorthand way of writing the processor below it for a simple
		 * swap.
		 */
		RandomBlockSwapProcessor cobbleToMossy = new RandomBlockSwapProcessor(Blocks.COBBLESTONE, 0.2F, Blocks.MOSSY_COBBLESTONE.getDefaultState());
		RandomBlockSwapProcessor goldDecay = new RandomBlockSwapProcessor(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR.getDefaultState());

		//@formatter:off
		RuleStructureProcessor brickDecay = new RuleStructureProcessor(ImmutableList.of(
				new RuleEntry(new RandomBlockMatchRuleTest(Blocks.STONE_BRICKS, 0.15F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.getDefaultState()),
				new RuleEntry(new RandomBlockMatchRuleTest(Blocks.STONE_BRICKS, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.CRACKED_STONE_BRICKS.getDefaultState())));
		//@formatter:on

		JigsawRegistryHelper registry = new JigsawRegistryHelper(SGDemoMod.MODID, "tower/");

		/**
		 * The JigsawRegistryHelper.register method takes a string for the name of the
		 * pool. This name takes the set modid and prefix into account. In this case,
		 * it'll end up being "structure_gel_demo:tower/root". This is the target pool
		 * in your jigsaw blocks. You could also just use a ResourceLocation directly
		 * instead of letting it fill in the repeated mod id and prefix.
		 *
		 * The second value is the JigsawPoolBuilder's built List<Pair<JigsawPiece,
		 * Integer>>. The "names" method tells the pool what options it has for
		 * structures to generate. These all take the mod id and prefix into account
		 * just like before. You can use ResourceLocations to handle the mod id and
		 * prefix yourself. You can also input a Map<String, Integer> to set weights.
		 *
		 * ...names(ImmutableMap.of("root", 1, "root_2", 9)) would generate "root_2" 90%
		 * of the time.
		 * 
		 * build() takes everything in the builder and compiles it into the List that
		 * the registry needs. Any settings like processors will take effect for all
		 * pieces passed in. This is done in the next line.
		 */
		registry.register("root", registry.builder().names("root").build());

		/**
		 * Here we have an example of using more than one structure in the names()
		 * method. Since they are simply written in a list, they will all have the same
		 * weight.
		 * 
		 * This registry is told to not maintain water and to use the structure
		 * processors (cobbleToMossy and brickDecay) defined above.
		 */
		registry.register("floor", registry.builder().names("floor_spider", "floor_zombie", "floor_skeleton", "floor_vex").maintainWater(false).processors(cobbleToMossy, brickDecay).build());

		/**
		 * Just another way to get the JigsawPoolBuilder if you like doing things like
		 * this.
		 */
		registry.register("gate", new JigsawPoolBuilder(registry).names("gate").maintainWater(false).build());

		/**
		 * Creating a JigsawPoolBuilder instance beforehand with shared settings that'll
		 * be used in the "top" pool.
		 */
		JigsawPoolBuilder topBuilder = registry.builder().maintainWater(false).processors(cobbleToMossy);

		/**
		 * In this case, I'm using the JigsawPoolBuilder.collect to merge two different
		 * pool builders into one. Since both "top" and "top_2" will use the same basic
		 * settings, but with "top_2" having a gold decay processor, I use a copy of the
		 * JigsawPoolBuilder instance created beforehand. With these copies, I can
		 * adjust the settings for each builder while keeping commmon settings the same.
		 * 
		 * It's worth noting that processors() functions as an append, meaning that the
		 * "top_2" structure will have both cobbleToMossy and goldDecay.
		 * 
		 * The weights of each structure set in names() are preserved, so set them as
		 * you normally would.
		 */
		registry.register("top", JigsawPoolBuilder.collect(topBuilder.clone().names(ImmutableMap.of("top", 1)), topBuilder.clone().names(ImmutableMap.of("top_2", 4)).processors(goldDecay)));

	}

	public static class Piece extends AbstractVillagePiece
	{
		public Piece(TemplateManager template, JigsawPiece jigsawPiece, BlockPos pos, int groundLevelDelta, Rotation rotation, MutableBoundingBox boundingBox)
		{
			super(SGDemoMod.Features.TOWER.getSecond(), template, jigsawPiece, pos, groundLevelDelta, rotation, boundingBox);
		}

		public Piece(TemplateManager template, CompoundNBT nbt)
		{
			super(template, nbt, SGDemoMod.Features.TOWER.getSecond());
		}
	}
}
