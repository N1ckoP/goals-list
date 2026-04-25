package com.goalslist.ui;

import com.goalslist.GoalsListPlugin;
import com.goalslist.goals.Goal;
import com.goalslist.goals.GoalType;
import com.goalslist.ui.inputs.QuestGoalInput;
import com.goalslist.ui.inputs.SkillGoalInput;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;

@Slf4j
public class AddGoalPanel extends JPanel
{
	public AddGoalPanel(GoalsListPlugin plugin)
	{
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		Goal draftGoal = new Goal("draft", "New Goal", GoalType.SKILL, "ATTACK", 1);
		Goal draftGoalQuest = new Goal("draft", "New Goal", GoalType.QUEST);

		add(new SkillGoalInput(plugin, draftGoal, "Add Skill Goal")
		{

		}.onSubmit(plugin::addGoalBridge));
		add(Box.createRigidArea(new Dimension(0, 8)));
		add(new QuestGoalInput(plugin, draftGoalQuest, "Add Quest Goal")
		{

		}.onSubmit(plugin::addGoalBridge));

		Dimension preferredSize = getPreferredSize();
		setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
	}
}
