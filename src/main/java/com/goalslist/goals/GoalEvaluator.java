package com.goalslist.goals;

public class GoalEvaluator
{
	public boolean isComplete(Goal goal)
	{
		return goal.getCurrentValue() >= goal.getTargetValue();
	}
}
