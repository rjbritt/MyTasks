package com.cpsc481.mytasks.databasestorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class is an embodiment of the sqlite database used to house all of the data for this app.
 * 
 * @author rjbritt
 *
 */
public class TaskDatabaseHelper extends SQLiteOpenHelper
{
	
	// This is the log tag used for output in log cat for debugging
    private static final String LOG_TAG = "Database Helper Log";  

    private static final String DATABASE_NAME = "TaskDatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    
	//  public static strings that are the names of the fields or needed constants for each table
    
	public static final String UNIQUE_ID = "_id"; // general unique id identifier. ALL tables have a UNIQUE_ID field.
	
	public static final int YES = 1;
	public static final int NO = 0;
	
	// Task Table specific fields
	public static final String TASK_TABLE = "Task";
	public static final String TASK_TASKNAME =  "TaskName";
	public static final String TASK_DIFFICULTY = "Difficulty";
	public static final String TASK_STARTDATE = "StartDate";
	public static final String TASK_STARTTIME = "StartTime";
	public static final String TASK_ENDDATE = "EndDate";
	public static final String TASK_ENDTIME = "EndTime";
	public static final String TASK_DUEDATE = "DueDate";
	public static final String TASK_DUETIME = "DueTime";
	public static final String TASK_COMPLETE_FLAG = "CompletedFlag";
	public static final String TASK_COMPLETE_ON_TIME_FLAG = "CompletedOTFlag";
	
	// TaskName Table specific fields
	public static final String TASKNAME_TABLE = "TaskName";
	public static final String TASKNAME_NAME = "name";

	// StartDate Table specific fields
	public static final String STARTDATE_TABLE ="StartDate";
	public static final String STARTDATE_DATE = "date";
	
	// StartTime Table specific fields
	public static final String STARTTIME_TABLE = "StartTime";
	public static final String STARTTIME_TIME = "time";
	
	// EndDate Table specific fields
	public static final String ENDDATE_TABLE ="EndDate";
	public static final String ENDDATE_DATE = "date";
	
	// EndTime Table specific fields
	public static final String ENDTIME_TABLE = "EndTime";
	public static final String ENDTIME_TIME = "time";
	
	// DueDate Table specific fields
	public static final String DUEDATE_TABLE ="DueDate";
	public static final String DUEDATE_DATE = "date";
	
	// DueTime Table specific fields
	public static final String DUETIME_TABLE = "DueTime";
	public static final String DUETIME_TIME = "time";
	
	// Difficulty Table specific fields
	public static final String DIFFICULTY_TABLE = "Difficulty";
	public static final String DIFFICULTY_DIFFICULTY = "difficulty";
	public static final String DIFFICULTY_ESTIMATEDTIME = "estimateCompleteTime";
	
	// Difficulty setting fields
	public static final String DIFFICULTY_ONE = "Easy";
	public static final String DIFFICULTY_TWO = "Medium";
	public static final String DIFFICULTY_THREE = "Hard";
	
    // These are the create statements for the database schema.

    private static final String CREATE_MASTER_TABLE =
        "create table Task (_id integer primary key autoincrement, "
        + "TaskName integer,Difficulty integer, StartDate integer," +
        "StartTime integer,EndDate integer,EndTime integer," +
        " DueDate integer, DueTime integer, CompletedFlag integer, CompletedOTFlag integer);";
    
    private static final String CREATE_TASKNAME_TABLE = 
    		"create table  TaskName(_id integer primary key autoincrement,"
    		+ "name text unique );";
    private static final String CREATE_DIFFICULTY_TABLE = 
    		"create table  Difficulty(_id integer primary key autoincrement,"
    		+ "difficulty text unique, estimateCompleteTime integer);";
    
    private static final String CREATE_STARTDATE_TABLE = 
    		"create table  StartDate(_id integer primary key autoincrement,"
    		+ "date integer unique);";    
    private static final String CREATE_STARTTIME_TABLE = 
    		"create table  StartTime(_id integer primary key autoincrement,"
    		+ "time integer unique);";
    
    private static final String CREATE_ENDDATE_TABLE = 
    		"create table  EndDate(_id integer primary key autoincrement,"
    		+ "date integer unique);";    
    private static final String CREATE_ENDTIME_TABLE = 
    		"create table  EndTime(_id integer primary key autoincrement,"
    		+ "time integer unique);";
    
    private static final String CREATE_DUEDATE_TABLE = 
    		"create table  DueDate(_id integer primary key autoincrement,"
    		+ "date integer unique);";

    private static final String CREATE_DUETIME_TABLE = 
    		"create table  DueTime(_id integer primary key autoincrement,"
    		+ "time integer unique);";
    
    public TaskDatabaseHelper(Context context) 
    {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    
	@Override
	public void onCreate(SQLiteDatabase db)
	{		
		//These statements are for having an integrated table. I will just have a single table to begin with.
		db.execSQL(CREATE_MASTER_TABLE);
		db.execSQL(CREATE_TASKNAME_TABLE);
		db.execSQL(CREATE_DIFFICULTY_TABLE);
		db.execSQL(CREATE_STARTDATE_TABLE);
		db.execSQL(CREATE_STARTTIME_TABLE);
		db.execSQL(CREATE_ENDDATE_TABLE);
		db.execSQL(CREATE_ENDTIME_TABLE);
		db.execSQL(CREATE_DUEDATE_TABLE);
		db.execSQL(CREATE_DUETIME_TABLE);

		
		// Only 3 levels of difficulty allowed. Hard coded in on database create
		db.execSQL("insert into Difficulty(difficulty,estimateCompleteTime) values('"+DIFFICULTY_ONE+"',-1);");
		db.execSQL("insert into Difficulty(difficulty,estimateCompleteTime) values('"+DIFFICULTY_TWO+"',-1);");
		db.execSQL("insert into Difficulty(difficulty,estimateCompleteTime) values('"+DIFFICULTY_THREE+"',-1);");
		
		//Must have -1 values for initialization of start/end dates and times.
		db.execSQL("insert into StartDate(date) values (-1);");
		db.execSQL("insert into StartTime(time) values (-1);");
		db.execSQL("insert into EndDate(date) values (-1);");
		db.execSQL("insert into EndTime(time) values (-1);");


	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
	    Log.w(LOG_TAG, "Upgrading database. Existing contents will be lost. ["
	            + oldVersion + "]->[" + newVersion + "]");
	    
	    db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE); 
	    db.execSQL("DROP TABLE IF EXISTS " + TASKNAME_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DIFFICULTY_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + STARTDATE_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + STARTTIME_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + ENDDATE_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + ENDTIME_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DUEDATE_TABLE);
	    db.execSQL("DROP TABLE IF EXISTS " + DUETIME_TABLE);
	    
	    onCreate(db);		
	}
	

}
