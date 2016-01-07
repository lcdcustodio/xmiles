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
	public static final String TABLE_USER_FAVORITES = "user_favorites";
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


	// CITY BUSLINE Table - column names
	public static final String KEY_CITY_BUSLINE = "city_busline";

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

	// REWARDS Table - column names	
	public static final String KEY_REWARD   	= "reward";
	public static final String KEY_REWARD_TYPE  = "reward_type";
	public static final String KEY_QUANTITY 	= "quantity";
	
	// REWARDS Table - column names	
	public static final String KEY_RANK   	= "rank";
	
	// NEWSFEDD Table - column names
	public static final String KEY_IMAGE 	  = "image";
	public static final String KEY_STATUS 	  = "status";
	public static final String KEY_TIME_STAMP = "time_stamp";
	
	// Table Create Statements
	private static final String CREATE_TABLE_USER_PROFILE = "CREATE TABLE "
			+ TABLE_USER_PROFILE + "(" + KEY_ID + " TEXT," + KEY_NAME
			+ " TEXT," + KEY_PICTURE + " TEXT," +
			KEY_SCORE  + " TEXT," +
			KEY_RANK   + " TEXT," +			
			KEY_CREATED_AT	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_CITY_BUSLINE = "CREATE TABLE "
			+ TABLE_CITY_BUSLINE + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_CITY_BUSLINE + " TEXT," +
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



	private static final String CREATE_TABLE_USER_FAVORITES = "CREATE TABLE "
			+ TABLE_USER_FAVORITES + "(" + KEY_FAVORITE_ID + " integer primary key autoincrement ,"  +
			KEY_ID + " TEXT," + KEY_NAME	+ " TEXT," +
			KEY_BUSLINE + " TEXT," +
			KEY_CITY + " TEXT," +
			KEY_UF + " TEXT," +
			KEY_FROM + " TEXT," +
			KEY_FROM_BUS_STOP_ID + " integer," +
			KEY_TO + " TEXT," +
			KEY_TO_BUS_STOP_ID + " integer," +
			KEY_BD_UPDATED + " TEXT," +
			KEY_CREATED_AT	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_USER_ROUTES = "CREATE TABLE "
			+ TABLE_USER_ROUTES + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_ID + " TEXT," + KEY_FAVORITE_ID	+ " TEXT," +
			KEY_BUSLINE + " TEXT," +
			KEY_BUS_STOP + " TEXT," +
			KEY_BUS_STOP_ID + " integer," +
			KEY_B_LATITUDE + "  DOUBLE," +
			KEY_B_LONGITUDE + " DOUBLE," +
			KEY_FLAG + " TEXT," +
			KEY_M_DISTANCE_K + " TEXT," +
			KEY_CREATED_AT	+ " DATETIME" + ")";

	private static final String CREATE_TABLE_USER_LOCATION = "CREATE TABLE "
			+ TABLE_USER_LOCATION + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_U_LATITUDE + " DOUBLE," +
			KEY_U_LONGITUDE + " DOUBLE," +
			KEY_SPEED + " DOUBLE," +
			KEY_LOCATION_PROVIDER + " TEXT," +
			//-------------------------------
			//------- new columns------------
			//-------------------------------			
			KEY_DIFF_DISTANCE + " DOUBLE," +
			KEY_DIFF_TIME + " DOUBLE," +
			KEY_LOCATION_STATUS + " TEXT," +
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
			KEY_SPEED + " DOUBLE," +
			KEY_DIRECTION + " TEXT," +
			KEY_SENSE + " TEXT," + 
			KEY_SCORE + " DOUBLE" + ")";
	
	private static final String CREATE_TABLE_BUS_GPS_URL = "CREATE TABLE "
			+ TABLE_BUS_GPS_URL + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_BUSCODE + " TEXT," +
			KEY_URL + " TEXT," +
			KEY_FLAG + " integer" + ")";

	private static final String CREATE_TABLE_REWARDS = "CREATE TABLE "
			+ TABLE_REWARDS + "(" + KEY_ROW_ID + " integer primary key autoincrement ,"  +
			KEY_REWARD 		+ " TEXT," +
			KEY_REWARD_TYPE + " TEXT," + 
			KEY_PICURL 		+ " TEXT," +
			KEY_QUANTITY 	+ " TEXT," +
			KEY_SCORE 		+ " TEXT,"  +
			KEY_CREATED_AT	+ " DATETIME" + ")";

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
			KEY_TIME_STAMP 	+ " TEXT," +
			KEY_URL     	+ " TEXT," +			
			KEY_CREATED_AT	+ " DATETIME" + ")";
	
	
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
		db.execSQL(CREATE_TABLE_USER_FAVORITES);
		db.execSQL(CREATE_TABLE_CITY_BUSLINE);
		db.execSQL(CREATE_TABLE_USER_ROUTES);
		db.execSQL(CREATE_TABLE_USER_LOCATION);
		//---------------------
		db.execSQL(CREATE_TABLE_BUS_GPS_DATA);		
		db.execSQL(CREATE_TABLE_BUS_GPS_URL);
		//---------------------
		db.execSQL(CREATE_TABLE_REWARDS);
		db.execSQL(CREATE_TABLE_RANKING);
		//---------------------
		db.execSQL(CREATE_TABLE_NEWSFEED);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /* Como ainda estamos na primeira versão do DB,
        *  não precisamos nos preocupar com o update agora.
        *  ----------------------------
        *  Esse método é importante quando formos adicionar mais tabelas, porém,
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
		// create new tables
		onCreate(db);
	}

	// ------------------------ "UserProfiles" table methods ----------------//

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


	public long createCityBusline(ContentValues contentValues) {

		//-----------
		resetCityBusline();
		//-----------
		long rowID = mDB.insert(TABLE_CITY_BUSLINE, null, contentValues);
		return rowID;

	}

	public long insertCityBusline(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_CITY_BUSLINE, null, contentValues);
		return rowID;

	}
	public long createUserFavorites(ContentValues contentValues) {

		resetUserFavorites();
		//-----------
		long rowID = mDB.insert(TABLE_USER_FAVORITES, null, contentValues);
		return rowID;

	}

	public long insertUserFavorites(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_USER_FAVORITES, null, contentValues);
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
	
	public long insertRewards(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_REWARDS, null, contentValues);
		return rowID;

	}

	public long insertRanking(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_RANKING, null, contentValues);
		return rowID;

	}
	
	public long insertNewsfeed(ContentValues contentValues) {

		//-----------
		long rowID = mDB.insert(TABLE_NEWSFEED, null, contentValues);
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


	public void resetCityBusline() {

		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_BUSLINE);
		mDB.execSQL(CREATE_TABLE_CITY_BUSLINE);

	}

	public void resetUserFavorites() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_FAVORITES);
		mDB.execSQL(CREATE_TABLE_USER_FAVORITES);
		//-----------

	}

	public void resetUserRoutes() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ROUTES);
		mDB.execSQL(CREATE_TABLE_USER_ROUTES);
		//-----------

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

	public void resetRewards() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_REWARDS);
		mDB.execSQL(CREATE_TABLE_REWARDS);
		//-----------
	}	
	
	public void resetRanking() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_RANKING);
		mDB.execSQL(CREATE_TABLE_RANKING);
		//-----------
	}	

	public void resetNewsfeed() {
		//-----------
		mDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSFEED);
		mDB.execSQL(CREATE_TABLE_NEWSFEED);
		//-----------
	}	
	
	
	/** Returns all the contacts in the table */
	public Cursor get_UserPlaces(){
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY,KEY_UF}, null, null, null, null, KEY_CREATED_AT + " desc ");
		return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY,KEY_UF,KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_PICURL}, null, null, null, null, null);
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_PICURL}, null, null, null, null, null);

	}

	public Cursor get_UserProfile(){
        //return mDB.query(TABLE_USER_PLACES, new String[] { KEY_ROW_ID,  KEY_NAME , KEY_PHONE } , null, null, null, null, KEY_NAME + " asc ");
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY}, null, null, null, null, null);
		return mDB.query(TABLE_USER_PROFILE, new String[] {KEY_ID, KEY_NAME, KEY_PICTURE, KEY_SCORE, KEY_RANK}, null, null, null, null, null);
	}

	public Cursor get_CityBusline(){
        //return mDB.query(TABLE_USER_PLACES, new String[] { KEY_ROW_ID,  KEY_NAME , KEY_PHONE } , null, null, null, null, KEY_NAME + " asc ");
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY}, null, null, null, null, null);
		return mDB.query(TABLE_CITY_BUSLINE, new String[] {KEY_CITY_BUSLINE}, null, null, null, null, null);
	}

	public Cursor get_UserFavorites(){
		//return mDB.query(TABLE_USER_FAVORITES, new String[] {KEY_ID, KEY_NAME}, null, null, null, null, null);
		//return mDB.query(TABLE_USER_FAVORITES, new String[] {KEY_ID, KEY_NAME, KEY_BUSLINE, KEY_CITY, KEY_UF, KEY_FROM, KEY_TO,KEY_FROM_BUS_STOP_ID,KEY_TO_BUS_STOP_ID,KEY_FAVORITE_ID}, null, null, null, null, null);
		return mDB.query(TABLE_USER_FAVORITES, new String[] {KEY_ID, KEY_NAME, KEY_BUSLINE, KEY_CITY, KEY_UF, KEY_FROM, KEY_TO,KEY_FROM_BUS_STOP_ID,KEY_TO_BUS_STOP_ID,KEY_FAVORITE_ID}, null, null, null, null, KEY_FAVORITE_ID + " DESC");
	}

	public Cursor get_UserRoutes(){

		return mDB.query(TABLE_USER_ROUTES, new String[] {KEY_ID, KEY_FAVORITE_ID, KEY_BUS_STOP, KEY_BUS_STOP_ID, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_FLAG, KEY_M_DISTANCE_K}, null, null, null, null, null);
	}

	public Cursor get_UserRoutesFlag(){

		//return mDB.query(TABLE_USER_ROUTES_FLAG, new String[] {KEY_FAVORITE_ID, KEY_BUS_STOP_ID, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_FLAG, KEY_M_DISTANCE_K}, null, null, null, null, null);
		String WHERE =  "flag= 'YES' ";
		return mDB.query(TABLE_USER_ROUTES, new String[] {KEY_FAVORITE_ID, KEY_BUS_STOP_ID, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_FLAG, KEY_M_DISTANCE_K}, WHERE, null, null, null, null);
	}

	public Cursor get_UserLocation(){

		//return mDB.query(TABLE_USER_LOCATION, new String[] {KEY_ROW_ID, KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_SPEED, KEY_LOCATION_PROVIDER, KEY_CREATED_AT}, null, null, null, null, null);
		return mDB.query(TABLE_USER_LOCATION, new String[] {KEY_ROW_ID, KEY_U_LATITUDE, KEY_U_LONGITUDE, KEY_SPEED, KEY_LOCATION_PROVIDER, KEY_CREATED_AT,KEY_DIFF_DISTANCE,KEY_DIFF_TIME,KEY_LOCATION_STATUS,KEY_ACCURACY}, null, null, null, null, null);
																																          

	}

	public Cursor get_BusGpsData(){

		return mDB.query(TABLE_BUS_GPS_DATA, new String[] {KEY_ROW_ID, KEY_CREATED_AT, KEY_BUSCODE, KEY_BUSLINE, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_SPEED, KEY_DIRECTION, KEY_SCORE}, null, null, null, null, null);
		//return mDB.query(TABLE_BUS_GPS_DATA, new String[] {KEY_ROW_ID, KEY_CREATED_AT, KEY_BUSCODE, KEY_BUSLINE, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_SPEED, KEY_DIRECTION, KEY_URL}, null, null, null, null, null);
	}

	public Cursor get_BusGpsUrl(){

		return mDB.query(TABLE_BUS_GPS_URL, new String[] {KEY_ROW_ID, KEY_BUSCODE, KEY_URL, KEY_FLAG}, null, null, null, null, null);
	}
	
	public Cursor get_Rewards(){

		return mDB.query(TABLE_REWARDS, new String[] {KEY_REWARD, KEY_REWARD_TYPE, KEY_PICURL, KEY_SCORE, KEY_QUANTITY}, null, null, null, null, null);
	}
	
	public Cursor get_Ranking(){

		return mDB.query(TABLE_RANKING, new String[] {KEY_ID, KEY_NAME, KEY_PICURL, KEY_SCORE}, null, null, null, null, null);
	}

	public Cursor get_Newsfeed(){

		return mDB.query(TABLE_NEWSFEED, new String[] {KEY_ROW_ID, KEY_ID, KEY_NAME, KEY_IMAGE, KEY_STATUS, KEY_PICURL, KEY_TIME_STAMP, KEY_URL}, null, null, null, null, null);
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
