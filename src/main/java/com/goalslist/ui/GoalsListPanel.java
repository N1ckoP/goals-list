package com.goalslist.ui;

import com.goalslist.GoalsListPlugin;
import com.goalslist.goals.Goal;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class GoalsListPanel extends PluginPanel
{
	private final GoalsListPlugin plugin;

	public GoalsListPanel(GoalsListPlugin plugin)
	{
		super(false);
		this.plugin = plugin;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
	}

	public void refreshGoals(List<Goal> goals)
	{
		removeAll();
		add(new AddGoalPanel(plugin));

		if (goals.isEmpty())
		{
			add(new JLabel("No goals configured yet."));
		}
		else
		{
			for (Goal goal : goals)
			{
				add(new GoalRowPanel(goal, plugin::removeGoalBridge));
			}
		}

		revalidate();
		repaint();
	}
}
