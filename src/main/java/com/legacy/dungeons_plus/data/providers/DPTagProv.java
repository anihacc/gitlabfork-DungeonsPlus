package com.legacy.dungeons_plus.data.providers;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DPTagProv
{
	public static class BlockProv extends BlockTagsProvider
	{
		public BlockProv(DataGenerator dataGen, ExistingFileHelper existingFileHelper)
		{
			super(dataGen, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags()
		{
			this.dungeonsPlus();
			this.vanilla();
		}

		public void dungeonsPlus()
		{

		}

		public void vanilla()
		{

		}

		@Override
		public String getName()
		{
			return "Dungeons Plus block tag provider";
		}
	}

	public static class ItemProv extends ItemTagsProvider
	{
		public ItemProv(DataGenerator dataGen, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper)
		{
			super(dataGen, blockTagsProvider, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags()
		{
			this.forge();
			this.dungeonsPlus();
			this.vanilla();
		}

		public void forge()
		{
			// TODO 1.19 Add forge tags for armor/tools/weapons
		}
		
		public void dungeonsPlus()
		{

		}

		public void vanilla()
		{

		}

		@Override
		public String getName()
		{
			return "Dungeons Plus item tag provider";
		}
	}
	
	public static class ConfiguredStructureFeatureProv extends ConfiguredStructureTagsProvider
	{
		public ConfiguredStructureFeatureProv(DataGenerator dataGen, ExistingFileHelper existingFileHelper)
		{
			super(dataGen, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags()
		{
			this.dungeonsPlus();
			this.vanilla();
		}

		public void dungeonsPlus()
		{
			this.allConfigured(DPTags.StructureTags.ON_REANIMATED_RUINS_MAPS, DPStructures.REANIMATED_RUINS);
			this.allConfigured(DPTags.StructureTags.ON_LEVIATHAN_MAPS, DPStructures.LEVIATHAN);
			this.allConfigured(DPTags.StructureTags.ON_SNOWY_TEMPLE_MAPS, DPStructures.SNOWY_TEMPLE);
			this.allConfigured(DPTags.StructureTags.ON_WARPED_GARDEN_MAPS, DPStructures.WARPED_GARDEN);
		}

		public void vanilla()
		{

		}

		private void allConfigured(TagKey<ConfiguredStructureFeature<?, ?>> tagKey, StructureRegistrar<?, ?> registrar)
		{
			var appender = this.tag(tagKey);
			registrar.getConfiguredStructures().values().forEach(holder -> appender.add(holder.value()));
		}
		
		@Override
		public String getName()
		{
			return "Dungeons Plus structure tag provider";
		}
	}
	
	public static class BiomeProv extends BiomeTagsProvider
	{
		public BiomeProv(DataGenerator dataGen, ExistingFileHelper existingFileHelper)
		{
			super(dataGen, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags()
		{
			this.dungeonsPlus();
		}

		@SuppressWarnings("unchecked")
		public void dungeonsPlus()
		{
			// TODO Revise with forge tags. Also mangrove
			this.tag(DPTags.BiomeTags.HAS_TOWER).addTags(BiomeTags.IS_MOUNTAIN, BiomeTags.IS_FOREST);
			this.tag(DPTags.BiomeTags.HAS_REANIMATED_RUINS_MOSSY).addTags(BiomeTags.HAS_SWAMP_HUT);
			this.tag(DPTags.BiomeTags.HAS_REANIMATED_RUINS_MESA).addTags(BiomeTags.IS_BADLANDS);
			this.tag(DPTags.BiomeTags.HAS_REANIMATED_RUINS_FROZEN).addTags(BiomeTags.HAS_VILLAGE_SNOWY);
			this.tag(DPTags.BiomeTags.HAS_LEVIATHAN).addTags(BiomeTags.HAS_VILLAGE_DESERT);
			this.tag(DPTags.BiomeTags.HAS_SNOWY_TEMPLE).add(Biomes.SNOWY_TAIGA, Biomes.FROZEN_PEAKS, Biomes.SNOWY_SLOPES);
			this.tag(DPTags.BiomeTags.HAS_WARPED_GARDEN).addTags(BiomeTags.IS_DEEP_OCEAN);
			this.tag(DPTags.BiomeTags.HAS_SOUL_PRISON).add(Biomes.SOUL_SAND_VALLEY);
			this.tag(DPTags.BiomeTags.HAS_END_RUINS).addTag(BiomeTags.HAS_END_CITY);
		}

		public void vanilla()
		{

		}

		@Override
		public String getName()
		{
			return "Dungeons Plus biome tag provider";
		}
	}
}
