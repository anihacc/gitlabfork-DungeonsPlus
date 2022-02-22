package com.legacy.dungeons_plus.data.providers;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

import com.legacy.dungeons_plus.DungeonsPlus;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class DPLangProvider extends LanguageProvider
{
	public DPLangProvider(DataGenerator gen)
	{
		super(gen, DungeonsPlus.MODID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		this.addAll(ForgeRegistries.BLOCKS, this::add, Map.of());
		
		this.add("filled_map.dungeons_plus:bigger_dungeon", "Buried Dungeon Map"); // TODO rename
		this.add("filled_map.dungeons_plus:leviathan", "Leviathan Map");
		this.add("filled_map.dungeons_plus:snowy_temple", "Snowy Temple Map");
		
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findTower, "Battle Towers?", "Find a Tower");
		//this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findTower, "Now this is a Dungeon", "Find a Buried Dungeon");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findLeviathan, "Ancient Remains", "Find a Leviathan");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findSnowyTemple, "Snowed In", "Find a Snowy Temple");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findWarpedGarden, "Something Unnatural", "Find a Warped Garden");

		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findEndRuins, "A Ruined End", "Find End Ruins");

		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findSoulPrison, "Caged Tears", "Find the Soul Prison");

		// TODO include maps, advancements, etc
	}

	private void addAdvancement(Advancement advancement, String title, String desc)
	{
		DisplayInfo display = advancement.getDisplay();
		this.add(display.getTitle().getString(), title);
		this.add(display.getDescription().getString(), desc);
	}
	
	private <T extends IForgeRegistryEntry<T>> void addAll(IForgeRegistry<T> registry, BiConsumer<T, String> adder, Map<T, String> overrides)
	{
		registry.getValues().stream().filter(o ->
		{
			ResourceLocation name = o.getRegistryName();
			return name != null && DungeonsPlus.MODID.equals(name.getNamespace());
		}).filter(o ->
		{
			return !overrides.containsKey(o);
		}).forEach(o ->
		{
			adder.accept(o, this.toName(o.getRegistryName()));
		});
		
		overrides.forEach(adder::accept);
	}

	private String toName(ResourceLocation location)
	{
		String path = location.getPath();
		String[] words = path.split("_");
		for (int i = words.length - 1; i > -1; i--)
			words[i] = words[i].substring(0, 1).toUpperCase(Locale.ENGLISH) + words[i].substring(1).toLowerCase(Locale.ENGLISH);
		return String.join(" ", words);
	}
}
