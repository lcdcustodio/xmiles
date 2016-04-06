package com.xmiles.android.sqlite.contentprovider;

import com.xmiles.android.sqlite.helper.*;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/** A custom Content Provider to do the database operations */
public class SqliteProvider extends ContentProvider{

	private static final String TAG = "FACEBOOK";
	public static final String PROVIDER_NAME = "com.xmiles.android.provider";

	/** A uri to do operations on contacts table. A content provider is identified by its uri */
	public static final Uri CONTENT_URI_USER_PROFILE = Uri.parse("content://" + PROVIDER_NAME + "/UserProfile");
	public static final Uri CONTENT_URI_USER_PLACES = Uri.parse("content://" + PROVIDER_NAME + "/UserPlaces");
	public static final Uri CONTENT_URI_USER_FRIENDS = Uri.parse("content://" + PROVIDER_NAME + "/UserFriends");
	public static final Uri CONTENT_URI_CITY_BUSLINE = Uri.parse("content://" + PROVIDER_NAME + "/CityBusline");
	public static final Uri CONTENT_URI_USER_FAVORITES = Uri.parse("content://" + PROVIDER_NAME + "/UserFavorites");
	public static final Uri CONTENT_URI_USER_PROFILE_create = Uri.parse("content://" + PROVIDER_NAME + "/UserProfile_create");
	public static final Uri CONTENT_URI_USER_FAVORITES_create = Uri.parse("content://" + PROVIDER_NAME + "/UserFavorites_create");
	public static final Uri CONTENT_URI_USER_FAVORITES_insert = Uri.parse("content://" + PROVIDER_NAME + "/UserFavorites_insert");
	public static final Uri CONTENT_URI_CITY_BUSLINE_create = Uri.parse("content://" + PROVIDER_NAME + "/CityBusline_create");
	public static final Uri CONTENT_URI_USER_PLACES_create = Uri.parse("content://" + PROVIDER_NAME + "/UserPlaces_create");
	public static final Uri CONTENT_URI_USER_ROUTES_create = Uri.parse("content://" + PROVIDER_NAME + "/UserRoutes_create");
	public static final Uri CONTENT_URI_USER_ROUTES_insert = Uri.parse("content://" + PROVIDER_NAME + "/UserRoutes_insert");
	public static final Uri CONTENT_URI_USER_ROUTES = Uri.parse("content://" + PROVIDER_NAME + "/UserRoutes");
	public static final Uri CONTENT_URI_USER_ROUTES_FLAG = Uri.parse("content://" + PROVIDER_NAME + "/UserRoutesFlag");
	public static final Uri CONTENT_URI_USER_LOCATION_create = Uri.parse("content://" + PROVIDER_NAME + "/UserLocation_create");
	public static final Uri CONTENT_URI_USER_LOCATION_insert = Uri.parse("content://" + PROVIDER_NAME + "/UserLocation_insert");
	public static final Uri CONTENT_URI_USER_LOCATION = Uri.parse("content://" + PROVIDER_NAME + "/UserLocation");
	public static final Uri CONTENT_URI_BUS_GPS_DATA_insert = Uri.parse("content://" + PROVIDER_NAME + "/BusGpsData_insert");
	public static final Uri CONTENT_URI_BUS_GPS_DATA = Uri.parse("content://" + PROVIDER_NAME + "/BusGpsData");
	public static final Uri CONTENT_URI_BUS_GPS_URL_insert = Uri.parse("content://" + PROVIDER_NAME + "/BusGpsUrl_insert");
	public static final Uri CONTENT_URI_BUS_GPS_URL = Uri.parse("content://" + PROVIDER_NAME + "/BusGpsUrl");
	//---------------------
	public static final Uri CONTENT_URI_BUSCODE_insert = Uri.parse("content://" + PROVIDER_NAME + "/Buscode_insert");
	public static final Uri CONTENT_URI_BUSCODE = Uri.parse("content://" + PROVIDER_NAME + "/Buscode");
	//---------------------
	public static final Uri CONTENT_URI_REWARDS_create = Uri.parse("content://" + PROVIDER_NAME + "/Rewards_create");
	public static final Uri CONTENT_URI_REWARDS = Uri.parse("content://" + PROVIDER_NAME + "/Rewards");
	public static final Uri CONTENT_URI_RANKING_create = Uri.parse("content://" + PROVIDER_NAME + "/Ranking_create");
	public static final Uri CONTENT_URI_RANKING = Uri.parse("content://" + PROVIDER_NAME + "/Ranking");
	public static final Uri CONTENT_URI_NEWSFEED_create = Uri.parse("content://" + PROVIDER_NAME + "/Newsfeed_create");
	public static final Uri CONTENT_URI_NEWSFEED = Uri.parse("content://" + PROVIDER_NAME + "/Newsfeed");
	public static final Uri CONTENT_URI_NEWSFEED_insert = Uri.parse("content://" + PROVIDER_NAME + "/Newsfeed_insert");
	public static final Uri CONTENT_URI_NEWSFEED_UPLOAD = Uri.parse("content://" + PROVIDER_NAME + "/Newsfeed_upload");
	public static final Uri CONTENT_URI_NEWSFEED_UPLOAD_insert = Uri.parse("content://" + PROVIDER_NAME + "/Newsfeed_upload_insert");	
	public static final Uri CONTENT_URI_LIKES = Uri.parse("content://" + PROVIDER_NAME + "/Likes");
	public static final Uri CONTENT_URI_LIKES_create = Uri.parse("content://" + PROVIDER_NAME + "/Likes_create");	
	public static final Uri CONTENT_URI_NEWSFEED_update = Uri.parse("content://" + PROVIDER_NAME + "/Newsfeed_update");
	public static final Uri CONTENT_URI_LIKES_UPLOAD = Uri.parse("content://" + PROVIDER_NAME + "/Likes_upload");
	public static final Uri CONTENT_URI_LIKES_UPLOAD_insert = Uri.parse("content://" + PROVIDER_NAME + "/Likes_upload_insert");
	public static final Uri CONTENT_URI_COMMENTS_UPLOAD = Uri.parse("content://" + PROVIDER_NAME + "/Comments_upload");
	public static final Uri CONTENT_URI_COMMENTS_UPLOAD_insert = Uri.parse("content://" + PROVIDER_NAME + "/Comments_upload_insert");

