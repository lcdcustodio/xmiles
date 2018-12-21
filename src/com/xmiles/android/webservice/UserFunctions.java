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

    private static String loginURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String registerURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    
    private static String uberURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    								

    private static String buslineURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";

    private static String user_routesURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String user_locationURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    //private static String bus_gpsURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String bus_gpsURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/busgps_php/";
    private static String user_rewardsURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String gps_notfoundURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String user_rankingURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String news_feedURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String buscodeURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String newsfeed_inboxURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/newsfeed_inbox_php/";
    //private static String newsfeed_inboxURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String post_actionsURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String likes_inboxURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String comments_inboxURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    //------------------------------------
    private static String news_feed_by_hashtagURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String inviteFriendsURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String api_buscodeURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    private static String upload_friendsURL = "http://ec2-52-90-92-199.compute-1.amazonaws.com/xmiles/login/";
    //------------------------------------
    private static String login_tag 		= "login";
    private static String register_tag 		= "register";

    private static String busline_tag 		= "busline";

    private static String user_routes_tag 	= "user_routes";
    private static String user_location_tag = "userlocation_register";
    //private static String bus_gps_tag 		= "busgps_register";
    private static String bus_gps_tag 		= "busgps_txt";
    private static String user_rewards_tag  = "rewards";
    private static String gps_notfound_tag  = "gps_notfound";
    private static String user_ranking_tag  = "ranking";
    private static String news_feed_tag  = "newsfeed";
    private static String buscode_tag  = "buscode";
    private static String newsfeed_inbox_tag = "newsfeed_inbox";
    private static String post_actions_tag = "newsfeed_actions";
    private static String likes_inbox_tag = "likes_inbox";
    private static String comments_inbox_tag = "comments_inbox";
    private static String news_feed_by_hashtag_tag = "newsfeed_by_hashtag";
    private static String invite_friends_tag = "invite_friends";
    private static String api_buscode_tag = "api_buscode";
    private static String upload_friends_tag = "upload_friends";
    
    private static String uber_token_tag = "uber_token";
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
    //public JSONObject loginUser(String id, String name, String picURL, String device, String android_api,
    //public JSONObject loginUser(String id, String name, String device, String android_api,
    //		String AppversionName, String AppversionCode, String access_type){

    public JSONObject loginUser(String id, String name, String device, String android_api,
    		String AppversionName, String AppversionCode, String access_type, String uber_package){    
    
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("device", device));
        params.add(new BasicNameValuePair("fb_id", id));
        params.add(new BasicNameValuePair("name", name));
        //params.add(new BasicNameValuePair("picURL", picURL));
        //------
        params.add(new BasicNameValuePair("android_api", android_api));
        params.add(new BasicNameValuePair("app_ver_name", AppversionName));
        params.add(new BasicNameValuePair("app_ver_code", AppversionCode));
        params.add(new BasicNameValuePair("access_type", access_type));
        //------
        params.add(new BasicNameValuePair("uber_package", uber_package));
        //------
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        //Log.i(TAG, "loginUser " + json.toString());
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
        //Log.i(TAG, "gps_notfoundURL " + json.toString());
        return json;
    }

    public JSONObject getRanking(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", user_ranking_tag));
        JSONObject json = jsonParser.getJSONFromUrl(user_rankingURL, params);
        // return json
        //Log.w(TAG, "getRanking " + json.toString());
        return json;
    }

    public JSONObject getNewsfeed(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fb_id", id));
        params.add(new BasicNameValuePair("tag", news_feed_tag));
        JSONObject json = jsonParser.getJSONFromUrl(news_feedURL, params);
        // return json
        //Log.w(TAG, "getNewsfeed " + json.toString());
        return json;
    }

    public JSONObject getNewsfeed_by_hashtag(String id, String hashtag){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("fb_id", id));
        params.add(new BasicNameValuePair("hashtag", hashtag));
        params.add(new BasicNameValuePair("tag", news_feed_by_hashtag_tag));
        JSONObject json = jsonParser.getJSONFromUrl(news_feed_by_hashtagURL, params);
        // return json
        //Log.w(TAG, "getNewsfeed_by_hashtag" + json.toString());
        return json;
    }

    public JSONObject getPost_actions(String feed_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("feed_id", feed_id));
        params.add(new BasicNameValuePair("tag", post_actions_tag));
        JSONObject json = jsonParser.getJSONFromUrl(post_actionsURL, params);
        // return json
        //Log.w(TAG, "getPost_actions " + json.toString());
        return json;
    }

    public JSONObject getBuscode_details(String buscode){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("buscode", buscode));
        params.add(new BasicNameValuePair("tag", buscode_tag));
        JSONObject json = jsonParser.getJSONFromUrl(buscodeURL, params);
        // return json
        //Log.w(TAG, "getBuscode Test" + json.toString());
        return json;
    }

    public JSONObject getApiBuscode(String buscode_digits, String buscode){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("buscode", buscode));
        params.add(new BasicNameValuePair("buscode_digits", buscode_digits));
        params.add(new BasicNameValuePair("tag", api_buscode_tag));
        JSONObject json = jsonParser.getJSONFromUrl(api_buscodeURL, params);
        // return json
        //Log.w(TAG, "getApiBuscode: " + json.toString());
        return json;
    }


    public JSONObject getRewards(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", user_rewards_tag));
        JSONObject json = jsonParser.getJSONFromUrl(user_rewardsURL, params);
        // return json
        //Log.w(TAG, "getRewards " + json.toString());
        return json;
    }


    public JSONObject comments_inbox(String feed_id,
    								 String sender,
								     String user_id,
						             String flag_action,
						             String time_stamp,
						             String status,
						             String feed_type,
						             String comment){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", comments_inbox_tag));

		params.add(new BasicNameValuePair("feed_id", feed_id));
		//----------
		params.add(new BasicNameValuePair("sender", sender));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("flag_action", flag_action));
		params.add(new BasicNameValuePair("time_stamp", time_stamp));
		//----------
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("feed_type", feed_type));
		params.add(new BasicNameValuePair("comment", comment));
		//----------
		JSONObject json = jsonParser.getJSONFromUrl(comments_inboxURL, params);

		// return json
		//Log.e(TAG, "comments_inbox " + json.toString());
		return json;
	}


    public JSONObject likes_inbox(String feed_id,
    							  String sender,
    							  String user_id,
						          String flag_action,
						          String time_stamp,
						          String status,
						          String feed_type){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", likes_inbox_tag));

		params.add(new BasicNameValuePair("feed_id", feed_id));
		//----------
		params.add(new BasicNameValuePair("sender", sender));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("flag_action", flag_action));
		params.add(new BasicNameValuePair("time_stamp", time_stamp));
		//----------
		params.add(new BasicNameValuePair("status", status));
		params.add(new BasicNameValuePair("feed_type", feed_type));
		//----------
		JSONObject json = jsonParser.getJSONFromUrl(likes_inboxURL, params);

		// return json
		//Log.e(TAG, "likes_inbox " + json.toString());
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
    						         String hashtag,
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
        params.add(new BasicNameValuePair("hashtag", hashtag));
        params.add(new BasicNameValuePair("time_stamp", time_stamp));
        //----------
        JSONObject json = jsonParser.getJSONFromUrl(newsfeed_inboxURL, params);

        // return json
        //Log.e(TAG, "newsfeed_inbox " + json.toString());
        return json;
    }

    public JSONObject inviteFriends(String user_id,
    								String request_id,
    								StringBuilder friends_id,
    								//String friends_id,
    								String flag_action){

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("tag", invite_friends_tag));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("request_id", request_id));
		params.add(new BasicNameValuePair("friends_id", friends_id.toString()));
		params.add(new BasicNameValuePair("flag_action", flag_action));

		JSONObject json = jsonParser.getJSONFromUrl(inviteFriendsURL, params);

		//Log.e(TAG, "Invite_Friends " + json.toString());
		return json;
	}
    
    public JSONObject uberToken(String id, String uber_code){
       	
        
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", uber_token_tag));
        
        params.add(new BasicNameValuePair("user_id", id));
        params.add(new BasicNameValuePair("uber_code", uber_code));
        
        JSONObject json = jsonParser.getJSONFromUrl(uberURL, params);

        return json;
    }        

    public JSONObject uploadFriends(String user_id, StringBuilder friends_id){

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("tag", upload_friends_tag));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("friends_id", friends_id.toString()));

		JSONObject json = jsonParser.getJSONFromUrl(upload_friendsURL, params);

		//Log.e(TAG, "Upload_Friends " + json.toString());


    	return json;
    }


    public JSONObject busGps(StringBuilder gps_bus_id,
			   				 StringBuilder user_id,
			   				 StringBuilder latitude,
			   				 StringBuilder longitude,
			   				 //StringBuilder speed,
			   				 StringBuilder bustype,
			   				 StringBuilder buscode,
			   				 StringBuilder busline,
			   				 //StringBuilder direction,
			   				 StringBuilder bus_hashcode,
			   				 StringBuilder created_at,
			   				 //-------------------------
			   				 StringBuilder u_locat_id,
			   				 StringBuilder u_latitude,
			   				 StringBuilder u_longitude,
			   				 //StringBuilder u_speed,
			   				 StringBuilder u_hashcode,
			   				 StringBuilder u_locat_provider,
			   				 StringBuilder u_created_at,
			   				 StringBuilder u_diff_dist,
			   				 StringBuilder u_diff_time,
			   				 //StringBuilder u_locat_status,
			   				StringBuilder u_status,
			   				 StringBuilder u_accuracy//,
			   				 //-------------------------
			   				 //StringBuilder score
			   			     //-------------------------
			   				 ){

	// Building Parameters
	List<NameValuePair> params = new ArrayList<NameValuePair>();

	params.add(new BasicNameValuePair("tag", bus_gps_tag));
	params.add(new BasicNameValuePair("gps_bus_id", gps_bus_id.toString()));
	params.add(new BasicNameValuePair("user_id", user_id.toString()));
	params.add(new BasicNameValuePair("latitude", latitude.toString()));
	params.add(new BasicNameValuePair("longitude", longitude.toString()));
	//params.add(new BasicNameValuePair("speed", speed.toString()));
	params.add(new BasicNameValuePair("bustype", bustype.toString()));
	params.add(new BasicNameValuePair("buscode", buscode.toString()));
	params.add(new BasicNameValuePair("busline", busline.toString()));
	//params.add(new BasicNameValuePair("direction", direction.toString()));
	params.add(new BasicNameValuePair("bus_hashcode", bus_hashcode.toString()));
	params.add(new BasicNameValuePair("created_at", created_at.toString()));
	//-------------------------------------------------
	params.add(new BasicNameValuePair("u_locat_id", u_locat_id.toString()));
	params.add(new BasicNameValuePair("u_latitude", u_latitude.toString()));
	params.add(new BasicNameValuePair("u_longitude", u_longitude.toString()));
	//params.add(new BasicNameValuePair("u_speed", u_speed.toString()));
	params.add(new BasicNameValuePair("u_hashcode", u_hashcode.toString()));
	params.add(new BasicNameValuePair("u_locat_provider", u_locat_provider.toString()));
	params.add(new BasicNameValuePair("u_created_at", u_created_at.toString()));
	params.add(new BasicNameValuePair("u_diff_dist", u_diff_dist.toString()));
	params.add(new BasicNameValuePair("u_diff_time", u_diff_time.toString()));
	//params.add(new BasicNameValuePair("u_locat_status", u_locat_status.toString()));
	params.add(new BasicNameValuePair("u_status", u_status.toString()));
	params.add(new BasicNameValuePair("u_accuracy", u_accuracy.toString()));
	//-------------------------------------------------
	//params.add(new BasicNameValuePair("score", score.toString()));
	//-------------------------------------------------

	JSONObject json = jsonParser.getJSONFromUrl(bus_gpsURL, params);

	//Log.e(TAG, "Bus_Gps " + json.toString());
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

        //Log.e(TAG, "USer_Location " + json.toString());
        return json;
    }



    public JSONObject userRoutes(String user_id, String favorite_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", user_routes_tag));
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("favorite_id", favorite_id));
        JSONObject json = jsonParser.getJSONFromUrl(user_routesURL, params);

        //Log.e(TAG, "USer_Routes " + json.toString());
        return json;
    }




    public JSONObject busline(String id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", busline_tag));
        params.add(new BasicNameValuePair("city", id));
        JSONObject json = jsonParser.getJSONFromUrl(buslineURL, params);
        // return json
        //Log.e(TAG, "Busline " + json.toString());
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
        //Log.e(TAG, "registerUser " + json.toString());
        return json;
    }

}