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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    // constructor
    public JSONParser() {
 
    }
 
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            //httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            //-----
            /*
            Log.i("FACEBOOK ", "httpPost.getURI(): " + httpPost.getURI().toString());
            Log.w("FACEBOOK", "httpPost.getRequestLine(): " + httpPost.getRequestLine().toString());
            Log.e("FACEBOOK", "httpPost.getEntity(): " + httpPost.getEntity().toString());
            Log.v("FACEBOOK", "httpPost.getAllHeaders(): " + httpPost.getAllHeaders().toString());
            */            
            //-----
 
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
            //BufferedReader reader = new BufferedReader(new InputStreamReader(
            //        is, "iso-8859-1"), 8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //sb.append(line + "n");
                sb.append(line + "\n");
                //-----
                Log.i("FACEBOOK", line);
                //-----
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);           
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
    /*
    public JSONObject getJSONFromHttpGet(String url) {
    	 
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            //-----
            //-----
 
            HttpResponse httpResponse = httpClient.execute(httpGet);           
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            //------------
            Log.d("FACEBOOK", "httpResponse.toString(): " + httpResponse.toString());
            Log.d("FACEBOOK", "httpEntity.getContent(): " + is);
            //------------
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(
            //        is, "iso-8859-1"), 8);
        	//BufferedReader reader = new BufferedReader( new InputStreamReader(is));
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
        	//----------
        	Log.d("FACEBOOK", "reader: " + reader);
        	//----------        	
        	
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //sb.append(line + "n");
                sb.append(line + "\n");
                //-----
                Log.i("FACEBOOK", line);
                //-----
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);           
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
    */
}
