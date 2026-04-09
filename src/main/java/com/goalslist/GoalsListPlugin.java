package com.goalslist;

import com.google.inject.Provides;
import com.goalslist.events.GoalCompletedNotifier;
import com.goalslist.goals.GoalEvaluator;
import com.goalslist.goals.GoalTracker;
import com.goalslist.persistence.GoalRepository;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Goals List",
	description = "Track goals and mark them complete automatically."
)
public class GoalsListPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private GoalsListConfig config;

	private GoalTracker goalTracker;

	@Override
	protected void startUp()
	{
		goalTracker = new GoalTracker(
			new GoalEvaluator(),
			new GoalRepository(config),
			new GoalCompletedNotifier(client, config)
		);
		goalTracker.loadGoals();
		log.debug("Goals List started");
	}

	@Override
	protected void shutDown()
	{
		if (goalTracker != null)
		{
			goalTracker.clear();
		}

		log.debug("Goals List stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN && goalTracker != null)
		{
			goalTracker.loadGoals();
			log.debug("Reloaded goal definitions after login");
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		if (goalTracker == null)
		{
			return;
		}

		Skill skill = statChanged.getSkill();
		goalTracker.updateSkillGoals(skill, client.getRealSkillLevel(skill));
	}

	@Provides
	GoalsListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GoalsListConfig.class);
	}
}
