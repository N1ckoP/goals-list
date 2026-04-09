package com.goalslist.ui;

import com.goalslist.goals.Goal;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GoalsListPanel extends JPanel
{
	public GoalsListPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	public void refreshGoals(List<Goal> goals)
	{
		removeAll();

		if (goals.isEmpty())
		{
			add(new JLabel("No goals configured yet."));
		}
		else
		{
			for (Goal goal : goals)
			{
				add(new GoalRowPanel(goal));
			}
		}

		revalidate();
		repaint();
	}
}
