package com.cpsc481.mytasks;

import java.util.Calendar;

import com.cpsc481.mytasks.customclasses.DateAndTime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/**
 *  Fragment that shows a DueDatePicker and handles the input by sending a Message through
 *  a Handler class instance.
 */
public class DueDatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener 
{
	Handler dateHandler;

	/**
	 * This method sets the data handler in order to make the data visible to other classes.
	 * The handler method is only used in {@link DueDatePickerFragment} and {@link DueTimePickerFragment}
	 * @param t The handler that the datepicker is to use.
	 */
	public void setHandler(Handler t)
	{
		dateHandler = t;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	/**
	 * This method tells the datepicker what to do when the date is set.
	 * @param view The Datepicker view that the message is being received from
	 * @param year The year chosen for the datepicker
	 * @param month The month chosen for the datepicker
	 * @param day The day chosen for the datepicker.
	 */
	public void onDateSet(DatePicker view, int year, int month, int day) 
	{
		int finalDate = DateAndTime.combineDate(month, day, year);
				
		//Create a new message to send to the date handler, and send it.
    	Message thisMessage = new Message();
    	Bundle data = new Bundle();
    	data.putInt("date", finalDate);
    	thisMessage.setData(data);
    	dateHandler.sendMessage(thisMessage);
		
	}
}
