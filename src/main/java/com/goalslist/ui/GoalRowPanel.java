package com.goalslist.ui;

import com.goalslist.goals.Goal;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GoalRowPanel extends JPanel
{
	public GoalRowPanel(Goal goal)
	{
		setLayout(new BorderLayout());
		add(new JLabel(goal.getTitle()), BorderLayout.WEST);
		add(new JLabel(goal.getCurrentValue() + " / " + goal.getTargetValue()), BorderLayout.EAST);
	} // UI FOR GOALS ADDED need to work on / WORK ON DELETE BUTTON?
}
