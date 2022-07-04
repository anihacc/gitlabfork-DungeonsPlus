package com.legacy.dungeons_plus.data.providers;

import java.util.Locale;
import java.util.Map;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

public class DPLangProvider extends LanguageProvider
{
	public DPLangProvider(DataGenerator gen)
	{
		super(gen, DungeonsPlus.MODID, "en_us");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void addTranslations()
	{
		this.addAll(Registry.BLOCK, Map.of());
		this.addAll(Registry.STRUCTURE_FEATURE, Map.of());

		this.add(mapName(DPStructures.REANIMATED_RUINS), "Reanimated Ruins Map");
		this.add(mapName(DPStructures.LEVIATHAN), "Leviathan Map");
		this.add(mapName(DPStructures.SNOWY_TEMPLE), "Snowy Temple Map");
		this.add(mapName(DPStructures.WARPED_GARDEN), "Warped Garden Map");

		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findTower, "Battle Towers?", "Find a Tower");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findReanimatedRuins, "Now this is a Dungeon", "Find the Reanimated Ruins");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findLeviathan, "Ancient Remains", "Find a Leviathan");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findSnowyTemple, "Snowed In", "Find a Snowy Temple");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findWarpedGarden, "Something Unnatural", "Find a Warped Garden");

		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findEndRuins, "A Ruined End", "Find End Ruins");

		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.findSoulPrison, "Caged Tears", "Find the Soul Prison");

		// TODO include maps, advancements, etc
	}

	public static String mapName(StructureRegistrar<?, ?> structure)
	{
		return "filled_map." + structure.getRegistryName();
	}

	private void addAdvancement(Advancement advancement, String title, String desc)
	{
		DisplayInfo display = advancement.getDisplay();
		this.add(display.getTitle().getString(), title);
		this.add(display.getDescription().getString(), desc);
	}

	private <T> void addAll(Registry<T> registry, Map<ResourceKey<T>, String> overrides)
	{
		registry.keySet().stream().filter(name -> DungeonsPlus.MODID.equals(name.getNamespace())).map(name -> ResourceKey.create(registry.key(), name)).filter(key -> !overrides.containsKey(key)).forEach(this::add);

		overrides.forEach(this::add);
	}

	private void add(ResourceKey<?> key)
	{
		this.add(key, this.toName(key.location()));
	}

	private void add(ResourceKey<?> key, String translation)
	{
		this.add(Util.makeDescriptionId(key.registry().getPath().replace('/', '.'), key.location()), translation);
	}

	// Converts camel case to a proper name. snowy_temple -> Snowy Temple
	private String toName(ResourceLocation location)
	{
		String path = location.getPath();
		String[] words = path.split("_");
		for (int i = words.length - 1; i > -1; i--)
			words[i] = words[i].substring(0, 1).toUpperCase(Locale.ENGLISH) + words[i].substring(1).toLowerCase(Locale.ENGLISH);
		return String.join(" ", words);
	}
}
