package com.goalslist.persistence;

import com.goalslist.GoalsListConfig;
import com.goalslist.goals.Goal;
import java.util.ArrayList;
import java.util.List;

public class GoalRepository
{
	private final GoalsListConfig config;

	public GoalRepository(GoalsListConfig config)
	{
		this.config = config;
	}

	public List<Goal> loadGoals()
	{
		String goalsData = config.goalsData().trim();

		if (goalsData.isEmpty())
		{
			return new ArrayList<>();
		}

		// TODO: Parse stored goal definitions once the editing format is finalized.
		return new ArrayList<>();
	}

	public void saveGoals(List<Goal> goals)
	{
		// TODO: Serialize goals back into config storage after the data format is defined.
	}
}
