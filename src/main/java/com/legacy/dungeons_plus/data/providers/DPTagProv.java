package com.legacy.dungeons_plus.data.providers;

import java.util.concurrent.CompletableFuture;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.data.DPTags;
import com.legacy.dungeons_plus.registry.DPEntityTypes;
import com.legacy.dungeons_plus.registry.DPItems;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.Registrar;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DPTagProv
{
	public static class BlockProv extends BlockTagsProvider
	{
		public BlockProv(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper)
		{
			super(packOutput, lookup, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			this.tag(DPTags.Blocks.WARPED_AXE_TELEPORTS_TO).add(Blocks.TARGET);
		}

		@Override
		public String getName()
		{
			return "Dungeons Plus block tag provider";
		}
	}

	public static class ItemProv extends ItemTagsProvider
	{
		public ItemProv(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper)
		{
			super(packOutput, lookup, blockTagsProvider, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			this.tag(Tags.Items.ARMORS_HELMETS).add(DPItems.FROSTED_COWL.get());
			this.tag(Tags.Items.TOOLS_SWORDS).add(DPItems.LEVIATHAN_BLADE.get());
			this.tag(Tags.Items.TOOLS_AXES).add(DPItems.WARPED_AXE.get());

			this.tag(DPTags.Items.LOOT_COMMON);
			this.tag(DPTags.Items.LOOT_UNCOMMON);
			this.tag(DPTags.Items.LOOT_RARE);
			this.lootTags(DPTags.Items.LOOT_TOWER_COMMON, DPTags.Items.LOOT_TOWER_UNCOMMON, DPTags.Items.LOOT_TOWER_RARE);
			this.lootTags(DPTags.Items.LOOT_REANIMATED_RUINS_COMMON, DPTags.Items.LOOT_REANIMATED_RUINS_UNCOMMON, DPTags.Items.LOOT_REANIMATED_RUINS_RARE);
			this.lootTags(DPTags.Items.LOOT_SNOWY_TEMPLE_COMMON, DPTags.Items.LOOT_SNOWY_TEMPLE_UNCOMMON, DPTags.Items.LOOT_SNOWY_TEMPLE_RARE);
			this.lootTags(DPTags.Items.LOOT_LEVIATHAN_COMMON, DPTags.Items.LOOT_LEVIATHAN_UNCOMMON, DPTags.Items.LOOT_LEVIATHAN_RARE);
			this.lootTags(DPTags.Items.LOOT_WARPED_GARDEN_COMMON, DPTags.Items.LOOT_WARPED_GARDEN_UNCOMMON, DPTags.Items.LOOT_WARPED_GARDEN_RARE);
			this.lootTags(DPTags.Items.LOOT_SOUL_PRISON_COMMON, DPTags.Items.LOOT_SOUL_PRISON_UNCOMMON, DPTags.Items.LOOT_SOUL_PRISON_RARE);
		}

		private void lootTags(TagKey<Item> common, TagKey<Item> uncommon, TagKey<Item> rare)
		{
			this.tag(common).addTag(DPTags.Items.LOOT_COMMON);
			this.tag(uncommon).addTag(DPTags.Items.LOOT_UNCOMMON);
			this.tag(rare).addTag(DPTags.Items.LOOT_RARE);
		}
	}

	public static class EntityTypeProv extends EntityTypeTagsProvider
	{
		public EntityTypeProv(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper)
		{
			super(packOutput, lookup, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(DPEntityTypes.WARPED_AXE.get(), DPEntityTypes.SOUL_FIREBALL.get());
			this.tag(DPTags.EntityTypes.WARPED_AXE_IMMUNE).add(EntityType.ENDERMAN);
		}
	}

	public static class StructureProv extends StructureTagsProvider
	{
		public StructureProv(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper)
		{
			super(packOutput, lookup, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			this.allStructures(DPTags.Structures.ON_REANIMATED_RUINS_MAPS, DPStructures.REANIMATED_RUINS);
			this.allStructures(DPTags.Structures.ON_LEVIATHAN_MAPS, DPStructures.LEVIATHAN);
			this.allStructures(DPTags.Structures.ON_SNOWY_TEMPLE_MAPS, DPStructures.SNOWY_TEMPLE);
			this.allStructures(DPTags.Structures.ON_WARPED_GARDEN_MAPS, DPStructures.WARPED_GARDEN);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void allStructures(TagKey<Structure> tagKey, StructureRegistrar<?> registrar)
		{
			TagAppender<Structure> appender = this.tag(tagKey);
			registrar.getStructures().values().stream().map(Registrar.Pointer::getKey).forEach(k -> appender.add((ResourceKey) k));
		}
	}

	public static class BiomeProv extends BiomeTagsProvider
	{
		public BiomeProv(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper)
		{
			super(packOutput, lookup, DungeonsPlus.MODID, existingFileHelper);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			this.tag(DPTags.Biomes.HAS_TOWER).addTags(BiomeTags.IS_MOUNTAIN, BiomeTags.IS_FOREST, Tags.Biomes.IS_MOUNTAIN);
			this.tag(DPTags.Biomes.HAS_REANIMATED_RUINS_MOSSY).addTags(Tags.Biomes.IS_SWAMP);
			this.tag(DPTags.Biomes.HAS_REANIMATED_RUINS_MESA).addTags(BiomeTags.IS_BADLANDS);
			this.tag(DPTags.Biomes.HAS_REANIMATED_RUINS_FROZEN).addTags(BiomeTags.HAS_VILLAGE_SNOWY);
			this.tag(DPTags.Biomes.HAS_LEVIATHAN).addTags(Tags.Biomes.IS_DESERT);
			this.tag(DPTags.Biomes.HAS_SNOWY_TEMPLE).add(Biomes.SNOWY_TAIGA, Biomes.FROZEN_PEAKS, Biomes.SNOWY_SLOPES, Biomes.GROVE);
			this.tag(DPTags.Biomes.HAS_WARPED_GARDEN).addTags(BiomeTags.IS_DEEP_OCEAN);
			this.tag(DPTags.Biomes.HAS_SOUL_PRISON).add(Biomes.SOUL_SAND_VALLEY);
			this.tag(DPTags.Biomes.HAS_END_RUINS).addTag(BiomeTags.HAS_END_CITY);
		}
	}

	public static class EnchantmentProv extends TagsProvider<Enchantment>
	{
		public EnchantmentProv(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper existingFileHelper)
		{
			super(packOutput, Registries.ENCHANTMENT, lookup, DungeonsPlus.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider lookup)
		{
			this.tag(DPTags.Enchantments.WARPED_AXE_APPLICABLE).addOptional(new ResourceLocation("soulfired", "soul_fire_aspect"));
		}
	}
}
