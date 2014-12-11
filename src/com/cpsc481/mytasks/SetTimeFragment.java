package com.cpsc481.mytasks;

import com.cpsc481.mytasks.customclasses.DateAndTime;
import com.cpsc481.mytasks.databasestorage.TaskDataStore;
import com.cpsc481.mytasks.databasestorage.TaskDatabaseHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * This fragment is meant to be shown as a dialog 
 * The XML which this references shows 2 NumberPickers
 * This fragment handles the input from the XML and calls
 * more dialogs if it is in "intro" mode.
 *  
 * @author Ryan Britt
 *
 */
public class SetTimeFragment extends DialogFragment
{
	private NumberPicker hrPick;
	private NumberPicker minPick;
	

	@Override
	/**
	 * This onCreateDialog sets up the min and max values for the hour and minute
	 * pickers as well as the action performed when the "Enter" button is tapped. 
	 * 
	 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
    public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		  // Get the layout inflater
	    LayoutInflater inflater =  getActivity().getLayoutInflater();
		View newView = inflater.inflate(R.layout.diffpicker, null);
	    
		
		hrPick = (NumberPicker)newView.findViewById(R.id.hrPicker);
		hrPick.setMaxValue(24);
		hrPick.setMinValue(0);
		hrPick.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		minPick = (NumberPicker)newView.findViewById(R.id.minPicker);
		minPick.setMinValue(0);
		minPick.setMaxValue(59);
		minPick.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		
		dialogBuilder.setView(newView);		
		dialogBuilder.setMessage("Please enter the estimated time for " + getArguments().getString("Difficulty") + " difficulty.");
		dialogBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				String difficulty = getArguments().getString("Difficulty");
				boolean intro = getArguments().getBoolean("Intro");
				
				int newTime = DateAndTime.combineTime(hrPick.getValue(), minPick.getValue());
				TaskDataStore.updateTimeForDifficulty(difficulty, newTime);
				
				Toast toast = Toast.makeText(getActivity(), "Hr: " + hrPick.getValue() + " Min: " + minPick.getValue(), Toast.LENGTH_SHORT);
				toast.show();
				
				if(intro && difficulty.equals(TaskDatabaseHelper.DIFFICULTY_ONE))
				{
					Bundle settings = new Bundle();
					
					settings.putString("Difficulty", TaskDatabaseHelper.DIFFICULTY_TWO);
					settings.putBoolean("Intro", true);
					
					SetTimeFragment newDialog = new SetTimeFragment();
					newDialog.setArguments(settings);
					
					newDialog.show(getFragmentManager(), "tag");
				}
				
				else if (intro && difficulty.equals(TaskDatabaseHelper.DIFFICULTY_TWO))
				{
					Bundle settings = new Bundle();
					
					settings.putString("Difficulty", TaskDatabaseHelper.DIFFICULTY_THREE);
					settings.putBoolean("Intro", false);
					
					SetTimeFragment newDialog = new SetTimeFragment();
					newDialog.setArguments(settings);
					
					newDialog.show(getFragmentManager(), "tag");
				}
			}
		});
	
		
		return dialogBuilder.create();
	}

	
}

