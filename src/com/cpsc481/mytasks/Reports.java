package com.cpsc481.mytasks;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cpsc481.mytasks.chart.PieChart;
import com.cpsc481.mytasks.customclasses.DateAndTime;
import com.cpsc481.mytasks.customclasses.Task;
import com.cpsc481.mytasks.databasestorage.TaskDataStore;
import com.cpsc481.mytasks.databasestorage.TaskDatabaseHelper;

/**
 * This is a fragment that computes and shows the reports based on all the information that is available.
 * @author Ryan Britt
 *
 */
public class Reports extends Fragment 
{
	double LEE_WAY_PERCENTAGE = 0.10;
	
	TextView tasksCompleted;
	TextView tasksInProg;
	double [] tasksCompletedValues;
	double [] tasksOnTimeValues;
	double [] easyStatistics;
	double [] mediumStatistics;
	double [] hardStatistics;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	
        return inflater.inflate(R.layout.activity_reports, container, false);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		int [] taskInt = TaskDataStore.getTotalCompletedVsNot();
		double[] tasks = {taskInt[0]/1.0, taskInt[1]/1.0};
		
		int [] onTimeInt = TaskDataStore.getCompletedOnTimeVsNot();
		double [] tasks2 = {onTimeInt[0]/1.0, onTimeInt[1]/1.0};
		
		List<Integer> easyValues = timesTakenPerDifficulty(TaskDatabaseHelper.DIFFICULTY_ONE);
		List<Integer> medValues = timesTakenPerDifficulty(TaskDatabaseHelper.DIFFICULTY_TWO);
		List<Integer> hardValues = timesTakenPerDifficulty(TaskDatabaseHelper.DIFFICULTY_THREE);
		
		
		tasksCompletedValues = tasks;
		tasksOnTimeValues = tasks2;
		easyStatistics = underOverOrNearPerDifficulty(TaskDatabaseHelper.DIFFICULTY_ONE, easyValues);
		mediumStatistics = underOverOrNearPerDifficulty(TaskDatabaseHelper.DIFFICULTY_TWO, medValues);
		hardStatistics = underOverOrNearPerDifficulty(TaskDatabaseHelper.DIFFICULTY_THREE, hardValues);

		tasksCompleted = (TextView) getActivity().findViewById(R.id.totalCompletedTaskNum);
		tasksInProg = (TextView)getActivity().findViewById(R.id.totalTasksInProgNum);
		TextView onTimeTask = (TextView) getActivity().findViewById(R.id.taskOnTimeNum);
		TextView notOnTimeTask = (TextView) getActivity().findViewById(R.id.taskNotOnTimeNum);
		
		TextView easyUnder = (TextView)getActivity().findViewById(R.id.easyUnderNum);
		TextView easyOver = (TextView)getActivity().findViewById(R.id.easyOverNum);
		TextView easyNear = (TextView)getActivity().findViewById(R.id.easyNearNum);
		
		TextView medUnder = (TextView)getActivity().findViewById(R.id.mediumUnderNum);
		TextView medOver = (TextView)getActivity().findViewById(R.id.mediumOverNum);
		TextView medNear = (TextView)getActivity().findViewById(R.id.mediumNearNum);
		
		TextView hardUnder = (TextView)getActivity().findViewById(R.id.hardUnderNum);
		TextView hardOver = (TextView)getActivity().findViewById(R.id.hardOverNum);
		TextView hardNear = (TextView)getActivity().findViewById(R.id.hardNearNum);

		TextView easyAdvice = (TextView)getActivity().findViewById(R.id.easyAdviceText);
		TextView medAdvice = (TextView)getActivity().findViewById(R.id.mediumAdviceText);
		TextView hardAdvice = (TextView)getActivity().findViewById(R.id.hardAdviceText);
			
		tasksCompleted.setText("" + taskInt[0]);
		tasksInProg.setText("" + taskInt[1]);
		onTimeTask.setText("" + onTimeInt[0]);
		notOnTimeTask.setText("" + onTimeInt[1]);
		
		easyUnder.setText("" + (int)easyStatistics[0]);
		easyOver.setText("" + (int)easyStatistics[1]);
		easyNear.setText("" + (int)easyStatistics[2]);
		
		medUnder.setText("" + (int)mediumStatistics[0]);
		medOver.setText("" + (int)mediumStatistics[1]);
		medNear.setText("" + (int)mediumStatistics[2]);
		
		hardUnder.setText("" + (int)hardStatistics[0]);
		hardOver.setText("" + (int)hardStatistics[1]);
		hardNear.setText("" + (int)hardStatistics[2]);
		
