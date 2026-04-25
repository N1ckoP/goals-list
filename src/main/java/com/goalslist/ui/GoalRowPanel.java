package com.goalslist.ui;

import com.goalslist.goals.Goal;
import com.goalslist.goals.GoalStatus;
import com.goalslist.goals.GoalType;
import com.goalslist.ui.components.TextButton;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;

public class GoalRowPanel extends JPanel
{
	protected Consumer<Goal> deleteListener;

	public GoalRowPanel(Goal goal, Consumer<Goal> deleteListener)
	{
		this.deleteListener = deleteListener;

		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
		setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel goalLabel = new JLabel(goal.getTitle());
		String description = goal.getType() == GoalType.QUEST
			? (goal.getStatus() == GoalStatus.COMPLETED ? "Completed" : "Active")
			: goal.getCurrentValue() + " / " + goal.getTargetValue();
		JLabel descriptionLabel = new JLabel(description);
		if (goal.getStatus() == GoalStatus.COMPLETED)
		{
			descriptionLabel.setForeground(Color.GREEN);
			goalLabel.setForeground(Color.GREEN);
		}
		else
		{
			descriptionLabel.setForeground(Color.YELLOW);
			goalLabel.setForeground(Color.YELLOW);
		}

		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		textPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textPanel.add(goalLabel);
		textPanel.add(descriptionLabel);
		add(textPanel, BorderLayout.CENTER);

		TextButton deleteButton = new TextButton("Delete").setMainColor(Color.RED);
		deleteButton.onClick(e -> deleteListener.accept(goal));
		add(deleteButton, BorderLayout.EAST);

		Dimension preferredSize = getPreferredSize();
		setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
	}

}
