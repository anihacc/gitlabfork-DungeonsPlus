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
import com.legacy.structure_gel.api.registry.registrar.StructureRegistrar;

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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

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
		protected static Advancement findTower, findReanimatedRuins, findLeviathan, findSnowyTemple, findWarpedGarden;
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
			findTower = this.inAnyStructure(this.builder(Blocks.MOSSY_STONE_BRICKS, "find_tower", FrameType.TASK).parent(adventureRoot), DPStructures.TOWER).save(con, this.locate("find_tower"));
			findReanimatedRuins = this.inAnyStructure(this.builder(Blocks.MOSSY_COBBLESTONE, "find_reanimated_ruins", FrameType.TASK).parent(findTower), DPStructures.REANIMATED_RUINS).save(con, this.locate("find_reanimated_ruins"));
			findLeviathan = this.inAnyStructure(this.builder(Blocks.BONE_BLOCK, "find_leviathan", FrameType.GOAL).parent(findReanimatedRuins), DPStructures.LEVIATHAN).save(con, this.locate("find_leviathan"));
			findSnowyTemple = this.inAnyStructure(this.builder(Blocks.PACKED_ICE, "find_snowy_temple", FrameType.GOAL).parent(findReanimatedRuins), DPStructures.SNOWY_TEMPLE).save(con, this.locate("find_snowy_temple"));
			findWarpedGarden = this.inAnyStructure(this.builder(Blocks.WARPED_FUNGUS, "find_warped_garden", FrameType.TASK).parent(findReanimatedRuins), DPStructures.WARPED_GARDEN).save(con, this.locate("find_warped_garden"));

			this.setSection("nether");
			findSoulPrison = this.inAnyStructure(this.builder(Blocks.SPAWNER, "find_soul_prison", FrameType.GOAL).parent(netherRoot), DPStructures.SOUL_PRISON).save(con, this.locate("find_soul_prison"));

			this.setSection("end");
			findEndRuins = this.inAnyStructure(this.builder(Blocks.END_STONE_BRICKS, "find_end_ruins", FrameType.GOAL).parent(endEnterGateway), DPStructures.END_RUINS).save(con, this.locate("find_end_ruins"));

			// TODO add advancements
		}

		private TranslatableComponent translate(String key)
		{
			return new TranslatableComponent(DungeonsPlus.MODID + ":advancements" + (section.equals("") ? "" : "." + section) + "." + key);
		}

		private void setSection(String name)
		{
			this.section = name;
		}

		private LocationTrigger.TriggerInstance inStructure(ResourceKey<ConfiguredStructureFeature<?, ?>> structure)
		{
			return LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(structure));
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private Advancement.Builder inAnyStructure(Advancement.Builder builder, StructureRegistrar<?, ?> structure)
		{
			structure.getConfiguredStructures().forEach((name, holder) ->
			{
				builder.addCriterion("in_" + (name.isEmpty() ? "structure" : name), this.inStructure((ResourceKey) holder.unwrapKey().get()));
			});
			builder.requirements(RequirementsStrategy.OR);
			return builder;
		}

		private String locate(String key)
		{
			return DungeonsPlus.locate((section.equals("") ? "" : section + "/") + key).toString();
		}

		private Advancement.Builder builder(ItemLike displayItem, String name, ResourceLocation background, FrameType frameType, boolean showToast, boolean announceToChat, boolean hidden)
		{
			return Advancement.Builder.advancement().display(displayItem, translate(name + ".title"), translate(name + ".description"), background, frameType, showToast, announceToChat, hidden);
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
