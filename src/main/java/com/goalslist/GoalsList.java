package com.goalslist;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.api.Client;
import javax.inject.Inject;

@ConfigGroup("example")
public interface GoalsList extends Config
{
	@ConfigItem(
		keyName = "greeting",
		name = "Attack level test",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}
}