		easyAdvice.setText(getAdviceLine(easyStatistics, TaskDatabaseHelper.DIFFICULTY_ONE));
		easyAdvice.setTextColor(Color.BLUE);
		medAdvice.setText(getAdviceLine(mediumStatistics, TaskDatabaseHelper.DIFFICULTY_TWO));
		medAdvice.setTextColor(Color.BLUE);
		hardAdvice.setText(getAdviceLine(hardStatistics, TaskDatabaseHelper.DIFFICULTY_THREE));
		hardAdvice.setTextColor(Color.BLUE);
		
		Button showCompletionChart = (Button)(getActivity().findViewById(R.id.showCompletionChart));
		Button showOnTimeChart = (Button)getActivity().findViewById(R.id.onTimeButton);
		Button showEasyStats = (Button)getActivity().findViewById(R.id.easyStatsButton);
		Button showMedStats = (Button)getActivity().findViewById(R.id.medStatsButton);
		Button showHardStats = (Button)getActivity().findViewById(R.id.hardStatsButton);
		
		
		
		showCompletionChart.setOnClickListener(new OnClickListener() 
				{
							
							@Override
							public void onClick(View v) 
							{
								showCompletionChart(v);
								
							}
						});
		
		showOnTimeChart.setOnClickListener(new OnClickListener() 
				{
					
					@Override
					public void onClick(View v)
					{
						showOnTimeChart(v);
						
					}
				});
		
