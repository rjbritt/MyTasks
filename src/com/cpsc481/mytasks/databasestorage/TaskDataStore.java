package com.cpsc481.mytasks.databasestorage;


import java.util.Calendar;

import com.cpsc481.mytasks.customclasses.DateAndTime;
import com.cpsc481.mytasks.customclasses.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This is the datastore class that stores all the methods for manipulating the database, such as
 * adding and removing items from the database. 
 * @author rjbritt
 *
 */
public class TaskDataStore
{	
	private static final String LOG_TAG = "TaskDataStore log"; 
	 
	public static TaskDatabaseHelper databaseHelper;
	private static SQLiteDatabase taskDB;
	private final Context currentContext;
	
	public TaskDataStore(Context ctx)
	{
		currentContext = ctx;
	}
	
	public TaskDataStore open() throws SQLException
	{
		if(databaseHelper == null)
		{
			Log.v(LOG_TAG, "Creating a new database Helper");
			databaseHelper = new TaskDatabaseHelper(currentContext);
		}
		
		taskDB = databaseHelper.getWritableDatabase();
		return(this);
	}
	
	
	/**
	 * Adds a new task to the database. 
	 * 
	 * @param name The name of the Task
	 * @param duedate The task's duedate
	 * @param duetime The task's duetime
	 * @param difficulty The task's difficulty
	 */
	public void addNewTask(String name, int duedate, int duetime, String difficulty)
	{	
		/*
		 * Create new content values with the new task name in it
		 * if there isn't a TaskName entry with that name, create the TaskName, then get it's _id reference to link to it.
		 * if there is a TaskName entry with that name, use the index of that name
    	 */
		ContentValues newTaskNameTableValue = new ContentValues();
		newTaskNameTableValue.put(TaskDatabaseHelper.TASKNAME_NAME, name);
		
		long taskID;
		try
		{
			Log.v(LOG_TAG, "Inserted new task name correctly.");
			taskID = taskDB.insertOrThrow(TaskDatabaseHelper.TASKNAME_TABLE, null, newTaskNameTableValue);
		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// TaskName table because it has the unique modifier on it. Now, we must find the id of that taskname
	
			Log.v(LOG_TAG, "Error inserting task name, " + sqle.getLocalizedMessage());
			taskID = getTaskNameID(name);

		};
		
		/*
		 * Create new content values with the new due date in it
		 * if there isn't a DueDate entry with that name, create the DueDate, then get it's _id reference to link to it.
		 * if there is a DueDate entry with that date, use the index of that date
    	 */
		ContentValues newDueDateTableValue = new ContentValues();
		newDueDateTableValue.put(TaskDatabaseHelper.DUEDATE_DATE, duedate);
		
		long dueDateID;
		try
		{
			Log.v(LOG_TAG, "Inserted new due date correctly.");
			dueDateID = taskDB.insertOrThrow(TaskDatabaseHelper.DUEDATE_TABLE, null, newDueDateTableValue);

		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// duedate table because it has the unique modifier on it. Now, we must find the id of that duedate
			Log.v(LOG_TAG, "Error inserting due date, " + sqle.getLocalizedMessage());
			dueDateID = getDueDateID(duedate);
			
		}
		
		/*
		 * The difficulty table will never be added to by the android app.Therefore, 
		 * all that is needed is to find the index of the current difficulty.		  
    	 */
		long difficultyID = getDifficultyID(difficulty);
		
		/*
		 * The start/end date/time will always be -1 at this point, but ids will be gotten from the database  
    	 */
		long startDateID = getStartDateID(-1);
		long startTimeID = getStartTimeID(-1);
		long endDateID = getEndDateID(-1);
		long endTimeID = getEndTimeID(-1);
		
		/*
		 * Create new content values with the new due time in it
		 * if there isn't a DueTime entry with that name, create the DueTime, then get it's _id reference to link to it.
		 * if there is a DueTime entry with that time, use the index of that time
    	 */
		ContentValues newDueTimeTableValue = new ContentValues();
		newDueTimeTableValue.put(TaskDatabaseHelper.DUETIME_TIME, duetime);
		
		long dueTimeID;
		try
		{
			Log.v(LOG_TAG, "Inserted new due time correctly.");
			dueTimeID = taskDB.insertOrThrow(TaskDatabaseHelper.DUETIME_TABLE, null, newDueTimeTableValue);

		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// dueto,e table because it has the unique modifier on it. Now, we must find the id of that duetime
			Log.v(LOG_TAG, "Error inserting due time, " + sqle.getLocalizedMessage());
			dueTimeID = getDueTimeID(duetime);			
		}
		
		/******************* Add a new set to the master table ******************************/
		ContentValues masterValues = new ContentValues();
		masterValues.put(TaskDatabaseHelper.TASK_TASKNAME, taskID);
		masterValues.put(TaskDatabaseHelper.TASK_DUEDATE, dueDateID);
		masterValues.put(TaskDatabaseHelper.TASK_DUETIME, dueTimeID);
		masterValues.put(TaskDatabaseHelper.TASK_DIFFICULTY, difficultyID);
		masterValues.put(TaskDatabaseHelper.TASK_STARTDATE, startDateID);
		masterValues.put(TaskDatabaseHelper.TASK_STARTTIME, startTimeID);
		masterValues.put(TaskDatabaseHelper.TASK_ENDDATE, endDateID);
		masterValues.put(TaskDatabaseHelper.TASK_ENDTIME, endTimeID);
		masterValues.put(TaskDatabaseHelper.TASK_COMPLETE_FLAG, TaskDatabaseHelper.NO);
		masterValues.put(TaskDatabaseHelper.TASK_COMPLETE_ON_TIME_FLAG, TaskDatabaseHelper.NO);
		
		try
		{
			taskDB.insertOrThrow(TaskDatabaseHelper.TASK_TABLE, null, masterValues);
		}
		catch(SQLException sqle)
		{			
			Log.v(LOG_TAG, "Something went wrong with inserting the task into the database");
		}
	}
	
