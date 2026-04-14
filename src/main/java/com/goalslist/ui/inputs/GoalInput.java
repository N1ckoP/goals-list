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
import javax.swing.text.DocumentFilter;
import lombok.Getter;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;

public abstract class GoalInput extends JPanel
{
    protected final GoalsListPlugin plugin;
    private final Goal goal;
    protected final JComboBox<String> skillDropdown;
    protected final JTextField targetValueField;
    protected Consumer<Goal> submitListener;
    @Getter
    private final JPanel inputRow;

    protected GoalInput(GoalsListPlugin plugin, Goal goal, String title)
    {
        super(new GridBagLayout());
        this.plugin = plugin;
        this.goal = goal;
        inputRow = new JPanel(new BorderLayout());
        inputRow.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        fieldsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        skillDropdown = new JComboBox<>(Arrays.stream(Skill.values())
            .map(Enum::name)
            .toArray(String[]::new));
        skillDropdown.setSelectedItem(goal.getTargetKey());
        fieldsPanel.add(skillDropdown);

        targetValueField = new JTextField(String.valueOf(goal.getTargetValue()), 3);
        ((AbstractDocument) targetValueField.getDocument()).setDocumentFilter(new LevelDocumentFilter());
        fieldsPanel.add(targetValueField);

        inputRow.add(fieldsPanel, BorderLayout.CENTER);

        if (showAddButton()) {
            TextButton addButton = new TextButton("Add");
            addButton.onClick(e -> submit());
            inputRow.add(addButton, BorderLayout.EAST);
        }

        add(inputRow);
    }

    public GoalInput onSubmit(Consumer<Goal> listener)
    {
        this.submitListener = listener;
        return this;
    }

    protected void submit()
    {
        String skill = (String) skillDropdown.getSelectedItem();
        String levelText = targetValueField.getText().trim();
        if (levelText.isEmpty())
        {
            return;
        }

        int levelInput = Integer.parseInt(levelText);
        int currentLevel = plugin.getClient().getRealSkillLevel(Skill.valueOf(skill));
        GoalStatus status = GoalStatus.ACTIVE;
        if(levelInput <= currentLevel) {
            status = GoalStatus.COMPLETED;
        }
        Goal goal = new Goal(
                "draft",
                skill,
                GoalType.SKILL,
                skill,
                levelInput,
                currentLevel,
                status
        );
        if (submitListener != null) {
            submitListener.accept(goal);
        }
    }

    protected boolean showAddButton()
    {
        return true;
    }

    private static final class LevelDocumentFilter extends DocumentFilter
    {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException
        {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
        {
            String current = fb.getDocument().getText(0, fb.getDocument().getLength());
            String next = current.substring(0, offset) + (text == null ? "" : text) + current.substring(offset + length);

            if (next.isEmpty())
            {
                fb.replace(offset, length, text, attrs);
                return;
            }

            if (!next.chars().allMatch(Character::isDigit))
            {
                return;
            }

            int value = Integer.parseInt(next);
            if (value < 1 || value > 99)
            {
                return;
            }

            fb.replace(offset, length, text, attrs);
        }
    }
}