	/** Constants to identify the requested operation */
	private static final int USER_PROFILE = 1;
	private static final int USER_PLACES = 2;
	private static final int USER_FRIENDS = 3;
	private static final int CITY_BUSLINE = 4;
	private static final int USER_FAVORITES = 5;
	private static final int USER_PROFILE_create = 6;
	private static final int USER_FAVORITES_create = 7;
	private static final int USER_FAVORITES_insert = 8;
	private static final int CITY_BUSLINE_create = 9;
	private static final int USER_PLACES_create = 10;
	private static final int USER_ROUTES_create = 11;
	private static final int USER_ROUTES_insert = 12;
	private static final int USER_ROUTES = 13;
	private static final int USER_ROUTES_FLAG = 14;
	private static final int USER_LOCATION_create = 15;
	private static final int USER_LOCATION_insert = 16;
	private static final int USER_LOCATION = 17;
	private static final int BUS_GPS_DATA_insert = 18;
	private static final int BUS_GPS_DATA = 19;
	private static final int BUS_GPS_URL_insert = 20;
	private static final int BUS_GPS_URL = 21;
	private static final int REWARDS_create = 22;
	private static final int REWARDS = 23;
	private static final int RANKING_create = 24;
	private static final int RANKING = 25;
	private static final int NEWSFEED_create = 26;
	private static final int NEWSFEED = 27;
	private static final int NEWSFEED_insert = 28;
	private static final int BUSCODE = 29;
	private static final int BUSCODE_insert = 30;
	private static final int NEWSFEED_UPLOAD = 31;
	private static final int NEWSFEED_UPLOAD_insert = 32;
	private static final int LIKES = 33;
	private static final int LIKES_create = 34;
	//---------
	private static final int NEWSFEED_update = 35;
	//---------
	private static final int LIKES_UPLOAD = 36;
	private static final int LIKES_UPLOAD_insert = 37;
	private static final int COMMENTS_UPLOAD = 38;
	private static final int COMMENTS_UPLOAD_insert = 39;


