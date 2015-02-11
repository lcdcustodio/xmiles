package com.xmiles.android.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	
}
