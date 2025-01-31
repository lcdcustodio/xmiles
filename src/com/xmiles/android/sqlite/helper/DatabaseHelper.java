package com.xmiles.android.sqlite.helper;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String TAG = "FACEBOOK";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "xmiles";

	// Table Names
	private static final String TABLE_USER_PROFILE = "user_profile";
	public static final String TABLE_USER_PLACES = "user_places";
	public static final String TABLE_USER_FRIENDS = "user_friends";
	//public static final String TABLE_USER_FAVORITES = "user_favorites";
	public static final String TABLE_CITY_BUSLINE = "city_busline";
	public static final String TABLE_USER_ROUTES = "user_routes";
	public static final String TABLE_USER_ROUTES_FLAG = "user_routes_flag";
	public static final String TABLE_USER_LOCATION = "user_location";
	//-----------------
	public static final String TABLE_BUS_GPS_DATA = "bus_gps_data";
	public static final String TABLE_BUS_GPS_URL = "bus_gps_url";
	public static final String TABLE_REWARDS = "rewards";
	public static final String TABLE_RANKING = "ranking";
	//-----------------
	public static final String TABLE_NEWSFEED = "newsfeed";
	public static final String TABLE_NEWSFEED_UPLOAD = "newsfeed_upload";
	//-----------------
	public static final String TABLE_BUSCODE = "buscode";
	//-----------------	
	public static final String TABLE_LIKES = "likes";
	public static final String TABLE_LIKES_UPLOAD = "likes_upload";
	public static final String TABLE_COMMENTS_UPLOAD = "comments_upload";	
	//-----------------
	public static final String TABLE_NEWSFEED_BY_HASHTAG = "newsfeed_by_hashtag";
	
	public static final String TABLE_HISTORY = "history";
	
	// Common column names
	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_ID = "id"; // (= USER_ID)
	public static final String KEY_CREATED_AT = "created_at";

	// USER PROFILE Table - column names
	public static final String KEY_NAME = "name";
	public static final String KEY_PICTURE = "picture";
	public static final String KEY_DEVICE = "device";

	// USER PLACES Table - column names
	public static final String KEY_PLACE_ID = "place_id";
	public static final String KEY_NEARBY = "nearby";
	public static final String KEY_PICURL = "picUrl";
	public static final String KEY_CITY = "city";

	public static final String KEY_CATEGORY = "category";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_U_LATITUDE = "user_latitude";
	public static final String KEY_U_LONGITUDE = "user_longitude";
	public static final String KEY_P_LATITUDE = "place_latitude";
	public static final String KEY_P_LONGITUDE = "place_longitude";

	// USER FRIENDS Table - column names
	public static final String KEY_FRIEND_ID = "friend_id";
	public static final String KEY_FRIEND_NAME = "friend_name";
	public static final String KEY_FRIEND_PICURL = "friend_picUrl";
	public static final String KEY_FRIEND_LOCATION = "friend_location";
	public static final String KEY_FRIEND_DEVICE = "friend_device";

	// USER FAVORITES Table - column names
	public static final String KEY_FAVORITE_ID = "favorite_id";
	public static final String KEY_BUSLINE = "busline";
	public static final String KEY_FROM = "_from";
	public static final String KEY_TO = "_to";
	public static final String KEY_UF = "uf";
	public static final String KEY_TO_BUS_STOP_ID = "_to_bus_stop_id";
	public static final String KEY_FROM_BUS_STOP_ID = "_from_bus_stop_id";
	public static final String KEY_BD_UPDATED = "bd_updated";

	// USER ROUTES Table - column names
	public static final String KEY_BUS_STOP = "bus_stop";
	public static final String KEY_BUS_STOP_ID = "bus_stop_id";
	public static final String KEY_B_LATITUDE = "bus_stop_latitude";
	public static final String KEY_B_LONGITUDE = "bus_stop_longitude";
	public static final String KEY_M_DISTANCE_K = "max_distance_km";
	public static final String KEY_FLAG = "flag";




	// USER LOCATION Table - column names
	public static final String KEY_LOCATION_PROVIDER = "location_provider";
	public static final String KEY_SPEED			 = "speed";
	//-------------------------------------
	//-----------new columns---------------
	public static final String KEY_DIFF_DISTANCE     = "diff_distance";
	public static final String KEY_DIFF_TIME         = "diff_time";
	public static final String KEY_LOCATION_STATUS   = "location_status";
	public static final String KEY_ACCURACY          = "accuracy";
	//-------------------------------------
	//-------------------------------------
	// GPS BUS DATA Table - column names	
	public static final String KEY_BUSCODE   = "buscode";
	public static final String KEY_SENSE = "sense";
	public static final String KEY_DIRECTION = "direction";
	public static final String KEY_URL = "url";
	public static final String KEY_SCORE = "score";
	
	// GPS BUS_GPS_URL Table - column manes
	public static final String KEY_BUSLINE_DESCRIPTION   = "busline_description";
	public static final String KEY_BUSLINE_COMPANY       = "busline_company";
	public static final String KEY_BUS_TYPE       		 = "bus_type";
	public static final String KEY_HASHCODE       		 = "hash_code";
	
	// REWARDS Table - column names	
	public static final String KEY_REWARD   	= "reward";
	public static final String KEY_REWARD_TYPE  = "reward_type";
	public static final String KEY_QUANTITY 	= "quantity";
	
	// REWARDS Table - column names	
	public static final String KEY_RANK   	= "rank";
	
	// NEWSFEED Table - column names	
	public static final String KEY_IMAGE 	  = "image";
	public static final String KEY_STATUS 	  = "status";
	public static final String KEY_HASHTAG 	  = "hashtag";
	public static final String KEY_TIME_STAMP = "time_stamp";
	public static final String KEY_CUSTOM_TIME_STAMP = "custom_time_stamp";	
	//-------	
	public static final String KEY_LIKE_STATS = "like_stats";
	public static final String KEY_COMMENT_STATS = "comment_stats";

	public static final String KEY_FLAG_ACTION = "flag_action";  //NewsFeed Upload
	public static final String KEY_YOU_LIKE_THIS = "you_like_this";  //NewsFeed
	public static final String KEY_SENDER 	     = "sender";  //NewsFeed
	public static final String KEY_FEED_TYPE 	 = "feed_type";  //NewsFeed

	public static final String KEY_LAST_UPDATE   = "last_update"; //History
	
	// LIKE_UPLOAD Table - column names
	public static final String KEY_U_ID = "user_id";

	// COMMENT_UPLOAD Table - column names
	public static final String KEY_COMMENT = "comment";
	
	// Table Create Statements
	private static final String CREATE_TABLE_USER_PROFILE = "CREATE TABLE "
			+ TABLE_USER_PROFILE + "(" + KEY_ID + " TEXT," + KEY_NAME
			+ " TEXT," + KEY_PICTURE + " TEXT," +
			KEY_SCORE  + " TEXT," +
			KEY_RANK   + " TEXT," +			
			KEY_CREATED_AT	+ " DATETIME" + ")";


	private static final String CREATE_TABLE_USER_FRIENDS = "CREATE TABLE "
			//+ TABLE_USER_FRIENDS + "(" + KEY_FRIEND_ID + " TEXT," + KEY_FRIEND_NAME
			+ TABLE_USER_FRIENDS + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_FRIEND_ID + " TEXT," + KEY_FRIEND_NAME
			+ " TEXT," + KEY_FRIEND_PICURL + " TEXT," +
			KEY_FRIEND_LOCATION + " TEXT," +
			KEY_FRIEND_DEVICE + " TEXT," + KEY_CREATED_AT
			+ " DATETIME" + ")";


	private static final String CREATE_TABLE_USER_PLACES = "CREATE TABLE "
			//+ TABLE_USER_PLACES + "(" + KEY_PLACE_ID + " TEXT," + KEY_NEARBY +
			+ TABLE_USER_PLACES + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_PLACE_ID + " TEXT," + KEY_NEARBY + " TEXT," +
			KEY_PICURL + " TEXT," +
			KEY_CITY + " TEXT," +
			KEY_UF + " TEXT," +
			KEY_CATEGORY + " TEXT," +
			KEY_DISTANCE + " TEXT," +
			KEY_U_LATITUDE + " DOUBLE," +
			KEY_U_LONGITUDE + " DOUBLE," +
			KEY_P_LATITUDE + " DOUBLE," +
			KEY_P_LONGITUDE + " DOUBLE," +
			KEY_CREATED_AT	+ " DATETIME" + ")";
 


	private static final String CREATE_TABLE_USER_LOCATION = "CREATE TABLE "
			+ TABLE_USER_LOCATION + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_U_LATITUDE + " DOUBLE," +
			KEY_U_LONGITUDE + " DOUBLE," +
			//KEY_SPEED + " DOUBLE," +
			KEY_HASHCODE + " TEXT," +
			KEY_LOCATION_PROVIDER + " TEXT," +
			//-------------------------------
			//------- new columns------------
			//-------------------------------			
			KEY_DIFF_DISTANCE + " DOUBLE," +
			KEY_DIFF_TIME + " DOUBLE," +
			//KEY_LOCATION_STATUS + " TEXT," +
			KEY_STATUS + " TEXT," +
			KEY_ACCURACY + " DOUBLE," +
			//-------------------------------			
			//------- end new columns--------
			//-------------------------------			
			KEY_CREATED_AT	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_BUS_GPS_DATA = "CREATE TABLE "
			+ TABLE_BUS_GPS_DATA + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_CREATED_AT	+ " DATETIME," +
			KEY_BUSCODE + " TEXT," +
			KEY_BUSLINE + " TEXT," +
			KEY_B_LATITUDE + " DOUBLE," +
			KEY_B_LONGITUDE + " DOUBLE," +
			//KEY_SPEED + " DOUBLE," +
			//KEY_DIRECTION + " TEXT," +
			//KEY_SENSE + " TEXT," + 
			KEY_BUS_TYPE + " TEXT," +
			KEY_HASHCODE + " TEXT" + ")";
			//KEY_SCORE + " DOUBLE" + ")";
	
	private static final String CREATE_TABLE_BUS_GPS_URL = "CREATE TABLE "
			+ TABLE_BUS_GPS_URL + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_BUSCODE + " TEXT," +
			//KEY_URL + " TEXT," +
			KEY_BUS_TYPE + " TEXT," +
			KEY_HASHCODE + " TEXT," +
			//KEY_BUSLINE + " TEXT," +
			//KEY_BUSLINE_DESCRIPTION + " TEXT," +
			//KEY_BUSLINE_COMPANY + " TEXT," +
			KEY_FLAG + " integer" + ")";

	private static final String CREATE_TABLE_BUSCODE = "CREATE TABLE "
			+ TABLE_BUSCODE + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_BUSCODE + " TEXT," +
			KEY_BUSLINE + " TEXT," +
			KEY_HASHTAG + " TEXT," +
			KEY_BUSLINE_DESCRIPTION + " TEXT," +
			KEY_BUSLINE_COMPANY + " TEXT" + ")";



	private static final String CREATE_TABLE_RANKING = "CREATE TABLE "
			+ TABLE_RANKING + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," +
			KEY_NAME		+ " TEXT," + 
			KEY_PICURL 		+ " TEXT," +
			KEY_SCORE 		+ " TEXT,"  +
			KEY_CREATED_AT	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_NEWSFEED = "CREATE TABLE "
			+ TABLE_NEWSFEED + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," +
			KEY_NAME		+ " TEXT," +
			KEY_IMAGE		+ " TEXT," +
			KEY_STATUS		+ " TEXT," +			
			KEY_PICURL 		+ " TEXT," +
			//-------
			KEY_LIKE_STATS 	+ " TEXT," +
			KEY_COMMENT_STATS + " TEXT," +
			KEY_FEED_TYPE 	+ " TEXT," +
			//-------
			KEY_HASHTAG		+ " TEXT," +
			//-------			
			KEY_TIME_STAMP 	+ " DATETIME," + 
			KEY_CUSTOM_TIME_STAMP 	+ " DATETIME," +
			KEY_URL     	  + " TEXT," +
			KEY_SENDER     	  + " TEXT," +
			KEY_YOU_LIKE_THIS + " TEXT" + ")";
	
	private static final String CREATE_TABLE_HISTORY = "CREATE TABLE "
			+ TABLE_HISTORY + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," +
			KEY_STATUS		+ " TEXT," +			
			KEY_TIME_STAMP 	+ " DATETIME," +
			KEY_LAST_UPDATE + " DATETIME" + ")";

	

	private static final String CREATE_TABLE_NEWSFEED_UPLOAD = "CREATE TABLE "
			+ TABLE_NEWSFEED_UPLOAD + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," + // = feed_id
			KEY_NAME		+ " TEXT," +
			KEY_IMAGE		+ " TEXT," +
			KEY_STATUS		+ " TEXT," +			
			KEY_PICURL 		+ " TEXT," +
			//-------
			KEY_LIKE_STATS 	+ " TEXT," +
			KEY_COMMENT_STATS + " TEXT," +
			KEY_FEED_TYPE 	+ " TEXT," +
			//-------
			KEY_HASHTAG		+ " TEXT," +
			//-------			
			
			KEY_FLAG_ACTION + " TEXT," +
			//-------			
			KEY_TIME_STAMP 	+ " DATETIME," + 
			KEY_CUSTOM_TIME_STAMP 	+ " DATETIME," +
			//KEY_URL     	+ " TEXT" + ")";
			//----------------------------
			KEY_URL     	  + " TEXT," +
			KEY_SENDER     	  + " TEXT," +
			KEY_YOU_LIKE_THIS + " TEXT" + ")";

	
	private static final String CREATE_TABLE_LIKES = "CREATE TABLE "
			+ TABLE_LIKES + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," +
			KEY_NAME		+ " TEXT," + 
			KEY_PICURL 		+ " TEXT," +
			KEY_TIME_STAMP	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_LIKES_UPLOAD = "CREATE TABLE "
			+ TABLE_LIKES_UPLOAD + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," + // = feed_id
			KEY_U_ID        + " TEXT," +
			KEY_SENDER     	+ " TEXT," +
			KEY_STATUS		+ " TEXT," +
			KEY_FEED_TYPE 	+ " TEXT," +
 			//KEY_NAME		+ " TEXT," + 
			//KEY_PICURL 		+ " TEXT," +
			KEY_FLAG_ACTION + " TEXT," +
			KEY_TIME_STAMP	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_COMMENTS_UPLOAD = "CREATE TABLE "
			+ TABLE_COMMENTS_UPLOAD + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," + // = feed_id
			KEY_U_ID        + " TEXT," +
			KEY_SENDER     	+ " TEXT," +
			KEY_STATUS		+ " TEXT," +
			KEY_FEED_TYPE 	+ " TEXT," + 
			KEY_COMMENT 	+ " TEXT," +
			KEY_FLAG_ACTION + " TEXT," +
			KEY_TIME_STAMP	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_NEWSFEED_BY_HASHTAG = "CREATE TABLE "
			+ TABLE_NEWSFEED_BY_HASHTAG + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID			+ " TEXT," +
			KEY_NAME		+ " TEXT," +
			KEY_IMAGE		+ " TEXT," +
			KEY_STATUS		+ " TEXT," +			
			KEY_PICURL 		+ " TEXT," +
			//-------
			KEY_LIKE_STATS 	+ " TEXT," +
			KEY_COMMENT_STATS + " TEXT," +
			KEY_FEED_TYPE 	+ " TEXT," +
			//-------
			KEY_HASHTAG		+ " TEXT," +
			//-------			
			KEY_TIME_STAMP 	+ " DATETIME," + 
			KEY_CUSTOM_TIME_STAMP 	+ " DATETIME," +
			KEY_URL     	  + " TEXT," +
			KEY_SENDER     	  + " TEXT," +
			KEY_YOU_LIKE_THIS + " TEXT" + ")";

	
	
    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;


    /** Constructor */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mDB = getWritableDatabase();


	}

    @Override
    public synchronized void close() {

	    /*if (myDataBase != null)
	        myDataBase.close();*/

    	super.close();

    }

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_USER_PROFILE);
		db.execSQL(CREATE_TABLE_USER_PLACES);
		db.execSQL(CREATE_TABLE_USER_FRIENDS);

		db.execSQL(CREATE_TABLE_USER_LOCATION);
		//---------------------
		db.execSQL(CREATE_TABLE_BUS_GPS_DATA);		
		db.execSQL(CREATE_TABLE_BUS_GPS_URL);
		//---------------------
		db.execSQL(CREATE_TABLE_HISTORY);
		db.execSQL(CREATE_TABLE_RANKING);
		//---------------------
		db.execSQL(CREATE_TABLE_NEWSFEED);
		db.execSQL(CREATE_TABLE_NEWSFEED_UPLOAD);		
		//---------------------		
		db.execSQL(CREATE_TABLE_BUSCODE);
		//---------------------		
		db.execSQL(CREATE_TABLE_LIKES);
		db.execSQL(CREATE_TABLE_LIKES_UPLOAD);
		db.execSQL(CREATE_TABLE_COMMENTS_UPLOAD);
		db.execSQL(CREATE_TABLE_NEWSFEED_BY_HASHTAG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /* Como ainda estamos na primeira vers�o do DB,
        *  n�o precisamos nos preocupar com o update agora.
        *  ----------------------------
        *  Esse m�todo � importante quando formos adicionar mais tabelas, por�m,
        *  precisamos preservar as existentes
        */

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PLACES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_FRIENDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_BUSLINE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ROUTES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOCATION);
		//--------------------
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_GPS_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_GPS_URL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REWARDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKING);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED);
		//--------------------
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED_UPLOAD);		
		//--------------------
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSCODE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES_UPLOAD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS_UPLOAD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED_BY_HASHTAG);
		//--------------------		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		// create new tables
		onCreate(db);
	}

	//Update method	
	public int updateNewsfeed(ContentValues contentValues, String selection, String[] selectionArgs) {
		
		
        int rowsUpdated = mDB.update(TABLE_NEWSFEED, 
        		contentValues,
                selection,
                selectionArgs);

        return rowsUpdated;
	}

	public int updateNewsfeed_by_Hashtag(ContentValues contentValues, String selection, String[] selectionArgs) {
		
		
        int rowsUpdated = mDB.update(TABLE_NEWSFEED_BY_HASHTAG, 
        		contentValues,
                selection,
                selectionArgs);

        return rowsUpdated;
	}
	
	
	/*
	 * Creating a UserProfile
	 */
	public long createUserProfile(ContentValues contentValues) {

		//-----------
		resetUserProfile();
		//-----------
		long rowID = mDB.insert(TABLE_USER_PROFILE, null, contentValues);
		return rowID;

	}

	public long insertUserProfile(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_USER_PROFILE, null, contentValues);
		return rowID;

	}
	/*
	 * Creating a UserPlaces
	 */

	public long insertUserPlaces(ContentValues contentValues) {

		long rowID = mDB.insert(TABLE_USER_PLACES, null, contentValues);
		return rowID;

	}




	public long insertUserRoutes(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_USER_ROUTES, null, contentValues);
		return rowID;

	}

	public long createUserLocation(ContentValues contentValues) {

		resetUserLocation();
		//-----------
		long rowID = mDB.insert(TABLE_USER_LOCATION, null, contentValues);
		return rowID;

	}

	public long insertUserLocation(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_USER_LOCATION, null, contentValues);
		return rowID;

	}

	public long insertBusGpsData(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_BUS_GPS_DATA, null, contentValues);
		return rowID;

	}

	public long insertBusGpsURl(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_BUS_GPS_URL, null, contentValues);
		return rowID;

	}
	
	public long insertBuscode(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_BUSCODE, null, contentValues);
		return rowID;

	}
	

	public long insertRanking(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_RANKING, null, contentValues);
		return rowID;

	}
	
	public long insertHistory(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_HISTORY, null, contentValues);
		return rowID;

	}	
	
	public long insertNewsfeed(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_NEWSFEED, null, contentValues);
		return rowID;

	}
	
	public long insertNewsfeed_upload(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_NEWSFEED_UPLOAD, null, contentValues);
		return rowID;

	}
	
	public long insertLikes_upload(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_LIKES_UPLOAD, null, contentValues);
		return rowID;

	}
	
	public long insertComments_upload(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_COMMENTS_UPLOAD, null, contentValues);
		return rowID;

	}
	
	public long insertNewsfeed_by_Hashtag(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_NEWSFEED_BY_HASHTAG, null, contentValues);
		return rowID;

	}

	
	public void resetUserFriends() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_FRIENDS);
		mDB.execSQL(CREATE_TABLE_USER_FRIENDS);
		//-----------

	}

	public void resetUserProfile() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE);
		mDB.execSQL(CREATE_TABLE_USER_PROFILE);
		//-----------

	}

	public void resetUserPlaces() {

		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PLACES);
		mDB.execSQL(CREATE_TABLE_USER_PLACES);

	}



	public void resetUserLocation() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOCATION);
		mDB.execSQL(CREATE_TABLE_USER_LOCATION);
		//-----------
	}
	

	public void resetBusGpsData() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_GPS_DATA);
		mDB.execSQL(CREATE_TABLE_BUS_GPS_DATA);
		//-----------
	}
	
	public void resetBusGpsUrl() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_GPS_URL);
		mDB.execSQL(CREATE_TABLE_BUS_GPS_URL);
		//-----------
	}
	
	public void resetBuscode() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSCODE);
		mDB.execSQL(CREATE_TABLE_BUSCODE);
		//-----------
	}	


	
	public void resetRanking() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKING);
		mDB.execSQL(CREATE_TABLE_RANKING);
		//-----------
	}	
	
	public void resetHistory() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		mDB.execSQL(CREATE_TABLE_HISTORY);
		//-----------
	}		

	public void resetNewsfeed() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED);
		mDB.execSQL(CREATE_TABLE_NEWSFEED);
		//-----------
	}	

	public void resetNewsfeed_Upload() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED_UPLOAD);
		mDB.execSQL(CREATE_TABLE_NEWSFEED_UPLOAD);
		//-----------
	}	

	public void resetLikes() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES);
		mDB.execSQL(CREATE_TABLE_LIKES);
		//-----------
	}	
	
	public void resetLikes_upload() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_LIKES_UPLOAD);
		mDB.execSQL(CREATE_TABLE_LIKES_UPLOAD);
		//-----------
	}	
	
	public void resetComments_upload() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS_UPLOAD);
		mDB.execSQL(CREATE_TABLE_COMMENTS_UPLOAD);
		//-----------
	}	
	
	public void resetNewsfeed_by_Hashtag() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED_BY_HASHTAG);
		mDB.execSQL(CREATE_TABLE_NEWSFEED_BY_HASHTAG);
		//-----------
	}	
	
	/** Returns all the contacts in the table */
	public Cursor get_UserPlaces(){
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY,KEY_UF}, null, null, null, null, KEY_CREATED_AT + " desc ");
		return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY,KEY_UF,KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_PICURL}, null, null, null, null, null);
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_PICURL}, null, null, null, null, null);

	}

	public Cursor get_UserProfile(){
		//return mDB.query(TABLE_USER_PROFILE, new String[] {KEY_ID, KEY_NAME, KEY_PICTURE, KEY_SCORE, KEY_RANK}, null, null, null, null, null);
		return mDB.query(TABLE_USER_PROFILE, new String[] {KEY_ID, KEY_NAME, KEY_PICTURE, KEY_SCORE, KEY_RANK, KEY_CREATED_AT}, null, null, null, null, null);
	}




	public Cursor get_UserLocation(){

		
		//return mDB.query(TABLE_USER_LOCATION, new String[] {KEY_ROW_ID, KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_SPEED, KEY_LOCATION_PROVIDER, KEY_CREATED_AT,KEY_DIFF_DISTANCE,KEY_DIFF_TIME,KEY_LOCATION_STATUS,KEY_ACCURACY}, null, null, null, null, null);
		return mDB.query(TABLE_USER_LOCATION, new String[] {KEY_ROW_ID, KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_HASHCODE, KEY_LOCATION_PROVIDER, KEY_CREATED_AT,KEY_DIFF_DISTANCE,KEY_DIFF_TIME,KEY_STATUS,KEY_ACCURACY}, null, null, null, null, null);
																																          

	}

	public Cursor get_BusGpsData(){

		//return mDB.query(TABLE_BUS_GPS_DATA, new String[] {KEY_ROW_ID, KEY_CREATED_AT, KEY_BUSCODE, KEY_BUSLINE, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_SPEED, KEY_DIRECTION, KEY_SCORE}, null, null, null, null, null);
		return mDB.query(TABLE_BUS_GPS_DATA, new String[] {KEY_ROW_ID, KEY_CREATED_AT, KEY_BUSCODE, KEY_BUSLINE, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_BUS_TYPE, KEY_HASHCODE}, null, null, null, null, null);
	}

	public Cursor get_BusGpsUrl(){

		//return mDB.query(TABLE_BUS_GPS_URL, new String[] {KEY_ROW_ID, KEY_BUSCODE, KEY_URL, KEY_FLAG}, null, null, null, null, null);
		return mDB.query(TABLE_BUS_GPS_URL, new String[] {KEY_ROW_ID, KEY_BUSCODE, KEY_BUS_TYPE, KEY_FLAG, KEY_HASHCODE}, null, null, null, null, null);
	}
	
	public Cursor get_Buscode(){

		return mDB.query(TABLE_BUSCODE, new String[] {KEY_ROW_ID, KEY_BUSCODE, KEY_BUSLINE, KEY_BUSLINE_DESCRIPTION, KEY_BUSLINE_COMPANY, KEY_HASHTAG}, null, null, null, null, null);
	}

	public Cursor get_Ranking(){

		return mDB.query(TABLE_RANKING, new String[] {KEY_ID, KEY_NAME, KEY_PICURL, KEY_SCORE}, null, null, null, null, null);
	}
	
	public Cursor get_History(){

		return mDB.query(TABLE_HISTORY, new String[] {KEY_ROW_ID, KEY_STATUS, KEY_TIME_STAMP, KEY_LAST_UPDATE}, null, null, null, null, null);
	}
	

	public Cursor get_Newsfeed(){

		//return mDB.query(TABLE_NEWSFEED, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS}, null, null, null, null, KEY_ROW_ID + " DESC");
		//return mDB.query(TABLE_NEWSFEED, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS, KEY_YOU_LIKE_THIS, KEY_SENDER}, null, null, null, null, KEY_ROW_ID + " DESC");
		//return mDB.query(TABLE_NEWSFEED, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS, KEY_YOU_LIKE_THIS, KEY_SENDER, KEY_FEED_TYPE}, null, null, null, null, KEY_ROW_ID + " DESC");
		return mDB.query(TABLE_NEWSFEED, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS, KEY_YOU_LIKE_THIS, KEY_SENDER, KEY_FEED_TYPE, KEY_HASHTAG}, null, null, null, null, KEY_ROW_ID + " DESC");
	}

	public Cursor get_Newsfeed_upload(){

		//return mDB.query(TABLE_NEWSFEED, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL}, null, null, null, null, null);
		//return mDB.query(TABLE_NEWSFEED_UPLOAD, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS, KEY_FEED_TYPE, KEY_FLAG_ACTION}, null, null, null, null, null);
		return mDB.query(TABLE_NEWSFEED_UPLOAD, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS, KEY_FEED_TYPE, KEY_FLAG_ACTION, KEY_HASHTAG}, null, null, null, null, null);
		
	}

	public Cursor get_Likes(){

		return mDB.query(TABLE_LIKES, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_PICURL, KEY_TIME_STAMP}, null, null, null, null, null);
	}

	public Cursor get_Likes_upload(){

		//return mDB.query(TABLE_LIKES_UPLOAD, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_PICURL, KEY_TIME_STAMP, KEY_FLAG_ACTION}, null, null, null, null, null);
		//return mDB.query(TABLE_LIKES_UPLOAD, new String[] {KEY_ROW_ID, KEY_ID, KEY_U_ID, KEY_TIME_STAMP, KEY_FLAG_ACTION, KEY_SENDER}, null, null, null, null, null);
		return mDB.query(TABLE_LIKES_UPLOAD, new String[] {KEY_ROW_ID, KEY_ID, KEY_U_ID, KEY_TIME_STAMP, KEY_FLAG_ACTION, KEY_SENDER, KEY_STATUS, KEY_FEED_TYPE}, null, null, null, null, null);
	}

	public Cursor get_Comments_upload(){

		return mDB.query(TABLE_COMMENTS_UPLOAD, new String[] {KEY_ROW_ID, KEY_ID, KEY_U_ID, KEY_TIME_STAMP, KEY_FLAG_ACTION, KEY_SENDER, KEY_STATUS, KEY_FEED_TYPE, KEY_COMMENT}, null, null, null, null, null);
	}
	
	public Cursor get_Newsfeed_by_Hashtag(){

		return mDB.query(TABLE_NEWSFEED_BY_HASHTAG, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL, KEY_CUSTOM_TIME_STAMP, KEY_LIKE_STATS, KEY_COMMENT_STATS, KEY_YOU_LIKE_THIS, KEY_SENDER, KEY_FEED_TYPE, KEY_HASHTAG}, null, null, null, null, null);
	}
	
	/** Returns all the contacts in the table */
	public Cursor get_FriendList(){
		//return mDB.query(TABLE_USER_FRIENDS, new String[] {KEY_FRIEND_ID, KEY_FRIEND_NAME}, null, null, null, null, null);
		return mDB.query(TABLE_USER_FRIENDS, new String[] {KEY_ROW_ID, KEY_FRIEND_ID, KEY_FRIEND_PICURL,KEY_FRIEND_NAME,KEY_FRIEND_DEVICE}, null, null, null, null, null);
	}

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
