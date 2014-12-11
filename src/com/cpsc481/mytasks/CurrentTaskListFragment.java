package com.cpsc481.mytasks;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cpsc481.mytasks.customclasses.Task;
import com.cpsc481.mytasks.databasestorage.TaskDataStore;
import com.cpsc481.mytasks.databasestorage.TaskDatabaseHelper;
/**
 * Fragment that displays the current task list
 * in the tabs. 
 * 
 * @author Ryan Britt
 * @version 1.0
 *
 */
public class CurrentTaskListFragment extends ListFragment
{
	private TaskSimpleAdapter tasks;
	
	private static TaskDataStore taskData;
	private Activity currentActivity;
	private int clickedItemIndex; //The index of the task that needs to be removed.
	
	private static ArrayList<Task> taskList;



	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_list, container, false);
    }
	
	/**
	 * This method shows the difficulty settings at the first run of the app.
	 * It starts out with an Easy difficulty setting and sets the Intro flag to true, 
	 * which lets the DialogFragment know to show the Medium and Hard settings 
	 * in sequential order after this dialog.
	 * 
	 * Calls {@link SetTimeFragment#SetTimeFragment SetTimeFragment}
	 */
	public void showDiffBegin()
	{
		Bundle settings = new Bundle();
		
		settings.putString("Difficulty", "Easy");
		settings.putBoolean("Intro", true);
		
		SetTimeFragment dialog = new SetTimeFragment();
		dialog.setArguments(settings);
		
		dialog.show(getFragmentManager(), "tag");
	}
	
	/**
	 *  This is the overridden onCreate method that checks to see if the initial
	 *  time per difficulty has been set.  It prompts the user to do so, if it has
	 *  not been set. This method also initializes the database.
	 *  
	 *  @param savedInstanceState The saved state if it is being resumed.
	 */
	@Override 
	public void onCreate(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		currentActivity = getActivity();
		taskData = new TaskDataStore(currentActivity);
		taskList = new ArrayList<Task>();
		
		taskData.open();
				
		if(!taskData.hasSetTime())
		{
			showDiffBegin();
		}
		
		fillData();
	}
	
	/**
	 * This method fills the data in the List.
	 * 
	 * This method of getting the content from a database has been deprecated. 
	 * A future way to make this app better would be to use content providers to get
	 * the content from content providers and using loaders as this method loads the information
	 * on the UI thread, and not on a separate thread.
	 *
	 *{@link com.cpsc481.mytasks.databasestorage.TaskDataStore#findAllTasks findAllTasks}
	 */
	private void fillData() 
	{
		Cursor c = taskData.findAllTasks();
        currentActivity.startManagingCursor(c);
              

        //These are the column names that dictate where the information will be coming from.
        String[] from = new String[] { TaskDatabaseHelper.TASKNAME_NAME, TaskDatabaseHelper.DUEDATE_DATE, 
        		 TaskDatabaseHelper.DUETIME_TIME,  TaskDatabaseHelper.DIFFICULTY_DIFFICULTY};
        
        
        
        //These are the locations that the from locations will linearly map to
        int[] to = new int[] { R.id.nameTextView,R.id.dueDateTextView,R.id.dueTimeTextView,R.id.difficultyTextView};
        
        // Now create an array adapter and set it to display using our row
        tasks = new TaskSimpleAdapter(currentActivity, R.layout.task_view, c, from, to);
        
        setListAdapter(tasks);        
	}
	
	/**
	 * Adds a new task to the database which is represented as a Task object on input.
	 * Calls {@link com.cpsc481.mytasks.databasestorage.TaskDataStore#addNewTask addnewTask};
	 * 
	 * @param taskToAdd The Task object which will be decomposed and added to the database.
	 * 
	 */
	public static void addTask(Task taskToAdd)
	{
		taskList.add(taskToAdd);
		
		taskData.addNewTask(taskToAdd.getName(),taskToAdd.getDueDate(), taskToAdd.getDueTime(), taskToAdd.getDifficulty());
	}
	
	/**
	 * Removes the task from the list. Calls {@link com.cpsc481.mytasks.databasestorage.TaskDataStore#completeTask completeTask}
	 * to complete the task and  {@link #fillData fillData} to refresh the screen.
	 * @param id The id of the task to be removed
	 */
	public void removeTask(long id)
	{	
		taskData.completeTask(id);
		Toast removeToast = Toast.makeText(currentActivity, "Task completed.", Toast.LENGTH_SHORT);
		removeToast.show();
		
		fillData(); // used to refresh the screen again.
	}

	/**
	 * Begins the task and calls {@link com.cpsc481.mytasks.databasestorage.TaskDataStore#startTask startTask}
	 * @param id The id of the task to begin
	 */
	public void startTask(long id)
	{	
		
		taskData.startTask(id);
		
		Toast removeToast = Toast.makeText(currentActivity, "Task Began.", Toast.LENGTH_SHORT);
		removeToast.show();
		
		fillData(); // used to refresh the screen again.
	}
	

	/**
	 * This is called when a list item is clicked.
	 * @see android.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 * 
	 * It shows the dialog box to set either begin or end the task.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		
		clickedItemIndex = position;
		ChooseBeginOrEndFragment confirmDelete = new ChooseBeginOrEndFragment();
		confirmDelete.show(getFragmentManager(), getTag());
	}
	
	
	/**
	 * This private class is the fragment for choosing to begin or end a task dialog.
	 * @author Ryan Britt
	 * @version 1.0
	 *
	 */
	@SuppressLint("ValidFragment")
	private class ChooseBeginOrEndFragment extends DialogFragment
	{

		/**
		 * 
		 */
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) 
		{
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
			
			dialogBuilder.setMessage("Would you like to begin or end this task?");
			dialogBuilder.setPositiveButton("Begin Task", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					startTask(tasks.getItemId(clickedItemIndex));
					dismiss();
				}
			});
			dialogBuilder.setNegativeButton("End Task", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					/* This if/else statement prevents the task from being ended if it hasn't been begun */
					if(taskData.isTaskStarted(tasks.getItemId(clickedItemIndex)))
					{
						removeTask(tasks.getItemId(clickedItemIndex));
					}
					else
					{
						Toast taskBegin = Toast.makeText(currentActivity, "You must begin the task before you can end it.", Toast.LENGTH_SHORT);
						taskBegin.show();
					}
				}
			});
			
			return dialogBuilder.create();
		}

	}

} 
