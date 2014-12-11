package com.cpsc481.mytasks.customclasses;

public class Task 
{
	private String name;
	private String difficulty;
	private int startDate;
	private int dueDate;
	private int startTime;
	private int dueTime;

	
	public Task(String name, String difficulty, int endDate, int endTime)
	{
		setName(name);
		setDifficulty(difficulty);
		
		setDueDate(endDate);
		setDueTime(endTime);
		
	}
	
	public Task(String name, String difficulty, int startDate, int endDate, int startTime, int endTime)
	{
		setName(name);
		setDifficulty(difficulty);
		setStartDate(startDate);
		setDueDate(endDate);
		setStartTime(startTime);
		setDueTime(endTime);
	}
	
	public String toString()
	{
		return name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the difficulty
	 */
	public String getDifficulty() {
		return difficulty;
	}

	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * @return the startDate
	 */
	public int getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the dueDate
	 */
	public int getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(int endDate) {
		this.dueDate = endDate;
	}

	/**
	 * @return the startTime
	 */
	public int getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the dueTime
	 */
	public int getDueTime() {
		return dueTime;
	}

	/**
	 * @param dueTime the dueTime to set
	 */
	public void setDueTime(int endTime) {
		this.dueTime = endTime;
	}

}