	/**
	 * This sets the endDate/time entry in the main table and creates a new entry in the endDate/Time tables as needed.
	 * It also sets the onComplete flag to YES and the completeOnTime flag to whether or not the task was completed on time.
	 * @param id
	 */
	public void completeTask(long id)
	{
		Calendar currentCal = Calendar.getInstance();
		int currentTime = DateAndTime.combineTime(currentCal.get(Calendar.HOUR_OF_DAY), currentCal.get(Calendar.MINUTE));
		int currentDate = DateAndTime.combineDate(currentCal.get(Calendar.MONTH), 
				currentCal.get(Calendar.DAY_OF_MONTH), currentCal.get(Calendar.YEAR));
		
		
		ContentValues endDateContent = new ContentValues();
		ContentValues endTimeContent = new ContentValues();
		
		ContentValues allCompleteContent = new ContentValues();
		ContentValues completeOnTimeContent = new ContentValues();
		
		endDateContent.put(TaskDatabaseHelper.ENDDATE_DATE, currentDate);
		endTimeContent.put(TaskDatabaseHelper.ENDTIME_TIME, currentTime);		
		
		//check and get the id for the end date
		long endDateID;
		try
		{
			Log.v(LOG_TAG, "Inserted new end date correctly.");
			endDateID = taskDB.insertOrThrow(TaskDatabaseHelper.ENDDATE_TABLE, null, endDateContent);

		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// EndDate table because it has the unique modifier on it. Now, we must find the id of that enddate
			Log.v(LOG_TAG, "Error inserting end date, " + sqle.getLocalizedMessage());
			endDateID = getEndDateID(currentDate);
			
		}
		
		//check and get the id for the end time
		long endTimeID;
		try
		{
			Log.v(LOG_TAG, "Inserted new end date correctly.");
			endTimeID = taskDB.insertOrThrow(TaskDatabaseHelper.ENDTIME_TABLE, null, endTimeContent);

		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// EndDate table because it has the unique modifier on it. Now, we must find the id of that enddate
			endTimeID = getEndTimeID(currentTime);
			
		}
		
		//Determine if the task has been completed on time and if so, mark the complete on time flag
		Cursor c = findSelectForTask(id);
		c.moveToFirst();
		
		int dueDate = c.getInt(c.getColumnIndex(TaskDatabaseHelper.DUEDATE_DATE));
		int dueTime = c.getInt(c.getColumnIndex(TaskDatabaseHelper.DUETIME_TIME));
		
		int onTime = TaskDatabaseHelper.NO;
		if(dueDate >= currentDate && dueTime >= currentTime)
		{
			onTime = TaskDatabaseHelper.YES;
		} 
		
		allCompleteContent.put(TaskDatabaseHelper.TASK_COMPLETE_FLAG, TaskDatabaseHelper.YES);
		allCompleteContent.put(TaskDatabaseHelper.TASK_ENDDATE, endDateID);
		allCompleteContent.put(TaskDatabaseHelper.TASK_ENDTIME, endTimeID);
		allCompleteContent.put(TaskDatabaseHelper.TASK_COMPLETE_ON_TIME_FLAG, onTime);
		
		
		taskDB.update(TaskDatabaseHelper.TASK_TABLE, allCompleteContent, "_id = ?", new String[]{""+ id});		
	}
	
