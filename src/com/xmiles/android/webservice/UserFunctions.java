package com.xmiles.android.webservice;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 

import android.util.Log;
 
public class UserFunctions {
     
    private JSONParser jsonParser;
    //----------------------------------
    private JSONArrayParser jsonArrayParser;
    //----------------------------------
    private static final String TAG = "FACEBOOK";
    
    //private static String loginURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/mazaah/login/";
    //private static String registerURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/mazaah/login/"; 
    private static String loginURL = "http://ec2-54-152-39-25.compute-1.amazonaws.com/mazaah/login/";
    private static String registerURL = "http://ec2-54-152-39-25.compute-1.amazonaws.com/mazaah/login/"; 
    
    
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String face2me_users_tag = "face2me_users";
     
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
        //-------------------
        jsonArrayParser = new JSONArrayParser();
        //-------------------
    }
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    //public JSONObject loginUser(String email, String password){
    public JSONObject loginUser(String email, String id){	
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("facebook_id", id));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        Log.e(TAG, "loginUser " + json.toString());
        return json;
    }
     
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    //public JSONObject registerUser(String name, String email, String password){
    public JSONObject registerUser(String name, String email, String id, String gender, String picURL){	
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("facebook_id", id));
        params.add(new BasicNameValuePair("gender", gender));
        params.add(new BasicNameValuePair("picURL", picURL));
        //params.add(new BasicNameValuePair("picURL", "blablabla"));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        Log.e(TAG, "registerUser " + json.toString());        
        return json;
    }
    /* 
    public JSONArray face2me_users() {
    	// ----------------------------
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", face2me_users_tag));
        // ----------------------------
        JSONArray contacts = null;
        // ----------------------------
		contacts = jsonArrayParser.getJSONFromUrl(face2me_users, params);
		//Log.e(TAG, "JSONArray " + contacts.toString());
        return contacts;
    }
    */
}