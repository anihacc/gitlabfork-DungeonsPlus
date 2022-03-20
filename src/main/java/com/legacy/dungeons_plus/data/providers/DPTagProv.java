package com.legacy.dungeons_plus.data.providers;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DPTagProv
{
	public static class Block extends BlockTagsProvider
	{
		public Block(DataGenerator dataGen, ExistingFileHelper existingFileHelper)
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

	public static class Item extends ItemTagsProvider
	{
		public Item(DataGenerator dataGen, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper)
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
}