	/**
	 * This method returns  a cursor pointing to a joined table that consists of the string or integer entries of the
	 * task name, start date, start time, end date, end time, due date, due time, difficulty, and completed flag
	 * for only the tasks that have been completed.
	 * @return The cursor mentioned above.
	 */
	public Cursor findAllTasks()
	{
		// return a cursor pointing to a query of all tasks with all columns returned in the main Task table
		return taskDB.rawQuery("select TaskName._id,TaskName.name,StartDate.date, StartTime.time, EndDate.date, EndTime.time," +
								"DueDate.date,DueTime.time,Difficulty.difficulty, Task.CompletedFlag " +
				"from Task " +
				"join TaskName on Task.TaskName = TaskName._id " +
				"join StartDate on Task.StartDate = StartDate._id " +
				"join StartTime on Task.StartTime = StartTime._id " +
				"join EndDate on Task.EndDate = EndDate._id " +
				"join EndTime on Task.EndTime = EndTime._id " +
				"join DueDate on Task.DueDate = DueDate._id " +
				"join DueTime on Task.DueTime = DueTime._id " +
				"join Difficulty on Task.Difficulty = Difficulty._id " +
				"where Task.CompletedFlag = 0", null);
	}
	
	/**
	 * Returns a cursor that points to the results of a query  for finding the task name, start date, start time,
	 * end date, end time, for a particular difficulty.
	 * 
	 * @param difficulty The difficulty that the list of tasks for is desired
	 * @return the cursor mentioned above.
	 */
	public static Cursor findTasksForDifficulty(String difficulty)
	{
		return taskDB.rawQuery("select TaskName._id,TaskName.name,StartDate.date, StartTime.time, EndDate.date, EndTime.time," +
								"Difficulty.difficulty, Task.CompletedFlag " +
				"from Task " +
				"join TaskName on Task.TaskName = TaskName._id " +
				"join StartDate on Task.StartDate = StartDate._id " +
				"join StartTime on Task.StartTime = StartTime._id " +
				"join EndDate on Task.EndDate = EndDate._id " +
				"join EndTime on Task.EndTime = EndTime._id " +
				"join DueDate on Task.DueDate = DueDate._id " +
				"join DueTime on Task.DueTime = DueTime._id " +
				"join Difficulty on Task.Difficulty = Difficulty._id " +
				"where Task.CompletedFlag = ? and Difficulty.difficulty = ?", new String [] {""+ TaskDatabaseHelper.YES,difficulty});		
	}
	
	/**
	 * returns a cursor pointing to select raw data results of the joined table that tells a particular tasks
	 * name, due date, due time, difficulty, and completed flag
	 * @param id The id of the task for which you want to find the select data
	 * @return the cursor mentioned above
	 */
	public Cursor findSelectForTask(long id)
	{
		return taskDB.rawQuery("select TaskName._id,TaskName.name,DueDate.date,DueTime.time,Difficulty.difficulty, Task.CompletedFlag " +
				"from Task join TaskName on Task.TaskName = TaskName._id join DueDate on Task.DueDate = DueDate._id " +
				"join DueTime on Task.DueTime = DueTime._id join Difficulty on Task.Difficulty = Difficulty._id where Task._id = ?", new String[] {"" + id});
	}
	
