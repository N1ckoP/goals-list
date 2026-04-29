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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import lombok.Getter;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;

public abstract class SkillGoalInput extends JPanel
{
    protected final GoalsListPlugin plugin;
    private final Goal goal;
    protected final JComboBox<String> skillDropdown;
    protected final JTextField targetValueField;
    protected Consumer<Goal> submitListener;
    @Getter
    private final JPanel inputRow;

    protected SkillGoalInput(GoalsListPlugin plugin, Goal goal, String title)
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

        skillDropdown = new JComboBox<>(Arrays.stream(Skill.values())
            .map(Enum::name)
            .toArray(String[]::new));
        skillDropdown.setSelectedItem(goal.getTargetKey());
        skillDropdown.setPrototypeDisplayValue("WOODCUTTING");

        targetValueField = new JTextField(String.valueOf(goal.getTargetValue()), 3);
        ((AbstractDocument) targetValueField.getDocument()).setDocumentFilter(new LevelDocumentFilter());
        targetValueField.setHorizontalAlignment(JTextField.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 0, 6);
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        inputRow.add(skillDropdown, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.NONE;
        inputRow.add(targetValueField, constraints);

        if (showAddButton()) {
            TextButton addButton = new TextButton("Add");
            addButton.onClick(e -> submit());
            constraints.gridx = 2;
            constraints.insets = new Insets(0, 0, 0, 0);
            inputRow.add(addButton, constraints);
        }

        add(inputRow, BorderLayout.CENTER);

        Dimension preferredSize = getPreferredSize();
        setMaximumSize(new Dimension(Integer.MAX_VALUE, preferredSize.height));
    }

    public SkillGoalInput onSubmit(Consumer<Goal> listener)
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

    static final class LevelDocumentFilter extends DocumentFilter
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
