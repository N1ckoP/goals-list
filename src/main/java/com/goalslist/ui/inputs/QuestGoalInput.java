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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
public abstract class QuestGoalInput extends JPanel
{
    protected final GoalsListPlugin plugin;
    private final Goal goal;
    protected final JComboBox<String> skillDropdown;
    protected Consumer<Goal> submitListener;
    @Getter
    private final JPanel inputRow;
    protected QuestGoalInput(GoalsListPlugin plugin, Goal goal, String title) {

    super(new GridBagLayout());
        this.plugin = plugin;
        this.goal = goal;
        inputRow = new JPanel(new BorderLayout());
        inputRow.setBackground(ColorScheme.DARKER_GRAY_COLOR);

    JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        fieldsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

    skillDropdown = new JComboBox<>(Arrays.stream(Quest.values())
        .map(Enum::name)
            .toArray(String[]::new));
        fieldsPanel.add(skillDropdown);


        inputRow.add(fieldsPanel, BorderLayout.CENTER);
        if (showAddButton()) {
            TextButton addButton = new TextButton("Add");
            addButton.onClick(e -> submit());
            inputRow.add(addButton, BorderLayout.EAST);
        }

        add(inputRow);
    }
    protected boolean showAddButton()
    {
        return true;
    }
    protected void submit()
    {
        String quest = (String) skillDropdown.getSelectedItem();
        GoalStatus status = GoalStatus.ACTIVE;
       /* if(levelInput <= currentLevel) {
            status = GoalStatus.COMPLETED;
        }*/
        Goal goal = new Goal(
                "draft",
                quest,
                GoalType.QUEST,
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