		showEasyStats.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				showDifficultyChart(TaskDatabaseHelper.DIFFICULTY_ONE);
				
			}
		});
		
		showMedStats.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				showDifficultyChart(TaskDatabaseHelper.DIFFICULTY_TWO);
			}
		});
		
		showHardStats.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				showDifficultyChart(TaskDatabaseHelper.DIFFICULTY_THREE);
			}
		});
		
	}
	/**
	 * This method shows the pie chart for how many tasks have been completed vs not.
	 * @param view The view from which this is called.
	 */
	public void showCompletionChart(View view)
	{
		PieChart chart = new PieChart();
		Intent chartIntent = chart.execute(getActivity(), tasksCompletedValues, PieChart.COMPLETION_TYPE);
		
		startActivity(chartIntent);		
	}
	
	/**
	 * This method shows the pie chart for how many tasks have been completed on time vs not
	 * @param view The view from which this is called.
	 */
	public void showOnTimeChart(View view)
	{
		PieChart chart = new PieChart();
		Intent chartIntent = chart.execute(getActivity(), tasksOnTimeValues, PieChart.ONTIME_TYPE);
		
		startActivity(chartIntent);	
	}

	/**
	 * This method shows the pie chart for each difficulty as to how many are under, over, or near
	 * the estimated time
	 * @param difficulty The name of the difficulty chart that is to be displayed.
	 */
	public void showDifficultyChart(String difficulty)
	{
		Intent chartIntent  = null;
		
		PieChart chart = new PieChart();
		if(difficulty.equals(TaskDatabaseHelper.DIFFICULTY_ONE))
		{
			chartIntent = chart.execute(getActivity(), easyStatistics, PieChart.EASY_STAT_TYPE);
		}
		else if(difficulty.equals(TaskDatabaseHelper.DIFFICULTY_TWO))
		{
			chartIntent = chart.execute(getActivity(), mediumStatistics, PieChart.MED_STAT_TYPE);
		}
		else if(difficulty.equals(TaskDatabaseHelper.DIFFICULTY_THREE))
		{
			chartIntent = chart.execute(getActivity(), hardStatistics, PieChart.HARD_STAT_TYPE);
		}
		
		startActivity(chartIntent);	
	}
	
	/**
	 * This method returns the raw value of the amount of time taken per task for a particular difficulty.
	 *  
	 * @param difficulty The difficulty to analyze
	 * @return a List with all the absolute time values of the amount of time taken per task. 
	 */
	public List<Integer> timesTakenPerDifficulty(String difficulty)
	{
		ArrayList <Integer> timeValues = new ArrayList<Integer>();
		
		Cursor c = TaskDataStore.findTasksForDifficulty(difficulty);
		c.moveToFirst();
		System.out.println("Count: " + c.getCount());
		while(!(c.isAfterLast()))
		{
			int timeElapsed = 0;
			
			int endDate = c.getInt(c.getColumnIndex(TaskDatabaseHelper.ENDDATE_DATE));
			int endTime = c.getInt(c.getColumnIndex(TaskDatabaseHelper.ENDTIME_TIME));
			
			// Since the columns are named the same, when they are joined, the getColumnIndex returns the last
			// index and will not find the other one. So have to do manual reversal.
			int beginDate = c.getInt(c.getColumnIndex(TaskDatabaseHelper.ENDDATE_DATE) - 2);
			int beginTime = c.getInt(c.getColumnIndex(TaskDatabaseHelper.ENDTIME_TIME) - 2);
				
			if(beginDate == endDate)
			{
				timeElapsed = endTime - beginTime;
			}
			else if(endDate > beginDate)
			{
				int days = endDate - beginDate;
				int hours;
				
				// If the endtime is later than the begin time than simple subtraction
				// will tell us how long has passed
				if(endTime >= beginTime)
				{
					
					hours = endTime - beginTime;
				}
				
				//otherwise, we need to get how long from 12am the begin time is and then add that to the amount
				// of hours after 12 am that is the endTime.
				else
				{
					days --; // the days value will give an additional 24hrs if it is not adjusted here for	a task
							// that ends before the time that it began.
						
					hours = endTime + (2400 - beginTime);
				}
				
				timeElapsed = (days * 24) + hours;
				
			}
			timeValues.add(timeElapsed);
			
			c.moveToNext();
		}
		return timeValues;
	}

	/**
	 * This method analyzes the time taken per task for a particular difficulty.  
	 * It splits it up into
	 * times that were under the estimated time (<- the LEE_WAY_PERCENTAGE constant as defined), 
	 * times that were  over the estimated time (>+ the LEE_WAY_PERCENTAGE constant as defined),and
	 * times that were 'near' the estimated time (+- the LEE_WAY_PERCENTAGE constant as defined)
	 *.
	 * @param difficulty the difficulty to analyze
	 * @param timesPerDifficulty the list of all the absolute values of the time taken per task.
	 * @return
	 */
	public double [] underOverOrNearPerDifficulty(String difficulty, List<Integer> timesPerDifficulty)
	{
		ArrayList <Integer> under = new ArrayList<Integer>();
		ArrayList <Integer> over = new ArrayList<Integer>();
		ArrayList <Integer> near = new ArrayList<Integer>();
		
		int estimatedTime = TaskDataStore.getEstimatedTimeForDifficulty(difficulty);
		
		for(int i = 0; i<timesPerDifficulty.size(); i++)
		{
			int time = timesPerDifficulty.get(i);
			
			int upperBound = (int) (estimatedTime + (estimatedTime * LEE_WAY_PERCENTAGE));
			int lowerBound = (int) (estimatedTime - (estimatedTime * LEE_WAY_PERCENTAGE));
			
			if(time > upperBound)
			{
				over.add(timesPerDifficulty.get(i));
			}
			else if (time <= upperBound && time >= lowerBound)
			{
				near.add(timesPerDifficulty.get(i));
			}
			else
			{
				under.add(timesPerDifficulty.get(i));
			}
			
		}
		double [] difficultyStatistics = {under.size(),over.size(),near.size()};
		
		return difficultyStatistics;
	}

	
	/**
	 * This method gets the advice line (whether to increase, decrease, or not change the estimated time)
	 * for a particular difficulty with a set of statistics.
	 * @param statistics An array of form [under,over,near] where each of the fields are the number of tasks
	 * for that particular difficulty that are under the estimated time, over the estimated time, or near the
	 * estimated time.
	 *  
	 * @param difficulty This is the difficulty for which you would like to obtain an advice line for.
	 * 
	 * @return This is a String that describes what should be done to the estimated time of the difficulty 
	 * based on the information available. 
	 */
	private String getAdviceLine(double [] statistics, String difficulty)
	{
		String adviceLine = "";
		
		int maxStat = (int) Math.max(statistics[0], statistics[1]);
		maxStat = (int) Math.max(maxStat,statistics[2]);
		
		
		if(statistics[0] == statistics[1]  && statistics[1] == statistics[2] && statistics[2] == maxStat)
		{
			adviceLine = "Can not suggest change at the moment for " + difficulty + " tasks.";

		}
		//there are more entries under the estimated time than anything else for this difficulty
		else if(statistics[0] == maxStat)
		{
			adviceLine = "Decrease estimated time for " + difficulty + " tasks";
		}
		//there are more entries over the estimated time than anything else for this difficulty
		else if(statistics[1] == maxStat)
		{
			adviceLine = "Increase estimated time for " + difficulty + " tasks";
		}
		else if(statistics[2] == maxStat)
		{
			adviceLine = "Estimated time for " + difficulty + " tasks is correct";
		}
		return adviceLine;
	}
}
