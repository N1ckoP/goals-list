package com.goalslist.ui.inputs;

import com.goalslist.GoalsListPlugin;
import com.goalslist.goals.Goal;
import com.goalslist.goals.GoalStatus;
import com.goalslist.goals.GoalType;
import com.goalslist.ui.components.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.client.ui.ColorScheme;
public abstract class QuestGoalInput extends JPanel
{
    protected final GoalsListPlugin plugin;
    private final Goal goal;
    protected final JComboBox<String> questDropdown;
    protected Consumer<Goal> submitListener;
    @Getter
    private final JPanel inputRow;
    protected QuestGoalInput(GoalsListPlugin plugin, Goal goal, String title)
    {
        super(new BorderLayout(0, 6));
        this.plugin = plugin;
        this.goal = goal;
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        inputRow = new JPanel(new GridBagLayout());
        inputRow.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        questDropdown = new JComboBox<>(Arrays.stream(Quest.values())
            .map(Enum::name)
            .toArray(String[]::new));
        questDropdown.setSelectedItem(goal.getTargetKey());
        questDropdown.setPrototypeDisplayValue("A_TAIL_OF_TWO_CATS");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 6);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputRow.add(questDropdown, constraints);

        if (showAddButton())
        {
            TextButton addButton = new TextButton("Add");
            addButton.onClick(e -> submit());
            constraints.gridx = 1;
            constraints.weightx = 0;
            constraints.fill = GridBagConstraints.NONE;
            constraints.insets = new Insets(0, 0, 0, 0);
            inputRow.add(addButton, constraints);
        }

        add(inputRow, BorderLayout.CENTER);

        Dimension preferredSize = getPreferredSize();
        setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
    }
    protected boolean showAddButton()
    {
        return true;
    }
    protected void submit()
    {
        String quest = (String) questDropdown.getSelectedItem();
        GoalStatus status = GoalStatus.ACTIVE;
        Goal goal = new Goal(
                "draft",
                quest,
                GoalType.QUEST,
                quest,
                1,
                0,
                status
        );
        if (submitListener != null) {
            submitListener.accept(goal);
        }
    }

    public QuestGoalInput onSubmit(Consumer<Goal> listener)
    {
        this.submitListener = listener;
        return this;
    }

}