	/**
	 * returns a cursor pointing all integer indices from the main table for a particular task 
	 * @param id The whose information you want to know
	 * @return the cursor described above
	 */
	public Cursor findTask(long id)
	{
		// find a specific task based on its _id
		return taskDB.query(false, TaskDatabaseHelper.TASK_TABLE, null, "_id = ?" , new String[]{""+ id},null, null, null, null);
	}	
	
	/* *********************************************************************************************************************************
	 * The next methods are designed to obtain the corresponding field ids from their tables.
	 */
	
	/**
	 * Gets the ID for a task name from its respective table
	 * @param name The task name whose ID you desire
	 * @return a long representing the ID of the task name
	 */
	public long getTaskNameID(String name)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.TASKNAME_TABLE, null, TaskDatabaseHelper.TASKNAME_NAME + "= '" 
	    + name + "'", null,null, null, null, null);
		c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}
	
	/**
	 * Gets the ID for a start date from its respective table
	 * @param startDate The integer startdate whose ID you desire
	 * @return a long representing the ID of the start date
	 */
	public long getStartDateID(int startDate)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.STARTDATE_TABLE, null, TaskDatabaseHelper.STARTDATE_DATE + "= '" 
			    + startDate + "'", null,null, null, null, null);
				c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}
	
	/**
	 * Gets the ID for a start time from its respective table
	 * @param startTime the integer start time whose ID you desire
	 * @return a long representing the ID of the start time
	 */
	public long getStartTimeID(int startTime)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.STARTTIME_TABLE, null, TaskDatabaseHelper.STARTTIME_TIME + "= '" 
			    + startTime + "'", null,null, null, null, null);
				c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
		
	}
	
	/**
	 * Gets the ID for an end date from its respective table
	 * @param endDate the integer end date whose ID you desire
	 * @return a long representing the ID of the end date
	 */
	public long getEndDateID(int endDate)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.ENDDATE_TABLE, null, TaskDatabaseHelper.ENDDATE_DATE + "= '" 
			    + endDate + "'", null,null, null, null, null);
				c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}
	
	/**
	 * Gets the ID for an end time from its respective table
	 * @param endTime the integer end time whose ID you desire
	 * @return a long representing the ID of the end time
	 */
	public long getEndTimeID(int endTime)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.ENDTIME_TABLE, null, TaskDatabaseHelper.ENDTIME_TIME + "= '" 
			    + endTime + "'", null,null, null, null, null);
				c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}
	
	/**
	 * Gets the ID for a particular difficulty from its respective table
	 * @param difficulty the string difficulty whose ID you desire
	 * @return a long representing the ID of the difficulty
	 */
	public long getDifficultyID(String difficulty)
	{
		Cursor c =  taskDB.query(true, TaskDatabaseHelper.DIFFICULTY_TABLE, null, TaskDatabaseHelper.DIFFICULTY_DIFFICULTY + "= '"
	    + difficulty + "'", null,null, null, null, null);
		c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}
	
	/**
	 * Gets the ID for a particular due date from its respective table
	 * @param duedate the integer due date whose ID you desire
	 * @return a long representing the ID of the due date
	 */
	public long getDueDateID(int duedate)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.DUEDATE_TABLE, null, TaskDatabaseHelper.DUEDATE_DATE + "= '" 
	    + duedate + "'", null,null, null, null, null);
		c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}
	
	/**
	 * Gets the ID for a particular due time from its respective table
	 * @param duetime the integer due time wose ID you desire
	 * @return a long representing the ID of the due date
	 */
	public long getDueTimeID(int duetime)
	{
		Cursor c = taskDB.query(true, TaskDatabaseHelper.DUETIME_TABLE, null, TaskDatabaseHelper.DUETIME_TIME + "= '" 
	    + duetime + "'", null,null, null, null, null);
		c.moveToFirst();
		return c.getLong(c.getColumnIndex("_id"));
	}

	
	/* *********************************************************************************************************************************
	 * The next methods are designed to obtain other information from the database
	 */
	
	/**
	 * This updates the estimated time for a particular difficulty. This method does not do error checking to make sure that the 
	 * difficulty to change is a valid difficulty and the SQL update will fail if it is not a valid difficulty option in the 
	 * Difficulty table
	 * @param difficulty The difficulty that you would like to update the estimated time of completion for.
	 * @param newTime The new estimated time for that particular difficulty.
	 */
	public static void updateTimeForDifficulty(String difficulty, int newTime)
	{
		ContentValues newEstimatedTimeValue = new ContentValues();		
		newEstimatedTimeValue.put(TaskDatabaseHelper.DIFFICULTY_ESTIMATEDTIME, newTime);
		
		taskDB.update(TaskDatabaseHelper.DIFFICULTY_TABLE, newEstimatedTimeValue, "difficulty = ?", new String[]{""+ difficulty});
	}
	
	/**
	 * This returns the estimated time for a particular difficulty as shown in the the difficulty table
	 * @param difficulty the difficulty whose estimated time is desired
	 * @return the integer representation of the time for the difficulty
	 */
	public static int getEstimatedTimeForDifficulty(String difficulty)
	{
		Cursor c = taskDB.query(false, TaskDatabaseHelper.DIFFICULTY_TABLE, null, 
				TaskDatabaseHelper.DIFFICULTY_DIFFICULTY + " = ?", new String[]{difficulty}, null, null, null, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex(TaskDatabaseHelper.DIFFICULTY_ESTIMATEDTIME));
		
	}
	
	/**
	 * This method is used to determine if the estimated time has been set in the internal database.
	 * @return boolean whether or not the time has been set initially.
	 */
	public boolean hasSetTime()
	{
		boolean timeIsSet =  false;
		Cursor c = taskDB.query(false, TaskDatabaseHelper.DIFFICULTY_TABLE, null, TaskDatabaseHelper.DIFFICULTY_ESTIMATEDTIME + " = -1" , null, null, null, null, null);
		
		if(c.getCount() == 0)
		{
			timeIsSet = true;
		}
		
		Log.v(LOG_TAG, "" + c.getCount());
		
		return timeIsSet;
	}
	
	/**
	 * This method checks to see if a particular task has been marked as complete or not.
	 * @param id The ID of the task whose completion status is to be investigated
	 * @return a boolean describing whether or not the task is marked as complete or not. True is equivalent to yes, the task is complete.
	 */
	public boolean isComplete(int id)
	{
		boolean taskComplete = false;
		
		Cursor c = taskDB.query(false, TaskDatabaseHelper.TASK_TABLE, null, null, null, null, null, null, null);
		c.moveToFirst();
		int completed = c.getInt(c.getColumnIndex(TaskDatabaseHelper.TASK_COMPLETE_FLAG));
		
		if(completed == TaskDatabaseHelper.YES)
		{
			taskComplete = true;
		}
		
		return taskComplete;
	}

	/**
	 * This method marks a task as begun at a particular time. It finds or inserts new start times and dates
	 * into the StartDate and StartTime tables as necessary and then updates the main table with the appropriate
	 * ID of the StartDate and StartTime.
	 * @param id The id of the task to start.
	 */
	public void startTask(long id) 
	{
		Calendar currentCal = Calendar.getInstance();
		int currentTime = DateAndTime.combineTime(currentCal.get(Calendar.HOUR_OF_DAY), currentCal.get(Calendar.MINUTE));
		int currentDate = DateAndTime.combineDate(currentCal.get(Calendar.MONTH), 
				currentCal.get(Calendar.DAY_OF_MONTH), currentCal.get(Calendar.YEAR));
		
		ContentValues startValues = new ContentValues();
		
		ContentValues startDateContent = new ContentValues();
		ContentValues startTimeContent = new ContentValues();
		
		startDateContent.put(TaskDatabaseHelper.STARTDATE_DATE, currentDate);
		startTimeContent.put(TaskDatabaseHelper.STARTTIME_TIME, currentTime);
		
		//check and get the id for the start date
		long startDateID;
		try
		{
			Log.v(LOG_TAG, "Inserted new start date correctly.");
			startDateID = taskDB.insertOrThrow(TaskDatabaseHelper.STARTDATE_TABLE, null, startDateContent);

		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// startdate table because it has the unique modifier on it. Now, we must find the id of that start date
			Log.v(LOG_TAG, "Error inserting start date, " + sqle.getLocalizedMessage());
			startDateID = getStartDateID(currentDate);
			
		}
		
		//check and get the id for the start time
		long startTimeID;
		try
		{
			Log.v(LOG_TAG, "Inserted new start time correctly.");
			startTimeID = taskDB.insertOrThrow(TaskDatabaseHelper.STARTTIME_TABLE, null, startTimeContent);

		}
		catch(SQLiteConstraintException sqle)
		{
			// An SQLException is thrown if there is already an entry in the 
			// start time table because it has the unique modifier on it. Now, we must find the id of that start time
			Log.v(LOG_TAG, "Error inserting start time, " + sqle.getLocalizedMessage());
			startTimeID = getStartTimeID(currentTime);
			
		}
		
		startValues.put(TaskDatabaseHelper.TASK_STARTDATE, startDateID);
		startValues.put(TaskDatabaseHelper.TASK_STARTTIME, startTimeID);
		
		taskDB.update(TaskDatabaseHelper.TASK_TABLE, startValues, "_id = ?", new String[]{""+ id});
		
	}
	/**
	 * This method checks to see if the task has started or not. The start time and date is marked as -1 if the task
	 * has not been started and this method checks that status on a particular task.
	 * @param id The task to check the start status of
	 * @return a boolean describing whether the task has been started or not. True is equivalent to yes, the task has started.
	 */
	public boolean isTaskStarted(long id)
	{
		boolean started = true;
		
		Cursor c = findTask(id);
		c.moveToFirst();
		
		Log.v(LOG_TAG, "task started count" + c.getCount());
		
		long startTimeID = c.getInt(c.getColumnIndex(TaskDatabaseHelper.TASK_STARTTIME));
		
		if(startTimeID == getStartTimeID(-1))
		{
			started = false;
		}
		
		return started;
	}
	
	/**
	 * This method checks to see how many tasks are completed or not and
	 * returns a size 2 integer array with the contents of how many tasks are completed in
	 * index 0 and how many are not completed in index 1.
	 * @return the array as mentioned above
	 */
	public static int[] getTotalCompletedVsNot()
	{	
		Cursor completedCursor = taskDB.query(false, TaskDatabaseHelper.TASK_TABLE, null, TaskDatabaseHelper.TASK_COMPLETE_FLAG + " = " + TaskDatabaseHelper.YES,
				null, null, null, null, null);
		int completed = completedCursor.getCount();
		
		Cursor notCompletedCursor = taskDB.query(false, TaskDatabaseHelper.TASK_TABLE, null, TaskDatabaseHelper.TASK_COMPLETE_FLAG + " = " + TaskDatabaseHelper.NO,
				null, null, null, null, null);;
		int notCompleted = notCompletedCursor.getCount();
		
		int [] values = {completed,notCompleted};
		return values;
		
	}
	
	/**
	 * This method checks to see how many tasks are completed on time or not and
	 * returns a size 2 integer array with the contents of how many tasks are completed on time in
	 * index 0 and how many are not completed on time in index 1.
	 * @return the array as mentioned above
	 */
	public static int[] getCompletedOnTimeVsNot()
	{
		
		Cursor times = taskDB.query(false, TaskDatabaseHelper.TASK_TABLE, null, 
				TaskDatabaseHelper.TASK_COMPLETE_ON_TIME_FLAG + " = ? and " + TaskDatabaseHelper.TASK_COMPLETE_FLAG + " = ?", new String [] {""+TaskDatabaseHelper.YES, "" + TaskDatabaseHelper.YES}, null, null, null, null);
		
		Cursor timesNot = taskDB.query(false, TaskDatabaseHelper.TASK_TABLE, null, 
				TaskDatabaseHelper.TASK_COMPLETE_ON_TIME_FLAG + " = ? and " + TaskDatabaseHelper.TASK_COMPLETE_FLAG + " = ?", new String [] {""+TaskDatabaseHelper.NO, "" + TaskDatabaseHelper.YES}, null, null, null, null);
		
		int [] values = {times.getCount(),timesNot.getCount()};
		
		return values;
	}
}
