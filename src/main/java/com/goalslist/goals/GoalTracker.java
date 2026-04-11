package com.goalslist.goals;

import com.goalslist.events.GoalCompletedNotifier;
import com.goalslist.storage.GoalRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.runelite.api.Skill;

public class GoalTracker
{
	private final GoalEvaluator goalEvaluator;
	private final GoalRepository goalRepository;
	private final GoalCompletedNotifier goalCompletedNotifier;
	private final List<Goal> goals = new ArrayList<>();

	public GoalTracker(GoalEvaluator goalEvaluator, GoalRepository goalRepository, GoalCompletedNotifier goalCompletedNotifier)
	{
		this.goalEvaluator = goalEvaluator;
		this.goalRepository = goalRepository;
		this.goalCompletedNotifier = goalCompletedNotifier;
	}

	public void loadGoals()
	{
		goals.clear();
		goals.addAll(goalRepository.loadGoals());
	}

	public List<Goal> getGoals()
	{
		return Collections.unmodifiableList(goals);
	}

	public void updateSkillGoals(Skill skill, int currentLevel)
	{
		boolean changed = false;

		for (Goal goal : goals)
		{
			if (goal.getType() != GoalType.SKILL)
			{
				continue;
			}

			if (!goal.getTargetKey().equalsIgnoreCase(skill.name()))
			{
				continue;
			}

			goal.setCurrentValue(currentLevel);

			if (goal.getStatus() == GoalStatus.ACTIVE && goalEvaluator.isComplete(goal))
			{
				goal.setStatus(GoalStatus.COMPLETED);
				goalCompletedNotifier.notifyGoalCompleted(goal);
			}

			changed = true;
		}

		if (changed)
		{
			goalRepository.saveGoals(goals);
		}
	}

	public void clear()
	{
		goals.clear();
	}

	public void addGoal(Goal goal)
	{
		goals.add(goal);
		goalRepository.saveGoals(goals);
	}
}
