package com.goalslist.ui;

import com.goalslist.GoalsListPlugin;
import com.goalslist.goals.Goal;
import com.goalslist.goals.GoalType;
import com.goalslist.ui.inputs.GoalInput;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;

@Slf4j
public class AddGoalPanel extends JPanel
{
	public AddGoalPanel(GoalsListPlugin plugin)
	{
		super(new GridBagLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		Goal draftGoal = new Goal("draft", "New Goal", GoalType.SKILL, "ATTACK", 1);

		add(new GoalInput(plugin, draftGoal, "Goal")
		{

		}.onSubmit(plugin::addGoalBridge), constraints);
	}
}
