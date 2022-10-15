package com.legacy.dungeons_plus.data.providers;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.items.DPItem;
import com.legacy.dungeons_plus.items.FrostedCowlItem;
import com.legacy.dungeons_plus.items.LeviathanBladeItem;
import com.legacy.dungeons_plus.items.SoulCannonItem;
import com.legacy.dungeons_plus.items.WarpedAxeItem;
import com.legacy.dungeons_plus.registry.DPItems;
import com.legacy.dungeons_plus.registry.DPSoundEvents;
import com.legacy.dungeons_plus.registry.DPStructures;
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
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
		this.addAll(BuiltinRegistries.STRUCTURES, Map.of());
		this.addAll(Registry.ITEM, Map.of());
		this.addAll(Registry.ENTITY_TYPE, Map.of());

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

		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.killGhast, "Special Delivery", "Kill a ghast with a soul fireball");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.zombieVillagerWeakness, "Hurtful Healing", "Give a zombie villager weakness with the Leviathan Blade");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.hideInSnow, "Cold Campout", "Hide in powder snow with a Frosted Cowl");
		this.addAdvancement(DPAdvancementProv.DungeonsPlusAdvancements.axePhantom, "What could go wrong?", "Throw a Warped Axe at a phantom from at least 25 meters away");
		
		
		this.add(DPSoundEvents.SOUL_CANNON_SHOOT, "Soul Blaster shoots");
		this.add(DPSoundEvents.WARPED_AXE_THROW, "Warped Axe thrown");
		this.add(DPSoundEvents.WARPED_AXE_LAND, "Warped Axe lands");
		this.add(DPSoundEvents.WARPED_AXE_HIT, "Warped Axe hits");
		this.add(DPSoundEvents.WARPED_AXE_RETURN, "Warped Axe returns");
		this.add(DPSoundEvents.WARPED_AXE_TELEPORT, "Player teleports");

		this.add("death.attack.dungeons_plus.consume_soul", "%1$s consumed their own soul");
		this.add("death.attack.dungeons_plus.consume_soul.player", "%1$s consumed their soul whilst fighting %2$s");

		this.add("death.attack.dungeons_plus.warped_axe", "%1$s was cleaved by %2$s");
		this.add("death.attack.dungeons_plus.warped_axe.item", "%1$s was cleaved by %2$s with %3$s");

		this.add(DPItem.HOLD_SHIFT_KEY, "Hold [Shift] for info");

		this.addItemInfo(DPItems.LEVIATHAN_BLADE, LeviathanBladeItem.WEAKNESS_KEY, "Afflicts targets with weakness.");

		this.addItemInfo(DPItems.FROSTED_COWL, FrostedCowlItem.FREEZE_KEY, "Grants immunity to freezing.");
		this.addItemInfo(DPItems.FROSTED_COWL, FrostedCowlItem.SLOWNESS_KEY, "Prevents the slowness potions from taking effect.");

		this.addItemInfo(DPItems.WARPED_AXE, WarpedAxeItem.TELEPORT_KEY, "Teleports the user to who the axe was thrown at.");
		this.addItemInfo(DPItems.WARPED_AXE, WarpedAxeItem.CROUCH_KEY, "Crouch to prevent teleporting.");
		
		this.addItemInfo(DPItems.SOUL_CANNON, SoulCannonItem.SHOOT_KEY, "Launches a fireball in the direction the user is facing.");
	}

	public static String mapName(StructureRegistrar<?> structure)
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
		this.add(this.makeDescriptionID(key), translation);
	}

	private void add(Supplier<SoundEvent> sound, String translation)
	{
		this.add("subtitles." + DungeonsPlus.MODID + "." + sound.get().getLocation().getPath(), translation);
	}

	@SuppressWarnings("deprecation")
	private void addItemInfo(Supplier<Item> item, String key, String translation)
	{
		var resourceKey = Registry.ITEM.getResourceKey(item.get()).get();
		ResourceLocation location = resourceKey.location();
		this.add(Util.makeDescriptionId(resourceKey.registry().getPath().replace('/', '.'), new ResourceLocation(location.getNamespace(), location.getPath() + "." + key)), translation);
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

	private String makeDescriptionID(ResourceKey<?> resourceKey)
	{
		return Util.makeDescriptionId(resourceKey.registry().getPath().replace('/', '.'), resourceKey.location());
	}
}
