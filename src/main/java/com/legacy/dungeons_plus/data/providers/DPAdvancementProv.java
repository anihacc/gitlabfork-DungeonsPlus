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

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

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

	private class DungeonsPlusAdvancements implements Consumer<Consumer<Advancement>>
	{
		private String section = "";

		@Override
		public void accept(Consumer<Advancement> consumer)
		{
			this.setSection("adventure");
		}

		private TranslatableComponent translate(String key)
		{
			return new TranslatableComponent("advancements." + DungeonsPlus.MODID + (section.equals("") ? "" : "." + section) + "." + key);
		}

		private void setSection(String name)
		{
			this.section = name;
		}

		private Advancement.Builder builder(ItemLike displayItem, String name, ResourceLocation background, FrameType frameType, boolean showToast, boolean announceToChat, boolean hidden)
		{
			return Advancement.Builder.advancement().display(displayItem, translate(name), translate(name + ".desc"), background, frameType, showToast, announceToChat, hidden);
		}

		private Advancement.Builder builder(ItemLike displayItem, String name, FrameType frameType, boolean showToast, boolean announceToChat, boolean hidden)
		{
			return builder(displayItem, name, (ResourceLocation) null, frameType, showToast, announceToChat, hidden);
		}
	}
}
