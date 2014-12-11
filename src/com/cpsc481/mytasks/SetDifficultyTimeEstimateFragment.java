package com.cpsc481.mytasks;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This fragment is used to show the buttons to change the estimated time per difficulty.
 * @author rjbritt
 *
 */
public class SetDifficultyTimeEstimateFragment extends Fragment 
{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{		
        return inflater.inflate(R.layout.activity_time_estimate, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);				
	}
	

}
