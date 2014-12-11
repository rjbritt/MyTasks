package com.cpsc481.mytasks.customclasses;

public class DateAndTime 
{
	/*
	 * These methods are for disseminating the month, year, and day from a combined date.
	 */
	
	public static String printDate(int date)
	{
		return "" +  getMonth(date) + "/" +  getDay(date) + "/" +  getYear(date);
	}
	public static int getMonth(int date)
	{
		return date/1000000;
	}
	
	public static int getDay(int date)
	{
		return ((date%1000000)/10000);
	}
	public static int getYear(int date)
	{
		return ((date%1000000)%10000);
	}
	
	
	/*
	 * These methods are for changing 24 hr time to 12 hr time
	 */
	
	public static String printTime(int time)
	{
		String temp;
		if(getMinute(time)<10)
		{
			temp = "" + getHour(time) + ":0" +  getMinute(time) +" " +  amOrPm(time);
		}
		else
		{
			temp ="" + getHour(time) + ":" +  getMinute(time) +" " +  amOrPm(time);
		}
		
		return temp;
	}
	
	public static int getHour(int time)
	{
		int hour = time/100;
		hour %= 12;
		
		if(hour == 0)
		{
			hour = 12;
		}
		
		return hour;
	}
	
	public static int getMinute(int time)
	{
		return time%100;
	}
	
	public static String amOrPm(int time)
	{
		String temp;
		
		if(0<= time && time <1200)
		{
			temp = "AM";
		}
		else
		{
			temp = "PM";
		}
		
		return temp;
	}

	/*
	 * This method is for turning month, day, and year into a single digit
	 */
	public static int combineDate(int month, int day, int year)
	{
		// Algorithm to make a single digit representing the date to be disseminated later.
		int monthMultiplied = (month + 1) * 1000000;// month is referenced beginning with January as zero
		int dayMultiplied = day * 10000; 
		int finalDate = monthMultiplied + dayMultiplied + year;
		
		return finalDate;
	}
	
	/*
	 * This method is for turning hours and minutes into a 24 hr number
	 * 
	 */
	public static int combineTime(int hour, int minute)
	{
		return (hour * 100) + minute;
	}
	
}
