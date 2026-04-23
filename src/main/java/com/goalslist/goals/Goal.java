package com.goalslist.goals;

import lombok.Setter;

import java.util.Objects;

public class Goal
{
	private final String id;
	private final String title;
	private final GoalType type;
	private String targetKey;
	private int targetValue;
	@Setter
    private int currentValue;
	private GoalStatus status;

	public Goal(String id, String title, GoalType type, String targetKey, int targetValue)
	{
		this(id, title, type, targetKey, targetValue, 0, GoalStatus.ACTIVE);
	}

	public Goal(String id, String title, GoalType type, String targetKey, int targetValue, int currentValue, GoalStatus status)
	{
		this.id = Objects.requireNonNull(id, "id");
		this.title = Objects.requireNonNull(title, "title");
		this.type = Objects.requireNonNull(type, "type");
		this.targetKey = Objects.requireNonNull(targetKey, "targetKey");
		this.targetValue = targetValue;
		this.currentValue = currentValue;
		this.status = Objects.requireNonNull(status, "status");
	}
	public Goal(String id, String title, GoalType type, GoalStatus status)
	{
		this.id = Objects.requireNonNull(id, "id");
		this.title = Objects.requireNonNull(title, "title");
		this.type = Objects.requireNonNull(type, "type");
		this.status = Objects.requireNonNull(status, "status");
	}
	public Goal(String id, String title, GoalType type)
	{
		this.id = Objects.requireNonNull(id, "id");
		this.title = Objects.requireNonNull(title, "title");
		this.type = Objects.requireNonNull(type, "type");
		this.status = GoalStatus.ACTIVE;
	}

	public String getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public GoalType getType()
	{
		return type;
	}

	public String getTargetKey()
	{
		return targetKey;
	}

	public int getTargetValue()
	{
		return targetValue;
	}

	public int getCurrentValue()
	{
		return currentValue;
	}

    public GoalStatus getStatus()
	{
		return status;
	}

	public void setStatus(GoalStatus status)
	{
		this.status = Objects.requireNonNull(status, "status");
	}

	@Override
	public String toString()
	{
		return "Goal{" +
			"id='" + id + '\'' +
			", title='" + title + '\'' +
			", type=" + type +
			", targetKey='" + targetKey + '\'' +
			", targetValue=" + targetValue +
			", currentValue=" + currentValue +
			", status=" + status +
			'}';
	}
}