	private static final UriMatcher uriMatcher ;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "UserProfile", USER_PROFILE);
		uriMatcher.addURI(PROVIDER_NAME, "UserPlaces", USER_PLACES);
		uriMatcher.addURI(PROVIDER_NAME, "UserFriends", USER_FRIENDS);
		uriMatcher.addURI(PROVIDER_NAME, "CityBusline", CITY_BUSLINE);
		uriMatcher.addURI(PROVIDER_NAME, "UserFavorites", USER_FAVORITES);
		uriMatcher.addURI(PROVIDER_NAME, "UserProfile_create", USER_PROFILE_create);
		uriMatcher.addURI(PROVIDER_NAME, "UserFavorites_create", USER_FAVORITES_create);
		uriMatcher.addURI(PROVIDER_NAME, "UserFavorites_insert", USER_FAVORITES_insert);
		uriMatcher.addURI(PROVIDER_NAME, "CityBusline_create", CITY_BUSLINE_create);
		uriMatcher.addURI(PROVIDER_NAME, "UserPlaces_create", USER_PLACES_create);
		uriMatcher.addURI(PROVIDER_NAME, "UserRoutes_create", USER_ROUTES_create);
		uriMatcher.addURI(PROVIDER_NAME, "UserRoutes_insert", USER_ROUTES_insert);
		uriMatcher.addURI(PROVIDER_NAME, "UserRoutes", USER_ROUTES);
		uriMatcher.addURI(PROVIDER_NAME, "UserRoutesFlag", USER_ROUTES_FLAG);
		uriMatcher.addURI(PROVIDER_NAME, "UserLocation_create", USER_LOCATION_create);
		uriMatcher.addURI(PROVIDER_NAME, "UserLocation_insert", USER_LOCATION_insert);
		uriMatcher.addURI(PROVIDER_NAME, "UserLocation", USER_LOCATION);
		uriMatcher.addURI(PROVIDER_NAME, "BusGpsData_insert", BUS_GPS_DATA_insert);
		uriMatcher.addURI(PROVIDER_NAME, "BusGpsData", BUS_GPS_DATA);
		uriMatcher.addURI(PROVIDER_NAME, "BusGpsUrl_insert", BUS_GPS_URL_insert);
		uriMatcher.addURI(PROVIDER_NAME, "BusGpsUrl", BUS_GPS_URL);
		uriMatcher.addURI(PROVIDER_NAME, "Rewards_create", REWARDS_create);
		uriMatcher.addURI(PROVIDER_NAME, "Rewards", REWARDS);
		uriMatcher.addURI(PROVIDER_NAME, "Ranking_create", RANKING_create);
		uriMatcher.addURI(PROVIDER_NAME, "Ranking", RANKING);
		uriMatcher.addURI(PROVIDER_NAME, "Newsfeed_create", NEWSFEED_create);
		uriMatcher.addURI(PROVIDER_NAME, "Newsfeed", NEWSFEED);
		uriMatcher.addURI(PROVIDER_NAME, "Newsfeed_insert", NEWSFEED_insert);		
		uriMatcher.addURI(PROVIDER_NAME, "Buscode", BUSCODE);
		uriMatcher.addURI(PROVIDER_NAME, "Buscode_insert", BUSCODE_insert);		
		uriMatcher.addURI(PROVIDER_NAME, "Newsfeed_upload", NEWSFEED_UPLOAD);
		uriMatcher.addURI(PROVIDER_NAME, "Newsfeed_upload_insert", NEWSFEED_UPLOAD_insert);		
		uriMatcher.addURI(PROVIDER_NAME, "Likes", LIKES);
		uriMatcher.addURI(PROVIDER_NAME, "Likes_create", LIKES_create);		
		uriMatcher.addURI(PROVIDER_NAME, "Newsfeed_update", NEWSFEED_update);		
		uriMatcher.addURI(PROVIDER_NAME, "Likes_upload", LIKES_UPLOAD);
		uriMatcher.addURI(PROVIDER_NAME, "Likes_upload_insert", LIKES_UPLOAD_insert);		
		uriMatcher.addURI(PROVIDER_NAME, "Comments_upload", COMMENTS_UPLOAD);
		uriMatcher.addURI(PROVIDER_NAME, "Comments_upload_insert", COMMENTS_UPLOAD_insert);		

	}

	/** This content provider does the database operations by this object */
	DatabaseHelper mDatabaseHelper;

	/** A callback method which is invoked when the content provider is starting up */
	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	/** A callback method which is invoked when delete operation is requested on this content provider */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/** A callback method which is invoked when insert operation is requested on this content provider */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Uri _uri=null;

	    switch (uriMatcher.match(uri)){
		    case USER_PROFILE_create:
		    	long rowID_1 = mDatabaseHelper.createUserProfile(values);
				//Uri _uri=null;
				if(rowID_1>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_PROFILE_create, rowID_1);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_PROFILE: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

		    case USER_PLACES:
		    	long rowID_2 = mDatabaseHelper.insertUserPlaces(values);
				//Uri _uri=null;
				if(rowID_2>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_PLACES, rowID_2);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_PLACES: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

		    case USER_PLACES_create:
		    	long rowID_2b = mDatabaseHelper.insertUserPlaces(values);
				//Uri _uri=null;
				if(rowID_2b>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_PLACES_create, rowID_2b);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_PLACES: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;


		    case CITY_BUSLINE_create:
		    	long rowID_3 = mDatabaseHelper.createCityBusline(values);
				//Uri _uri=null;
				if(rowID_3>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_CITY_BUSLINE_create, rowID_3);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_CITY_BUSLINE: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

		    case USER_FAVORITES_insert:
		    	long rowID_4 = mDatabaseHelper.insertUserFavorites(values);
				//Uri _uri=null;
				if(rowID_4>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_FAVORITES_insert, rowID_4);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_FAVORITES: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			
		    case NEWSFEED_insert:
		    	long rowID_4d = mDatabaseHelper.insertNewsfeed(values);
				//Uri _uri=null;
				if(rowID_4d>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_NEWSFEED_insert, rowID_4d);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_NEWSFEED: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;			
			
		    case NEWSFEED_UPLOAD_insert:
		    	long rowID_4e = mDatabaseHelper.insertNewsfeed_upload(values);

				if(rowID_4e>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_NEWSFEED_UPLOAD_insert, rowID_4e);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_NEWSFEED_UPLOAD: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;			
			
		    case LIKES_UPLOAD_insert:
		    	long rowID_4f = mDatabaseHelper.insertLikes_upload(values);

				if(rowID_4f>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_LIKES_UPLOAD_insert, rowID_4f);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_LIKES_UPLOAD: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;			

		    case COMMENTS_UPLOAD_insert:
		    	long rowID_4g = mDatabaseHelper.insertComments_upload(values);

				if(rowID_4g>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_COMMENTS_UPLOAD_insert, rowID_4g);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_COMMENTS_UPLOAD: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			
		    case USER_FAVORITES_create:
		    	long rowID_4b = mDatabaseHelper.createUserFavorites(values);
				//Uri _uri=null;
				if(rowID_4b>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_FAVORITES_create, rowID_4b);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_FAVORITES: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			
		    case USER_LOCATION_insert:
		    	long rowID_5 = mDatabaseHelper.insertUserLocation(values);
				//Uri _uri=null;
				if(rowID_5>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_LOCATION_insert, rowID_5);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_LOCATION: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

		    case BUS_GPS_DATA_insert:
		    	long rowID_6 = mDatabaseHelper.insertBusGpsData(values);
				//Uri _uri=null;
				if(rowID_6>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_BUS_GPS_DATA_insert, rowID_6);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_BUS_GPS_DATA: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

		    case BUS_GPS_URL_insert:
		    	long rowID_7 = mDatabaseHelper.insertBusGpsURl(values);
				//Uri _uri=null;
				if(rowID_7>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_BUS_GPS_URL_insert, rowID_7);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_BUS_GPS_URL: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;
			
		    case BUSCODE_insert:
		    	long rowID_7a = mDatabaseHelper.insertBuscode(values);

				if(rowID_7a>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_BUSCODE_insert, rowID_7a);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_BUSCODE: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

			
		    case USER_LOCATION_create:
		    	long rowID_5b = mDatabaseHelper.createUserLocation(values);
				//Uri _uri=null;
				if(rowID_5b>0){
					_uri = ContentUris.withAppendedId(CONTENT_URI_USER_LOCATION_create, rowID_5b);
				}else {
					try {
						throw new SQLException("Failed to insert at TABLE_USER_LOCATION: " + uri);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			break;

		    }
		return _uri;
	}


	/** A callback method which is invoked when insert operation is requested on this content provider */
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
	    int uriType = 0;
	    int insertCount = 0;
	    try {

	        uriType = uriMatcher.match(uri);

	        switch (uriType) {
	        case USER_PLACES:
	            try {
	            	//------------
	            	mDatabaseHelper.resetUserPlaces();
	            	//------------
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//Log.d(TAG, "values " + values.toString());
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_PLACES, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;

	        case USER_FRIENDS:
	            try {
	            	//----
	            	mDatabaseHelper.resetUserFriends();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_FRIENDS, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;

	        case USER_FAVORITES_create:
	            try {
	            	//----
	            	mDatabaseHelper.resetUserFavorites();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_FAVORITES, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;
	            
	        case REWARDS_create:
	            try {
	            	//----
	            	mDatabaseHelper.resetRewards();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_REWARDS, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;	            

	        case RANKING_create:
	            try {
	            	//----
	            	mDatabaseHelper.resetRanking();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_RANKING, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;	            
	            
	        case NEWSFEED_create:
	            try {
	            	//----
	            	mDatabaseHelper.resetNewsfeed();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_NEWSFEED, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;	            
	            
	        case LIKES_create:
	            try {
	            	//----
	            	mDatabaseHelper.resetLikes();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_LIKES, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;	            
	            
	            
	        case USER_ROUTES_create:
	            try {
	            	//----
	            	mDatabaseHelper.resetUserRoutes();
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_ROUTES, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;

	        case USER_ROUTES_insert:
	            try {
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_USER_ROUTES, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;

	        case BUS_GPS_DATA_insert:
	            try {
	            	//----
	            	mDatabaseHelper.getWritableDatabase().beginTransaction();
	                for (ContentValues value : values) {
	                	//---------
	                	//---------
	                    long id = mDatabaseHelper.getWritableDatabase().insert(DatabaseHelper.TABLE_BUS_GPS_DATA, null, value);
	                    if (id > 0)
	                        insertCount++;
	                }
	                mDatabaseHelper.getWritableDatabase().setTransactionSuccessful();
	            } catch (Exception e) {
	                // Your error handling
	            } finally {
	            	mDatabaseHelper.getWritableDatabase().endTransaction();
	            }
	            break;
	            
	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	        }
	        // getContext().getContentResolver().notifyChange(uri, null);
	    } catch (Exception e) {
	      // Your error handling
	    }

	    return insertCount;
	}

	/** A callback method which is by the default content uri */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		if(uriMatcher.match(uri)==USER_PLACES){
			return mDatabaseHelper.get_UserPlaces();

		}else if (uriMatcher.match(uri)==USER_FRIENDS){
			return mDatabaseHelper.get_FriendList();

		}else if (uriMatcher.match(uri)==USER_PROFILE){
			return mDatabaseHelper.get_UserProfile();

		}else if (uriMatcher.match(uri)==CITY_BUSLINE){
			return mDatabaseHelper.get_CityBusline();

		}else if (uriMatcher.match(uri)==USER_FAVORITES){
			return mDatabaseHelper.get_UserFavorites();

		}else if (uriMatcher.match(uri)==USER_ROUTES){
			return mDatabaseHelper.get_UserRoutes();

		}else if (uriMatcher.match(uri)==USER_ROUTES_FLAG){
			return mDatabaseHelper.get_UserRoutesFlag();

		}else if (uriMatcher.match(uri)==USER_LOCATION){
			return mDatabaseHelper.get_UserLocation();

		}else if (uriMatcher.match(uri)==BUS_GPS_DATA){
			return mDatabaseHelper.get_BusGpsData();

		}else if (uriMatcher.match(uri)==BUS_GPS_URL){
			return mDatabaseHelper.get_BusGpsUrl();
		
		}else if (uriMatcher.match(uri)==BUSCODE){
			return mDatabaseHelper.get_Buscode();
	
			
		}else if (uriMatcher.match(uri)==REWARDS){
			return mDatabaseHelper.get_Rewards();			

		}else if (uriMatcher.match(uri)==RANKING){
			return mDatabaseHelper.get_Ranking();			
			
		}else if (uriMatcher.match(uri)==NEWSFEED){
			return mDatabaseHelper.get_Newsfeed();			
			
		}else if (uriMatcher.match(uri)==NEWSFEED_UPLOAD){
			return mDatabaseHelper.get_Newsfeed();			
			
		}else if (uriMatcher.match(uri)==LIKES){
			return mDatabaseHelper.get_Likes();			

		}else if (uriMatcher.match(uri)==LIKES_UPLOAD){
			return mDatabaseHelper.get_Likes_upload();			

		}else if (uriMatcher.match(uri)==COMMENTS_UPLOAD){
			return mDatabaseHelper.get_Comments_upload();			
			
			
		}else{
			return null;
		}

	}


	/** A callback method which is invoked when update operation is requested on this content provider */

	@Override
	public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub

		int uriType = 0;
		int rowsUpdated = 0;
		
	    try {

	    	uriType = uriMatcher.match(uri);
	        switch (uriType) {
	        case NEWSFEED_update:
	            rowsUpdated = mDatabaseHelper.updateNewsfeed(contentValues, selection, selectionArgs);
	        	break;	

	        default:
	            throw new IllegalArgumentException("Unknown URI: " + uri);
	        }

	    } catch (Exception e) {
	      // Your error handling
	    	
	    }

		return rowsUpdated;

	}
}
