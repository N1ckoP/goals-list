package com.goalslist.storage;

import com.goalslist.GoalsListConfig;
import com.goalslist.goals.Goal;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Serializer;

public class GoalRepository
{
	private final GoalsListConfig config;
	private final ConfigManager configManager;
	private final Gson serializer = new Gson();
	public GoalRepository(GoalsListConfig config,  ConfigManager configManager)
	{
		this.config = config;
		this.configManager = configManager;
	}

	public List<Goal> loadGoals()
	{
		String goalsData = config.goalsData().trim();
		if (goalsData.isEmpty())
		{
			return new ArrayList<>();
		}
		try
		{
			List<Goal> goalsMemory = serializer.fromJson(goalsData, TypeToken.getParameterized(List.class, Goal.class).getType());
			if (goalsMemory == null)
			{
				return new ArrayList<>();
			}
			return goalsMemory;
		}
		catch (RuntimeException ex)
		{
			return new ArrayList<>();
		}
	}

	public void saveGoals(List<Goal> goals)
	{
		String jsonString = serializer.toJson(goals);
		configManager.setConfiguration("goalslist","goalsData",jsonString);
	}
}
