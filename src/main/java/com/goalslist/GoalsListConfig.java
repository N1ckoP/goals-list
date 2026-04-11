package com.goalslist;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import java.awt.*;

@ConfigGroup("goalslist")
public interface GoalsListConfig extends Config
{
	@ConfigItem(
		keyName = "goalsData",
		name = "Goals data",
		description = "Serialized goal definitions for future parsing and storage.",
		hidden = true
	)
	default String goalsData()
	{
		return "";
	}

	@ConfigItem(
		keyName = "sendChatNotification",
		name = "Chat notifications",
		description = "Send a chat message when a goal is completed."
	)
	default boolean sendChatNotification()
	{
		return true;
	}
}
