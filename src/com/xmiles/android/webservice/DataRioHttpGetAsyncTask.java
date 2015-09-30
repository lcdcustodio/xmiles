package com.xmiles.android.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

public class DataRioHttpGetAsyncTask  extends AsyncTask<String, Void, String> {
    
	//protected static JSONArray jsonArray;
	protected static String jsonArray;
	
	@Override
    protected String doInBackground(String... urls) {
          
        return GET(urls[0]);
    }
	// onPostExecute displays the results of the AsyncTask.	
    @Override
    protected void onPostExecute(String result) {    	

    	Log.w("FACEBOOK", "DataRio result: " + result);
    	
    	jsonArray = result;
    
   }

    
    public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			
	        // Cria o GET e define a URL
	        //HttpGet httpget=new HttpGet(ENDPOINT + linhaDeOnibus);
			HttpGet httpget=new HttpGet(url);	
	        // Define o tipo de dados para a correta execução do http GET
	        httpget.setHeader("Content-Type", "application/json");
	        httpget.setHeader("Accept", "application/json");
			
			// make GET request to the given URL
			//HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
	        HttpResponse httpResponse = httpclient.execute(httpget);
			
			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			
			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		
		} catch (Exception e) {
			Log.d("FACEBOOK", e.getLocalizedMessage());
		}
		
		return result;
	} 
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        
        inputStream.close();
        return result;
        
    }
}
