/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpsc481.mytasks.chart;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import com.cpsc481.mytasks.databasestorage.TaskDatabaseHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

/**
 * Budget demo pie chart.
 */
public class PieChart extends AbstractChart 
{
	public static String COMPLETION_TYPE = "completion";
	public static String ONTIME_TYPE = "onTime";
	public static String EASY_STAT_TYPE = "easyChart";
	public static String MED_STAT_TYPE = "mediumChart";
	public static String HARD_STAT_TYPE = "hardChart";
	
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Progress Report";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "The status of completed to in progress tasks";
  }

  /**
   * Executes the chart. Builds the appropriate chart from the values based on the type
   * of chart.
   * 
   * @param context the context
   * @param values The values being passed to build the pie chart.
   * @param type The type of chart to be created. From the public static fields in this class.
   * @return the built intent
   */  
  public Intent execute(Context context, double [] values, String type)
  { 
	  
	    int[] colors = new int[] {Color.BLUE, Color.GREEN}; 
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setZoomButtonsVisible(false);
	    renderer.setZoomEnabled(false);
	    renderer.setChartTitleTextSize(30);
	    renderer.setLabelsColor(Color.BLACK);
	    renderer.setPanEnabled(false);
	    
	    int[] colors2 = new int[] {Color.BLUE, Color.RED, Color.GREEN}; 
	    DefaultRenderer renderer2 = buildCategoryRenderer(colors2);
	    renderer2.setZoomButtonsVisible(false);
	    renderer2.setZoomEnabled(false);
	    renderer2.setChartTitleTextSize(30);
	    renderer2.setLabelsColor(Color.BLACK);
	    renderer2.setPanEnabled(false);
	    
	    Intent t = null;
	    
	    if(type.equals(COMPLETION_TYPE))
	    {
	    	t = ChartFactory.getPieChartIntent(context, buildCategoryDataset("Tasks Completed Vs In Progress", values, type), renderer, "Completion Percent");
	    }
	    else if(type.equals(ONTIME_TYPE))
	    {
	    	t = ChartFactory.getPieChartIntent(context, buildCategoryDataset("Tasks Completed on time vs those not", values, type), renderer, "Tasks Completed On Time");
	    }
	    else if(type.equals(EASY_STAT_TYPE))
	    {
	    	
	    	t = ChartFactory.getPieChartIntent(context, buildCategoryDataset(TaskDatabaseHelper.DIFFICULTY_ONE + " tasks and time completed",
	    			values, type), renderer2, TaskDatabaseHelper.DIFFICULTY_ONE + " Task Completion");
	    }
	    else if(type.equals(MED_STAT_TYPE))
	    {
	    	t = ChartFactory.getPieChartIntent(context, buildCategoryDataset(TaskDatabaseHelper.DIFFICULTY_TWO + " tasks and time completed",
	    			values, type), renderer2, TaskDatabaseHelper.DIFFICULTY_TWO + " Task Completion");
	    }
	    else if(type.equals(HARD_STAT_TYPE))
	    {
	    	t = ChartFactory.getPieChartIntent(context, buildCategoryDataset(TaskDatabaseHelper.DIFFICULTY_THREE + " tasks and time completed",
	    			values, type), renderer2, TaskDatabaseHelper.DIFFICULTY_THREE + " Task Completion");
	    }
	    return t;	
	  }
  

}
