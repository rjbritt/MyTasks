package com.cpsc481.mytasks;

import com.cpsc481.mytasks.customclasses.DateAndTime;
import com.cpsc481.mytasks.customclasses.Task;
import com.cpsc481.mytasks.databasestorage.TaskDatabaseHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is a fragment that allows the addition of a new task.
 * It instantiates a spinner and the appropriate DatePickerFragments and the TimePickerFragments
 * 
 * @author Ryan Britt
 * @version 1.0
 */
public class AddTask extends FragmentActivity
{

	private int dueTime = 0;
	private int dueDate = 0;
	private String difficulty = "Easy";
	private String taskName = "";
	
	private String difficultySettings [] = {TaskDatabaseHelper.DIFFICULTY_ONE, TaskDatabaseHelper.DIFFICULTY_TWO, TaskDatabaseHelper.DIFFICULTY_THREE};
	
	/**
	 * This method is called upon the creation of this fragment, per the Android
	 * fragment life cycle.
	 * 
	 * @param savedInstanceState the Bundle that this method is called with,
	 * per the android fragment life cycle.
	 * 
	 * @since 1.0
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		
		Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty);
		
		ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, difficultySettings);
		
		difficultySpinner.setAdapter(spin_adapter);
		
		/* This is the spinner to choose difficulty levels and this determines what will happen upon choosing a level*/
		difficultySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
				{
					@Override
					public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)  
					{
						/* This allows the difficulty to be set to whatever the index is */
						difficulty = difficultySettings[position];
					}
					
				    @Override
				    public void onNothingSelected(AdapterView<?> parentView)
				    {
				    	
				    }
				});
	}

	/**
	 * This method is used to create a task. It calls a static method (not the best way, I admit)
	 * to pass a reference to an object that contains all of the task details. It will
	 * not call finish()  until all the fields have been instantiated. If all
	 * the fields have not been instantiated, it will show a Toast to indicate so.
	 * 
	 * @param view The view from which createTask is being called, generally a button.
	 * @since 1.0
	 */
	public void createTask(View view)
	{

		TextView nameText = (TextView) findViewById(R.id.nameText);
		taskName = nameText.getText().toString();
		
		ImageView image = (ImageView) findViewById(R.id.image);
				
		if(dueTime != 0 && dueDate !=0 && !taskName.equals(""))
		{
			CurrentTaskListFragment.addTask(new Task(taskName,difficulty, dueDate, dueTime));
			image.setImageResource(R.drawable.check);
				
			finish();
		}
		else
		{
			Toast errorToast = Toast.makeText(getApplicationContext(), "You have not entered all the required information", Toast.LENGTH_SHORT);
			errorToast.show();
			image.setImageResource(R.drawable.x);

		}

	}
	
	/**
	 * This method adds the menu to the action bar if it is present. While
	 * included, this method is not used as the action bar menu has not been given any 
	 * additional function yet.
	 * 
	 * @param menu The menu added to the action bar
	 * @since 1.0
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_item, menu);
		return true;
	}
		
	/*
	 * The handler helps to pass information from the DatePicker to this fragment
	 * 
	 * The warning says that it should be static, but by making it static, other 
	 * fields have to be made static and there is a chance of nonstatic references.
	 */
	Handler dueDateHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			dueDate = msg.getData().getInt("date");

			TextView dateText = (TextView) findViewById(R.id.dueDateText);
			dateText.setText(DateAndTime.printDate(dueDate));
		}
	};
	
	/*
	 * This handler helps to pass information from the TimePicker to this fragment
	 * 
	 * The warning says that it should be static, but by making it static, other 
	 * fields have to be made static and there is a chance of nonstatic references.
	 * 
	 * It would be optimal to be able to make it static to get rid of the warnings.
	 */
	Handler dueTimeHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{	
			dueTime = msg.getData().getInt("time");
			
			TextView timeText = (TextView) findViewById(R.id.dueTimeText);
			timeText.setText(DateAndTime.printTime(dueTime));
		}
	};
	
	
	/**
	 *This method shows the date picker for the due date
	 *@param view The view from which this method is called, generally a button.
	 *@since 1.0
	 */
	public void showDatePicker(View view)
	{
		DueDatePickerFragment dateFragment = new DueDatePickerFragment();
		dateFragment.setHandler(dueDateHandler);
		dateFragment.show(getSupportFragmentManager(), "datePicker" );	
	}
	
	/**
	 * This method shows the time picker for the due time
	 * @param view The view from which this method is called, generally a button.
	 * @since 1.0
	 */
	public void showTimePicker(View view)
	{
		DueTimePickerFragment timeFragment = new DueTimePickerFragment();
		timeFragment.setHandler(dueTimeHandler);
		timeFragment.show(getSupportFragmentManager(), "timePicker" );	
	}
}
