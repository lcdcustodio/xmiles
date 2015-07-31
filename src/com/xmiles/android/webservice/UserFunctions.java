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
    private static String loginURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String registerURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String favoritesURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String insert_favoritesURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String buslineURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String bus_stopURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    
    //private static String loginURL = "http://ec2-54-152-39-25.compute-1.amazonaws.com/mazaah/login/";
    //private static String registerURL = "http://ec2-54-152-39-25.compute-1.amazonaws.com/mazaah/login/"; 
    
    
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String favorites_tag = "favorites";
    private static String insert_favorites_tag = "favorites_register";
    private static String busline_tag = "busline";
    private static String bus_stop_tag = "bus_stop";
     
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
    //public JSONObject loginUser(String email, String id){
    public JSONObject loginUser(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        //params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("fb_id", id));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json        
        Log.e(TAG, "loginUser " + json.toString());
        return json;
    }

    public JSONObject favoritesRoutes(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", favorites_tag));
        //params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("fb_id", id));
        JSONObject json = jsonParser.getJSONFromUrl(favoritesURL, params);
        // return json        
        Log.e(TAG, "favorites_Routes " + json.toString());
        return json;
    }
    
    public JSONObject insert_favoritesRoutes(String user_id, String name, String busline, String _from, String _to,
    	    String city, String uf, String _to_bus_stop_id, String _from_bus_stop_id){
		    //String city, String uf, int _to_bus_stop_id, int _from_bus_stop_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", insert_favorites_tag));
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("busline", busline));
        params.add(new BasicNameValuePair("_from", _from));
        params.add(new BasicNameValuePair("_to", _to));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("uf", uf));
        params.add(new BasicNameValuePair("_to_bus_stop_id", _to_bus_stop_id));
        params.add(new BasicNameValuePair("_from_bus_stop_id", _from_bus_stop_id));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(insert_favoritesURL, params);
        //JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        Log.e(TAG, "insert_favoritesRoutes " + json.toString());        
        return json;
    }

    
    public JSONObject busline(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", busline_tag));
        params.add(new BasicNameValuePair("city", id));
        JSONObject json = jsonParser.getJSONFromUrl(buslineURL, params);
        // return json        
        Log.e(TAG, "Busline " + json.toString());
        return json;
    }

    public JSONObject bus_stop(String bl, String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", bus_stop_tag));
        params.add(new BasicNameValuePair("busline", bl));
        params.add(new BasicNameValuePair("city", id));
        JSONObject json = jsonParser.getJSONFromUrl(bus_stopURL, params);
        // return json        
        Log.e(TAG, "Bus Stop " + json.toString());
        return json;
    }
    
    
    /**
     * function make Login Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String id, String gender, String picURL){	
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        //params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("fb_id", id));
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