package com.xmiles.android.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONArrayParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    //---------------------------
    static JSONArray jObj_rev2 = null;
    static JSONArray jArray  = null;
    private static final String TAG = "FACEBOOK";
    //---------------------------
    static String json = "";
 
    // constructor
    public JSONArrayParser() {
 
    }
 
    public JSONArray getJSONFromUrl(String url, List<NameValuePair> params) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();            
            //Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try{
            
            jArray = new JSONArray(json);
            /*
            for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Log.i(TAG,"facebook_id: "+json_data.getString("facebook_id"));
                    Log.i(TAG,"name: "+json_data.getString("name"));
                    Log.i(TAG,"email: "+json_data.getString("email"));
                    Log.i(TAG,"gender: "+json_data.getString("gender"));
                    Log.i(TAG,"picURL: "+json_data.getString("picURL"));

            }
            */
	    }
	    catch(JSONException e){
	            Log.e("log_tag", "Error parsing data "+e.toString());
	    }        
 
        return jArray;
 
    }
}
