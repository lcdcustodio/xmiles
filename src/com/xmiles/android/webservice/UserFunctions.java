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
    
    private static String loginURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String registerURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String favoritesURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String insert_favoritesURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String buslineURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String bus_stopURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String user_routesURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String user_locationURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String bus_gpsURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String user_rewardsURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String gps_notfoundURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String user_rankingURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String news_feedURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String buscodeURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    private static String newsfeed_inboxURL = "http://ec2-54-209-160-58.compute-1.amazonaws.com/xmiles/login/";
    //------------------------------------    
    private static String login_tag 		= "login";
    private static String register_tag 		= "register";
    private static String favorites_tag 	= "favorites";
    private static String insert_favorites_tag = "favorites_register";
    private static String busline_tag 		= "busline";
    private static String bus_stop_tag 		= "bus_stop";
    private static String user_routes_tag 	= "user_routes";
    private static String user_location_tag = "userlocation_register";
    private static String bus_gps_tag 		= "busgps_register";
    private static String user_rewards_tag  = "rewards";
    private static String gps_notfound_tag  = "gps_notfound";
    private static String user_ranking_tag  = "ranking";
    private static String news_feed_tag  = "newsfeed";
    private static String buscode_tag  = "buscode";
    private static String newsfeed_inbox_tag = "newsfeed_inbox";
    //------------------------------------     
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
    public JSONObject loginUser(String id, String name, String picURL, String device){

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("fb_id", id));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("picURL", picURL));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json        
        Log.i(TAG, "loginUser " + json.toString());
        return json;
    }
    
    public JSONObject gps_Notfound(String user_id, String name, String url, String buscode, String created_at){

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", gps_notfound_tag));
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("buscode", buscode));
        params.add(new BasicNameValuePair("url", url));
        params.add(new BasicNameValuePair("created_at", created_at));
        JSONObject json = jsonParser.getJSONFromUrl(gps_notfoundURL, params);
        // return json        
        Log.i(TAG, "gps_notfoundURL " + json.toString());
        return json;
    }

    public JSONObject getRanking(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", user_ranking_tag));
        JSONObject json = jsonParser.getJSONFromUrl(user_rankingURL, params);
        // return json        
        Log.w(TAG, "getRanking " + json.toString());
        return json;
    }
    
    public JSONObject getNewsfeed(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fb_id", id));
        params.add(new BasicNameValuePair("tag", news_feed_tag));
        JSONObject json = jsonParser.getJSONFromUrl(news_feedURL, params);
        // return json        
        Log.w(TAG, "getNewsfeed " + json.toString());
        return json;
    }

    public JSONObject getBuscode(String buscode){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("buscode", buscode));
        params.add(new BasicNameValuePair("tag", buscode_tag));
        JSONObject json = jsonParser.getJSONFromUrl(buscodeURL, params);
        // return json        
        Log.w(TAG, "getBuscode Test" + json.toString());
        return json;
    }    
    
    
    public JSONObject getRewards(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", user_rewards_tag));
        JSONObject json = jsonParser.getJSONFromUrl(user_rewardsURL, params);
        // return json        
        Log.w(TAG, "getRewards " + json.toString());
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
    
    public JSONObject newsFeed_inbox(String feed_id,
    						         String name,
    						         String image,
    						         String status,
    						         String profilepic,
    						         String url,
    						         String sender,
    						         String destination,
    						         String feed_type,
    						         String like_stats,
    						         String comment_stats,
    						         String flag_action,
    						         String time_stamp){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", newsfeed_inbox_tag));

        params.add(new BasicNameValuePair("feed_id", feed_id));
        //----------
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("image", image));
        params.add(new BasicNameValuePair("status", status));
        params.add(new BasicNameValuePair("profilepic", profilepic));
        params.add(new BasicNameValuePair("url", url));
        params.add(new BasicNameValuePair("sender", sender));
        params.add(new BasicNameValuePair("destination", destination));
        params.add(new BasicNameValuePair("feed_type", feed_type));
        params.add(new BasicNameValuePair("like_stats", like_stats));
        params.add(new BasicNameValuePair("comment_stats", comment_stats));
        params.add(new BasicNameValuePair("flag_action", flag_action));
        params.add(new BasicNameValuePair("time_stamp", time_stamp));
        //----------                
        JSONObject json = jsonParser.getJSONFromUrl(newsfeed_inboxURL, params);

        // return json        
        Log.e(TAG, "newsfeed_inbox " + json.toString());
        return json;
    }
    
    public JSONObject busGps(StringBuilder gps_bus_id, 
			   				 StringBuilder user_id, 
			   				 StringBuilder latitude,
			   				 StringBuilder longitude,
			   				 StringBuilder speed,
			   				 StringBuilder buscode,
			   				 StringBuilder busline,
			   				 StringBuilder direction,
			   				 StringBuilder created_at,
			   				 //-------------------------
			   				 StringBuilder u_locat_id,
			   				 StringBuilder u_latitude,
			   				 StringBuilder u_longitude,
			   				 StringBuilder u_speed,
			   				 StringBuilder u_locat_provider,
			   				 StringBuilder u_created_at,
			   				 StringBuilder u_diff_dist,
			   				 StringBuilder u_diff_time,
			   				 StringBuilder u_locat_status,
			   				 StringBuilder u_accuracy,
			   				 //-------------------------
			   				 StringBuilder score
			   			     //-------------------------
			   				 ){

	// Building Parameters
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	
	params.add(new BasicNameValuePair("tag", bus_gps_tag));
	params.add(new BasicNameValuePair("gps_bus_id", gps_bus_id.toString()));
	params.add(new BasicNameValuePair("user_id", user_id.toString()));
	params.add(new BasicNameValuePair("latitude", latitude.toString()));
	params.add(new BasicNameValuePair("longitude", longitude.toString()));        
	params.add(new BasicNameValuePair("speed", speed.toString()));
	params.add(new BasicNameValuePair("buscode", buscode.toString()));
	params.add(new BasicNameValuePair("busline", busline.toString()));
	params.add(new BasicNameValuePair("direction", direction.toString()));		   
	params.add(new BasicNameValuePair("created_at", created_at.toString()));
	//-------------------------------------------------
	params.add(new BasicNameValuePair("u_locat_id", u_locat_id.toString()));
	params.add(new BasicNameValuePair("u_latitude", u_latitude.toString()));
	params.add(new BasicNameValuePair("u_longitude", u_longitude.toString()));
	params.add(new BasicNameValuePair("u_speed", u_speed.toString()));
	params.add(new BasicNameValuePair("u_locat_provider", u_locat_provider.toString()));
	params.add(new BasicNameValuePair("u_created_at", u_created_at.toString()));
	params.add(new BasicNameValuePair("u_diff_dist", u_diff_dist.toString()));
	params.add(new BasicNameValuePair("u_diff_time", u_diff_time.toString()));
	params.add(new BasicNameValuePair("u_locat_status", u_locat_status.toString()));
	params.add(new BasicNameValuePair("u_accuracy", u_accuracy.toString()));
	//-------------------------------------------------
	params.add(new BasicNameValuePair("score", score.toString()));
	//-------------------------------------------------
		
	JSONObject json = jsonParser.getJSONFromUrl(bus_gpsURL, params);
	
	Log.e(TAG, "Bus_Gps " + json.toString());
	return json;
	}
    
    
    public JSONObject userLocation(StringBuilder user_location_id, 
    							   StringBuilder user_id, 
    							   StringBuilder latitude,
    							   StringBuilder longitude,
    							   StringBuilder speed,
    							   StringBuilder location_provider,
    							   StringBuilder bus_stop_radius_flag,
    							   StringBuilder bus_stop_radius_count,
    							   StringBuilder favorite_id,
    							   StringBuilder bus_stop_id,
    							   StringBuilder created_at){
    	    	
    	// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        
	    params.add(new BasicNameValuePair("tag", user_location_tag));
	    params.add(new BasicNameValuePair("user_location_id", user_location_id.toString()));
	    params.add(new BasicNameValuePair("user_id", user_id.toString()));
	    params.add(new BasicNameValuePair("latitude", latitude.toString()));
	    params.add(new BasicNameValuePair("longitude", longitude.toString()));        
	    params.add(new BasicNameValuePair("speed", speed.toString()));
	    params.add(new BasicNameValuePair("location_provider", location_provider.toString()));
	    params.add(new BasicNameValuePair("bus_stop_radius_flag", bus_stop_radius_flag.toString()));
	    params.add(new BasicNameValuePair("bus_stop_radius_count", bus_stop_radius_count.toString()));		   
	    params.add(new BasicNameValuePair("favorite_id", favorite_id.toString()));
	    params.add(new BasicNameValuePair("bus_stop_id", bus_stop_id.toString()));
	    params.add(new BasicNameValuePair("created_at", created_at.toString()));
 
        
        JSONObject json = jsonParser.getJSONFromUrl(user_locationURL, params);
        
        Log.e(TAG, "USer_Location " + json.toString());
        return json;
    }

    
    
    public JSONObject userRoutes(String user_id, String favorite_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", user_routes_tag));
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("favorite_id", favorite_id));
        JSONObject json = jsonParser.getJSONFromUrl(user_routesURL, params);
        
        Log.e(TAG, "USer_Routes " + json.toString());
        return json;
    }

    
    public JSONObject insert_favoritesRoutes(String user_id, String name, String busline, String _from, String _to,
    	    String city, String uf, String _to_bus_stop_id, String _from_bus_stop_id, String favorite_id){

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
        params.add(new BasicNameValuePair("favorite_id", favorite_id));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(insert_favoritesURL, params);

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

}