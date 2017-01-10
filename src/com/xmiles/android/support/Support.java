package com.xmiles.android.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.format.DateUtils;
import android.util.Log;

public class Support {
	
	//TAG
	private static String TAG = "FACEBOOK";  

	// constructors
	public Support() {

	}
	/**
	 * get datetime
	 * */
	public String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	
	public String getAppversionName(Context c) {
		
		String app_ver = null;
		try
		{
		    app_ver = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
		    
		}
		catch (NameNotFoundException e)
		{
		    Log.e(TAG, e.getMessage());
		}
		
		return app_ver;
	}
	
	public String getAppversionCode(Context c) {
		
		int app_ver = 0;
		try
		{
		    app_ver = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
		    
		}
		catch (NameNotFoundException e)
		{
		    Log.e(TAG, e.getMessage());
		}
		
		return Integer.toString(app_ver);
	}	
	
	public String getHour() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public String DiffTime(String time1, String time2){
		
		//String lala = Locale.getDefault();

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Difference is in milliseconds/1000 = seconds.
		long difference = (date1.getTime() - date2.getTime())/1000; 
		
		return Long.toString(difference);
	}
	
	public String getDateTime_long(String time1){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		Date d;
		long milliseconds = 0;
		
		try {
			d = dateFormat.parse(time1);
			milliseconds = d.getTime();
			
			//Log.e(TAG,"getDateTime(): " + getDateTime());
			//Log.d(TAG,"milliseconds: " + milliseconds);
			


		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return Long.toString(milliseconds);
		//return milliseconds;
	}
	
	
	
	public String fixDateTime(String timeStamp) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
		String day   		  = timeStamp.split("-")[1];
		String month 		  = timeStamp.split("-")[0];
		String year_and_hour  = timeStamp.split("-")[2];
		//String hour  = timeStamp.split(" ")[1];
		
		String dateInString = year_and_hour.split(" ")[0] + "-" + month + "-" + day + " " + year_and_hour.split(" ")[1];
		//Date date = new Date();
		//return dateFormat.format(date);
		java.util.Date date = null;
		try {
			date = dateFormat.parse(dateInString);			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dateFormat.format(date);
	}
	
	public String getLocalTime(String timeStamp){
		
		String hour = timeStamp.split("T")[1].substring(0, 
					  timeStamp.split("T")[1].length() - 5);
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateInString = timeStamp.split("T")[0] + " " + hour;										  
	
		TimeZone tz = TimeZone.getTimeZone("UTC");
		formatter.setTimeZone(tz);
		java.util.Date date = null;
		try {
			date = formatter.parse(dateInString);
			String UTCtime = formatter.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		// To TimeZone Brazil/East
		SimpleDateFormat sdfBrazil = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone tzInBrazil = TimeZone.getDefault();
		sdfBrazil.setTimeZone(tzInBrazil);					
		String sDateBrazil = sdfBrazil.format(date); // Convert to String first
		/*
		Log.v(TAG, "UTCtime: " + UTCtime);
		Log.i(TAG, "dateInBrazil: " + sDateBrazil);		
		Log.v(TAG, "lala: " + getDateTime());
		*/
		
		return sDateBrazil;
		
	}
	
}
