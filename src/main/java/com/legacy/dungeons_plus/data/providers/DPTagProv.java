package com.legacy.dungeons_plus.data.providers;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ConfiguredStructureTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
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

			//this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add();
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
			return "Dungeons Plus configured structure feature tag provider";
		}
	}
}
