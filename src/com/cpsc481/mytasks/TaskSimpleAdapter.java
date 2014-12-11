package com.cpsc481.mytasks;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpsc481.mytasks.customclasses.DateAndTime;
import com.cpsc481.mytasks.databasestorage.TaskDatabaseHelper;

/**
 * This is a custom adapter based on SimpleCursorAdapter w/ a custom viewbinder in order 
 * to specifically display the integer date and time in a human readable format.
 * @author rjbritt
 *
 */
public class TaskSimpleAdapter extends SimpleCursorAdapter
{
	Context mContext;
	Cursor cursor;
	boolean removed = false;

	@SuppressWarnings("deprecation")
	public TaskSimpleAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		
		mContext = context;
		cursor = c;
		
		this.setViewBinder(new TaskBinder());
	}

	/**
	 * This is a new viewbinder made to specifically put the date and time in a human readable context.
	 * @author rjbritt
	 *
	 */
	private static class TaskBinder implements ViewBinder
	{
		
		public boolean setViewValue(View v, Cursor c, int columnIndex)
		{
			boolean returnVal = false;
			
			if(v.getId() == R.id.dueDateTextView)
			{
				TextView t = (TextView)v;
				t.setText(DateAndTime.printDate(c.getInt(c.getColumnIndex(TaskDatabaseHelper.DUEDATE_DATE))));
				returnVal = true;
			}
			else if(v.getId() == R.id.dueTimeTextView)
			{
				TextView t = (TextView)v;
				t.setText(DateAndTime.printTime(c.getInt(c.getColumnIndex(TaskDatabaseHelper.DUETIME_TIME))));
				returnVal = true;
			}
			
			return returnVal;
		}
		
	}
	
}
