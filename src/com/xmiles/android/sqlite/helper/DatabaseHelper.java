package com.xmiles.android.sqlite.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "FACEBOOK";

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

	// Table Create Statements
	private static final String CREATE_TABLE_USER_PROFILE = "CREATE TABLE "
			+ TABLE_USER_PROFILE + "(" + KEY_ID + " TEXT," + KEY_NAME
			+ " TEXT," + KEY_PICTURE + " TEXT," +
			//KEY_DEVICE + " TEXT," + KEY_CREATED_AT
			KEY_CREATED_AT
			+ " DATETIME" + ")";

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


	/** Returns all the contacts in the table */
	public Cursor get_UserPlaces(){
		return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY,KEY_UF}, null, null, null, null, KEY_CREATED_AT + " desc ");
	}

	public Cursor get_UserProfile(){
        //return mDB.query(TABLE_USER_PLACES, new String[] { KEY_ROW_ID,  KEY_NAME , KEY_PHONE } , null, null, null, null, KEY_NAME + " asc ");
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY}, null, null, null, null, null);
		return mDB.query(TABLE_USER_PROFILE, new String[] {KEY_ID, KEY_NAME,KEY_PICTURE}, null, null, null, null, null);
	}

	public Cursor get_CityBusline(){
        //return mDB.query(TABLE_USER_PLACES, new String[] { KEY_ROW_ID,  KEY_NAME , KEY_PHONE } , null, null, null, null, KEY_NAME + " asc ");
		//return mDB.query(TABLE_USER_PLACES, new String[] {KEY_ROW_ID, KEY_NEARBY,KEY_CITY}, null, null, null, null, null);
		return mDB.query(TABLE_CITY_BUSLINE, new String[] {KEY_CITY_BUSLINE}, null, null, null, null, null);
	}

	public Cursor get_UserFavorites(){
		//return mDB.query(TABLE_USER_FAVORITES, new String[] {KEY_ID, KEY_NAME}, null, null, null, null, null);
		return mDB.query(TABLE_USER_FAVORITES, new String[] {KEY_ID, KEY_NAME, KEY_BUSLINE, KEY_CITY, KEY_UF, KEY_FROM, KEY_TO,KEY_FROM_BUS_STOP_ID,KEY_TO_BUS_STOP_ID,KEY_FAVORITE_ID}, null, null, null, null, null);
	}

	public Cursor get_UserRoutes(){

		return mDB.query(TABLE_USER_ROUTES, new String[] {KEY_ID, KEY_FAVORITE_ID, KEY_BUS_STOP, KEY_BUS_STOP_ID, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_FLAG, KEY_M_DISTANCE_K}, null, null, null, null, null);
	}

	public Cursor get_UserRoutesFlag(){

		//return mDB.query(TABLE_USER_ROUTES_FLAG, new String[] {KEY_FAVORITE_ID, KEY_BUS_STOP_ID, KEY_B_LATITUDE, KEY_B_LONGITUDE, KEY_FLAG, KEY_M_DISTANCE_K}, null, null, null, null, null);
		String WHERE =  "flag= 'YES' ";
		return mDB.query(TABLE_USER_ROUTES, new String[] {KEY_FAVORITE_ID, KEY_BUS_STOP_ID, KEY_FLAG}, WHERE, null, null, null, null);
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
