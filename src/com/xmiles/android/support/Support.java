package com.xmiles.android.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class Support {

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
