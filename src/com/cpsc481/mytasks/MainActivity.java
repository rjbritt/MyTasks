package com.cpsc481.mytasks;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Main class that loads in the app.
 * This activity holds the tabs, which are the fragments {@link CurrentTaskListFragment}, 
 * {@link Reports}, {@link SetDifficultyTimeEstimateFragment}
 * 
 * @author rjbritt
 *
 */
public class MainActivity extends ListActivity {

	
	/**
	 * Shows the dialog for setting the time based on the button
	 * that was pressed. The button is physically located on the Settings tab XML,
	 * but the button calls are redirected here.
	 * 
	 * @param view The view that called this method, typically a button
	 */
	public void showDiff(View view)
	{
		Button thisButton = (Button)view;
		
		Bundle settings = new Bundle();
		
		settings.putString("Difficulty", thisButton.getText().toString());
		settings.putBoolean("Intro", false);
		
		SetTimeFragment dialog = new SetTimeFragment();
		dialog.setArguments(settings);
		
		dialog.show(getFragmentManager(), "tag");
	}
	
	/**
	 * Shows information view.
	 * Begins a new activity
	 * 
	 * @param view The view this is called from, typically a button.
	 */
	public void getInfo(View view)
	{
		Intent infoIntent = new Intent(this, Information.class);
		startActivity(infoIntent);
	}
	
	/**
	 * Begins the process to add a new task.
	 * Begins a new Activity
	 * 
	 * @param view The view this is called from, typically a button.
	 */
	public void addTaskButton(View view)
	{
		Intent addTaskIntent = new Intent(this,AddTask.class);
		startActivity(addTaskIntent);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		ActionBar actionbar = getActionBar();
		
		actionbar.setHomeButtonEnabled(true);
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		Tab listTab = actionbar.newTab();
		listTab.setText("Tasks")
				.setContentDescription("List of Current Tasks")
				.setTabListener(new TabListener<CurrentTaskListFragment>(this, "tag", CurrentTaskListFragment.class));
		
		Tab reportTab = actionbar.newTab();
		reportTab.setText("Reports")
				 .setContentDescription("Reports on Tasks")
				 .setTabListener(new TabListener<Reports>(this, "tag2", Reports.class));
		
		Tab settingsTab = actionbar.newTab();
		settingsTab.setText("Settings")
				 .setContentDescription("Estimated Time Settings")
				 .setTabListener(new TabListener<SetDifficultyTimeEstimateFragment>(this, "tag3", SetDifficultyTimeEstimateFragment.class));
		
		actionbar.addTab(listTab);
		actionbar.addTab(reportTab);
		actionbar.addTab(settingsTab);
		
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
