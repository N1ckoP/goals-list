package com.goalslist;

import com.goalslist.goals.Goal;
import com.google.inject.Provides;
import com.goalslist.events.GoalCompletedNotifier;
import com.goalslist.goals.GoalEvaluator;
import com.goalslist.goals.GoalTracker;
import com.goalslist.goals.GoalType;
import com.goalslist.storage.GoalRepository;
import com.goalslist.ui.GoalsListPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(
	name = "Goals List",
	description = "Track goals and mark them complete automatically."
)
public class GoalsListPlugin extends Plugin
{
	@Inject
	@Getter
	private Client client;

	@Inject
	private GoalsListConfig config;
	@Inject
	private ConfigManager configManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ClientThread clientThread;

	private GoalTracker goalTracker;
	private GoalsListPanel goalsListPanel;
	private NavigationButton navigationButton;

	@Override
	protected void startUp()
	{
		goalTracker = new GoalTracker(
			new GoalEvaluator(),
			new GoalRepository(config,configManager),
			new GoalCompletedNotifier(client, config),
				client
		);
		goalsListPanel = new GoalsListPanel(this);
		goalTracker.loadGoals();
		goalsListPanel.refreshGoals(goalTracker.getGoals());
		scheduleQuestRefresh();

		navigationButton = NavigationButton.builder()
			.tooltip("Goals List")
			.icon(createNavigationIcon())
			.priority(5)
			.panel(goalsListPanel)
			.build();
		clientToolbar.addNavigation(navigationButton);
		log.debug("Goals List started");
	}

	@Override
	protected void shutDown()
	{
		if (navigationButton != null)
		{
			clientToolbar.removeNavigation(navigationButton);
			navigationButton = null;
		}

		if (goalTracker != null)
		{
			goalTracker.clear();
		}
		goalsListPanel.refreshGoals(goalTracker == null ? java.util.List.of() : goalTracker.getGoals());

		log.debug("Goals List stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN && goalTracker != null)
		{
			goalTracker.loadGoals();
			goalsListPanel.refreshGoals(goalTracker.getGoals());
			scheduleQuestRefresh();
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
		goalsListPanel.refreshGoals(goalTracker.getGoals());
	}

	@Provides
	GoalsListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GoalsListConfig.class);
	}

	private BufferedImage createNavigationIcon()
	{
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(0xF39C12));
		graphics.fillRoundRect(1, 1, 14, 14, 4, 4);
		graphics.setColor(new Color(0x2B2B2B));
		graphics.fillRect(4, 4, 8, 1);
		graphics.fillRect(4, 7, 5, 1);
		graphics.setColor(Color.WHITE);
		graphics.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graphics.drawLine(5, 11, 7, 13);
		graphics.drawLine(7, 13, 11, 9);
		graphics.dispose();
		return image;
	}
	public void addGoalBridge(Goal goal)
	{
		goalTracker.addGoal(goal);
		if (goal.getType() == GoalType.QUEST && client.getGameState() == GameState.LOGGED_IN)
		{
			scheduleQuestRefresh();
		}
		goalsListPanel.refreshGoals(goalTracker.getGoals());
	}
	public void  removeGoalBridge(Goal goal)
	{
		goalTracker.removeGoal(goal);
		goalsListPanel.refreshGoals(goalTracker.getGoals());
	}

	private void scheduleQuestRefresh()
	{
		clientThread.invokeLater(() ->
		{
			refreshQuestGoals();
			goalsListPanel.refreshGoals(goalTracker.getGoals());
		});
	}

	private void refreshQuestGoals()
	{
		for (Goal goal : goalTracker.getGoals())
		{
			if (goal.getType() != GoalType.QUEST)
			{
				continue;
			}

			try
			{
				goalTracker.updateQuestGoals(Quest.valueOf(goal.getTargetKey()));
			}
			catch (IllegalArgumentException ex)
			{
				log.debug("Skipping invalid quest goal target {}", goal.getTargetKey(), ex);
			}
		}
	}
}
