package com.xmiles.android.support;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xmiles.android.R;
import com.xmiles.android.webservice.DataRioHttpGetAsyncTask;
import com.xmiles.android.webservice.HttpGetAsyncTask;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

public class Score_Algorithm {

	private final Context mContext;	
	private static final String TAG = "FACEBOOK";
	
	public Score_Algorithm(Context ctx) {
		this.mContext = ctx;		
		//Log.w(TAG,"Score_Algorithm: " + "");
		
	}
	
    public JSONObject getBusPosition(String url, String buscode) {

    	//JSONArray jsonArray;
    	JSONObject json;
    	//String url = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/onibus/";
    	try {
    		
    		json = new JSONObject(new DataRioHttpGetAsyncTask().execute(url + buscode).get());
	     } catch (Exception e) {
	         throw new RuntimeException(e);
	     }

    	return json;
    }
    
    
    public JSONObject getBrtPosition(String url, String buscode) {

    	//JSONArray jsonArray;
    	JSONObject json;
    	//String url = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/brt/";
    	try {
    		
    		json = new JSONObject(new DataRioHttpGetAsyncTask().execute(url + buscode).get());
	     } catch (Exception e) {
	         throw new RuntimeException(e);
	     }

    	return json;
    }
}
