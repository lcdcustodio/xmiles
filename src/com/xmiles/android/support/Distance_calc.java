package com.xmiles.android.support;

public class Distance_calc {
	
	private double result;
	
	public double calculo(Double lat1, float lat, Double long1, float l){
		
  	    //result = 6371*Math.acos(Math.cos(Math.PI*(90-jsonObject_location.getDouble("latitude"))/180)*Math.cos((90-Lat)*Math.PI/180) +
        //        	 Math.sin((90-jsonObject_location.getDouble("latitude"))*Math.PI/180)*Math.sin((90-Lat)*Math.PI/180)*Math.cos((Long-jsonObject_location.getDouble("longitude"))*Math.PI/180));
        //***********************************************
		//-- Formula da distancia entre 02 pontos (Km)---
  	    result = 6371*Math.acos(Math.cos(Math.PI*(90-lat1)/180)*Math.cos((90-lat)*Math.PI/180) +
           	 Math.sin((90-lat1)*Math.PI/180)*Math.sin((90-lat)*Math.PI/180)*Math.cos((l-long1)*Math.PI/180));
        //***********************************************  	    
		return result;
	}

}
