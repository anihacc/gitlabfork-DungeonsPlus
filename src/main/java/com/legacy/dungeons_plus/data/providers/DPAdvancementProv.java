package com.legacy.dungeons_plus.data.providers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.legacy.dungeons_plus.DungeonsPlus;
import com.legacy.dungeons_plus.registry.DPStructures;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

@SuppressWarnings("unused")
public class DPAdvancementProv implements DataProvider
{
	private static final Logger LOGGER = DungeonsPlus.makeLogger(DPAdvancementProv.class);
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;
	private final List<Consumer<Consumer<Advancement>>> advancements = List.of(new DungeonsPlusAdvancements());

	public DPAdvancementProv(DataGenerator generatorIn)
	{
		this.generator = generatorIn;
	}

	@Override
	public void run(HashCache cache) throws IOException
	{
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> set = Sets.newHashSet();
		Consumer<Advancement> advancement = (a) ->
		{
			if (!set.add(a.getId()))
			{
				throw new IllegalStateException("Duplicate advancement " + a.getId());
			}
			else
			{
				Path path1 = getPath(path, a);

				try
				{
					DataProvider.save(GSON, cache, a.deconstruct().serializeToJson(), path1);
				}
				catch (IOException ioexception)
				{
					LOGGER.error("Couldn't save advancement {}", path1, ioexception);
				}

			}
		};

		for (Consumer<Consumer<Advancement>> adv : this.advancements)
		{
			adv.accept(advancement);
		}

	}

	private static Path getPath(Path path, Advancement advancement)
	{
		return path.resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
	}

	@Override
	public String getName()
	{
		return "Dungeons Plus Advancements";
	}

	protected class DungeonsPlusAdvancements implements Consumer<Consumer<Advancement>>
	{
		protected static Advancement findTower, findLeviathan, findSnowyTemple, findWarpedGarden;
		protected static Advancement findSoulPrison;
		protected static Advancement findEndRuins;
		private String section = "";

		@Override
		public void accept(Consumer<Advancement> con)
		{
			Advancement adventureRoot = this.builder(Items.STONE, "adventure", FrameType.TASK).addCriterion("trigger", KilledTrigger.TriggerInstance.playerKilledEntity()).build(new ResourceLocation("adventure/root"));
			Advancement netherRoot = this.builder(Items.STONE, "nether", FrameType.TASK).addCriterion("trigger", KilledTrigger.TriggerInstance.playerKilledEntity()).build(new ResourceLocation("nether/root"));
			Advancement endEnterGateway = this.builder(Items.STONE, "end", FrameType.TASK).addCriterion("trigger", KilledTrigger.TriggerInstance.playerKilledEntity()).build(new ResourceLocation("end/enter_end_gateway"));

			this.setSection("adventure");
			findTower = this.builder(Blocks.MOSSY_STONE_BRICKS, "find_tower", FrameType.TASK).parent(adventureRoot).addCriterion("enter_structure", this.inStructure(DPStructures.TOWER.getStructure())).save(con, this.locate("find_tower"));
			// TODO bigger dungeon replacement
			findLeviathan = this.builder(Blocks.BONE_BLOCK, "find_leviathan", FrameType.GOAL).parent(adventureRoot).addCriterion("enter_structure", this.inStructure(DPStructures.LEVIATHAN.getStructure())).save(con, this.locate("find_leviathan"));
			findSnowyTemple = this.builder(Blocks.PACKED_ICE, "find_snowy_temple", FrameType.GOAL).parent(adventureRoot).addCriterion("enter_structure", this.inStructure(DPStructures.SNOWY_TEMPLE.getStructure())).save(con, this.locate("find_snowy_temple"));
			findWarpedGarden = this.builder(Blocks.WARPED_FUNGUS, "find_warped_garden", FrameType.TASK).parent(adventureRoot).addCriterion("enter_structure", this.inStructure(DPStructures.WARPED_GARDEN.getStructure())).save(con, this.locate("find_warped_garden"));

			this.setSection("nether");
			findSoulPrison = this.builder(Blocks.SPAWNER, "find_soul_prison", FrameType.GOAL).parent(netherRoot).addCriterion("enter_structure", this.inStructure(DPStructures.SOUL_PRISON.getStructure())).save(con, this.locate("find_soul_prison"));
			
			this.setSection("end");
			findEndRuins = this.builder(Blocks.END_STONE_BRICKS, "find_end_ruins", FrameType.GOAL).parent(endEnterGateway).addCriterion("enter_structure", this.inStructure(DPStructures.END_RUINS.getStructure())).save(con, this.locate("find_end_ruins"));

			// TODO add advancements
		}

		private TranslatableComponent translate(String key)
		{
			return new TranslatableComponent("advancements." + DungeonsPlus.MODID + (section.equals("") ? "" : "." + section) + "." + key);
		}

		private void setSection(String name)
		{
			this.section = name;
		}

		private LocationTrigger.TriggerInstance inStructure(StructureFeature<?> structure)
		{
			return LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(structure));
		}

		private String locate(String key)
		{
			return DungeonsPlus.locate((section.equals("") ? "" : section + "/") + key).toString();
		}

		private Advancement.Builder builder(ItemLike displayItem, String name, ResourceLocation background, FrameType frameType, boolean showToast, boolean announceToChat, boolean hidden)
		{
			return Advancement.Builder.advancement().display(displayItem, translate(name), translate(name + ".desc"), background, frameType, showToast, announceToChat, hidden);
		}

		private Advancement.Builder builder(ItemLike displayItem, String name, FrameType frameType, boolean showToast, boolean announceToChat, boolean hidden)
		{
			return this.builder(displayItem, name, (ResourceLocation) null, frameType, showToast, announceToChat, hidden);
		}

		private Advancement.Builder builder(ItemLike displayItem, String name, FrameType frameType)
		{
			return this.builder(displayItem, name, frameType, true, true, false);
		}
	}
}
