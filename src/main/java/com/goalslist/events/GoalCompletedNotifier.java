package com.goalslist.events;

import com.goalslist.GoalsListConfig;
import com.goalslist.goals.Goal;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;

public class GoalCompletedNotifier
{
	private final Client client;
	private final GoalsListConfig config;

	public GoalCompletedNotifier(Client client, GoalsListConfig config)
	{
		this.client = client;
		this.config = config;
	}

	public void notifyGoalCompleted(Goal goal)
	{
		if (client == null || !config.sendChatNotification())
		{
			return;
		}

		client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Goal completed: " + goal.getTitle(), null);
	}
}
