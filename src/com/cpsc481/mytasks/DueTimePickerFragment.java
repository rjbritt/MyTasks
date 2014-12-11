package com.cpsc481.mytasks;

import java.util.Calendar;

import com.cpsc481.mytasks.customclasses.DateAndTime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * This class is the fragment for a time picker dialog and handles the input by sending a Message through
 *  a Handler class instance.
 */
public class DueTimePickerFragment extends DialogFragment implements
										TimePickerDialog.OnTimeSetListener
{
	Handler timeHandler;

	/**
	 * This method sets the data handler in order to make the data visible to other classes.
	 * The handler method is only used in {@link DueDatePickerFragment} and {@link DueTimePickerFragment}
	 * @param t The handler that the timepicker is to use.
	 */
	public void setHandler(Handler t)
	{
		timeHandler = t;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
  
        
    }
		
    /**
     * This method is called when the time is set in the time picker and tells the picker what
     * to do with the data.
     * @param view The timepicker view from which this method is called
     * @param hourOfDay The hour of the day (in 24 hr notation) that has been chosen
     * @param minute The minute of the day that has been chosen	
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
    {
    	int combinedTime = DateAndTime.combineTime(hourOfDay, minute);
    	
    	//Create a new message to send to the timeHandler and send it.
    	Message thisMessage = new Message();
    	Bundle data = new Bundle();
    	data.putInt("time", combinedTime);
    	thisMessage.setData(data);
    	timeHandler.sendMessage(thisMessage);
    }

}
